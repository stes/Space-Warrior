package sw.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class ActionDelegate implements ActionListener
{
	private String _methodName;
	private SWFrame _parent;
	
	public ActionDelegate(SWFrame parent, String method)
	{
		_parent = parent;
		_methodName = method;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Class<? extends SWFrame> cl = _parent.getClass();
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
