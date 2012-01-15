package sw.client.plugins.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sw.client.plugins.AIPlayerLoader;
import sw.client.plugins.PluginLoader;

public class PluginLoaderTest
{
	PluginLoader _ploader;
	
	@Before
	public void setUp() throws Exception
	{
		_ploader = new PluginLoader();
		_ploader.addDirectory("C:\\Users\\Steffen\\Projekte\\Projekte\\SpaceWarrior\\current_build", "sample");
	}

	@Test
	public void testGetAIs()
	{
		File[] files = _ploader.getAIs("sample");
		for (File f : files)
		{
			System.out.println(f.getAbsolutePath() + "  " + AIPlayerLoader.isValid(f));
		}
	}

}
