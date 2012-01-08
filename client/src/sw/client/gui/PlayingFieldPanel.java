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
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

import sw.client.ClientConstants;
import sw.client.gcontrol.IGameStateManager;
import sw.client.gui.sprites.IShotSprite;
import sw.client.gui.sprites.LaserSprite;
import sw.client.gui.sprites.ProjectileSprite;
import sw.client.gui.sprites.SpaceShipSprite;
import sw.client.gui.sprites.Sprite;
import sw.client.player.HumanPlayer;
import sw.client.psystem.ParticleSystem;
import sw.shared.GameConstants;
import sw.shared.data.entities.IEntity;
import sw.shared.data.entities.IStaticEntity;
import sw.shared.data.entities.players.IDamageable;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.data.entities.shots.LaserBeam;
import sw.shared.data.entities.shots.Mine;
import sw.shared.data.entities.shots.Projectile;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayingFieldPanel extends JPanel
{
	@SuppressWarnings("unused")
	private static final Random _random = new Random(System.currentTimeMillis());
	private static final long serialVersionUID = -8647279084154615455L;

	private PlayingFieldPanel _self;
	private BufferedImage _backgroundImg;
	private double _snapTime;

	private boolean _isDebugActive;

	private IGameStateManager _stateManager;
	private HashMap<Integer, Sprite> _sprites;
	private ParticleSystem _particleSystem;

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
		_sprites = new HashMap<Integer, Sprite>();
		_particleSystem = new ParticleSystem();
		this.setIgnoreRepaint(true);
		this.init();
	}

	@Override
	public void paintComponent(Graphics g)
	{}

	@Override
	public void paintComponents(Graphics g)
	{}

	public void playerInit()
	{
		if (_stateManager.getLocalPlayer() instanceof HumanPlayer)
		{
			this.addKeyListener((HumanPlayer) _stateManager.getLocalPlayer());
		}
	}

	public void render(Graphics g)
	{
		// determine scale factors
		double scaleX = this.getScaleX();
		double scaleY = this.getScaleY();

		// draw
		g.drawImage(_backgroundImg, 0, 0, this.getWidth(), this.getHeight(), null);
		Graphics2D g2d = (Graphics2D) g;
		_stateManager.setRendering(true);
		_snapTime = _stateManager.snapTime();
		this.updateSprites();
		_stateManager.setRendering(false);
		_particleSystem.render(g2d, scaleX, scaleY);

		for (Sprite s : _sprites.values())
		{
			if (s instanceof IShotSprite)
			{
				s.render(g2d, scaleX, scaleY, _snapTime);
			}
		}
		for (Sprite s : _sprites.values())
		{
			if (s instanceof SpaceShipSprite)
			{
				s.render(g2d, scaleX, scaleY, _snapTime);
			}
		}
		if (_stateManager.getLocalPlayer() != null)
		{
			SpaceShip p = _stateManager.getLocalPlayer().getDataSet();
			if (p == null || !p.isAlive())
			{
				return;
			}
			if (p.isLocal())
			{
				this.paintBars(g2d, p);
				if (_isDebugActive)
				{
					this.showDebugInfo(g2d, p);
				}
			}
		}
	}

	public void setDebugMode(boolean active)
	{
		_isDebugActive = active;
	}

	// TODO improve this
	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, this.rotateTransform(src, degrees), null);
		return rotatedImage;
	}

	protected AffineTransform rotateTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(degrees, src.getWidth() / 2, src.getHeight() / 2);
	}

	private double getScaleX()
	{
		return (double) this.getWidth() / (double) GameConstants.PLAYING_FIELD_WIDTH;
	}

	private double getScaleY()
	{
		return (double) this.getHeight() / (double) GameConstants.PLAYING_FIELD_HEIGHT;
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

	private void invokeExplosion(double x, double y)
	{
		_particleSystem.explosion(x, y);
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
		g2d.drawLine(start_x, y, end_x, y);

		end_x = start_x + d.getAmmo() * ClientConstants.BAR_LENGTH / GameConstants.MAX_AMMO;
		y = 30;
		pat = new GradientPaint(start_x, 10, Color.GRAY, end_x, 60, new Color(100, 100, 100, 100));
		g2d.setPaint(pat);
		g2d.drawLine(start_x, y, end_x, y);
	}

	private void removeOutdatedSprites()
	{
		Integer[] keys = _sprites.keySet().toArray(new Integer[] {});
		for (int i = 0; i < keys.length; i++)
		{
			if (_sprites.containsKey(keys[i]) && !_sprites.get(keys[i]).isUpdated())
			{
				_sprites.remove(keys[i]);
			}
		}
	}

	private void showDebugInfo(Graphics2D g2d, SpaceShip pl)
	{
		int x = this.getWidth() - 200;
		int y = 300;

		g2d.setColor(Color.WHITE);
		String[] info = new String[] { "X:\t\t" + pl.getX(), "Y:\t\t" + pl.getY(),
				"Direction:\t" + pl.getDirection(), "Speed:\t" + pl.getSpeed(),
				"Turn Speed:\t" + pl.getTurnSpeed(), "Score:\t" + pl.getScore(),
				"ID:\t" + pl.getID() };

		for (String s : info)
		{
			g2d.drawString(s, x, y += 35);
		}
	}

	private void updateSprites()
	{
		for (Sprite s : _sprites.values())
		{
			s.notifyUpdate();
		}
		for (IEntity ent : _stateManager.getGameWorld().getAllEntities())
		{
			// TODO improve/sort the if statements
			if (ent instanceof IStaticEntity)
			{
				IStaticEntity e = (IStaticEntity) ent;
				IStaticEntity prevEnt = (IStaticEntity) _stateManager.getPrevGameWorld().getEntityByID(ent.getID());
				if (prevEnt == null)
				{
					prevEnt = e;
				}
				if (_sprites.containsKey(e.getID()))
				{
					if ((e instanceof Projectile && ((Projectile) e).isExploding())
							|| (e instanceof IDamageable && !((SpaceShip) e).isAlive()))
					{
						System.out.println("removed " + e.toString());
						this.invokeExplosion(e.getX(), e.getY());
						_sprites.remove(e.getID());
					}
					else
					{
						_sprites.get(e.getID()).updateEntity(e, prevEnt);
					}
				}
				else if (!((e instanceof Projectile && ((Projectile) e).isExploding()) || (e instanceof IDamageable && !((SpaceShip) e).isAlive())))
				{
					if (e instanceof SpaceShip)
					{
						_sprites.put(e.getID(), new SpaceShipSprite((SpaceShip) e, _particleSystem));
					}
					else if (e instanceof Projectile)
					{
						_sprites.put(e.getID(), new ProjectileSprite((Projectile) e,
								_particleSystem));
					}
					else if (e instanceof LaserBeam)
					{
						_sprites.put(e.getID(), new LaserSprite((LaserBeam) e));
					}
					else if (e instanceof Mine)
					{
						System.out.println("added mine");
						_sprites.put(e.getID(), new MineSprite((Mine) e));
					}
					// TODO handle other entity types as well
				}
			}
		}
		this.removeOutdatedSprites();
	}
}
