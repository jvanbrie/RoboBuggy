package org.roboclub.robobuggy.linearAlgebra;

import org.roboclub.robobuggy.main.LogicException;

/**
 * 
 * @author Trevor Decker
 * @version 0.0
 * 
 * A Integer which implements the Number class. 
 * This is so that the number can be used with 
 * code designed for the number interface 
 *
 */
public class Integer_Number implements Number{
	private int value;
	/**
	 * TODO document 
	 * @param numberValue
	 */
	public Integer_Number(int numberValue) {
		this.value = numberValue;
	}

	@Override
	/**
	 * TODO document
	 */
	public Integer_Number add(Number otherNumber) {
		return new Integer_Number(value+((Integer_Number) otherNumber).getValue());
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public int getValue(){
		return value;
	}

	@Override
	/**
	 * TODO document 
	 * @param Number other Number
	 */
	public Integer_Number sub(Number otherNumber) {
		return  new Integer_Number(value-((Integer_Number) otherNumber).getValue());
	}

	@Override
	/**
	 * TODO document
	 * @param Number other Number 
	 */
	public Integer_Number mult(Number otherNumber) {
		return  new Integer_Number(value*((Integer_Number) otherNumber).getValue());
	}

	@Override
	/**
	 * TODO document 
	 * @param Number otherNumber
	 */
	public Integer_Number div(Number otherNumber) {
		return new Integer_Number(value/((Integer_Number) otherNumber).getValue());
	}

	@Override
	/**
	 * TODO document
	 */
	public Integer_Number zero() {
		return new Integer_Number(0);
	}

	@Override
	/**
	 * TODO document 
	 */
	public Integer_Number One() {
		return new Integer_Number(1);
	}

	@Override
	/**
	 * TODO document
	 */
	public Integer_Number inverse() {
		return new Integer_Number(-value);
	}

	@Override
	/**
	 * TODO document
	 * @param Number someNumber
	 */
	public Integer_Number mod(Number someNumber) throws LogicException {
		if(someNumber.getClass() != Integer_Number.class){
			throw new LogicException("trying to mod an integer number by a number type that does not make sense");
		}
		Integer_Number otherInteger = (Integer_Number)someNumber;
		return new Integer_Number(value % otherInteger.value);
	}

	@Override
	/**
	 * TODO document
	 * @param Number otherNumber
	 */
	public boolean isLess(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Integer_Number.class){
			Integer_Number otherInteger = (Integer_Number)otherNumber;
			return value < otherInteger.value; 
		}else if(this.isLess(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");
	}

	@Override
	/**
	 * TODO document
	 * @param Number otherNumber
	 */
	public boolean isGreater(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Integer_Number.class){
			Integer_Number otherInteger = (Integer_Number)otherNumber;
			return value > otherInteger.value; 
		}else if(this.isGreater(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");

	}

	@Override
	/**
	 * TODO document
	 * @param  Number otherNumber
	 */
	public boolean isEqual(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Integer_Number.class){
			Integer_Number otherInteger = (Integer_Number)otherNumber;
			return value == otherInteger.value; 
		}else if(this.toDouble().equals(otherNumber)){
			return true;
		}
		//otherNumber type does not make sense
		throw new LogicException("trying check equality to an  integer number by a number type that does not make sense");

	}

	@Override
	/**
	 * TODO document
	 */
	public Integer_Number sqrt() {
		return new Integer_Number((int) Math.sqrt(value));
	}

	@Override
	/**
	 * TODO document
	 */
	public Integer_Number signum(){
		if(value >= 0){
			return new Integer_Number(1);
		}else{
			return new Integer_Number(-1);
		}
	}
	
	/**
	 * TODO document
	 * @return
	 */
	public Double_Number toDouble(){
		return new  Double_Number(value);
	}

}