package net.antiparagon.analog6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.antiparagon.analog6.block.IBlock;

public class PetriNet {
	
	public PetriNet(List<IBlock> blockList) {
		
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
		markingsVector = new int[numPlaces];
		
		dMinusMatrix = new int[numTransitions][numPlaces];
		dPlusMatrix = new int[numTransitions][numPlaces];
		    
		//
		// Setup a map for index lookup:
		//
		Map<IBlock, Integer> indexLookup = new HashMap<IBlock, Integer>();
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
			IBlock bloc = blocks.get(i);
			int transition = indexLookup.get(bloc);
			// Handle source blocks
			if(bloc.getNumInputs() == -1) {
				dMinusMatrix[transition][placeNum] = 1;
				markingsVector[placeNum] = 1;
				placeNum++;
				continue;
			}
			
			List<IBlock> in = bloc.getInputList();
			for(int j = 0; j < in.size(); j++) {
				dMinusMatrix[transition][placeNum] = 1;
				int posOne = indexLookup.get(in.get(j));
				dPlusMatrix[posOne][placeNum] = 1;
				if(in.get(j).hasState()) markingsVector[placeNum] = 1;
				placeNum++;
			}        
		}
		
		dMatrix = addMatrices(dPlusMatrix, negateMatrix(copyMatrix(dMinusMatrix)));
	}
  
	public List<IBlock> determineOrdering() {
		for(int i = 0; i < blocks.size(); i++) {
			if(isBlockEnabled(i)) System.out.println(blocks.get(i).getName() + " is enabled at start.");
		}
		List<IBlock> orderedBlocks = new ArrayList<IBlock>();
		for(int i = 0; i < blocks.size(); i++) {
			for(int j = 0; j < blocks.size(); j++) {
				if(orderedBlocks.contains(blocks.get(j))) continue;
				if(isBlockEnabled(j)) {
					System.out.println(blocks.get(j).getName() + " is enabled. Firing...");
					System.out.println("Markings vector before:");
					System.out.println(printMarkingsVector());
					orderedBlocks.add(blocks.get(j));
					fireBlock(j);
					System.out.println("Markings vector after:");
					System.out.println(printMarkingsVector());
					break;
				}
			}
		}
		return orderedBlocks;
	}
	
	private void fireBlock(int index) {
		int[] firing = createFiringVector();
		firing[index] = 1;
		int[] result = multiplyFiringVectorBy(firing, getDMatrix());
		for(int i = 0; i < markingsVector.length; ++i)
			markingsVector[i] += result[i];
	}
  
	public boolean isBlockEnabled(int index) {
		if(index < 0 || index >= numTransitions) {
			throw new IllegalArgumentException("Invalid index " + index);
		}
		int[] firing = createFiringVector();
		firing[index] = 1;
		int[] result = multiplyFiringVectorBy(firing, getDMinusMatrix());
		if(markingsVector[index] >= result[index]) {
			return true;
		}
		return false;
	}
	
	private int[] multiplyFiringVectorBy(int firingVector[], int[][] aDMatrix) {
		int[] result = new int[aDMatrix.length];
		int sum = 0;
		for(int i = 0; i < firingVector.length; i++) {
			for(int j = 0; j < aDMatrix.length; j++) {
				sum += firingVector[j] * aDMatrix[j][i];
			}
			result[i] = sum;
			sum = 0;
		}
		return result;
	}
      
	public String printMarkingsVector() { return printVector(markingsVector); }
	
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
			out.append(vector[i]).append(" ");        
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
	
	public int[] createFiringVector() {
		return new int[numTransitions];
	}
	
	public int getNumPlaces() {
		return numPlaces;
	}

	public int getNumTransitions() {
		return numTransitions;
	}

	public int[] getMarkingsVector() {
		return markingsVector;
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

	List<IBlock> blocks = null;
  
	int numPlaces = 0;
	int numTransitions = 0;
  
	int markingsVector[] = null;
	
	int dPlusMatrix[][] = null;
	int dMinusMatrix[][] = null;
	int dMatrix[][] = null;
}
