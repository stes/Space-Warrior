/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
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
package sw.server.cli;

import java.util.ArrayList;

/**
 * 
 * @author Redix, stes
 * @version 08.01.2012
 */
public class Command
{
	private CommandType _type;
	private String[] _args;
	private String _owner;
	
	public Command(String command, String owner)
	{
		this.parseString(command);
		_owner = owner;
	}
	
	private void parseString(String commandString)
	{
		System.out.println("string: " + commandString);
		String[] substrings = commandString.split(" ");
		ArrayList<String> parts = new ArrayList<String>();
		// remove empty entries
		for (int i = 0; i < substrings.length; i++)
		{
			if (!substrings[i].isEmpty())
				parts.add(substrings[i]);
		}
		
		String cmd = parts.get(0);
		parts.remove(0);
		
		_args = parts.toArray(new String[]{});
		_type = CommandType.valueOf(cmd.toUpperCase());
		
	}
	
	public CommandType getType()
	{
		return _type;
	}
	
	public String getArgument(int index)
	{
		return _args[index];
	}
	
	public int getArgumentCount()
	{
		return _args.length;
	}

	public String getOwner()
	{
		return _owner;
	}
}
