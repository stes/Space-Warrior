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

public enum CommandType
{
	KICK("<player name>", "kicks the specified player"),
	BAN("<ip address>", "bans the specified ip"),
	STOP("\t", "forces the server to shut down"),
	NAME("<server name>", "changes the servername"),
	RESTART("\t", "starts a new round"),
	HELP("[command name]", "displays this help, or the help of the specified command"),
	INFO("\t","displays the server info");
	
	private String _helpMessage;
	private String _syntax;
	private CommandType(String syntax, String help)
	{
		_syntax = syntax;
		_helpMessage = help;
	}
	
	public String getHelpMessage()
	{
		return _helpMessage;
	}
	
	public String getSyntax()
	{
		return this.toString() + " " + _syntax;
	}
}
