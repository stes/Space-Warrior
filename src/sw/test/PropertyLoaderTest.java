package sw.test;

import sw.server.PropertyLoader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PropertyLoaderTest extends TestCase
{
	private PropertyLoader _propLoader;
	
	@Override
	public void setUp()
	{
		_propLoader = new PropertyLoader();
	}
	
	@Override
	public void tearDown()
	{
		
	}
	
	public void test()
	{
		System.out.println(_propLoader.getMaxPlayers());
		System.out.println(_propLoader.getPort());
		System.out.println(_propLoader.getServerName());
	}
	
	public static Test suite()
	{
		return new TestSuite(PropertyLoaderTest.class);
	}
}
