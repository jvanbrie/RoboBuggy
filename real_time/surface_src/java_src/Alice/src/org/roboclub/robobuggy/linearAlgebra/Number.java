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
	 * TODO document 
	 * @return
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
	 */
	abstract public Number zero();
	
	/**
	 * TODO document
	 * @return
	 */
	abstract public Number One();

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
}
