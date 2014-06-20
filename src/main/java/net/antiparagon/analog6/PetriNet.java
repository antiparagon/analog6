package net.antiparagon.analog6;

import java.util.HashMap;
import java.util.List;

import net.antiparagon.analog6.block.BlockInterface;

public class PetriNet {
	
	public PetriNet(List<BlockInterface> blockList) {
		
		System.out.println("Creating Analog Computer Petri Net:");
		blocks = blockList;
		//
		// Number of transitions is the number of blocks:
		//
		numTransitions = blocks.size();
		System.out.println("Number of transitions: " + numTransitions);
    
		//
		// Count number of places:
		//
		int input = 0;
    
		for(int i = 0; i < blocks.size(); i++) {
			input = blocks.get(i).getNumInputs();
			// Sources have value of -1 for number of inputs
			if(input == -1) input = 1;
			numPlaces += input;        
		}       
		System.out.println("Number of places: " + numPlaces);
    
		//
		// Create the vector and matrices:
		//
		ptMatrix = new int[numPlaces][numTransitions];
		mVector = new int[numPlaces];
		
		dMinusMatrix = new int[numTransitions][numPlaces];
		dPlusMatrix = new int[numTransitions][numPlaces];
		    
		//
		// Setup a map for index lookup:
		//
		HashMap<BlockInterface, Integer> indexLookup = new HashMap<BlockInterface, Integer>();
		for(int i = 0; i < blocks.size(); i++) {
			indexLookup.put(blocks.get(i), i);
		}
    
		System.out.println("Filling in PT matrix...");
		//
		// Loop thru each block
		//
		System.out.println("Looping through " + blocks.size() + " blocks.");
		int placeNum = 0;
		for(int i = 0; i < blocks.size(); i++) {			
			BlockInterface bloc = blocks.get(i);
			int transition = indexLookup.get(bloc);
			// Handle source blocks
			if(bloc.getNumInputs() == -1) {
				ptMatrix[placeNum][transition] = -1;
				dMinusMatrix[transition][placeNum] = 1;
				mVector[placeNum] = 1;
				placeNum++;
				continue;
			}
			
			List<BlockInterface> in = bloc.getInputList();
			//System.out.println("Block " + i + " has " + in.size() + " inputs.");
			
			//System.out.println("Negative one should occur for block " + negOne);
			for(int j = 0; j < in.size(); j++) {
				ptMatrix[placeNum][transition] = -1;
				dMinusMatrix[transition][placeNum] = 1;
				int posOne = indexLookup.get(in.get(j));
				ptMatrix[placeNum][posOne] = 1;
				dPlusMatrix[posOne][placeNum] = 1;
				if(in.get(j).hasState()) mVector[placeNum] = 1;
				placeNum++;
			}        
		}
		
		dMatrix = addMatrices(dPlusMatrix, negateMatrix(copyMatrix(dMinusMatrix)));
	}
  
	public void determineOrdering() {
		System.out.println("Blocks enabled at start:");
		for(int i = 0; i < blocks.size(); i++) {
			if(isBlockEnabled(i)) System.out.println(blocks.get(i).getName() + " is enabled.");
		}
	}
  
	public boolean isBlockEnabled(int index) {
		int mPrime[] = new int[numPlaces];
		int sMatrix[] = new int[numTransitions];
		if(index < numTransitions) sMatrix[index] = 1;
		multiplyPTbyS(mPrime, sMatrix);
		for(int i = 0; i < mPrime.length; i++) {
			mPrime[i] += mVector[i];
			if(mPrime[i] < 0) return false;
		}
		return true;
	}
  
	public void multiplyPTbyS(int mPrime[], int sMatrix[]) {
		if(sMatrix.length != ptMatrix[0].length) {
			throw new IllegalArgumentException("Unable to multiply PT by S because dimensions don't match.");
		}
		int sum = 0;
		for(int i = 0; i < ptMatrix.length; i++) {
			for(int j = 0; j < ptMatrix[i].length; j++) {
				sum += ptMatrix[i][j] * sMatrix[j];
			}
			mPrime[i] = sum;
			sum = 0;
		}
	}
      
	public String printPTMatrix() { return printMatrix(ptMatrix); }
	public String printMVector() { return printVector(mVector); }
	
	public String printDPlusMatrix() { return printMatrix(dPlusMatrix); }
	public String printDMinusMatrix() { return printMatrix(dMinusMatrix); }
	public String printDMatrix() { return printMatrix(dMatrix); }
	
	public String printMatrix(int matrix[][]) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++)
				out.append(matrix[i][j]).append("\t");
			out.append(System.getProperty("line.separator"));
		}
		return out.toString();
	}
  
	public String printVector(int vector[]) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < vector.length; i++) {
			out.append(vector[i]).append(System.getProperty("line.separator"));        
		}
		return out.toString();
	}
	
	public int[][] zeroMatrix(int matrix[][]) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = 0;
		}
		return matrix;
	}
	
	public int[][] negateMatrix(int matrix[][]) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++)
				matrix[i][j] *= -1;
		}
		return matrix;
	}
	
	public int[][] copyMatrix(int matrix[][]) {
		int[][] result = new int[matrix.length][matrix[0].length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++)
				result[i][j] = matrix[i][j];
		}
		return result;
	}
	
	public int[][] transposeMatrix(int matrix[][]) {
		int[][] result = new int[matrix[0].length][matrix.length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++)
				result[j][i] = matrix[i][j];
		}
		return result;
	}
	
	public int[][] addMatrices(int[][] lhsMatrix, int[][] rhsMatrix) {
		if(lhsMatrix.length != rhsMatrix.length) {
			throw new IllegalArgumentException("Unable to add matrices because the number of row do not match.");
		}
		if(lhsMatrix[0].length != rhsMatrix[0].length) {
			throw new IllegalArgumentException("Unable to add matrices because the number of columns do not match.");
		}
		int[][] result = new int[lhsMatrix.length][lhsMatrix[0].length];
		for(int i = 0; i < lhsMatrix.length; i++) {
			for(int j = 0; j < lhsMatrix[i].length; j++)
				result[i][j] = lhsMatrix[i][j] + rhsMatrix[i][j];
		}
		return result;
	}
	
	public int getNumPlaces() {
		return numPlaces;
	}

	public int getNumTransitions() {
		return numTransitions;
	}

	public int[][] getPTMatrix() {
		return ptMatrix;
	}

	public int[] getMVector() {
		return mVector;
	}

	public int[][] getDPlusMatrix() {
		return dPlusMatrix;
	}

	public int[][] getDMinusMatrix() {
		return dMinusMatrix;
	}

	public int[][] getDMatrix() {
		return dMatrix;
	}

	List<BlockInterface> blocks = null;
  
	int numPlaces = 0;
	int numTransitions = 0;
  
	int ptMatrix[][] = null;
	int mVector[] = null;
	
	int dPlusMatrix[][] = null;
	int dMinusMatrix[][] = null;
	int dMatrix[][] = null;
}
