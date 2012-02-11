package sw.client.player.ai.fagent;

import sw.client.player.ai.fagent.data.DataVector;

public interface IState
{
	boolean isTerminal();

	DataVector getFeatures();

	double getFeature(int i);
}
