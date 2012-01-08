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

package sw.server.cli;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author Redix, stes
 * @version 08.01.2012
 */
public class CommandParser
{
	public CommandParser()
	{

	}

	public void performAction(Command command)
			throws ClassNotFoundException,
			SecurityException,
			NoSuchMethodException,
			IllegalArgumentException,
			IllegalAccessException,
			InvocationTargetException
	{
		Class<?> cls = Class.forName("sw.server.cli.SWCommandParser");
		String commandName = command.getType().toString();
		commandName = commandName.substring(0, 1).toUpperCase() + commandName.substring(1).toLowerCase();
		String methodName = "process" + commandName;
		Method m = cls.getMethod(methodName, Command.class);
		m.invoke(this, command);
	}
}
