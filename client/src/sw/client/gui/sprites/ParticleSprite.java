package sw.client.gui.sprites;

import java.awt.Graphics2D;
import java.util.Random;

import sw.client.psystem.ParticleSystem;
import sw.shared.data.entities.IStaticEntity;

/**
 * A sprite which uses a particle system
 * 
 * @author stes
 * @version 05.01.2012
 */
public class ParticleSprite extends ImageSprite
{
	protected static Random _random = new Random(System.currentTimeMillis());
	
	private ParticleSystem _particleSystem;
	private long _lastParticleUpdate;
	
	public ParticleSprite(IStaticEntity entity)
	{
		super(entity);
		_particleSystem = new ParticleSystem();
	}

	@Override
	public void render(
			Graphics2D g2d,
			double scaleX,
			double scaleY,
			double time)
	{
		_particleSystem.render(g2d);
		super.render(g2d, scaleX, scaleY, time);
		this.processParticles(scaleX, scaleY);
	}

	protected ParticleSystem getParticleSystem()
	{
		return _particleSystem;
	}
	
	protected void processParticles(double scaleX, double scaleY)
	{
		// TODO use an own thread for this?
		if (System.currentTimeMillis() - this._lastParticleUpdate < 10)
		{
			return;
		}
		_lastParticleUpdate = System.currentTimeMillis();
		
		_particleSystem.tick();
	}
}
