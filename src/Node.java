import java.util.ArrayList;

public class Node {

	float value;
	float bias;

	float z;
	float delta;

	ArrayList<Synapse> left = new ArrayList<>();
	ArrayList<Synapse> right = new ArrayList<>();

	int x;
	int y;


	public void setXY(int newX, int newY)
	{
		x = newX;
		y = newY;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public float getDelta()
	{
		return delta;
	}

	public void setDelta(float newDelta)
	{
		delta = newDelta;
	}

	public float getZValue()
	{
		return z;
	}

	public void setZValue(float newZValue)
	{
		z = newZValue;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float newVal) {
		value = newVal;
	}

	public float getBias() {
		return bias;
	}

	public void setBias(float newBias) {
		bias = newBias;
	}

	public ArrayList<Synapse> getLeftSynapse() {
		return left;
	}

	public ArrayList<Synapse> getRightSynapse() {
		return right;
	}

	public void addLeftSynapse(Synapse addedSynapse) {
		left.add(addedSynapse);
	}

	public void addRightSynapse(Synapse addedSynapse) {
		right.add(addedSynapse);
	}
}