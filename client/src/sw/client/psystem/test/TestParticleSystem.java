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


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import sw.client.psystem.ParticleSystem;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;

public class TestParticleSystem extends TestCase
{
	private ParticleSystem _psys;
	private JFrame _frame;
	private JPanel _contentPanel;
	private Random _random;
	
	@Before
	public void setUp() throws Exception
	{
		_psys = new ParticleSystem(new ValuePair(100, 100));
		_random = new Random(System.currentTimeMillis());
		_frame = new JFrame();
		_frame.setSize(400, 400);
		_frame.setLayout(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_contentPanel = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g)
			{
				_psys.render((Graphics2D)g);
			}
		};
		_contentPanel.setSize(_frame.getSize());
		
		_frame.add(_contentPanel);
		_frame.setVisible(true);
		_frame.toFront();
	}
	
	@Test
	public void testParticles()
	{
		assertTrue(true);
		for (int i = 0; i < 100; i++)
		{
			_psys.spawnParticle(
					ParticleType.CIRCULAR, 
					new ValuePair((_random.nextDouble() * 6) - 3, (_random.nextDouble() * 6) - 3), 
					new ValuePair((_random.nextDouble() * 6) - 3, (_random.nextDouble() * 6) - 3));
		}
		while (_psys.countParticles() > 0)
		{
			Graphics g = _contentPanel.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, _contentPanel.getWidth(), _contentPanel.getHeight());
			_psys.render((Graphics2D)_frame.getGraphics());
			_psys.tick();
			for (int i = 0; i < 100 - _psys.countParticles(); i++)
			{
				_psys.spawnParticle(
						ParticleType.CIRCULAR, 
						new ValuePair(0, 0), 
						new ValuePair((_random.nextDouble() * 2) - 1, (_random.nextDouble() * 2) - 1));
			}
		}
		System.out.println("terminated");
	}
}
