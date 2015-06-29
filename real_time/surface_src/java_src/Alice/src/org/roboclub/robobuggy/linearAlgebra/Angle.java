package org.roboclub.robobuggy.linearAlgebra;

import org.roboclub.robobuggy.main.LogicException;

/**
 * TODO document
 * @author Trevor Decker
 * @version 0.0
 * 
 */
public class Angle implements Number{
	Number value;
	ANGULAR_UNITS unit;
	


	/***
	 * Creates an object that represents a measurement and the units used to make that measurement  
	 * @param units
	 * @param value
	 */
	public Angle(ANGULAR_UNITS units,Number value){
		setUNITS(units);
		setMeassurmentValue(value);
	}
	
	
	/***
	 * produces a new unit object which represents the same angle but in degrees
	 * if the calling object is not an angle then an error is thrown 
	 * @return
	 * @throws LogicException 
	 */
	public Angle toDegrees() throws LogicException{
		Angle result = new Angle(this.unit, this.value);
		switch(getUnits()){
		case DEGREES:
			//already in degree so do nothing 
			break;
		case RADIANS:
			result.setMeassurmentValue((new Double_Number(.2957795)).mult(getMeassurmentValue()));
			break;
		default:
			throw new LogicException("unit type is not a angle");
			
		}
		result.setUNITS(ANGULAR_UNITS.DEGREES);
		return result;
	}

	/***
	 * produces a new unit object which represents the same angle but in degrees
	 * if the calling object is not an angle then an error is thrown  
	 * @return
	 * @throws LogicException 
	 */
	public Angle toRadians() throws LogicException{
		Angle result = toDegrees();
		result.setMeassurmentValue(new Double_Number(.0174532925).mult(getMeassurmentValue()));
		return null;
	}
	
	/***
	 * returns the Units that this measurement is currently using 
	 * @return
	 */
	public ANGULAR_UNITS getUnits(){
		return this.unit;
	}

	/**
	 * TODO document 
	 * @throws LogicException 
	 */
	public Angle add(Number otherNumber) throws LogicException {
		throw new LogicException("it does not make sense to add a angle and a number, convert to angle first");
	}
	
	/**
	 * TODO document 
	 * note result will be an angle in radians
	 * @throws LogicException 
	 */
	public Angle add(Angle otherAngle) throws LogicException {
	    Angle thisAngle = toRadians();
	    otherAngle = otherAngle.toRadians();
	    Number newValue = thisAngle.value.add(otherAngle.value);
		return new Angle(thisAngle.unit, newValue);
	}

	
	/**
	 * TODO document
	 * @throws LogicException 
	 */
	public Angle sub(Number otherNumber) throws LogicException {
		throw new LogicException("it does not make sense to subtract a angle and a number, convert to angle first");
	}
	
	/**
	 * TODO document
	 * note result will be an angle in radians
	 * @throws LogicException 
	 */
	public Angle sub(Angle otherAngle) throws LogicException {
	    Angle thisAngle = toRadians();
	    otherAngle = otherAngle.toRadians();
	    Number newValue = thisAngle.value.sub(otherAngle.value);
		return new Angle(thisAngle.unit, newValue);
	}

	
	/**
	 * TODO document 
	 * @throws LogicException 
	 */
	public Angle mult(Number otherNumber) throws LogicException {
		return new Angle(this.unit, this.value.mult(otherNumber));
	}

	/**
	 * It does not make sense to multiply an angle by another angle, for now need to create a angle^2 class
	 * @throws LogicException 
	 */
	public Angle mult(Angle otherNumber) throws LogicException {
		throw new LogicException("It does not make sense to multiply an angle by another angle, for now need to create a angle^2 class");
	}
	
	/**
	 * TODO document
	 */
	public Angle div(Number otherNumber) {
		return new Angle(this.unit, value.div(otherNumber));
	}
	
	/**
	 * TODO document
	 * @throws LogicException 
	 */
	public Number div(Angle otherAngle) throws LogicException {
		Angle thisAngle = toRadians();
		otherAngle = otherAngle.toRadians();
		return thisAngle.value.div(otherAngle.value);
	}

	
	/**
	 * TODO document
	 * @return
	 */
	public Angle zero() {
		return new Angle(ANGULAR_UNITS.RADIANS, value.zero());
	}

	
	/**
	 * Evaluates to 1 radian, Be careful this is only the true one object 
	 * if the other object is a radian 
	 * @return A Angle object encoding 1 radian
	 */
	public Angle One() {
		return new Angle(ANGULAR_UNITS.RADIANS, value.One());
	}
	
	/**
	 * TODO document
	 * @param newValue
	 */
	public void setMeassurmentValue(Number newValue){
		this.value = newValue;
	}


	/***
	 * returns the scaler(Number) numerical value that currently represents the measurement 
	 * @return
	 */
	public Number getMeassurmentValue(){
		return this.value;
	}
	
	/***
	 * Overrides the current value of this units units. Note that the measurement value stays the same.
	 * Use setValue to change the measurement. 
	 * @param newUnits
	 */
	public void setUNITS(ANGULAR_UNITS newUnits){
		this.unit = newUnits;
	}


	@Override
	/**
	 * TODO document
	 */
	public Angle inverse() {
		return new Angle(this.unit, value.inverse());
}


	@Override
	/**
	 * TODO document
	 */
	public Angle mod(Number someNumber) throws LogicException {
		return new Angle(this.unit, value.mod(someNumber));
	}


	@Override
	/**
	 * TODO document
	 */
	public boolean isLess(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Angle.class){
			Angle otherAngle = (Angle)otherNumber;
			return value.isLess(otherAngle.value);
		}
		throw new LogicException("can not compare the size of a angle and non angle number");
	}


	@Override
	/**
	 * TODO document
	 */
	public boolean isGreater(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Angle.class){
			Angle otherAngle = (Angle)otherNumber;
			return value.isGreater(otherAngle.value);
		}
		throw new LogicException("can not compare the size of a angle and non angle number");

	}


	@Override
	/**
	 * TODO document
	 * TODO implement
	 */
	public boolean isEqual(Number otherNumber) throws LogicException {
		if(otherNumber.getClass() == Angle.class){
			Angle otherAngle = (Angle)otherNumber;
			return value.isEqual(otherAngle.value);
		}
		throw new LogicException("can not compare the size of a angle and non angle number");
	}


	@Override
	/**
	 * TODO document 
	 */
	public Angle sqrt() {
		//TODO remove try catch 
		Angle result = null;
		try {
			result = (Angle) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		result.value = value.sqrt();
		return result;
	}


	@Override
	/**
	 * TODO document
	 */
	public Angle signum() {
		//TODO remove try catch 
		Angle result = null;
		try {
			result = (Angle) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		result.value = result.value.signum();		
		return result;
	}


}
