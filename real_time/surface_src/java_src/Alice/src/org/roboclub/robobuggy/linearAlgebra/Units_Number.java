package org.roboclub.robobuggy.linearAlgebra;

import org.roboclub.robobuggy.map.LogicException;

/**
 * an object that represents a measurement and the units used to make that measurement  
 * the goal of this class is to allow for all data to have its units tracked with it and 
 * to make type conversions effortless.  Note that using this class does have a small 
 * space and time runtime overhead  
 * @author Trevor Decker
 * @version 1.0
 * CHANGELOG:         NONE 
 * 
 */
public class Units_Number implements Number{
double value;
UNITS unit;


/***
 * Creates an object that represents a measurement and the units used to make that measurement  
 * @param units
 * @param value
 */
public Units_Number(UNITS units,double value){
	setUNITS(units);
	setMeassurmentValue(value);
}

/***
 * produces a new unit object which represents the same distance but in millimeters
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toMillimeters() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(1000*getMeassurmentValue());
	result.setUNITS(UNITS.MILLIMETERS);
	return result;
}

/***
 * produces a new unit object which represents the same distance but in centimeters
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toCentimeters() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(100*getMeassurmentValue());
	result.setUNITS(UNITS.CENTIMETERS);
	return result;	
}

/***
 * produces a new unit object which represents the same distance but in meters
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toMeters() throws LogicException{
	Units_Number result = new Units_Number(this.unit, this.value);
	switch(getUnits()){
		case CENTIMETERS:
			result.setMeassurmentValue(.01*getMeassurmentValue());
			break;
		case MILLIMETERS :
			result.setMeassurmentValue(.001*getMeassurmentValue());
			break;
		case METERS:
			//DO noting becasue the units are already meters	
			break;
		case KILIOMETERS:
			result.setMeassurmentValue(1000*getMeassurmentValue());
			break;
		case INCHES:
			result.setMeassurmentValue(0.0254*getMeassurmentValue());
			break;
		case FEET:
			result.setMeassurmentValue(0.3048*getMeassurmentValue());
			break;
		case MILES:
			result.setMeassurmentValue(1609.34*getMeassurmentValue());
			break;
		default:
			throw new LogicException("unit type is not a distince");
	}
	result.setUNITS(UNITS.METERS);
	return result;
}

/***
 * produces a new unit object which represents the same distance but in kilometers
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toKilometers() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(.001*getMeassurmentValue());
	result.setUNITS(UNITS.KILIOMETERS);
	return result;
}

/***
 * produces a new unit object which represents the same distance but in Inches
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toInches() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(39.3700787*getMeassurmentValue());
	result.setUNITS(UNITS.INCHES);
	return result;
}

/***
 * produces a new unit object which represents the same distance but in feet
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toFeet() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(3.28084*getMeassurmentValue());
	result.setUNITS(UNITS.FEET);
	return result;
}

/***
 * produces a new unit object which represents the same distance but in miles
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toMiles() throws LogicException{
	Units_Number result = this.toMeters();
	result.setMeassurmentValue(0.000621371*getMeassurmentValue());
	result.setUNITS(UNITS.MILES);
	return result;
}

/***
 * produces a new unit object which represents the same angle but in degrees
 * if the calling object is not an angle then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_Number toDegrees() throws LogicException{
	Units_Number result = new Units_Number(this.unit, this.value);
	switch(getUnits()){
	case DEGREES:
		//already in degree so do nothing 
		break;
	case RADIANS:
		result.setMeassurmentValue(57.2957795*getMeassurmentValue());
		break;
	default:
		throw new LogicException("unit type is not a angle");
		
	}
	result.setUNITS(UNITS.DEGREES);
	return result;
}

/***
 * produces a new unit object which represents the same angle but in degrees
 * if the calling object is not an angle then an error is thrown  
 * @return
 * @throws LogicException 
 */
public Units_Number toRadians() throws LogicException{
	Units_Number result = toDegrees();
	result.setMeassurmentValue(0.0174532925*getMeassurmentValue());
	return null;
}

/**
 *  sets the new unit to be unitless with the some value 
 * @return
 */
public Units_Number toUnitless(){
	return new Units_Number(UNITS.UNITLESS, this.value);
}

/***
 * Overrides the current value of this units units. Note that the measurement value stays the same.
 * Use setValue to change the measurement. 
 * @param newUnits
 */
public void setUNITS(UNITS newUnits){
	this.unit = newUnits;
}

/*** 
 * Overrides the current value of this unit measurement.  Note that the units stay the same.
 * Use setUNITS to change the units
 * @param newValue
 */
public void setMeassurmentValue(double newValue){
	this.value = newValue;
}


/***
 * returns the scaler(double) numerical value that currently represents the measurement 
 * @return
 */
public double getMeassurmentValue(){
	return this.value;
}

/***
 * returns the Units that this measurement is currently using 
 * @return
 */
public UNITS getUnits(){
	return this.unit;
}

@Override
/**
 * TODO implement
 * TODO document 
 */
public Number add(Number otherNumber) {
	// TODO Auto-generated method stub
	return null;
}

@Override
/**
 * TODO implement
 * TODO document
 */
public Number sub(Number otherNumber) {
	// TODO Auto-generated method stub
	return null;
}

@Override
/**
 * TODO implement
 * TODO document 
 */
public Number mult(Number otherNumber) {
	// TODO Auto-generated method stub
	return null;
}

@Override
/**
 * TODO implement
 * TODO document
 */
public Number div(Number othterNumber) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Number zero() {
	// TODO Auto-generated method stub
	return null;
}


}
