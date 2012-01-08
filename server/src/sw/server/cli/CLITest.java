package sw.server.cli;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class CLITest extends TestCase
{
	SWCommandParser _parser;
	
	@Before
	public void setUp() throws Exception
	{
		_parser = new SWCommandParser();
	}

	@Test
	public void testCommands()
	{
		for (CommandType c : CommandType.values())
		{
			Command cmd = new Command(c.toString() + " hallo");
			try
			{
				_parser.performAction(cmd);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
