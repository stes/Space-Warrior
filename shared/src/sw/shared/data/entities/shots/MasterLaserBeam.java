package sw.shared.data.entities.shots;

import sw.shared.data.entities.players.SpaceShip;

/**
 * 
 * @author Redix, stes
 * @version 08.01.2012
 */
public class MasterLaserBeam extends LaserBeam
{
	public MasterLaserBeam(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, true);
	}
	
}
