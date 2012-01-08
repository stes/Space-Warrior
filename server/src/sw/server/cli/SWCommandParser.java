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

import sw.server.GameController;
import sw.server.IServer;
import sw.shared.Packettype;
import sw.shared.net.Packer;

/**
 * @author Redix, stes
 * @version 08.01.2012
 */
public class SWCommandParser extends CommandParser
{
	private IServer _server;
	// TODO use some interface
	private GameController _gameController;

	public SWCommandParser(IServer server, GameController controller)
	{
		_server = server;
		_gameController = controller;
	}

	public void processKick(Command command)
	{
		_server.kick(command.getArgument(0));
	}

	public void processBan(Command command)
	{
		_server.ban(command.getArgument(0));
	}

	public void processStop(Command command)
	{
		_server.close();
	}

	public void processName(Command command)
	{
		_server.setServerName(command.getArgument(0));
	}

	public void processRestart(Command command)
	{
		_gameController.startGame();
	}

	public void processHelp(Command command)
	{
		StringBuilder sb = new StringBuilder();
		if (command.getArgumentCount() == 0)
		{
			for (CommandType type : CommandType.values())
			{
				appendHelp(sb, type);
			}
		}
		else
		{
			CommandType type = CommandType.valueOf(command.getArgument(0));
			appendHelp(sb, type);
		}
		Packer p = new Packer(Packettype.SV_CHAT_MESSAGE);
		p.writeUTF("");
		p.writeUTF(sb.toString());
		_server.sendPacket(command.getOwner(), p);
	}

	private void appendHelp(StringBuilder sb, CommandType type)
	{
		sb.append(type.getSyntax());
		sb.append("\t");
		sb.append(type.getHelpMessage());
		sb.append(System.getProperty("line.separator"));
	}

	public void processInfo(Command command)
	{
		// TODO implement
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
