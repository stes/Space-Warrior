package sw.pagent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

public class ProbabilityDistribution
{
	private static final Random _random = new Random(System.currentTimeMillis());
	
	private HashMap<PState, double[]> _distribution;
	private int _actions;
	
	public ProbabilityDistribution(int actions)
	{
		_actions = actions;
		_distribution = new HashMap<PState, double[]>();
	}
	
	public int getActions()
	{
		return _actions;
	}

	public double getProbability(int action, PState givenState)
	{
		return _distribution.get(givenState)[action];
	}
	
	public void setProbabilty(int action, PState givenState, double value)
	{
		if (_distribution.containsKey(givenState))
		{
			_distribution.get(givenState)[action] = value;
		}
		else
		{
			initProbabilities(givenState);
			setProbabilty(action, givenState, value);
		}
	}
	
	public void initProbabilities(PState givenState)
	{
		double[] d = new double[getActions()];
		for (int i = 0; i < getActions(); i++)
			d[i] = 1;
		_distribution.put(givenState, d);
	}
	
	public void normalize()
	{
		for (double[] d : _distribution.values())
		{
			double sum = 0;
			for (int j = 0; j < getActions(); j++)
			{
				sum += d[j];
			}
			if (sum == 0)
				continue;
			for (int j = 0; j < getActions(); j++)
			{
				d[j] /= sum;
			}
		}
	}
	
	public void init(PState givenState)
	{
		double[] d = _distribution.get(givenState);
		for (int j = 0; j < getActions(); j++)
		{
			d[j] = 1;
		}
		this.normalize();
	}
	
	public int sampleAction(PState state)
	{
		double[] probs = _distribution.get(state);
		double p = 0;
		for (int i = 0; i < getActions(); i++)
		{
			p += probs[i];
			if (_random.nextDouble() <= p)
				return i;
		}
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (double[] d : _distribution.values())
		{
			for (int j = 0; j < getActions(); j++)
			{
				sb.append("P(" + j + "|" + (i++) + ") = " + d[j] + "\n");
			}
		}
		return sb.toString();
	}
	
	public void save(String directory) throws IOException
	{
		File current = new File(directory + "/probs.txt" );
		current.createNewFile();
		FileOutputStream outp = new FileOutputStream(current);
		OutputStreamWriter writer = new OutputStreamWriter(outp);
		
		for (Entry<PState, double[]> entry : _distribution.entrySet())
		{
			writer.write(entry.getKey().toString());
			writer.write(',');
			for (int i = 0; i < entry.getValue().length; i++)
			{
				writer.write(entry.getValue()[i]+";");
			}
			writer.write('\n');
		}
		writer.close();
	}
	
	public void load(String directory) throws FileNotFoundException
	{
		File current = new File(directory + "/probs.txt" );
		FileInputStream inp = new FileInputStream(current);
		Scanner scanner = new Scanner(inp);
		
		while(scanner.hasNext())
		{
			String currentLine = scanner.nextLine();
			String[] parts = currentLine.split(",");
			PState state = PState.fromString(parts[0]);
			String[] values = parts[1].split(";");
			for (int i = 0; i < values.length; i++)
			{
				if (values[i].equals(""))
					continue;
				this.setProbabilty(i, state, Double.parseDouble(values[i]));
			}
			
		}
	}
}
