package net.antiparagon.analog6;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Stack;

import net.antiparagon.analog6.block.AnalogComputer;
import net.antiparagon.analog6.block.IBlock;
import net.antiparagon.analog6.block.integrator.Euler;
import net.antiparagon.analog6.block.math.Gain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analog6 {
	private static final Logger logger = LoggerFactory.getLogger(Analog6.class);
    
	public static void main(String[] args) throws FileNotFoundException {
		logger.info("Analog 6 - SAC Evolved");
		
		double R = 20e3;
		double C = .125e-6;
		double L = 8.0;
		double IC = 98000.0;
		Euler euler1 = new Euler("Euler1", IC);
		Euler euler2 = new Euler("Euler2", 0.0);
		Gain gain1 = new Gain("Gain1", -1.0/ (R * C));
		Gain gain2 = new Gain("Gain2", -1.0 / (L * C));
		
		euler1.addInput(gain1);
		euler1.addInput(gain2);
		gain1.addInput(euler1);
		euler2.addInput(euler1);
		gain2.addInput(euler2);
				
		AnalogComputer computer = new AnalogComputer("Analog Computer");
		
		computer.addBlock(euler1);
		computer.addBlock(gain1);
		computer.addBlock(euler2);
		computer.addBlock(gain2);
		
		/*
		PetriNet net = new PetriNet(computer.getBlocks());
		
		System.out.println("Marking vector");
		System.out.println(net.printMarkingsVector());
		System.out.println();
		System.out.println("D- Matrix");
		System.out.println(net.printDMinusMatrix());
		System.out.println("D+ Matrix");
		System.out.println(net.printDPlusMatrix());
		System.out.println("D Matrix");
		System.out.println(net.printDMatrix());
		
		//net.determineOrdering();
		//*/
		//
		computer.orderBlocks();
		
		List<IBlock> order = computer.getBlocks();
		for(int i = 0; i < order.size(); ++i) {
			System.out.println((i + 1) + ". " + order.get(i).getName());
		}
		//
				
		/*
		List<BlockInterface> outputBlocks = new ArrayList<BlockInterface>();
		//outputBlocks.add(sine1);
		outputBlocks.add(gain1);
		outputBlocks.add(euler1);
		
		//PrintStream out = new PrintStream("test.csv");
		PrintStream out = System.out;
		outputColumnHeader(outputBlocks, out);
		double t = 0.0;
		computer.initialize(t);
		outputBlockOutput(t, outputBlocks, out);
		double timestep = 0.1;
		for(t = timestep; t <= 1.0; t += timestep) {
			computer.step(timestep);
			computer.update();
			outputBlockOutput(t, outputBlocks, out);
		}
		out.close();
		//*/
	}
	
	public static void outputBlocksWithState(List<IBlock> blocks, PrintStream out) {
		if(blocks.isEmpty()) throw new IllegalArgumentException("Block list is empty");
		for(int i = 0; i < blocks.size(); ++i) {
			if(!blocks.get(i).hasState()) continue;
			out.print("[" + i + "] ");
			out.print(blocks.get(i).getName());
			out.println(" has state");
		}
	}
	
	public static void outputColumnHeader(List<IBlock> blocks, PrintStream out) {
		if(blocks.isEmpty()) throw new IllegalArgumentException("Block list is empty");
		out.print("Time,");
		for(int i = 0; i < blocks.size() - 1; ++i) {
			out.print(blocks.get(i).getName());
			out.print(",");
		}
		out.println(blocks.get(blocks.size() - 1).getName());
	}
	
	public static void outputBlockOutput(double time, List<IBlock> blocks, PrintStream out) {
		if(blocks.isEmpty()) throw new IllegalArgumentException("Block list is empty");
		out.print(time);
		out.print(",");
		for(int i = 0; i < blocks.size() - 1; ++i) {
			out.print(blocks.get(i).getOutput(time));
			out.print(",");
		}
		out.println(blocks.get(blocks.size() - 1).getOutput(time));
	}
	

	public static void outputInputChain(IBlock bi, PrintStream out) {
		Stack<IBlock> stack = new Stack<IBlock>();
		outputInputChainHelper(stack, bi);
		while(!stack.isEmpty()) {
			IBlock block = stack.pop();
			out.print(block.getName());
			if(!stack.isEmpty()) out.print(" -> ");
		}
		out.println();
	}
	
	private static void outputInputChainHelper(Stack<IBlock> stack, IBlock bi) {
		stack.push(bi);
		for(IBlock block : bi.getInputList()) {
			outputInputChainHelper(stack, block);
		}
	}
	
}
