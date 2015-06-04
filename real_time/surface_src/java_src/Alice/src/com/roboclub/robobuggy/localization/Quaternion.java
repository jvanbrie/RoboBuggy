package com.roboclub.robobuggy.localization;

import org.ejml.simple.SimpleMatrix;

/***
 * for more info look around pg 33 of 	http://www.cds.caltech.edu/~murray/books/MLS/pdf/mls94-complete.pdf
 * @author trevor decker
 *
 */
public class Quaternion {
	double q0;
	SimpleMatrix qV;
	
	/***
	 * constructor for a quaterinion being passed in the scale(q0) and rotation vector (q1,q2,q3)
	 * @param q0 magnitude 
	 * @param q1
	 * @param q2
	 * @param q3
	 */
	public Quaternion(double q0,double q1,double q2,double q3){
		this.q0 = q0; //magnitude 
		double[][] inputArray = {{q1},{q2},{q3}};
		qV = new SimpleMatrix(inputArray);
	}
	
	/**
	 * constructor for a quaterinon being passed in the scale(q0) and rotation vector qv
	 * @param q0
	 * @param qV
	 */
	public Quaternion(double q0, SimpleMatrix qV) {
		this.q0 = q0;
		this.qV = qV;
	}

	/**
	 * produces a new Quaternion that is the conjugate of this Quaternion 
	 * @return
	 */
	public Quaternion conjugate(){
		return new Quaternion(q0,-qV.get(0),-qV.get(1),-qV.get(2));
	}
	
	/**
	 * computes the reciprical of this quaterion this*this.reciprical() = identity quatrinon 
	 * @return
	 */
	public Quaternion reciprical(){
		return new Quaternion(this.q0/Math.pow(this.norm(), 2),this.conjugate().qV);
	}
	
	/**
	 * computes the norm of this quaternion 
	 * @return
	 */
	public double norm(){
		return Math.sqrt(q0*q0 + qV.dot(qV));
	}
	
	/***
	 * computes this + Q, the independent element wise addition of the scale and vector components 
	 * @param Q
	 * @return
	 */
	public Quaternion addition(Quaternion Q){
		return new Quaternion(q0 + Q.q0, qV.plus(Q.qV));
	}
	
	/***
	 * Scales the rotation of this quaterinon by a double 
	 * @param scaler
	 * @return
	 */
	public Quaternion ScalerMultiplication(double scaler){
		return new Quaternion (scaler*q0,qV.get(0),qV.get(1),qV.get(2));
	}
	
	/**
	 * helper function which calculates the cross product of two vectors 
	 * @param A
	 * @param B
	 * @return
	 */
	private SimpleMatrix crossProduct(SimpleMatrix A,SimpleMatrix B){
		assert(A.getNumElements() == 3);
		assert(B.getNumElements() == 3);
		//cross product is not defined for matrices other then vectors of length 3
		double C0 = A.get(2)*B.get(3) - A.get(3)*B.get(2);
		double C1 = A.get(3)*B.get(1) - A.get(1)*B.get(3);
		double C2 = A.get(1)*B.get(2) - A.get(2)*B.get(1);
		double[][] C = {{C0},{C1},{C2}};
		return new SimpleMatrix(C);
	}
	
	/**
	 * helper function which scales a vector/matrix by a double
	 * @param scale
	 * @param A
	 * @return
	 */
	private SimpleMatrix scaleVector(double scale,SimpleMatrix A){
		SimpleMatrix B = A.copy();
		for(int i = 0;i<A.getNumElements();i++){
			B.set(i,scale*A.get(i));
		}
		return B;
	}
	
	/**
	 * result of Q*P  where Q is this quaternion and P is another quaternion  
	 * @param P
	 * @return
	 */
	public Quaternion QuaternionMultiplication(Quaternion P){
		double c0 = q0*P.q0 - qV.dot(P.qV);
		SimpleMatrix C = scaleVector(q0,P.qV).plus(scaleVector(P.q0, qV)).plus(crossProduct(qV,P.qV));				
		return new Quaternion(c0,C);
	}
	
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public RotationMatrix_SO2 toRotationMatrix_SO2(){
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public RotationMatrix_SO3 toRotationMatrix_SO3(){
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
