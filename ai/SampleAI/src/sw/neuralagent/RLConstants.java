/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

public class RLConstants
{
	public static final double DISCOUNT_FACTOR = 0.7;

	public static final int MAX_ACTIONS = 4;
	public static final int MAX_STATES = GridWorldPanel.WORLD.length
			* GridWorldPanel.WORLD[0].length;
}
