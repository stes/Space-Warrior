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
package sw.client.psystem.test;

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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.RepaintManager;

import sw.client.psystem.ParticleSystem;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ParticleDemo extends JFrame
{
	class UnRepaintManager extends RepaintManager
	{
		@Override
		public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {}
		@Override
		public void addInvalidComponent(JComponent invalidComponent) {}
		@Override
		public void markCompletelyDirty(JComponent aComponent) {}
		@Override
		public void paintDirtyRegions() {}
	}

	// change to limit fps in order to minimize cpu usage
	public final int SLEEP_TIME = 1;
	private static final long serialVersionUID = 1575599799999464878L;
	
	private ParticleSystem _psys;
	// should be okay for now
	private VolatileImage _screen;
	private BufferStrategy _bufferStrategy;
	private boolean _isRunning;
	private int _fps;
	private Random _random = new Random(System.currentTimeMillis());
	private ParticleDemo _self;
	

	/**
	 * Creates a new SWFrame
	 */
	public ParticleDemo()
	{
		super("Particle Demo");
		_self = this;
		this.setIgnoreRepaint(true);

		RepaintManager repaintManager = new UnRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);

		this.setSize(400, 400);

		((JComponent) this.getContentPane()).setOpaque(false);

		this.init();
		
		new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					_self.spawnLoop();
					try
					{
						Thread.sleep(1);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}.start();

		this.createBufferStrategy(2);
		_bufferStrategy = this.getBufferStrategy();
		
		_psys = new ParticleSystem();
		
		_screen = this.createVolatileImage(this.getWidth(), this.getHeight());

		_isRunning = true;
		this.renderLoop();
	}
	
	private void init()
	{
		this.setVisible(true);
		this.toFront();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = this.getInsets();
		int insetWide = insets.left + insets.right;
		int insetTall = insets.top + insets.bottom;
		this.setSize(this.getWidth() + insetWide, this.getHeight() + insetTall);

		System.out.println("init");
	}

	/**
	 * Method containing the game's loop. Each iteration of the loop updates all
	 * animations and sprite locations and draws the graphics to the screen
	 */
	public void renderLoop()
	{
		long oldTime = System.nanoTime();
		long nanoseconds = 0;
		int frames = 0;
		_fps = 0;

		while (_isRunning)
		{
			// fps
			long elapsedTime = System.nanoTime() - oldTime;
			oldTime = oldTime + elapsedTime;
			nanoseconds = nanoseconds + elapsedTime;
			frames = frames + 1;
			if (nanoseconds >= 1000000000)
			{
				_fps = frames;
				nanoseconds = nanoseconds - 1000000000;
				frames = 0;
			}
			// related to drawing
			Graphics g = null;
			try
			{
				g = _bufferStrategy.getDrawGraphics();
				do
				{
					int state = _screen.validate(getGraphicsConfiguration());
					if (state == VolatileImage.IMAGE_INCOMPATIBLE)
					{
						_screen = this.createVolatileImage(this.getWidth(), this.getHeight());
					}
					Graphics2D g2d = _screen.createGraphics();
					this.render(g2d);
					g2d.dispose();
					Insets insets = this.getInsets();
					g.drawImage(_screen, insets.left, insets.top, null);
				} while (_screen.contentsLost());
			}
			finally
			{
				g.dispose();
			}

			if (!_bufferStrategy.contentsLost())
			{
				_bufferStrategy.show();
			}
			Toolkit.getDefaultToolkit().sync();
			
			if (this.SLEEP_TIME > 0)
			{
				try
				{
					Thread.sleep(this.SLEEP_TIME);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	public void render(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		_psys.render(g2d);

		
		g2d.setColor(Color.BLACK);
		g2d.drawString("FPS: " + _fps, 0, 100 + g2d.getFont().getSize());
	}
	
	private long _lastUpdate;
	
	private void spawnLoop()
	{
		if (_psys == null)
			return;
		long timespan = System.currentTimeMillis() - _lastUpdate;
		if (timespan < 25)
			return;
		_lastUpdate = System.currentTimeMillis();
		_psys.tick();
		if (_psys.countParticles() < 1000)
		{
			// explosion:
			for (int i = 0; i < 1000; i++)
			{
				_psys.spawnParticle(
						ParticleType.CIRCULAR, 
						50,
						new ValuePair(200, 200),
						new ValuePair(0, 0), 
						new ValuePair(10 * ((_random.nextDouble() * 2) - 1), 10 * ((_random.nextDouble() * 2) - 1)));
			}
			
			// fountain
//			for (int i = 0; i < 20; i++)
//			{
//				double acc = 20 * (_random.nextDouble() - 0.5);
//				_psys.spawnParticle(
//					ParticleType.CIRCULAR,
//					new ValuePair(200, 200),
//					new ValuePair(acc * 0.75 * _random.nextDouble(), -Math.abs(acc)),
//					new ValuePair(acc * 0.075 * _random.nextDouble(), 0.01 * Math.abs(acc)));
//			}
		}
		else
			System.out.println("enough particles");
	}
	
	public static void main(String[] args)
	{
		new ParticleDemo();
	}
}

