package com.roboclub.robobuggy.localization;

import com.roboclub.robobuggy.map.LogicException;

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
public class Units_class {
double value;
UNITS unit;


/***
 * Creates an object that represents a measurement and the units used to make that measurement  
 * @param units
 * @param value
 */
public Units_class(UNITS units,double value){
	setUNITS(units);
	setMeassurmentValue(value);
}

/***
 * produces a new unit object which represents the same distance but in millimeters
 * if the calling object is not a distance then an error is thrown 
 * @return
 * @throws LogicException 
 */
public Units_class toMillimeters() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toCentimeters() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toMeters() throws LogicException{
	Units_class result = new Units_class(this.unit, this.value);
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
public Units_class toKilometers() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toInches() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toFeet() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toMiles() throws LogicException{
	Units_class result = this.toMeters();
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
public Units_class toDegrees() throws LogicException{
	Units_class result = new Units_class(this.unit, this.value);
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
public Units_class toRadians() throws LogicException{
	Units_class result = toDegrees();
	result.setMeassurmentValue(0.0174532925*getMeassurmentValue());
	return null;
}

/**
 *  sets the new unit to be unitless with the some value 
 * @return
 */
public Units_class toUnitless(){
	return new Units_class(UNITS.UNITLESS, this.value);
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


}
