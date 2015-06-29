package org.roboclub.robobuggy.linearAlgebra;

import org.roboclub.robobuggy.main.LogicException;

/**
 *
 * @author Trevor Decker
 * @version 0.0
 *
 * A double which implements the Number class. 
 * This is so that the number can be used with 
 * code designed for the number interface
 */
public class Double_Number implements Number {
	private double value;
	
	/**
	 * Constructs an object as a Double Number. 
	 * A double which implements the Number class. 
	 *  This is so that the number can be used with code designed for the number interface
	 *  @param value : the value for the double that this number represents
	 */
	public Double_Number(double value){
		value = value;
	}
	
	/**
	 * returns the current value of this number 
	 * @return
	 */
	public double getValue(){
		return value;
	}
	
	/**
	 * returns a new number representing this number added to otehrNumber
	 * @param otherNumber
	 */
	public Number add(Number otherNumber) {
		return (Number) new Double_Number(value+((Double_Number) otherNumber).getValue());
	}

	/**
	 * Subtracts this numbers value by the other numbers value 
	 * @param otherNumber
	 */
	@Override
	public Number sub(Number otherNumber) {
		return (Number) new Double_Number(value+((Double_Number) otherNumber).getValue());
	}

	/**
	 * Multiplies this numbers value by the otherNumbers value 
	 * @param otherNumber
	 */
	@Override
	public Number mult(Number otherNumber) {
		return (Number) new Double_Number(value*((Double_Number) otherNumber).getValue());
	}

	/**
	 * Divides this numbers value by the otherNumbers value 
	 * @param otherNumber
	 */
	@Override
	public Number div(Number otherNumber) {
		return (Number) new Double_Number(value/((Double_Number) otherNumber).getValue());
	}
	
	/**
	 * converts this Number to an integer, note that this will cause a loss of perscion. 
	 * this.toIntegr_Number().toDouble_Number == this iff this.value is an integer  
	 * @return
	 */
	public Integer_Number toInteger_Number(){
		return new Integer_Number((int) this.getValue()); 
	}

	@Override
	/**
	 * Creates a Double number representing zero
	 */
	public Double_Number zero() {
		return new Double_Number(0.0);
	}
	
	@Override
	/**
	 * Creates a Double number representing the identy element 1.0
	 */
	public Double_Number One(){
		return new Double_Number(1.0);
	}

	@Override
	/**
	 * TODO document
	 */
	public Double_Number inverse() {
		return new Double_Number(-value);
	}

	@Override
	/**
	 * TODO document
	 */
	public Double_Number mod(Number someNumber) throws LogicException {
		if(someNumber.getClass() == Integer_Number.class){
			Integer_Number otherInteger = (Integer_Number)someNumber;
			return new Double_Number(value % otherInteger.getValue());
		}		
		
		if(someNumber.getClass() == Double_Number.class){
			Double_Number otherDouble = (Double_Number)someNumber;
			return new Double_Number(value % otherDouble.value);
		}
		
		throw new LogicException("trying to mod a double number by a number type that does not make sense");

	}

	@Override
	/**
	 * TODO document
	 */
	public boolean isLess(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Double_Number.class){
			Double_Number otherDouble = (Double_Number)otherNumber;
			return value < otherDouble.value; 
		}else if(this.toInteger_Number().isLess(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");

	}

	@Override
	/**
	 * TODO document
	 */
	public boolean isGreater(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Double_Number.class){
			Double_Number otherDouble = (Double_Number)otherNumber;
			return value > otherDouble.value; 
		}else if(this.toInteger_Number().isGreater(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");
	}

	@Override
	/**
	 * TODO document
	 */
	public boolean isEqual(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Double_Number.class){
			Double_Number otherDouble = (Double_Number)otherNumber;
			return value == otherDouble.value; 
		}else if(this.toInteger_Number().equals(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");
	}

	@Override
	/**
	 * TODO document
	 */
	public Double_Number sqrt() {
		return new Double_Number(Math.sqrt(value));
	}

	@Override
	/**
	 * TODO document
	 */
	public Double_Number signum() {
		if(value >= 0.0){
			return new Double_Number(1.0);
		}else{
			return new Double_Number(-1.0);
		}
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public Integer_Number toInteger(){
		return new Integer_Number((int) value);
	}
	
}