/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import sw.client.ClientConstants;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.shared.GameConstants;
import sw.shared.data.PlayerDataSet;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayingFieldPanel extends JPanel implements MouseListener, GameStateChangedListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647279084154615455L;

	private BufferedImage _localPlayerImg;
	private BufferedImage _opposingPlayerImg;
	private BufferedImage _backgroundImg;

	private IGameStateManager _stateManager;

	/**
	 * Creates a new playing field given a reference to the player list
	 * 
	 * @param stateManager
	 *            a game state manager
	 */
	public PlayingFieldPanel(int width, int height,
			IGameStateManager stateManager)
	{
		super();
		this.addMouseListener(this);
		this.setLayout(null);
		this.setSize(width, height);
		_stateManager = stateManager;
		_localPlayerImg = ImageContainer.getLocalInstance().getLocalPlayerImg();
		_opposingPlayerImg = ImageContainer.getLocalInstance()
				.getOpposingPlayerImg();
		_backgroundImg = ImageContainer.getLocalInstance().getBackgroundImg();
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
		// Graphics2D g2d = (Graphics2D) g;
		g.drawImage(_backgroundImg, 0, 0, this.getWidth(), this.getHeight(), null);
		
		BufferedImage img = new BufferedImage(GameConstants.PLAYING_FIELD_WIDTH,
				GameConstants.PLAYING_FIELD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = img.createGraphics();
		
		g2d.setColor(new Color(0, 0, 0, 0));
		ShotPool.paint(g2d);

		for (int i = 0; i < _stateManager.getPlayerList().size(); i++)
		{
			if (_stateManager.getPlayerList().dataAt(i) == null)
				continue;
			PlayerDataSet d = _stateManager.getPlayerList().dataAt(i);
			if (d.local())
			{
				this.paintBars(g2d, d);
				g2d.drawImage(
						rotateImage(_localPlayerImg, 180 - d.getDirection()),
						null, (int) d.getPosition().getX()
								- GameConstants.PLAYER_SIZE / 2, (int) d
								.getPosition().getY()
								- GameConstants.PLAYER_SIZE / 2);
			}
			else
			{
				g2d.drawImage(
						rotateImage(_opposingPlayerImg, 180 - d.getDirection()),
						null, (int) d.getPosition().getX()
								- GameConstants.PLAYER_SIZE / 2, (int) d
								.getPosition().getY()
								- GameConstants.PLAYER_SIZE / 2);
			}
		}
		
		//((Graphics2D) g).drawImage(img, AffineTransform.getScaleInstance(600, 600), null);
		((Graphics2D) g).drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

		// ((Graphics2D)g).drawImage(img, this.getX(), this.getY(),
		// this.getWidth(), this.getHeight(), null);
	}

	private AffineTransform affineTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(Math.toRadians(degrees),
				src.getWidth() / 2, src.getHeight() / 2);
	}

	private BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, affineTransform(src, degrees), null);
		return rotatedImage;
	}

	private void paintBars(Graphics2D g2d, PlayerDataSet d)
	{
		g2d.setStroke(new BasicStroke(15));

		int start_x = ClientConstants.BAR_X;
		int end_x = start_x + d.getLifepoints() * ClientConstants.BAR_LENGTH
				/ GameConstants.MAX_LIVES;
		int y = 10;
		GradientPaint pat = new GradientPaint(start_x, 10, Color.RED, end_x,
				60, new Color(255, 0, 0, 100));
		g2d.setPaint(pat);
		g2d.drawLine(start_x, y, end_x, y);

		end_x = start_x + d.getAmmo() * ClientConstants.BAR_LENGTH
				/ GameConstants.MAX_AMMO;
		y = 30;
		pat = new GradientPaint(start_x, 10, Color.GRAY, end_x, 60, new Color(
				100, 100, 100, 100));
		g2d.setPaint(pat);
		g2d.drawLine(start_x, y, end_x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.requestFocusInWindow();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void gameStateChanged(GameStateChangedEvent e){	}

	@Override
	public void newRound(GameStateChangedEvent e){}

	@Override
	public void playerInit(GameStateChangedEvent e)
	{
		if (_stateManager.getLocalPlayer() instanceof HumanPlayer)
		{
			this.addKeyListener((HumanPlayer) _stateManager.getLocalPlayer());
		}
	}
}
