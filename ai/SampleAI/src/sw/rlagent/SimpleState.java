package sw.rlagent;

import java.util.ArrayList;
import java.util.Random;

import sw.shared.GameConstants;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.players.SpaceShip;

public class SimpleState
{
	private static Random _random = new Random(System.currentTimeMillis());

	private SpaceShip _localDataSet;
	private SpaceShip[] _playerList;
	private double[] _features;
	private double[] _weights;

	public ArrayList<Byte> id;

	public SimpleState(SpaceShip localDataSet, SpaceShip[] list)
	{
		_localDataSet = localDataSet;
		_playerList = list.clone();
		this.init();
	}

	public int getHash()
	{
		ArrayList<Byte> buffer = new ArrayList<Byte>();
		for (SpaceShip d : _playerList)
		{
			buffer.add((byte) ((int) d.getDirection()));
			buffer.add((byte) ((int) d.getPosition().x));
			buffer.add((byte) ((int) d.getPosition().y));
		}

		id = buffer;

		int h = 0;
		for (int i = 0; i < buffer.size(); i++)
		{
			int highorder = h & 0xf8000000;
			h = h << 5;
			h = h ^ (highorder >> 27);
			h = h ^ buffer.get(i);
		}
		return h;
	}

	private void init()
	{
		this.initFeatures();
	}

	private void initFeatures()
	{
		double minDist = Double.MAX_VALUE;
		for (int i = 0; i < _playerList.length; i++)
		{
			SpaceShip d = _playerList[i];
			if (d != null && !d.equals(_localDataSet))
			{
				double dist = _localDataSet.getPosition().distance(d.getPosition());
				if (minDist > dist)
				{
					minDist = dist;
				}
			}
		}
		_features = new double[] { minDist };
		_weights = new double[] { 1 };
	}

	public double value()
	{
		double sum = 0;
		for (int i = 0; i < _features.length; i++)
		{
			sum += _features[i] * _weights[i];
		}
		return sum;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof SimpleState))
			return false;
		SimpleState s = (SimpleState) object;
		if (_playerList.length != s._playerList.length)
			return false;
		for (int i = 0; i < _playerList.length; i++)
		{
			SpaceShip d1 = _playerList[i];
			SpaceShip d2 = s._playerList[i];
			if (d1 != null && d2 != null && !d1.equals(d2))
				return false;
		}
		return true;
	}

	public SimpleState successor(Actions a)
	{
		SpaceShip data = new SpaceShip(_localDataSet);
		PlayerInput input = RLAgent._self.applyAction(a);
		if (data != null && data.isAlive())
		{
			data.accelerate(GameConstants.ACCELERATION * input.moveDirection());
			data.reload();
			data.move();
		}

		return new SimpleState(_localDataSet, _playerList);
	}

}
