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
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> mult(Matrix<TYPE> otherMatrix){
		Matrix<TYPE> result = this.clone();
		for(int x = 0;x<getNumCols();x++)
		{
			for(int y = 0;y<getNumRows();y++)
			{
				result.set(getRow(x).mult(otherMatrix.getCol(y)),x,y);
			}
		}
				
		return null;
	}
	
	public void set(TYPE value, int row,int col){
		//TODO 
	}
	
	public Vector<TYPE> getRow(int row){
		return null;
		//TODO
	}
	
	public Vector<TYPE> getCol(int col){
		return null;
		//TODO
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
	 * TODO document
	 * @return
	 */
	public Matrix<TYPE> add(Matrix<TYPE> b){
		Matrix<TYPE> result =this.clone();
		result.data = this.data.add(b.data);
		return result;
	}
	
	/**
	 * TODO document
	 * @return
	 */
	public int getNumRows(){
		int[] dim = data.getDimensions();
		return dim[0];
	}
	
	/**
	 * TODO document
	 * @return
	 */
	public int getNumCols(){
		int[] dim = data.getDimensions();
		return dim[1];
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
	
	
	/**
	 * TODO implement
	 * TODO document 
	 * @return
	 */
	@Override
	public Matrix<TYPE> clone(){
		//TODO
		return null;
	}
	
}
