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
package sw.shared.net;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Unpacker implements DataInput
{
	private ByteArrayInputStream _input;
	private DataInputStream _data;

	private byte _type;

	public Unpacker(byte[] data)
	{
		_input = new ByteArrayInputStream(data);
		_data = new DataInputStream(_input);

		_type = this.readByte();
	}

	public byte getType()
	{
		return _type;
	}

	@Override
	public boolean readBoolean()
	{
		try
		{
			return _data.readBoolean();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public byte readByte()
	{
		try
		{
			return _data.readByte();
		}
		catch (EOFException e)
		{
			throw new IllegalStateException(e);
		}
		catch (IOException impossible)
		{
			throw new AssertionError(impossible);
		}
	}

	@Override
	public char readChar()
	{
		try
		{
			return _data.readChar();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public double readDouble()
	{
		try
		{
			return _data.readDouble();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public float readFloat()
	{
		try
		{
			return _data.readFloat();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void readFully(byte b[])
	{
		try
		{
			_data.readFully(b);
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void readFully(byte b[], int off, int len)
	{
		try
		{
			_data.readFully(b, off, len);
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int readInt()
	{
		try
		{
			return _data.readInt();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Deprecated
	@Override
	public String readLine()
	{
		try
		{
			return _data.readLine();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public long readLong()
	{
		try
		{
			return _data.readLong();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public short readShort()
	{
		try
		{
			return _data.readShort();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int readUnsignedByte()
	{
		try
		{
			return _data.readUnsignedByte();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int readUnsignedShort()
	{
		try
		{
			return _data.readUnsignedShort();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String readUTF()
	{
		try
		{
			return _data.readUTF();
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int skipBytes(int n)
	{
		try
		{
			return _data.skipBytes(n);
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}
}