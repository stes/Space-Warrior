package sw.client.player.ai.neuralagent;

import stes.ai.ann.data.DataVector;

public interface IState
{
	boolean isTerminal();

	DataVector getFeatures();

	double getFeature(int i);
}
