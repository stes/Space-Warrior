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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import sw.client.ClientConstants;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Entity;
import sw.shared.data.PlayerData;
import sw.shared.data.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayingFieldPanel extends JPanel implements GameStateChangedListener
{
	private static final long serialVersionUID = -8647279084154615455L;

	private PlayingFieldPanel _self;
	private BufferedImage _backgroundImg;
	private double _lastPaint;
	
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
		_self = this;
		_stateManager = stateManager;
		_backgroundImg = ImageContainer.getLocalInstance().getImage(GameConstants.Images.BACKGROUND);
		this.setIgnoreRepaint(true);
		this.init();
	}

	private void init()
	{
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				_self.requestFocusInWindow();
			}
		});
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
//				double scaleX = (double)_self.getWidth() / (double)GameConstants.PLAYING_FIELD_WIDTH;
//				double scaleY = (double)_self.getHeight() / (double)GameConstants.PLAYING_FIELD_HEIGHT;
//				ImageContainer.getLocalInstance().scaleImages(scaleX, scaleY);
				_self.repaint();
			}
		});
		this.setLayout(null);
		this.setBackground(Color.BLACK);
	}
	
	/**
	 * Paints the playing field with its contents
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		//super.paintComponents(g);
		//this.render(g);
		//long l = System.currentTimeMillis();
		//super.paintComponent(g);
		//drawContent(g);

		//g.drawString(""+(System.currentTimeMillis() - l), this.getWidth()-100, 50);
	}
	
	/**
	 * Paints the playing field with its contents
	 */
	@Override
	public void paintComponents(Graphics g)
	{
		//super.paintComponents(g);
		//this.render(g);
		//long l = System.currentTimeMillis();
		//super.paintComponent(g);
		//drawContent(g);

		//g.drawString(""+(System.currentTimeMillis() - l), this.getWidth()-100, 50);
	}

	public void render(Graphics g)
	{
		g.drawImage(_backgroundImg, 0, 0, this.getWidth(), this.getHeight(), null);
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + 1000/ (System.currentTimeMillis() - _lastPaint), this.getWidth()-100, 20);
		_lastPaint = System.currentTimeMillis();
		
		BufferedImage img = new BufferedImage(GameConstants.PLAYING_FIELD_WIDTH,
				GameConstants.PLAYING_FIELD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = img.createGraphics();
		
		g2d.setColor(new Color(0, 0, 0, 0));

		for (Entity ent : _stateManager.getGameWorld().getAllEntities())
		{
			if(ent.getType() == Packettype.SNAP_PLAYERDATA)
			{
				PlayerData pl = (PlayerData) ent;
				if (!pl.isAlive())
					continue;
				
				if (pl.isLocal())
				{
					this.paintBars(g2d, pl);
				}
				
				g2d.drawImage(
							rotateImage(ImageContainer.getLocalInstance().getImage(pl.getImageID()), Math.PI - pl.getDirection()), null,
							(int) (pl.getPosition().getX() - GameConstants.PLAYER_SIZE / 2),
							(int) (pl.getPosition().getY() - GameConstants.PLAYER_SIZE / 2));
			}
			else if(ent.getType() == Packettype.SNAP_SHOT)
			{
				Shot s = (Shot) ent;
				g2d.setColor(Color.BLUE);
		        g2d.setStroke(new BasicStroke(3));
		        g2d.drawLine((int)s.startPoint().getX(), (int)s.startPoint().getY(), (int)s.endPoint().getX(), (int)s.endPoint().getY());
			}
		}
		
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
		g.setColor(Color.WHITE);
	}
	
	private void paintBars(Graphics2D g2d, PlayerData d)
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
	
	protected AffineTransform affineTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(degrees,
				src.getWidth() / 2, src.getHeight() / 2);
	}

	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, affineTransform(src, degrees), null);
		return rotatedImage;
	}

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
