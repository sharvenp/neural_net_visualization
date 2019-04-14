 README
# neural_net_visualization
A simple MLP neural net visualizer that can be used to see how a MLP adjusts its parameters and learns. 

# Usage 
Set the model parameters of the MLP in TestNN2.java and run TestNN2.java. The visualizer draws circles to represent neurons and the fill of these circles represets that neurons activation value. The lines are synapses and a green line reprsents a positive value while a red line represents a negative value and the brightness of this color represents the magnitude. 

# Training/Testing  
For the sake of simplicity and quick results, the default task has been set to classifying binary input into decimal classes. The activation function used is sigmoid as it allows to simply multiply the neuron value with the rgb to get the brightness of the fill for the circle that represents that neuron. If other activation functions such as RELu or Tanh are used, then the code that drives the visualizer needs to be modified.  

# Saving/Loading 
In TestNN2.java and RawNN.java, blocks of code can be uncommented to enable saving and loading. The saved file follows the naming convention of VNet_Save_inputNeurons_hiddenLayers_hiddenNeurons_outputNeurons.txt

# Dependencies
None, this is written in pure Java.
