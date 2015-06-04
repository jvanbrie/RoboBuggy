package org.roboclub.robobuggy.localization;

import org.roboclub.robobuggy.linearAlgebra.UNITS;
import org.roboclub.robobuggy.linearAlgebra.Units_Number;

import org.ejml.simple.SimpleMatrix;
//TODO merge with SO3

/***
 * Computer representation of a rotation matrix 
 * @author trevor decker
 *
 */
public class RotationMatrix_SO2 {
	SimpleMatrix R;
	
	
	/**
	 * TODO document 
	 * @param R
	 */
	public RotationMatrix_SO2(SimpleMatrix R){
		/*
		 * valid Rotation Matrix in SO2 should be of the form 
		 *             [cos(th) -sin(th)   0;
		 *              sin(th)  cos(th)   0;
		 *              	  0        0   1]
		 */
		assert(R.numCols() == 2);
		assert(R.numRows() == 2);
		this.R = R.copy();
	}
	
	/**
	 * evaluets to a copy of the rotation matrix Scaled by a scaler S
	 * @param S
	 * @return
	 */
	public RotationMatrix_SO2 scale(double S){
		SimpleMatrix M = R.copy();
		M.scale(S);
		return new RotationMatrix_SO2(M);
	}
	
	/**
	 * evaluates to a rotation Matrix equivalent to R2*this
	 * @param R2
	 * @return
	 */
	public RotationMatrix_SO2 preMult(RotationMatrix_SO2 R2){
		return new RotationMatrix_SO2(R2.R.mult(this.R));
	}
	
	/**
	 * evaluates to a rotation Matrix equivalent to this*R2
	 * @param R2
	 * @return
	 */
	public RotationMatrix_SO2 postMult(RotationMatrix_SO2 R2){
		return new RotationMatrix_SO2(R.mult(R2.R));
	}
	
	/**
	 * evaluates to a rotationMatrix such that this*this.inverse() = I 
	 * @return
	 */
	public RotationMatrix_SO2 inverse(){
		//could also use R.inverse but R.transpose comes to the same result with n^2 run time 
		return new RotationMatrix_SO2(R.transpose());
	}
	
	/**
	 * TODO document
	 * TODO implement
	 * @return
	 */
	public Quaternion toQuaternion(){
		return null;
		//TODO
	}
	
	/**
	 * TODO document
	 * TODO implement
	 * @return
	 */
	public RotationMatrix_SO3 toRotationMatrix_SO3(){
		return null;
		//TODO
	}
	
	/**
	 * gets the euler representation of this rotation matrix 
	 * @return
	 */
	public EulerAngles toEulerAngles(){
		double angle = Math.atan2(R.get(1,0), R.get(0,0));
		return new EulerAngles(new Units_Number(UNITS.DEGREES, angle));
	}
	
	
}
