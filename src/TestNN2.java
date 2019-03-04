import javax.swing.JFrame;
import java.awt.Color;
import java.util.Scanner;

public class TestNN2 {
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setTitle("VNet by Shoozi");
		frame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.white);
		
		int inputNeurons = 4;
		int hiddenLayers = 3;
		int hiddenNeurons = 20;
		int outputNeurons = 16;
		float lr = 1f;
		
		int winSize = 1000;
		
		RawNN neuralNet = new RawNN(inputNeurons, hiddenLayers, hiddenNeurons, outputNeurons, lr, winSize);
		
		//String name = String.format("VNet_Save_%d_%d_%d_%d", inputNeurons, hiddenLayers, hiddenNeurons, outputNeurons);
		//boolean worked = neuralNet.loadNet(name);

		//if (!worked)
		//System.out.println("Save for this network model does not exist.");

		frame.setSize(500 +  (100 * (hiddenLayers)), winSize);
		frame.setLocation(0,0);
		
		frame.add(neuralNet);
		frame.setVisible(true);
		
		//Scanner in = new Scanner(System.in);
		
		while (true)
		{
			neuralNet.testTrain();
			//System.out.println("Enter Binary Number");
			//String input = in.nextLine();
			//neuralNet.test(input);
		}
	}
}