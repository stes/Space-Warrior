package sw.client;

import java.util.ArrayList;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.player.Player;
import sw.client.player.ai.AIPlayer;
import sw.client.player.ai.bots.RandomBot;
import sw.client.player.ai.fagent.FollowAgent;
import sw.shared.GameConstants.Images;
import sw.shared.Packettype;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Controls all gameplay related issues in a singleplayer game
 * 
 * @author Redix, stes
 * @version 25.11.11
 */
public class SPGameController extends GameController
{
	private static SPGameEngine _gameEngine = new SPGameEngine();
	
	private ArrayList<Player> _opponents;
	
	public SPGameController()
	{
		super();
		_opponents = new ArrayList<Player>();
		_gameEngine.addListener(this);
	}
	
	private SPGameController(AIPlayer player)
	{
		super();
		setLocalPlayer(player);
		player.setStateManager(this);
		_gameEngine.addListener(this);
	}
	
	@Override
	public void init()
	{
		super.init();
		//_opponents.add(new SPGameController(new FollowAgent("Killer")).getLocalPlayer());
		_opponents.add(new SPGameController(new RandomBot("Simple")).getLocalPlayer());
		_opponents.add(new SPGameController(new RandomBot("Simple2")).getLocalPlayer());
		
		_gameEngine.addPlayer(getLocalPlayer().getDataSet().getName(), getLocalPlayer().getDataSet().getImageID(), true);
		for (Player p : _opponents)
		{
			_gameEngine.addPlayer(p.getDataSet().getName(), Images.SHIP_1.getID());
		}
		_gameEngine.startGame();
		_gameEngine.start();
	}

	@Override
	public void stateUpdated(PlayerInput input)
	{
		_gameEngine.playerInput(getLocalPlayer().getDataSet().getName(), input);
	}

	public void snapshot()
	{
		this.setGameworld(_gameEngine.getWorld());
		for (SpaceShip pl : getPlayers())
		{
			if (pl.getName().equals(getLocalPlayer().getDataSet().getName()))
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
	public boolean isReady()
	{
		return true;
	}
}
