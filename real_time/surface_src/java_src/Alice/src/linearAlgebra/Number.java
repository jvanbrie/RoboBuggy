package linearAlgebra;

public interface Number {

	public Number add(Number otherNumber);
	public Number sub(Number otherNumber);
	public Number mult(Number otherNumber);
	public Number div(Number otherNumber);
	public Number zero();
	//TODO think about adding mod 
	//TODO think about making genric 
}
