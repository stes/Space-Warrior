package sw.neuralagent;

import stes.ai.ann.data.DataVector;
import sw.shared.data.entities.players.SpaceShip;

public interface IState
{

	boolean isTerminal();

	DataVector getFeatures();

	double getFeature(int i);


}
