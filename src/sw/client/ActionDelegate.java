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