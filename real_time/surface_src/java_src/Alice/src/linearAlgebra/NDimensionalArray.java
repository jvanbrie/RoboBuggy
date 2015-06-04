package linearAlgebra;

import java.lang.reflect.Array;

/***
 * A way for creating arbitrary sized arrays of a generic type 
 * based on code from 	//http://stackoverflow.com/questions/4770926/java-n-dimensional-arrays
 * 
 * @author Trevor Decker
 *
 */
public class NDimensionalArray<CELL_TYPE  extends Number> {
	private int[] dimensions;
	private CELL_TYPE[] array;
	private int[] offsetMultipliers;
	
	/**
	 * Creates an N dimensional array of type given 
	 * smapleELment is required for the way that java allocates arrays dynamically at runtime. 
	 * The compiler does not know the type beforehand because of this. sampleElemnt should be 
	 * an initialized sample version of the element. 
	 * @param sampleElement
	 * @param dimensions
	 */
	@SuppressWarnings("unchecked")
	public NDimensionalArray(CELL_TYPE sampleElment,int ... dimensions) {
		this.dimensions = dimensions;
		int arraySize = 1;
		for(int i =0;i<dimensions.length;i++){
			offsetMultipliers[i] = arraySize;
			arraySize *= dimensions[i];
		}
		array = (CELL_TYPE[]) Array.newInstance(sampleElment.getClass(), arraySize);
	}
	
	/**
	 * sets the cell that is pointed to by indices
	 * @param indices
	 * @return
	 */
	public CELL_TYPE get(int ... indices){
		return array[getOffset(indices)];
	}
	
	/***
	 * sets the cell that is pointed to by indices
	 * @param newValue
	 * @param indices
	 */
	public void set(CELL_TYPE newValue,int ... indices){
		array[getOffset(indices)] = newValue;
	}
	
	/**
	 * Gets the element in the array at the ith location in memory 
	 * if all elements were stored in a 1d row. 
	 */
	public CELL_TYPE getIndex(int index){
		//TODO
		return null;
	}
	
	/**
	 * Sets the element in the array at the ith location in memory if
	 * all elements were stored in a 1d row. 
	 */
	public void setIndex(int index,CELL_TYPE newValue){
		//TODO
	}
	
	/**
	 * returns the dimensions of the matrix
	 * @return
	 */
	public int[] getDimensions(){
		return dimensions;
	}
	
	/***
	 * gets the offset for the cell at the indices given in the int[] indices 
	 * @param indices
	 * @return
	 */
	private int getOffset(int ... indices){
		int offset =0;
		for(int i = 0;i<dimensions.length;i++){
			offset += offsetMultipliers[i]*indices[i];
		}
		return offset;
	}
	
	/**
	 * TODO document 
	 * @return
	 */
	public NDimensionalArray<CELL_TYPE> add(NDimensionalArray<CELL_TYPE> b){
		assert(this.getDimensions() == b.getDimensions()); //TODO check this
		NDimensionalArray<CELL_TYPE> result =this.clone();
		int numCells = 1;
		int[] dimensions = this.getDimensions();
		for(int dim = 0;dim<dimensions.length;dim++){
			numCells *= dimensions[dim];
		}
		
		for(int i = 0;i<numCells;i++){
			@SuppressWarnings("unchecked")
			CELL_TYPE newValue = (CELL_TYPE) this.getIndex(i).add(b.getIndex(i));
				result.setIndex(i,newValue);
			}
		
		return result;
	}
	
	/**
	 * TODO document 
	 * TODO implement 
	 */
	public NDimensionalArray<CELL_TYPE> clone(){
		//TODO
		return null;
	}
	
}
