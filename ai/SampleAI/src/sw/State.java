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
package sw;

public abstract class State
{
	private double[] _features;
	private double[] _weights;

	public State(int features)
	{
		_features = new double[features];
		_weights = new double[features];
	}

	protected double[] getFeatures()
	{
		return _features;
	}

	public void setFeature(int index, double value)
	{
		_features[index] = value;
	}

	public double getFeature(int index)
	{
		return _features[index];
	}

	public void setWeight(int index, double value)
	{
		_weights[index] = value;
	}

	public double getWeight(int index)
	{
		return _weights[index];
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
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (double f : _features)
		{
			sb.append(f);
			sb.append(";");
		}
		return sb.toString();
	}
}
