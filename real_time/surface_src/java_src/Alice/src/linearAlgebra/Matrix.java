package linearAlgebra;

/***
 * TODO document
 * @author Trevor Decker
 *
 */
public class Matrix<TYPE extends Number> {
	NDimensionalArray<TYPE> data; 
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public Matrix(TYPE sampleElment,int ... dimensions){
		data = new NDimensionalArray<TYPE>(sampleElment, dimensions);
	
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> mult(){
		//TODO
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> determinate(){
		//TODO
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> add(Matrix<TYPE> b){
		assert(this.data.getDimensions() == b.data.getDimensions()); //TODO check this
		int numCells = 1;
		int[] dimensions = this.data.getDimensions();
		for(int dim = 0;dim<dimensions.length;dim++){
			numCells *= dimensions[dim];
		}
		
		for(int i = 0;i<numCells;i++){
			this.data.getIndex(i).add(b.data.getIndex(i));  //todo set result
		}
		
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public int[] dimensions(){
		//TODO
		return null;
	}
	
	/**
	 * TODO implement
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> inverse(){
		//TODO
		return null;
	}
	
	
	
	//TODO replace the EJMT library with this library since it is more general and capable 
	//of holding arrays of arbeatray data types
	
	//TODO
}
