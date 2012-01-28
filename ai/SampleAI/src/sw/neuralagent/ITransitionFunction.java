/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

/**
 * Interface for a State Transition Function. A state transition function maps a
 * given state and a given action to the successor state
 * 
 * @author stes
 * @version 02.01.2012
 */
public interface ITransitionFunction
{
	public int getSuccessor(int state, int a);
}
