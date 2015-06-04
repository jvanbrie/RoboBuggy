package linearAlgebra;

public class Double_Number implements Number {
	private double number;
	
	/**
	 * TODO document
	 */
	public Double_Number(double value){
		number = value;
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public double getValue(){
		return number;
	}
	
	/**
	 * TODO document 
	 */
	public Number add(Number otherNumber) {
		return (Number) new Double_Number(number+((Double_Number) otherNumber).getValue());
	}

	/**
	 * TODO document 
	 */
	@Override
	public Number sub(Number otherNumber) {
		return (Number) new Double_Number(number+((Double_Number) otherNumber).getValue());
	}

	/**
	 * TODO document
	 */
	@Override
	public Number mult(Number otherNumber) {
		return (Number) new Double_Number(number*((Double_Number) otherNumber).getValue());
	}

	/**
	 * TODO document 
	 */
	@Override
	public Number div(Number otherNumber) {
		return (Number) new Double_Number(number/((Double_Number) otherNumber).getValue());
	}
	
	/**
	 * TOD document 
	 * TODO implement
	 * @return
	 */
	public Integer_Number toInteger_Number(){
		return null; 
	//TODO 
	}

	@Override
	public Number zero() {
		// TODO Auto-generated method stub
		return null;
	}
}
