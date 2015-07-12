package org.roboclub.robobuggy.linearAlgebra;

import org.roboclub.robobuggy.main.LogicException;

/**
 * TODO document 
 * @author Trevor Decker
 * @version 0.0
 *
 */
public interface Number {
	/**
	 * evaluates to the inverse representation of the number
	 * ie Number + Number.inverse() = 0 
	 * 	@return inverse of calling number
	 */
	abstract public Number inverse();
	
	/**
	 * TODO document 
	 * @param otherNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public Number add(Number otherNumber) throws LogicException;
	
	/**
	 * TODO document 
	 * @param otherNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public Number sub(Number otherNumber) throws LogicException;
	
	/**
	 * TODO document
	 * @param otherNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public Number mult(Number otherNumber) throws LogicException;
	
	/**
	 * TODO document
	 * @param otherNumber
	 * @return
	 */
	abstract public Number div(Number otherNumber);
	
	/**
	 * TODO document 
	 * @return
	 * @throws LogicException 
	 */
	 public static Number zero() throws LogicException {
		 throw new LogicException("This function needs to be over written");

	}
	 
	/**
	 * TODO document
	 * @return
	 */
	 public static Number one() {
		 //must be overwritten 
		return null;
	}

	/**
	 * TODO document 
	 * @param someNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public Number mod(Number someNumber) throws LogicException;
	
	/**
	 * TODO document
	 * @param someNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public boolean isLess(Number someNumber) throws LogicException;
	
	/**
	 * TODO document
	 * @param someNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public boolean isGreater(Number someNumber) throws LogicException;
	
	/**
	 * TODO document 
	 * @param somberNumber
	 * @return
	 * @throws LogicException 
	 */
	abstract public boolean isEqual(Number somberNumber) throws LogicException;
	
	/**
	 * TODO document 
	 */
	abstract public Number sqrt();
	
	/**
	 * TODO document 
	 */
	abstract public Number signum();
	
	/**
	 * TODO document
	 * @return
	 * @throws LogicException
	 */
	public default Number getZero() throws LogicException{
		return zero();
	}
	
	/**
	 * TODO document
	 * @return
	 * @throws LogicException
	 */
	public default Number getOne() throws LogicException{
		return one();
	}

	public default Integer_Number toInteger_Number() throws LogicException{
		throw new LogicException("To Integer Number has not been implmented yet or does not make sense for this Number type");
	}
	public default Double_Number toDouble_Number() throws LogicException{
		throw new LogicException("To Integer Number has not been implmented yet or does not make sense for this Number type");
	}

}
