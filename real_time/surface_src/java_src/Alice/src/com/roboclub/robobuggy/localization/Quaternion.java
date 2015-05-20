package com.roboclub.robobuggy.localization;

//TODO
//for more info look around pg 33 of 	http://www.cds.caltech.edu/~murray/books/MLS/pdf/mls94-complete.pdf
public class Quaternion {
	double q0;
	double q1;
	double q2;
	double q3;
	
	/***
	 * TODO document 
	 * @param q0 magnitude 
	 * @param q1
	 * @param q2
	 * @param q3
	 */
	public Quaternion(double q0,double q1,double q2,double q3){
		this.q0 = q0; //magnitude 
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}
	
	/**
	 * TODO document
	 * TODO implement 
	 * @param R
	 */
	public Quaternion(RotationMatrix R){
		//TODO
	}
	
	/**
	 * produces a new Quaternion that is the conjugate of this Quaternion 
	 * @return
	 */
	public Quaternion conjugate(){
		return new Quaternion(q0,-q1,-q2,-q3);
	}
	
	/**
	 * TODO document
	 * TODO implement
	 * @return
	 */
	public Quaternion reciprical(){
		return this.conjugate()/Math.pow(this.norm(),2);
	}
	
	/**
	 * TODO document
	 * @return
	 */
	public double norm(){
		return Math.sqrt(q0*q0 + q1*q1 + q2*q2 + q3*q3);
	}
	
	/***
	 * TODO document 
	 * TODO implement 
	 * @param Q
	 * @return
	 */
	public Quaternion addition(Quaternion Q){
		//TODO
		return Q;
	}
	
	/***
	 * TODO document
	 * @param scaler
	 * @return
	 */
	public Quaternion ScalerMultiplication(double scaler){
		return new Quaternion (scaler*q0,q1,q2,q3);
	}
	
	/**
	 * TODO implement
	 * TODO document 
	 * @param Q
	 * @return
	 */
	public Quaternion QuaternionMultiplication(Quaternion Q){
		//TODO  Q · P = (q0p0 − ~q · ~p, q0~p + p0~q + ~q × ~p ).
		double c0 = q0*Q.q0 - (q1*Q.q1 + q2*Q.q2 + q3*Q.q3);
		//double c1 = 
		//double c2 = 
		//double c3 =
		return new Quaternion(c0,c1,c2,c3);
	}
	
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public RotationMatrix toRotationMatrix(){
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public EulerAngles toEulerAngles(){
		return null;
	}
	
	
	
	
}
