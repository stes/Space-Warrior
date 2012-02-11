/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.client.player.ai.fagent;

/**
 * Interface for a Policy used by the agent to determine the best action in each
 * state. The Policy maps states to actions using the state transition function.
 * 
 * @author stes
 * @version 02.01.2012
 */
public interface IPolicy
{
	public Action getAction(IState state, ITransitionFunction t);
}
