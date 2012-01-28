package sw.neuralagent;

import sw.shared.GameConstants;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class Tools
{
	private static final int VALUE_RANGE = 255;

	public static double distanceToNearestShip(SpaceShip player, GameWorld world)
	{
		double max = Math.sqrt(Math.pow(GameConstants.PLAYING_FIELD_WIDTH, 2)
				+ Math.pow(GameConstants.PLAYING_FIELD_HEIGHT, 2));
		SpaceShip s = nextShip(player, world);
		if (s != null)
			return player.distanceTo(s) * VALUE_RANGE / max;
		else
			return VALUE_RANGE;
	}

	public static SpaceShip nextShip(SpaceShip player, GameWorld world)
	{
		double minDist = 1000;
		SpaceShip next = null;
		for (SpaceShip pl : world.getPlayers())
		{
			if (pl.getName().equals(player.getName()))
				continue;
			double dist = pl.distanceTo(player);
			if (dist < minDist)
			{
				minDist = dist;
				next = pl;
			}
		}
		return next;
	}

	/**
	 * @return the angle between the local player's direction and the location
	 *         of the next player
	 */
	public static double angleToNextPlayer(SpaceShip player, GameWorld world)
	{
		SpaceShip next = nextShip(player, world);

		if (next == null)
			return 0;

		double x0 = player.getX();
		double y0 = player.getY();
		double x1 = next.getX();
		double y1 = next.getY();

		// TODO simplify :D
		double alpha = player.getDirection() * VALUE_RANGE / (2 * Math.PI);
		double beta = Math.atan2(x0 - x1, y0 - y1) * VALUE_RANGE / (2 * Math.PI);

		double diff = Math.min(Math.abs(beta - alpha), Math.abs(alpha - beta));
		diff = Math.abs(Math.abs(diff - VALUE_RANGE / 2) - VALUE_RANGE / 2);

		return diff;
	}

	public static double nextPlayersAngle(SpaceShip player, GameWorld world)
	{
		SpaceShip next = nextShip(player, world);

		if (next == null)
			return 0;

		double x0 = next.getX();
		double y0 = next.getY();
		double x1 = player.getX();
		double y1 = player.getY();

		// TODO simplify :D
		double alpha = next.getDirection() * VALUE_RANGE / (2 * Math.PI);
		double beta = Math.atan2(x0 - x1, y0 - y1) * VALUE_RANGE / (2 * Math.PI);

		double diff = Math.min(Math.abs(beta - alpha), Math.abs(alpha - beta));
		diff = Math.abs(Math.abs(diff - VALUE_RANGE / 2) - VALUE_RANGE / 2);

		return diff;
	}
}
