package com.roboclub.robobuggy.localization;

import org.ejml.simple.SimpleMatrix;

/***
 * Computer representation of a rotation matrix 
 * @author trevor decker
 *
 */
public class RotationMatrix {
	SimpleMatrix R;
	
	
	/**
	 * TODO document 
	 * @param R
	 */
	public RotationMatrix(SimpleMatrix R){
		this.R = R.copy();
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public Quaternion toQuaternion(){
		//TODO
		return null;
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public EulerAngles toEulerAngles(){
		//TODO
		return null;
	}
	
	/**
	 * evaluates to a rotation Matrix equivalent to this*R2
	 * @param R2
	 * @return
	 */
	public RotationMatrix mult(RotationMatrix R2){
		return new RotationMatrix(R.mult(R2.R));
	}
	
	/**
	 * evaluates to a rotationMatrix such that this*this.inverse() = I 
	 * @return
	 */
	public RotationMatrix inverse(){
		//could also use R.inverse but R.transpose comes to the same result with n^2 run time 
		return new RotationMatrix(R.transpose());
	}
	
}
