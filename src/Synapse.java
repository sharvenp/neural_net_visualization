public class Synapse {

	float weight;

	Node left;
	Node right;

	float delta;

	public float getWeight() {
		return weight;
	}

	public void setWeight(float newWeight) {
		weight = newWeight;
	}

	public Node getLeftNode() {
		return left;
	}

	public Node getRightNode() {
		return right;
	}

	public void setLeftNode(Node addedNode) {
		left = addedNode;
	}

	public void setRightNode(Node addedNode) {
		right = addedNode;
	}

	public void updateWeight()
	{
		weight -= delta;
	}

	public void setDelta(float newDelta)
	{
		delta = newDelta;
	}


}