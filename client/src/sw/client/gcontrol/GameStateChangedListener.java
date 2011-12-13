package sw.client.gcontrol;

/**
 * 
 * @author Redix stes Abbadonn
 * @version 02.12.2011
 */
public interface GameStateChangedListener
{
	public void gameStateChanged(GameStateChangedEvent e);
	
	public void newRound(GameStateChangedEvent e);

	public void playerInit(GameStateChangedEvent e);
}
