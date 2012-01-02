package sw.client.psystem.test;


import java.awt.Graphics2D;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import sw.client.SWFrame;
import sw.client.psystem.ParticleSystem;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;

public class TestParticleSystem extends TestCase
{
	private ParticleSystem _psys;
	private SWFrame _frame;
	
	@Before
	public void setUp() throws Exception
	{
		_psys = new ParticleSystem(new ValuePair(100, 100), new ValuePair(0, 0), new ValuePair(1, 0));
		_frame = new SWFrame(true)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -6539688455012282865L;

			@Override
			public void render(Graphics2D g)
			{
				super.render(g);
				_psys.render(g);
			}
		};
	}
	
	@Test
	public void testParticles()
	{
		assertTrue(true);
		_psys.spawnParticle(ParticleType.CIRCULAR);
		while (_psys.countParticles() > 0)
		{
			System.out.println("loop");
			_psys.render((Graphics2D)_frame.getGraphics());
			_psys.tick();
		}
		System.out.println("terminated");
	}
	
	

}
