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
			if(input < 0) input = 1;
			numPlaces += input;        
		}       
		System.out.println("Number of places: " + numPlaces);
    
		//
		// Create the vector and matrices:
		//
		ptMatrix = new int[numPlaces][numTransitions];
		mMatrix = new int[numPlaces];
    
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
		int place_num = 0;
		for(int i = 0; i < blocks.size(); i++) {			
			BlockInterface bloc = blocks.get(i);
			List<BlockInterface> in = bloc.getInputList();
			//System.out.println("Block " + i + " has " + in.size() + " inputs.");
			int negOne = indexLookup.get(bloc);
			//System.out.println("Negative one should occur for block " + neg_one);
			for(int j = 0; j < in.size(); j++) {
				ptMatrix[place_num][negOne] = -1;
				int posOne = indexLookup.get(in.get(j));
				ptMatrix[place_num][posOne] = 1;
				if(in.get(j).hasState()) mMatrix[place_num] = 1;
				place_num++;
			}        
		}
	}
  
	public void determineOrdering() {
		System.out.println("Blocks enabled at start.");
		for(int i = 0; i< blocks.size(); i++) {
			if(isBlockEnabled(i)) System.out.println(blocks.get(i).getName() + " is enabled.");
		}
	}
  
	public boolean isBlockEnabled(int index) {
		int mPrime[] = new int[numPlaces];
		int sMatrix[] = new int[numTransitions];
		if(index < numTransitions) sMatrix[index] = 1;
		multiplyPTbyS(mPrime, sMatrix);
		for(int i = 0; i < mPrime.length; i++) {
			mPrime[i] += mMatrix[i];
			if(mPrime[i] < 0) return false;
		}
		return true;
	}
  
	public void multiplyPTbyS(int mPrime[], int sMatrix[]) {
		if(sMatrix.length != ptMatrix[0].length) {
			System.out.println("Unable to multiply PT by S because dimensions don't match.");
			return;
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
      
	public String printPTMatrix() {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < ptMatrix.length; i++) {
			for(int j = 0; j < ptMatrix[i].length; j++)
				out.append(ptMatrix[i][j]).append("\t");
			out.append("\n");
		}
		return out.toString();
	}
  
	public String printMMatrix() {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < mMatrix.length; i++) {
			out.append(mMatrix[i]).append("\n");        
		}
		return out.toString();
	}
  
	List<BlockInterface> blocks = null;
  
	int numPlaces = 0;
	int numTransitions = 0;
  
	int ptMatrix[][] = null;
	int mMatrix[] = null;
}
