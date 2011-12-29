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
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sw.client.ClientConstants;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.shared.GameConstants;
import sw.shared.GameConstants.Images;
import sw.shared.Packettype;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.Entity;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.data.entities.shots.LaserBeam;
import sw.shared.data.entities.shots.Rocket;
import sw.shared.data.entities.shots.ShotEntity;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayingFieldPanel extends JPanel implements GameStateChangedListener
{
	private static final long serialVersionUID = -8647279084154615455L;

	private PlayingFieldPanel _self;
	private BufferedImage _backgroundImg;
	private Insets _insets;
	private double _snapTime;

	private boolean _isDebugActive;

	private IGameStateManager _stateManager;

	/**
	 * Creates a new playing field given a reference to the player list
	 * 
	 * @param stateManager
	 *            a game state manager
	 */
	public PlayingFieldPanel(int width, int height, IGameStateManager stateManager)
	{
		super();
		_self = this;
		_stateManager = stateManager;
		_backgroundImg = ImageContainer.getLocalInstance().getImage(GameConstants.Images.BACKGROUND);
		this.setIgnoreRepaint(true);
		this.init();
	}

	private JFrame getParentFrame(Component c)
	{
		if (c.getParent() == null)
			return null;
		if (c.getParent() instanceof JFrame)
			return (JFrame) c.getParent();
		else
			return getParentFrame(c.getParent());
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
				_self.repaint();
			}
		});
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		_isDebugActive = true;;
	}

	public void setDebugMode(boolean active)
	{
		_isDebugActive = active;
	}

	public void paintComponent(Graphics g)
	{
	}

	public void paintComponents(Graphics g)
	{
	}

	public void render(Graphics g)
	{
		// get parent frame's insets
		if (_insets == null)
			_insets = getParentFrame(this).getInsets();

		// determine scale factors
		double scaleX = (double) this.getWidth() / (double) GameConstants.PLAYING_FIELD_WIDTH;
		double scaleY = (double) this.getHeight() / (double) GameConstants.PLAYING_FIELD_HEIGHT;

		// TODO use insets
		g.drawImage(_backgroundImg,
				_insets.left,
				_insets.top,
				this.getWidth(),
				this.getHeight(),
				null);

		Graphics2D g2d = (Graphics2D) g;
		
		_stateManager.setRendering(true);
		
		_snapTime = _stateManager.snapTime();
		GameWorld prevWorld = _stateManager.getPrevGameWorld();
		GameWorld world = _stateManager.getGameWorld();
		
		_stateManager.setRendering(false);
		
		for (Entity ent : world.getEntitiesByType(Packettype.SNAP_SHOT, new Entity[] {}))
		{
			this.drawEntity(g2d, ent, ent, scaleX, scaleY);
		}
		
		for (SpaceShip ent : world.getPlayers())
		{
			SpaceShip prevEnt = ent;
			for(SpaceShip prev : prevWorld.getPlayers())
			{
				if(prev.getName().equals(ent.getName()))
					prevEnt = prev;
			}
			this.drawEntity(g2d, ent, prevEnt, scaleX, scaleY);
		}
	}

	private void drawEntity(Graphics2D g2d, Entity ent, Entity prevEnt, double scaleX, double scaleY)
	{
		if (ent.getMainType() == Packettype.SNAP_PLAYERDATA)
		{
			SpaceShip prevPl = (SpaceShip) prevEnt;
			SpaceShip pl = (SpaceShip) ent;
			if (!pl.isAlive())
				return;

			if (pl.isLocal())
			{
				this.paintBars(g2d, pl);
				if (_isDebugActive)
				{
					this.showDebugInfo(g2d, pl);
				}
			}
			
			// TODO: generalize this for all entities
			Point2D.Double pos = new Point2D.Double(
					prevPl.getPosition().getX() + (pl.getPosition().getX() - prevPl.getPosition().getX()) * _snapTime,
					prevPl.getPosition().getY() + (pl.getPosition().getY() - prevPl.getPosition().getY()) * _snapTime);
			
			double direction = prevPl.getDirection() + Math.asin(Math.sin(pl.getDirection() - prevPl.getDirection())) * _snapTime;

			g2d.drawImage(rotateImage(ImageContainer.getLocalInstance().getImage(pl.getImageID()), -direction),
					_insets.left + (int) (scaleX * (pos.getX() - GameConstants.PLAYER_SIZE / 2)),
					_insets.top + (int) (scaleY * (pos.getY() - GameConstants.PLAYER_SIZE / 2)),
					(int) (GameConstants.PLAYER_SIZE * scaleX),
					(int) (GameConstants.PLAYER_SIZE * scaleY),
					null);
		}
		else if (ent.getMainType() == Packettype.SNAP_SHOT)
		{
			switch (ent.getSubType())
			{
				case ShotEntity.LASER:
				case ShotEntity.MASTER_LASER:
				{
					LaserBeam s = (LaserBeam) ent;
					g2d.setColor(Color.BLUE);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawLine(_insets.left + (int) (s.getX() * scaleX),
							_insets.top + (int) (s.getY() * scaleY),
							_insets.left + (int) (s.endPoint().getX() * scaleX),
							_insets.top + (int) (s.endPoint().getY() * scaleY));
					break;
				}
				case ShotEntity.ROCKET:
				{
					Rocket r = (Rocket) ent;
					g2d.setColor(Color.GREEN);
					double size = GameConstants.ROCKET_SIZE;
					g2d.drawImage(rotateImage(ImageContainer.getLocalInstance().getImage(Images.SHOT_ROCKET),
							-r.getDirection()),
							_insets.left + (int) (scaleX * (r.getX() - size / 2)),
							_insets.top + (int) (scaleY * (r.getY() - size / 2)),
							(int) (size * scaleX),
							(int) (size * scaleY),
							null);
				}
			}
		}
	}

	private void showDebugInfo(Graphics2D g2d, SpaceShip pl)
	{
		int x = this.getWidth() - 200;
		int y = 300;

		g2d.setColor(Color.WHITE);
		String[] info = new String[]
        {
			"X:\t\t" + pl.getX(),
			"Y:\t\t" + pl.getY(),
			"Direction:\t" + pl.getDirection(),
			"Speed:\t" + pl.getSpeed(),
			"Turn Speed:\t" + pl.getTurnSpeed()
		};
		
		for (String s : info)
		{
			g2d.drawString(s, x, y += 35);
		}
	}

	private void paintBars(Graphics2D g2d, SpaceShip d)
	{
		g2d.setStroke(new BasicStroke(15));

		int start_x = ClientConstants.BAR_X;
		int end_x = start_x + d.getLifepoints() * ClientConstants.BAR_LENGTH
				/ GameConstants.MAX_LIVES;
		int y = 10;
		GradientPaint pat = new GradientPaint(start_x, 10, Color.RED, end_x, 60, new Color(255,
				0,
				0,
				100));
		g2d.setPaint(pat);
		g2d.drawLine(_insets.left + start_x, _insets.top + y, _insets.left + end_x, _insets.top + y);

		end_x = start_x + d.getAmmo() * ClientConstants.BAR_LENGTH / GameConstants.MAX_AMMO;
		y = 30;
		pat = new GradientPaint(start_x, 10, Color.GRAY, end_x, 60, new Color(100, 100, 100, 100));
		g2d.setPaint(pat);
		g2d.drawLine(_insets.left + start_x, _insets.top + y, _insets.left + end_x, _insets.top + y);
	}

	protected AffineTransform rotateTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(degrees, src.getWidth() / 2, src.getHeight() / 2);
	}

	// TODO improve this
	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, rotateTransform(src, degrees), null);
		return rotatedImage;
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{
	}

	@Override
	public void playerInit(GameStateChangedEvent e)
	{
		if (_stateManager.getLocalPlayer() instanceof HumanPlayer)
		{
			this.addKeyListener((HumanPlayer) _stateManager.getLocalPlayer());
		}
	}
}
