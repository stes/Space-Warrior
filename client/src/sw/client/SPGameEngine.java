package sw.client;

import java.util.ArrayList;

import sw.shared.GameConstants;
import sw.shared.GameEngine;
import sw.shared.data.entities.players.SpaceShip;

public class SPGameEngine extends GameEngine
{
	private ArrayList<SPGameController> _controller;
	private boolean _isReady;
	
	public SPGameEngine()
	{
		super();
		_isReady = false;
		_controller = new ArrayList<SPGameController>();
		new Thread()
		{
			private long _lastTick = System.currentTimeMillis();
			
			@Override
			public void run()
			{
				while(!_isReady)
					Thread.yield();
				while(true)
				{
					while (System.currentTimeMillis() - _lastTick < GameConstants.TICK_INTERVAL)
						;
					tick();
					for (SPGameController c : _controller)
					{
						c.snapshot();
					}
					_lastTick = System.currentTimeMillis();
					Thread.yield();
				}
			}
		}.start();
	}
	
	@Override
	public void invokePlayerWon(SpaceShip pl)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void addListener(SPGameController c)
	{
		_controller.add(c);
	}
	
	public void start()
	{
		_isReady = true;
	}
	
}
