package sw.shared.data.entities.shots;

import sw.shared.data.entities.players.SpaceShip;

/**
 * 
 * @author Redix, stes
 * @version 08.01.2012
 */
public class MasterLaserBeam extends LaserBeam
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 25948694013123675L;

	public MasterLaserBeam(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, true);
	}
	
}
