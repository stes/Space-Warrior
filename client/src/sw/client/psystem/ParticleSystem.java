package sw.client.psystem;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ParticleSystem
{
	public enum ParticleType {CIRCULAR};
	
	private ArrayList<Particle> _particles;
	
	private ValuePair _spawnPoint;
	private ValuePair _initialVelocity;
	private ValuePair _initialAcceleration;
	
	public ParticleSystem(ValuePair spawnPoint, ValuePair velocity, ValuePair acceleration)
	{
		_spawnPoint = spawnPoint;
		_initialVelocity = velocity;
		_initialAcceleration = acceleration;
		_particles = new ArrayList<Particle>();
	}
	
	public void spawnParticle(ParticleType type)
	{
		Particle particle = null;
		switch (type)
		{
			case CIRCULAR:
				particle = new CircularParticle(_spawnPoint, _initialVelocity, _initialAcceleration);
				break;
		}
		_particles.add(particle);
	}
	
	public void tick()
	{
		for (int i = 0; i < _particles.size(); i++)
		{
			Particle p = _particles.get(i);
			p.tick();
			if (!p.isAlive())
				_particles.remove(i);
		}
	}
	
	public void render(Graphics2D g)
	{
		for (Particle p : _particles)
		{
			p.render(g);
		}
	}
	
	public int countParticles()
	{
		return _particles.size();
	}
}
