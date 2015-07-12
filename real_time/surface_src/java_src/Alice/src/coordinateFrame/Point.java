package coordinateFrame;

import java.util.ArrayList;

import org.roboclub.robobuggy.linearAlgebra.DISTINCE_UNITS;
import org.roboclub.robobuggy.linearAlgebra.Distince;
import org.roboclub.robobuggy.linearAlgebra.Double_Number;
import org.roboclub.robobuggy.linearAlgebra.Vector;
import org.roboclub.robobuggy.linearAlgebra.Number;
import org.roboclub.robobuggy.main.LogicException;

/**
 * @author Trevor Decker
 * TODO document
 * @version 0.0
 *
 */
public class Point implements PositionRepersentation{
	private Vector<Distince> coordinates = new Vector<Distince>(0);
	//The direction that each coordinate is in 
	private ArrayList<SpacialDimensions> directions = new ArrayList<SpacialDimensions>();
	
	/**
	* creates a representation of a point in n space with the value of each dimension set based on 
	* the values in the given vector.  All axis directions are set to not specified use setIndex to 
	* change an axis or give it a direction 
	* @param values
	*/
	public Point(Vector values){
		coordinates = values;
		for(int i = 0;i<values.getLength();i++){
			directions.add(SpacialDimensions.notSpecified);
		}
	}
	
	/**
	* creates a representation of a point in n space with the value of each dimension set based on 
	* the values in the given vector.  All axis directions are set to the corresponding index in directions 
	* @param Vector values
	* @param directions
	* @throws LogicException 
	*/
	public Point(Vector values,ArrayList<SpacialDimensions> directions) throws LogicException{
		if(values.getLength() != directions.size()){
			throw new LogicException("number of values is not the same as the number of directions");
		}
		coordinates = values;
		this.directions = directions;
	}
	
	/**
	 * evaluates to the distance along the ith index of the Point, note no assumption
	 *  is made of which axis this represents getDimesnion is preferred if you want a 
	 *  Distance in a specific direction
	 * @param index
	 * @return
	 */
	public Distince getIndexCoordinate(int index){
		return coordinates.getIndex(index);
	}
	
	/**
	 * TODO document
	 * @param index
	 * @return
	 */
	public SpacialDimensions getIndexDirection(int index){
		return directions.get(index);
	}
	
	/**
	 * TODO document 
	 * @param index
	 * @param newValue
	 * @throws LogicException 
	 */
	public void setIndex(int index,Distince newValue) throws LogicException{
		setIndex(index,newValue,SpacialDimensions.notSpecified);
	}
	
	/**
	 * TODO document 
	 * @param index
	 * @param newValue
	 * @throws LogicException 
	 */
	public void setIndex(int index,Distince newValue,SpacialDimensions direction) throws LogicException{
		if(index < 0){
			throw new LogicException("can not set a negative index of a point");
		}
		if(index >= coordinates.getLength()){
			//need to add a new element to the list
			this.coordinates = this.coordinates.append(newValue);
			this.directions.add(direction);
		}else{
			this.coordinates.setIndex(index, newValue);
			this.directions.set(index, direction);
		}
		
	}
	
	/**
	 * Returns true if this Point and the other point has the 
	 * same number of directions and those directions are the in the same order.
	 * @return
	 * @throws LogicException 
	 */
	private boolean sameDirections(Point otherPoint) throws LogicException{
		//validates that both points are of the correct size
		if(otherPoint.directions.size() != this.directions.size()){
			throw new LogicException("Points are of diffrent size");
		}
		if(otherPoint.directions.size() != otherPoint.coordinates.getLength()
				|| this.directions.size() != this.coordinates.getLength()){
			throw new LogicException("Point has a diffrent number of coordinates then directions");
		}
		for(int i = 0;i<this.directions.size();i++){
			if(this.directions.get(i) != otherPoint.directions.get(i)){
				//at least one of the directions are different so the points are diffrent
				return false;
			}
		}
		//all of the directions are the same 
		return true;
	}
	
	
	/**
	 * TODO document 
	 * @param otherPoint
	 * @return
	 * @throws LogicException 
	 */
	public Number dotProduct(Point otherPoint) throws LogicException{
		if(!sameDirections(otherPoint)){
			throw new LogicException("trying to take the dotProduct of two points with diffrent directions");
		}
		return this.coordinates.dotProduct(otherPoint.coordinates,new Double_Number(0.0));
	}
	
	/**
	 * TODO document
	 * Is only defined for Points of size 3 otherwise a logic exception will be thrown 
	 * @param otherPoint
	 * @return
	 */
	public Point crossProduct(Point otherPoint) throws LogicException{
		if(!sameDirections(otherPoint)){
			throw new LogicException("trying to take the crossProduct of two points with diffrent directions");
		}		
		if(!(this.coordinates.getLength() == 3)){
			throw new LogicException("trying to take the crossProuct of two points that are not of size 3, which is not defined");
		}
		return new Point(this.coordinates.crossProduct(otherPoint.coordinates),directions);
	}

	@Override
	/**
	 * TODO document
	 */
	public void setDimension(SpacialDimensions direction, Distince newValue) throws LogicException {
		int dimensionIndex = -1;
		//assumes that each direction only appears once in a point 
		for(int i = 0;i<this.directions.size();i++){
			if(this.directions.get(i).compareTo(direction) == 0){
				//they are the same so we are done searching 
				break;
			}
		}
		if(dimensionIndex == -1){
			//the direction has not been found so we extend the point by adding it 
			this.coordinates = this.coordinates.append(newValue);
		    this.directions.add(direction);
		}else{
			this.coordinates.setIndex(dimensionIndex,newValue);
 		}
	}

	@Override
	/**
	 * Returns the current position of the point in a specific direction 
	 * @param SpacialDimensions direction, the direction for  which you want the distance
	 * @return The distance this point is in the direction given  
	 */
	public Distince getDimension(SpacialDimensions direction) throws LogicException {
		int dimensionIndex = -1;
		//assumes that each direction only appears once in a point 
		for(int i = 0;i<this.directions.size();i++){
			if(this.directions.get(i).compareTo(direction) == 0){
				//they are the same so we are done searching 
				break;
			}
		}
		if(dimensionIndex == -1){
			throw new LogicException("Tried to get Dimesnion of a direction that was not defined for this point");
		}
		return coordinates.getIndex(dimensionIndex);
	}

	@Override
	/**
	 * TODO implement 
	 * TODO document
	 */
	public PositionRepersentation applyRotation(RotationalRepersentation R) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * TODO implement
	 * TODO document 
	 */
	public PositionRepersentation add(PositionRepersentation T) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * TODO implement
	 * TODO document
	 */
	public Distince getDistince() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Point zero() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Distince getDimensionIndex(int index) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpacialDimensions[] getDimensions() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
