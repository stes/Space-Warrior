/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

import sw.client.player.ai.neuralagent.IState;

/**
 * Interface for a Value Function. A value function maps states to values
 * 
 * @author stes
 * @version 02.01.2012
 */
public interface IValueFunction
{
	public double getValue(IState state);

	public void setValue(IState state, double value);

}
