package sw.neuralagent;

import sw.client.player.ai.fagent.data.DataVector;
import sw.shared.data.entities.players.SpaceShip;

public interface IState
{

	boolean isTerminal();

	DataVector getFeatures();

	double getFeature(int i);


}
