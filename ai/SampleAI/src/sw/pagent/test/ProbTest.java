package sw.pagent.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import sw.pagent.PState;
import sw.pagent.ProbabilityDistribution;

public class ProbTest extends TestCase
{
	ProbabilityDistribution _distribution;
	PState _state;
	
	@Before
	public void setUp() throws Exception
	{
		_distribution = new ProbabilityDistribution(3);
		_state = new PState();
		_state.setFeature(0, 100);
		_state.setFeature(1, 150);
		_state.setFeature(2, 200);
	}

	@Test
	public void testDistribution()
	{
		_distribution.setProbabilty(0, _state, 5);
		_distribution.setProbabilty(1, _state, 1);
		_distribution.setProbabilty(2, _state, 2);
		
		System.out.println(_distribution.toString());
		
		_distribution.normalize();
		
		System.out.println(_distribution.toString());
		
		System.out.println(_distribution.sampleAction(_state));
	}
	
	@Test
	public void testState()
	{
		System.out.println(_state.toString());
		
		assertTrue(_state.equals(PState.fromString(_state.toString())));
	}
	
	@Test
	public void testIO()
	{
		_distribution.setProbabilty(0, _state, 5);
		_distribution.setProbabilty(1, _state, 1);
		_distribution.setProbabilty(2, _state, 2);
		
		ProbabilityDistribution p = new ProbabilityDistribution(3);
		try
		{
			_distribution.save(System.getProperty("user.dir"));
			 p.load(System.getProperty("user.dir"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(_distribution.toString());
		System.out.println(p.toString());
		assertEquals(_distribution,  p);
	}
}
