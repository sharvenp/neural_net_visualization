// VNet Raw NN
// Author: Sharven Prasad Dhanasekar

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class RawNN extends JPanel{
	
	int inputLength;
	int hiddenLength;
	int outputLength;
	int hiddenLayCount;
	
	Node[] inputNeurons;
	Node[][] hiddenNeurons;
	Node[] outputNeurons;
	
	float[] answer;
	Random r;
	
	float learningRate;
	
	JLabel lossLabel;
	int windowSize;
	
	int interval = 0;
	int size = 20;
	
	private void initComponents() {
		setSize(100 * hiddenLayCount + 2, windowSize* 100);
		setLayout(null);
		setBackground(Color.black);
		
		lossLabel = new JLabel("size",JLabel.LEFT);		
		lossLabel.setBounds(0,0,300,20);
		lossLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lossLabel.setForeground(Color.white);
		lossLabel.setText("");
		add(lossLabel);
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0,0,600 +  (100 * (hiddenLayCount)), windowSize + 50);
		
		float max = Float.NEGATIVE_INFINITY;
		float min = Float.POSITIVE_INFINITY;
		
		// Drawing Synapses
		for (int i = 0; i < inputLength; i++)
		{
			ArrayList<Synapse> syn = inputNeurons[i].getRightSynapse();
			for (Synapse s : syn)
			{
				float weight = s.getWeight();
				
				if (weight > max && weight > 0f)
					max = weight;
				
				if (weight < min && weight < 0f)
					min = weight;
				
				if (weight < 0f)
				{
					g.setColor(new Color(0.79f,0.46f,0.47f,(float)Math.abs(weight/min)));
				}
				else if (weight > 0f)
				{
					g.setColor(new Color(0.05f,0.75f,0.29f,(float)Math.abs(weight/max)));
				}
				
				g.drawLine(s.getLeftNode().getX() + size, s.getLeftNode().getY() + size/2, s.getRightNode().getX(), s.getRightNode().getY() + size/2);
			}
		}
		
		max = Float.NEGATIVE_INFINITY;
		min = Float.POSITIVE_INFINITY;
		
		for (int col = 0; col < hiddenLayCount; col++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				ArrayList<Synapse> syn = hiddenNeurons[row][col].getRightSynapse();
				for (Synapse s : syn)
				{					
					float weight = s.getWeight();
					
					if (weight > max && weight > 0)
						max = weight;
					
					if (weight < min && weight < 0)
						min = weight;
					
					if (weight < 0f)
					{
						g.setColor(new Color(0.79f,0.46f,0.47f,(float)Math.abs(weight/min)));
					}
					else if (weight > 0f)
					{
						g.setColor(new Color(0.05f,0.75f,0.29f,(float)Math.abs(weight/max)));
					}
					
					g.drawLine(s.getLeftNode().getX() + size, s.getLeftNode().getY() + size/2, s.getRightNode().getX(), s.getRightNode().getY() + size/2);
				}
			}
		}
		
		// Drawing Nodes
		for (int input = 0; input < inputLength; input++)
		{	
			int x = inputNeurons[input].getX();
			int y = inputNeurons[input].getY();
			g.setColor(new Color(inputNeurons[input].getValue(),inputNeurons[input].getValue(),inputNeurons[input].getValue()));
			g.fillOval(x,y,size,size);
			g.setColor(Color.white);
			g.drawOval(x,y,size, size);
		}
		
		for (int col = 0; col < hiddenLayCount; col++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{		
				int x = hiddenNeurons[row][col].getX();
				int y = hiddenNeurons[row][col].getY();
				g.setColor(new Color(hiddenNeurons[row][col].getValue(),hiddenNeurons[row][col].getValue(),hiddenNeurons[row][col].getValue()));
				g.fillOval(x, y,size,size);
				g.setColor(Color.white);
				g.drawOval(x,y,size, size);
			}
		}
		
		for (int out = 0; out < outputLength; out++)
		{
			int x = outputNeurons[out].getX();
			int y = outputNeurons[out].getY();
			g.setColor(new Color(outputNeurons[out].getValue(),outputNeurons[out].getValue(),outputNeurons[out].getValue()));
			g.fillOval(x,y,size,size);
			g.setColor(Color.white);
			g.drawOval(x,y,size, size);
		}
	}
	
	public RawNN(int inputCount, int hiddenLayers, int hiddenCount, int outputCount, float learnRate, int winSize) {
		
		windowSize = winSize - 50;
		inputLength = inputCount;
		hiddenLength = hiddenCount;
		outputLength = outputCount;
		hiddenLayCount = hiddenLayers;
		
		inputNeurons = new Node[inputLength];
		hiddenNeurons = new Node[hiddenLength][hiddenLayers];
		outputNeurons = new Node[outputLength];
		
		r = new Random();
		answer = new float[outputLength];
		r.setSeed(1);
		learningRate = learnRate;
		
		initComponents();
		
		float defualtInitialization = 0.2f;
		
		for (int i = 0; i < inputLength; i++)
		{
			inputNeurons[i] = new Node();
			int x = 125;
			int y = calculatePositioning(i, inputLength);;
			inputNeurons[i].setXY(x,y);
		}
		
		for (int row = 0; row < hiddenLength; row++)
		{
			for (int col = 0; col < hiddenLayers; col++)
			{
				hiddenNeurons[row][col] = new Node();
				hiddenNeurons[row][col].setBias(getRandomFloat(-defualtInitialization, defualtInitialization));
				int x = 100 * col + 275;
				int y = calculatePositioning(row, hiddenLength);
				hiddenNeurons[row][col].setXY(x, y);
			}
		}
		
		for (int i = 0; i < outputLength; i++)
		{
			outputNeurons[i] = new Node();
			outputNeurons[i].setBias(getRandomFloat(-defualtInitialization, defualtInitialization));
			int x = 100 * hiddenLayCount + 375;
			int y = calculatePositioning(i, outputLength);
			outputNeurons[i].setXY(x,y);
		}
		
		//Input-Hidden Connections
		for (int i = 0; i < inputLength; i++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				Synapse syn = new Synapse();
				syn.setWeight(getRandomFloat(-defualtInitialization, defualtInitialization));
				syn.setLeftNode(inputNeurons[i]);
				syn.setRightNode(hiddenNeurons[row][0]);
				inputNeurons[i].addRightSynapse(syn);
				hiddenNeurons[row][0].addLeftSynapse(syn);
			}
		}
		
		//Hidden-Hidden Connections
		for (int col = 0; col < hiddenLayers - 1; col++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				for (int row2 = 0; row2 < hiddenLength; row2++)
				{
					Synapse syn = new Synapse();
					syn.setWeight(getRandomFloat(-defualtInitialization, defualtInitialization));
					syn.setLeftNode(hiddenNeurons[row][col]);
					syn.setRightNode(hiddenNeurons[row2][col+1]);
					hiddenNeurons[row][col].addRightSynapse(syn);
					hiddenNeurons[row2][col+1].addLeftSynapse(syn);
				}
			}
		}
		
		//Hidden-Output Connections
		for (int row = 0; row < hiddenLength; row++)
		{
			for (int i = 0; i < outputLength; i++)
			{
				Synapse syn = new Synapse();
				syn.setWeight(getRandomFloat(-defualtInitialization, defualtInitialization));
				syn.setLeftNode(hiddenNeurons[row][hiddenLayers - 1]);
				syn.setRightNode(outputNeurons[i]);
				hiddenNeurons[row][hiddenLayers - 1].addRightSynapse(syn);
				outputNeurons[i].addLeftSynapse(syn);
			}
		}
	}
	
	private int calculatePositioning(int i, int length)
	{
		int affordableGap = ((windowSize)/length) - 10;
		if (affordableGap < size)
			affordableGap += size/2;
		int totalGap = affordableGap * (length - 1);
		int offSet = (windowSize - totalGap) / 2;
		return (affordableGap * i) + offSet;
	}
	
	private void propagateForward() {
		
		for (int col = 0; col < hiddenLayCount; col++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				float sum = 0f;
				Node hidden = hiddenNeurons[row][col];
				ArrayList<Synapse> connectedSyn = hidden.getLeftSynapse();
				for (int i = 0; i < connectedSyn.size(); i++)
				{
					Synapse syn = connectedSyn.get(i);
					Node connectedNode = syn.getLeftNode();
					sum += connectedNode.getValue() * syn.getWeight();
				}
				float z = sum + hidden.getBias();
				hidden.setZValue(z);
				float value = sigmoid(z, false);
				hidden.setValue(value);
			}
		}
		
		for (int i = 0; i < outputLength; i++)
		{
			float sum = 0f;
			ArrayList<Synapse> connectedSyn = outputNeurons[i].getLeftSynapse();
			for (int s = 0; s < connectedSyn.size(); s++)
			{
				Synapse syn = connectedSyn.get(s);
				Node connectedNode = syn.getLeftNode();
				sum += connectedNode.getValue() * syn.getWeight();
			}
			float z = sum + outputNeurons[i].getBias();
			outputNeurons[i].setZValue(z);
			float value = sigmoid(z, false);
			outputNeurons[i].setValue(value);
		}
	}
	
	public void propagateBackward() {
		
		for (int i = 0; i < outputLength; i++)
		{
			outputNeurons[i].setDelta(sigmoid(outputNeurons[i].getZValue(), true) * (outputNeurons[i].getValue() - answer[i]));
			ArrayList<Synapse> connectedSyn = outputNeurons[i].getLeftSynapse();
			for (int s = 0; s < connectedSyn.size(); s++)
			{
				float pa = connectedSyn.get(s).getLeftNode().getValue();
				connectedSyn.get(s).setDelta(outputNeurons[i].getDelta() * learningRate * pa);
			}
			float currentBias = outputNeurons[i].getBias();
			outputNeurons[i].setBias(currentBias - (outputNeurons[i].getDelta() * learningRate));
		}
		
		for (int col = hiddenLayCount - 1; col > 0; col--)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				ArrayList<Synapse> connectedForwardSyn = hiddenNeurons[row][col].getRightSynapse();
				float sum = 0f;
				for (int s = 0; s < connectedForwardSyn.size(); s++)
				{
					sum += connectedForwardSyn.get(s).getWeight() * connectedForwardSyn.get(s).getRightNode().getDelta();
				}
				hiddenNeurons[row][col].setDelta(sum * sigmoid(hiddenNeurons[row][col].getZValue(), true));
				ArrayList<Synapse> connectedSyn = hiddenNeurons[row][col].getLeftSynapse();
				for (int s = 0; s < connectedSyn.size(); s++)
				{
					float pa = connectedSyn.get(s).getLeftNode().getValue();
					connectedSyn.get(s).setDelta(hiddenNeurons[row][col].getDelta() * pa * learningRate);
				}
				float currentBias = hiddenNeurons[row][col].getBias();
				hiddenNeurons[row][col].setBias(currentBias - (hiddenNeurons[row][col].getDelta() * learningRate));
			}
		}
		
		// Update Weights
		for (int i = 0; i < inputLength; i++)
		{
			ArrayList<Synapse> connectedSyn = inputNeurons[i].getRightSynapse();
			for (int s = 0; s < connectedSyn.size(); s++)
			{
				connectedSyn.get(s).updateWeight();
			}
		}
		
		for (int col = 0; col < hiddenLayCount; col++)
		{
			for (int row = 0; row < hiddenLength; row++)
			{
				ArrayList<Synapse> connectedSyn = hiddenNeurons[row][col].getRightSynapse();
				for (int s = 0; s < connectedSyn.size(); s++)
				{
					connectedSyn.get(s).updateWeight();
				}
			}
		}
	}
	
	public void input(int[] input)
	{
		//Reset Input
		for (int i = 0; i < inputLength; i++)
		{
			inputNeurons[i].setValue(0f);
			inputNeurons[i].setValue(input[i]);
		}
		propagateForward();
	}
	
	public int output()
	{
		float max = -1000f;
		int index = 0;
		for (int i = 0; i < outputLength; i++)
		{
			if (outputNeurons[i].getValue() > max)
			{
				max = outputNeurons[i].getValue();
				index = i;
			}
		}
		return index;
	}
	
	public void setAnswer(int trueMove)
	{
		answer = new float[outputLength];
		answer[trueMove] = 1f;
	}
	
	private void delay(int amount)
	{
		try
		{
			Thread.sleep(amount);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	// Binary to Decimal Converter Training
	public void testTrain()
	{
		ArrayList<String> dataset = new ArrayList<>();
		
		String form = "%" + Integer.toString((int)((Math.log10((double)outputLength))/(Math.log10(2.0)))) + "s";
		
		for (int i = 0; i < outputLength; i++)
		{
			dataset.add(String.format(form,Integer.toBinaryString(i)).replace(' ', '0'));
			//System.out.println(dataset.get(i) + " True Val: " + Integer.parseInt(dataset.get(i), 2));
		}
		
		Collections.shuffle(dataset);
		float sum = 0f;
		int count = 0;
		int correct = 0;
		for (String s : dataset)
		{
			int[] arr = new int[(int)((Math.log10((double)outputLength))/(Math.log10(2.0)))];
			
			char[] c = s.toCharArray();
			
			for (int x = 0; x < c.length; x++)
			{
				arr[x] = Integer.parseInt(Character.toString(c[x]));
			}
			
			int trueVal = Integer.parseInt(s, 2);
			
			input(arr);
			setAnswer(trueVal);
			repaint();
			
			sum += costFunction();
			count++;
			interval++;
			if (output() == trueVal)
			{
				correct++;
			}
			propagateBackward();
		}
		
		// === Uncomment this block to enable saving === 
		// if (interval % 100000 == 0)
		// {
			// 	String name = String.format("VNet_Save_%d_%d_%d_%d", inputLength, hiddenLayCount, hiddenLength, outputLength);
			// 	saveNet("savednets//"+name);
			// }
		String text = String.format("LOSS: %.8f  CORRECT: %.2f", (float)(sum/count), ((float)correct/count)*100.0f );
		lossLabel.setText(text);
			//System.out.println("LOSS : " + (float) (1f/20f)*(sum/count) + " CORRECT : " + Math.round((double)correct/count*100.0) + "%");
	}

	public void test(String binaryText)
	{
		int[] arr = new int[(int)((Math.log10((double)outputLength))/(Math.log10(2.0)))];
		char[] c = binaryText.toCharArray();

		for (int x = 0; x < inputLength; x++)
		{
			arr[x] = Integer.parseInt(Character.toString(c[x]));
		}

		input(arr);
		repaint();

		lossLabel.setText("Loss: " + Float.toString(costFunction()));
	}

	public float costFunction() {
		float sum = 0f;
		for (int i = 0; i < outputLength; i++) {
			sum += ((float) Math.pow((outputNeurons[i].getValue() - answer[i]), 2));
		}
		return sum;
	}

	private float getRandomFloat(float min, float max) {
		return min + r.nextFloat() * (max - min);
	}

	private float sigmoid(float input, boolean derivative) {
		if (derivative) {
			float val = sigmoid(input, false);
			return val * (1f - val);
		}
		return (float) (1f / (1f + (Math.exp(-input))));
	}


	public void saveNet(String name) {
		try {

			FileWriter fw = new FileWriter(name + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);

			for (int input = 0; input < inputLength; input++) {
				ArrayList<Synapse> synapses = inputNeurons[input].getRightSynapse();
				for (int synapseIndex = 0; synapseIndex < synapses.size(); synapseIndex++) {
					bw.write(Float.toString(synapses.get(synapseIndex).getWeight()));
					bw.newLine();
				}
			}

			for (int col = 0; col < hiddenLayCount; col++) {
				for (int row = 0; row < hiddenLength; row++)
				{
					bw.write(Float.toString(hiddenNeurons[row][col].getBias()));
					bw.newLine();
				}
			}

			for (int col = 0; col < hiddenLayCount; col++) {
				for (int row = 0; row < hiddenLength; row++)
				{
					ArrayList<Synapse> synapses = hiddenNeurons[row][col].getRightSynapse();
					for (int synapseIndex = 0; synapseIndex < synapses.size(); synapseIndex++) {
						bw.write(Float.toString(synapses.get(synapseIndex).getWeight()));
						bw.newLine();
					}
				}
			}

			for (int out = 0; out < outputLength; out++) {
				bw.write(Float.toString(outputNeurons[out].getBias()));
				bw.newLine();
			}

			System.out.println("Done Saving.");
			bw.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public boolean loadNet(String fileName) {
		String directory = fileName + ".txt";
		boolean loaded = true;
		try {

			FileReader fr = new FileReader(directory);
			BufferedReader br = new BufferedReader(fr);

			for (int input = 0; input < inputLength; input++) {
				ArrayList<Synapse> synapses = inputNeurons[input].getRightSynapse();
				for (int synapseIndex = 0; synapseIndex < synapses.size(); synapseIndex++) {
					float val = Float.parseFloat(br.readLine());
					synapses.get(synapseIndex).setWeight(val);
				}
			}

			for (int col = 0; col < hiddenLayCount; col++) {
				for (int row = 0; row < hiddenLength; row++)
				{
					float val = Float.parseFloat(br.readLine());
					hiddenNeurons[row][col].setBias(val);
				}
			}

			for (int col = 0; col < hiddenLayCount; col++) {
				for (int row = 0; row < hiddenLength; row++)
				{
					ArrayList<Synapse> synapses = hiddenNeurons[row][col].getRightSynapse();
					for (int synapseIndex = 0; synapseIndex < synapses.size(); synapseIndex++) {
						float val = Float.parseFloat(br.readLine());
						synapses.get(synapseIndex).setWeight(val);
					}
				}
			}

			for (int out = 0; out < outputLength; out++) {
				float val = Float.parseFloat(br.readLine());
				outputNeurons[out].setBias(val);
			}

			System.out.println("Loaded " + fileName);
			br.close();

		} catch (IOException e) {
			System.out.println(e);
		}

		return loaded;
	}
}
