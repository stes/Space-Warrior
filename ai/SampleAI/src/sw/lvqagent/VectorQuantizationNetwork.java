package sw.lvqagent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class VectorQuantizationNetwork
{
	// for test purposes
	public static void main(String[] args)
	{
		VectorQuantizationNetwork map = new VectorQuantizationNetwork(3, 2, 0.5);
		map.addTrainingSet(new double[] { 0.0, 0.0, 5.0 });
		map.addTrainingSet(new double[] { 0.0, 0.0, 5.0 });
		map.addTrainingSet(new double[] { 0.0, 0.0, 5.0 });
		map.addTrainingSet(new double[] { 1.0, 0.0, 0.0 });
		map.addTrainingSet(new double[] { 1.0, 0.0, 0.0 });
		map.addTrainingSet(new double[] { 1.0, 0.0, 0.0 });
		map.addTrainingSet(new double[] { 1.0, 0.0, 0.0 });
		map.addTrainingSet(new double[] { 1.0, 0.0, 0.0 });

		System.out.println("start");
		while (true)
		{
			for (int i = 0; i < 20; i++)
			{
				System.out.println();
			}
			map.trainIteration();
			map.printResults();
			try
			{
				System.in.read();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static Random _random = new Random();

	private int _inputNeurons;
	private int _outputNeurons;
	private double[][] _weights;
	private double[] _netInputs;
	private double[] _outputs;
	private double[] _currentInput;
	private double[] _normalizer;
	private int _winner;
	private double _learnrate;
	
	public double[] getCurrentInput()
	{
		return _currentInput;
	}

	public void setCurrentInput(double[] trainingSet)
	{
		_currentInput = trainingSet.clone();
	}

	private ArrayList<double[]> _trainingSets;

	public VectorQuantizationNetwork(int inputNeurons, int outputNeurons, double learnrate)
	{
		_trainingSets = new ArrayList<double[]>();
		_inputNeurons = inputNeurons;
		_outputNeurons = outputNeurons;
		_learnrate = learnrate;
		_normalizer = new double[_inputNeurons];
		_weights = new double[_inputNeurons][_outputNeurons];
		_netInputs = new double[_outputNeurons];
		_outputs = new double[_outputNeurons];
		setCurrentInput(new double[_inputNeurons]);
		this.initWeights();
	}

	public void addTrainingSet(double[] trainingSet)
	{
		if (trainingSet.length != _inputNeurons)
			throw new IllegalArgumentException();
		_trainingSets.add(trainingSet);
		this.updateNormalizer(trainingSet);
	}

	public void trainIteration()
	{
		for (double[] trainingSet : _trainingSets)
		{
			setCurrentInput(trainingSet);
			this.calcOutput();
			for (int i = 0; i < _inputNeurons; i++)
			{
				_weights[i][_winner] += _learnrate * _outputs[_winner] * getCurrentInput()[i];
			}
		}
	}

	private double neuronOutput(double netInput)
	{
		return Math.tanh(netInput);
	}

	private void calcNetInput()
	{
		for (int j = 0; j < _outputNeurons; j++)
		{
			_netInputs[j] = 0.0;
			for (int i = 0; i < _inputNeurons; i++)
			{
				_netInputs[j] += _weights[i][j] * getCurrentInput()[i];
			}
		}
	}

	public void calcOutput()
	{
		_winner = -1;
		double maxValue = Double.NEGATIVE_INFINITY;
		this.calcNetInput();
		for (int j = 0; j < _weights[0].length; j++)
		{
			_outputs[j] = this.neuronOutput(_netInputs[j]);
			if (_outputs[j] > maxValue)
			{
				maxValue = _outputs[j];
				_winner = j;
			}
		}
	}

	private void updateNormalizer(double[] trainingSet)
	{
		for (int i = 0; i < _inputNeurons; i++)
		{
			_normalizer[i] += trainingSet[i];
		}
	}

	private void normalize()
	{
		for (int i = 0; i < _inputNeurons; i++)
		{
			if (_normalizer[i] != 0)
			{
				getCurrentInput()[i] /= _normalizer[i];
				getCurrentInput()[i] = getCurrentInput()[i] * 2 - 1;
			}
		}
	}

	private void initWeights()
	{
		for (int i = 0; i < _weights.length; i++)
		{
			for (int j = 0; j < _weights[0].length; j++)
			{
				_weights[i][j] = _random.nextDouble() * 2 - 1;
			}
		}
	}

	public void printWeights()
	{
		for (int i = 0; i < _inputNeurons; i++)
		{
			for (int j = 0; j < _outputNeurons; j++)
			{
				System.out.print(_weights[i][j] + "; ");
			}
			System.out.println();
		}
	}

	public void printResults()
	{
		for (double[] trainingSet : _trainingSets)
		{
			setCurrentInput(trainingSet);
			this.calcOutput();
			for (int i = 0; i < _inputNeurons; i++)
			{
				System.out.print(trainingSet[i] + " ;");
			}
			System.out.print(" => ");
			System.out.println(_winner);
		}
	}
}
