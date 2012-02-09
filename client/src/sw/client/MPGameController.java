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
package sw.client;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.shared.Packettype;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Controls all gameplay related issues in a multiplayer game
 * 
 * @author Redix, stes
 * @version 25.11.11
 */
public class MPGameController extends GameController implements ClientConnectionListener, ClientMessageListener,
		IGameStateManager
{
	private IClient _client;
	private boolean _isConnected;

	/**
	 * creates an new GameController
	 */
	public MPGameController(IClient client)
	{
		_client = client;
	}

	@Override
	public void chatMessage(String name, String text)
	{}

	@Override
	public void connected()
	{
		this.setIsConnected(true);
		this.init();
	}

	@Override
	public void disconnected(String reason)
	{
		this.setIsConnected(false);
	}

	public boolean isConnected()
	{
		return _isConnected;
	}

	@Override
	public void snapshot(Unpacker snapshot)
	{
		GameWorld world = new GameWorld();
		world.fromSnap(snapshot);
		this.setGameworld(world);
		for (SpaceShip pl : getPlayers())
		{
			if (pl.isLocal())
			{
				getLocalPlayer().setDataSet(pl);
			}
		}
		GameState[] state = getWorld().getEntitiesByType(Packettype.SNAP_GAMESTATE, new GameState[] {});
		if (state.length >= 1)
		{
			if (state[0].isNewRoundStarted())
			{
				this.newRound();
			}
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(getLocalPlayer().getDataSet());
		// TODO only pass a copy!
		e.setGameWorld(getWorld());
		this.invokeStateChanged(e);
	}

	@Override
	public void stateUpdated(PlayerInput input)
	{
		Packer p = input.pack();
		_client.sendPacket(p);
	}

	private void setIsConnected(boolean _isConnected)
	{
		this._isConnected = _isConnected;
	}

	@Override
	public boolean isReady()
	{
		return isConnected();
	}
}
