/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Delegates an invoked {@see java.awt.event.ActionEvent} to a specified <p>
 * method 
 * 
 * @author stes
 * @version 26.11.11
 */
public class ActionDelegate implements ActionListener
{
	private String _methodName;
	private JComponent _parent;
	
	public ActionDelegate(JComponent parent, String method)
	{
		_parent = parent;
		_methodName = method;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Class<? extends JComponent> cl = _parent.getClass();
		try
		{
			Method m = cl.getMethod(_methodName, e.getClass());
			m.invoke(_parent, e);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
