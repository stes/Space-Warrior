/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import javax.swing.JPanel;

import sw.client.ClientConstants;
import sw.shared.GameConstants;
import sw.shared.data.PlayerDataSet;
import sw.shared.data.PlayerList;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayingFieldPanel extends JPanel implements MouseListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8647279084154615455L;
	
	private PlayerList _playerList;
    private BufferedImage _localPlayerImg;
    private BufferedImage _opposingPlayerImg;
    private BufferedImage _backgroundImg;
    
    /**
     * Creates a new playing field given a reference to the player list
     * 
     * @param playerList a player list (may be empty)
     * @throws IllegalArgumentException if playerList is null
     */
    public PlayingFieldPanel(PlayerList playerList)
    {
        super();
        this.addMouseListener(this);
        if (playerList == null)
        	throw new IllegalArgumentException("argument playerList must not be null");
        this.setLayout(null);
        _localPlayerImg = ImageContainer.getLocalInstance().getLocalPlayerImg();
        _opposingPlayerImg = ImageContainer.getLocalInstance().getOpposingPlayerImg();
        _backgroundImg = ImageContainer.getLocalInstance().getBackgroundImg();
        _playerList = playerList;
        ShotPool.init(this);
        this.setBackground(Color.BLACK);
    }
    /**
     * Paints the playing field with its contents
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(
        		_backgroundImg,
        		this.getX(),
        		this.getY(),
        		this.getWidth(),
        		this.getHeight(),
        		null);
        
        ShotPool.paint(g);
        for (int i = 0 ; i < _playerList.size(); i++)
        {
            if (_playerList.dataAt(i) == null)
                continue;
            PlayerDataSet d = _playerList.dataAt(i);
            if (d.lokal())
            {
                this.paintBars(g2d, d);
                g2d.drawImage(
                    rotateImage(_localPlayerImg, 180-d.richtung()),
                    null,
                    (int)d.position().getX() - GameConstants.PLAYER_SIZE/2,
                    (int)d.position().getY() - GameConstants.PLAYER_SIZE/2);
            }
            else
            {
                g2d.drawImage(
                    rotateImage(_opposingPlayerImg, 180-d.richtung()),
                    null,
                    (int)d.position().getX() - GameConstants.PLAYER_SIZE/2,
                    (int)d.position().getY() - GameConstants.PLAYER_SIZE/2);
            }
        }
    }
    
    private AffineTransform affineTransform(BufferedImage src, double degrees)
    {
        return AffineTransform.getRotateInstance(
            Math.toRadians(degrees),
            src.getWidth() / 2,
            src.getHeight() / 2);
    }
    
    private BufferedImage rotateImage(BufferedImage src, double degrees)
    {
        BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src
                .getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(src, affineTransform(src, degrees), null);
        return rotatedImage;
    }
    
    private void paintBars(Graphics2D g2d, PlayerDataSet d)
    {
        g2d.setStroke(new BasicStroke(15));
       
        int start_x = ClientConstants.BAR_X;
        int end_x = start_x + d.leben() * ClientConstants.BAR_LENGTH / GameConstants.MAX_LIVES;
        int y = 10;
        GradientPaint pat
           = new GradientPaint(start_x, 10, Color.RED,
                               end_x, 60, new Color(255, 0, 0, 100));
        g2d.setPaint(pat);
        g2d.drawLine(start_x, y, end_x, y);
        
        end_x = start_x + d.munition() * ClientConstants.BAR_LENGTH / GameConstants.MAX_AMMO;
        y = 30;
        pat
           = new GradientPaint(start_x, 10, Color.GRAY,
                               end_x, 60, new Color(100, 100, 100, 100));
        g2d.setPaint(pat);
        g2d.drawLine(start_x, y, end_x, y);
    }

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e)
	{
		this.requestFocusInWindow();	
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
}