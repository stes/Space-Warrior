package sw.pagent.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sw.pagent.PState;

public class ProbTest
{

	@Before
	public void setUp() throws Exception
	{
		PState state = new PState();
		state.setFeature(0, 100);
		state.setFeature(1, 150);
		state.setFeature(2, 200);
	}

	@Test
	public void testProbabilityDistribution()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetActions()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetProbability()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetProbabilty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testNormalize()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testInit()
	{
		fail("Not yet implemented");
	}

}
