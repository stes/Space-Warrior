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
package sw.shared.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Packer implements DataOutput
{
	private ByteArrayOutputStream _output;
	private DataOutputStream _data;

	public Packer(byte type)
	{
		_output = new ByteArrayOutputStream();
		_data = new DataOutputStream(_output);

		this.writeByte(type);
	}

	@Override
	public void write(int b)
	{
		try
		{
			_data.write(b);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void write(byte[] b)
	{
		try
		{
			_data.write(b);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void write(byte[] b, int off, int len)
	{
		try
		{
			_data.write(b, off, len);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeBoolean(boolean v)
	{
		try
		{
			_data.writeBoolean(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeByte(int v)
	{
		try
		{
			_data.writeByte(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeBytes(String s)
	{
		try
		{
			_data.writeBytes(s);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeChar(int v)
	{
		try
		{
			_data.writeChar(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeChars(String s)
	{
		try
		{
			_data.writeChars(s);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeDouble(double v)
	{
		try
		{
			_data.writeDouble(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeFloat(float v)
	{
		try
		{
			_data.writeFloat(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeInt(int v)
	{
		try
		{
			_data.writeInt(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeLong(long v)
	{
		try
		{
			_data.writeLong(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeShort(int v)
	{
		try
		{
			_data.writeShort(v);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeUTF(String s)
	{
		try
		{
			_data.writeUTF(s);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public byte[] toByteArray()
	{
		return _output.toByteArray();
	}
}