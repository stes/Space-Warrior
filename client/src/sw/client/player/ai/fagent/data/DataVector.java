package sw.client.player.ai.fagent.data;

import java.text.DecimalFormat;

public class DataVector
{
	private static final double MULTIPLIER = 50;
	
	private double[] _data;

	private DataVector()
	{
	}
	
	public DataVector(int size)
	{
		this();
		_data = new double[size];
	}
	
	public DataVector(double[] data)
	{
		this();
		_data = data.clone();
	}
	
	public double[] getData()
	{
		return _data;
	}
	
	public int getSize()
	{
		return _data.length;
	}

	public double getDataAt(int i)
	{
		return _data[i];
	}

	public void setDataAt(int i, double netInp)
	{
		_data[i] = netInp;		
	}
	
	public double sum()
	{
		double sum = 0;
		for (double d : _data)
		{
			sum += d;
		}
		return sum;
	}
	
	public DataVector subtract(DataVector vector)
	{
		if (this.getSize() != vector.getSize())
			throw new IllegalArgumentException("Vectors must have the same length");
		DataVector data = new DataVector(this.getSize());
		for (int i = 0; i < _data.length; i++)
		{
			data.setDataAt(i, this.getDataAt(i) - vector.getDataAt(i));
		}
		return data;
	}
	
	public double sumSquares()
	{
		double sum = 0;
		for (double d : _data)
		{
			sum += d * d;
		}
		return sum;
	}

	public DataVector multiply(DataVector input)
	{
		if (this.getSize() != input.getSize())
			throw new IllegalArgumentException("Vectors must have the same length");
		DataVector product = new DataVector(this.getSize());
		for (int i = 0; i < getSize(); i++)
		{
			product.setDataAt(i, this.getDataAt(i) * input.getDataAt(i));
		}
		return product;
	}
	
	public DataVector transform(IFunction f)
	{
		DataVector v = new DataVector(this.getSize());
		for (int i = 0; i < getSize(); i++)
		{
			v.setDataAt(i, f.valueOf(this.getDataAt(i)));
		}
		return v;
	}
	
	public DataMatrix toMatrixRows(int rows)
	{
		DataMatrix m = new DataMatrix(rows, getSize());
		for (int i = 0; i < m.getRowCount(); i++)
		{
			for (int j = 0; j < m.getColumnCount(); j++)
			{
				m.setValueAt(i, j, this.getDataAt(j));
			}
		}
		return m;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof DataVector))
			return false;
		DataVector v = (DataVector)o;
		if (this.getSize() != v.getSize())
			return false;
		for (int i = 0; i < _data.length; i++)
		{
			if (getDataAt(i) != v.getDataAt(i))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		double hash = this.getSize() * MULTIPLIER;
		for (int i = 0; i < _data.length; i++)
		{
			hash += _data[i] * MULTIPLIER;
		}
		return (int)hash;
	}
	
	public DataMatrix toMatrixCols(int columns)
	{
		DataMatrix m = new DataMatrix(getSize(), columns);
		for (int i = 0; i < m.getRowCount(); i++)
		{
			for (int j = 0; j < m.getColumnCount(); j++)
			{
				m.setValueAt(i, j, this.getDataAt(i));
			}
		}
		return m;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("#0.000");
		sb.append("[ ");
		for (int i = 0; i < _data.length; i++)
			sb.append(format.format(_data[i]) + " ");
		sb.append("]");
		return sb.toString();
	}

	public DataVector multiply(double value)
	{
		DataVector product = new DataVector(this.getSize());
		for (int i = 0; i < getSize(); i++)
		{
			product.setDataAt(i, this.getDataAt(i) * value);
		}
		return product;
	}

	public void setData(DataVector data)
	{
		if (this.getSize() != data.getSize())
			throw new IllegalArgumentException("new data vector must have the same size");
		_data = data.getData();
	}
}
