package sw.rlagent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import sw.shared.data.PlayerData;
import sw.shared.data.PlayerList;


public class State
{
	private static Random _random = new Random(System.currentTimeMillis());

	private PlayerData _localDataSet;
	private PlayerList _playerList;
	private double[] _features;
	private double[] _weights;

	public ArrayList<Byte> id;

	public State(PlayerData localDataSet, PlayerList list)
	{
		_localDataSet = new PlayerData(localDataSet);
		_playerList = new PlayerList(list.size());
		this.init(list);
	}

	public int getHash()
	{
		ArrayList<Byte> buffer = new ArrayList<Byte>();
		for (int i = 0; i < _playerList.size(); i++)
		{
			PlayerData d = _playerList.dataAt(i);
			if (d != null)
			{
				buffer.add((byte) ((int)d.getDirection()));
				buffer.add((byte) ((int)d.getPosition().x));
				buffer.add((byte) ((int)d.getPosition().y));
			}
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

	private void init(PlayerList list)
	{
		_localDataSet.getPosition().setLocation(
				transform(_localDataSet.getPosition()));
		_localDataSet.setDirection((int)_localDataSet.getDirection()
				/ AIConstants.ROTATION_STEP);
		for (int i = 0; i < list.size(); i++)
		{
			PlayerData d = list.dataAt(i);
			if (d == null)
				continue;
			else
			{
				PlayerData transformed = new PlayerData(d);
				transformed.getPosition().setLocation(
						transform(d.getPosition()));
				transformed.setDirection((int)d.getDirection()
						/ AIConstants.ROTATION_STEP);
				_playerList.insert(transformed, null);
			}
		}
		this.initFeatures();
	}

	private void initFeatures()
	{
		_features = new double[] { (double) _playerList.count(),
				(double) _localDataSet.getLifepoints(),
				(double) _localDataSet.getAmmo() };
		_weights = new double[_features.length];
		for (int i = 0; i < _weights.length; i++)
			_weights[i] = _random.nextDouble();
	}

	private Point transform(Point.Double p)
	{
		return new Point((int) p.x / AIConstants.TILESIZE, (int) p.y
				/ AIConstants.TILESIZE);
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
		if (!(object instanceof State))
			return false;
		State s = (State) object;
		if (_playerList.size() != s._playerList.size())
			return false;
		for (int i = 0; i < _playerList.size(); i++)
		{
			PlayerData d1 = _playerList.dataAt(i);
			PlayerData d2 = s._playerList.dataAt(i);
			if (d1 != null && d2 != null && !d1.equals(d2))
				return false;
		}
		return true;
	}
}
