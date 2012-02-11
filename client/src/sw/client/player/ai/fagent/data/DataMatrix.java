package sw.client.player.ai.fagent.data;

import java.text.DecimalFormat;

public class DataMatrix
{
	private double[][] _data;
	
	public DataMatrix(int rows, int columns)
	{
		_data = new double[rows][columns];
	}
	
	public DataMatrix(double[][] data)
	{
		_data = data.clone();
	}
	
	public DataVector getRow(int row)
	{
		return new DataVector(_data[row]);
	}
	
	public DataVector getColumn(int column)
	{
		DataVector v = new DataVector(this.getRowCount());
		for (int i = 0; i < this.getRowCount(); i++)
		{
			v.setDataAt(i, this.getValueAt(i, column));
		}
		return v;
	}
	
	public double getValueAt(int row, int column)
	{
		return _data[row][column];
	}
	
	public void setValueAt(int row, int column, double value)
	{
		_data[row][column] = value;
	}
	
	public int getRowCount()
	{
		return _data.length;
	}
	
	public int getColumnCount()
	{
		return _data[0].length;
	}

	public DataMatrix add(DataMatrix m)
	{
		DataMatrix sum = new DataMatrix(this.getRowCount(), this.getColumnCount());
		if (this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount())
		{
			throw new IllegalArgumentException("Matrizes does not have the same size");
		}
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				sum.setValueAt(i, j, this.getValueAt(i, j) + m.getValueAt(i, j));
			}
		}
		return sum;
	}
	
	public DataMatrix transform(IFunction f)
	{
		DataMatrix m = new DataMatrix(this.getRowCount(), this.getColumnCount());
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				m.setValueAt(i, j, f.valueOf(m.getValueAt(i, j)));
			}
		}
		return m;
	}

	public DataMatrix multiply(DataMatrix m)
	{
		DataMatrix product = new DataMatrix(this.getRowCount(), this.getColumnCount());
		if (this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount())
		{
			throw new IllegalArgumentException("Matrizes does not have the same size");
		}
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				product.setValueAt(i, j, this.getValueAt(i, j) * m.getValueAt(i, j));
			}
		}
		return product;
	}
	
	public DataMatrix multiply(double x)
	{
		DataMatrix product = new DataMatrix(this.getRowCount(), this.getColumnCount());
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				product.setValueAt(i, j, this.getValueAt(i, j) * x);
			}
		}
		return product;
	}
	
	public DataVector sumRows()
	{
		DataVector v = new DataVector(this.getRowCount());
		for (int i = 0; i < this.getRowCount(); i++)
		{
			double sum = 0;
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				sum += getValueAt(i, j);
			}
			v.setDataAt(i, sum);
		}
		return v;
	}
	
	public DataVector sumCols()
	{
		DataVector v = new DataVector(this.getColumnCount());
		for (int j = 0; j < this.getColumnCount(); j++)
		{
			double sum = 0;
			for (int i = 0; i < this.getRowCount(); i++)
			{
				sum += getValueAt(i, j);
			}
			v.setDataAt(j, sum);
		}
		return v;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof DataMatrix))
			return false;
		DataMatrix m = (DataMatrix)o;
		if (this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount())
			return false;
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				if (getValueAt(i, j) != m.getValueAt(i, j))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("#0.000");
		for (int i = 0; i < this.getRowCount(); i++)
		{
			sb.append("[ ");
			for (int j = 0; j < this.getColumnCount(); j++)
			{
				sb.append(format.format(this.getValueAt(i, j)));
				sb.append(" ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
}
