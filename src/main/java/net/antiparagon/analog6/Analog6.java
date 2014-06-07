package net.antiparagon.analog6;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.antiparagon.analog6.block.AnalogComputer;
import net.antiparagon.analog6.block.BlockInterface;
import net.antiparagon.analog6.block.integrator.Euler;
import net.antiparagon.analog6.block.math.Gain;
import net.antiparagon.analog6.block.source.Sine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analog6 {
	private static final Logger logger = LoggerFactory.getLogger(Analog6.class);
    
	public static void main(String[] args) throws FileNotFoundException {
		logger.info("Analog 6 - SAC Evolved");
		
		Sine sine = new Sine("Sine1");
		Gain gain = new Gain("Gain1", 3.0);
		gain.addInput(sine);
		Euler euler = new Euler("Euler1", 0.0);
		euler.addInput(gain);
		
		AnalogComputer computer = new AnalogComputer("Analog Computer");
		
		//
		computer.addBlock(euler);
		computer.addBlock(gain);
		computer.addBlock(sine);
		//*/
		
		/*
		computer.addBlock(sine);
		computer.addBlock(gain);
		computer.addBlock(euler);
		//*/
				
		outputInputChain(euler, System.out);
		
		List<BlockInterface> outputBlocks = new ArrayList<BlockInterface>();
		outputBlocks.add(sine);
		//outputBlocks.add(gain);
		//outputBlocks.add(euler);
		
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
	}
	
	public static void outputColumnHeader(List<BlockInterface> blocks, PrintStream out) {
		if(blocks.isEmpty()) throw new IllegalArgumentException("Block list is empty");
		out.print("Time,");
		for(int i = 0; i < blocks.size() - 1; ++i) {
			out.print(blocks.get(i).getName());
			out.print(",");
		}
		out.println(blocks.get(blocks.size() - 1).getName());
	}
	
	public static void outputBlockOutput(double time, List<BlockInterface> blocks, PrintStream out) {
		if(blocks.isEmpty()) throw new IllegalArgumentException("Block list is empty");
		out.print(time);
		out.print(",");
		for(int i = 0; i < blocks.size() - 1; ++i) {
			out.print(blocks.get(i).getOutput(time));
			out.print(",");
		}
		out.println(blocks.get(blocks.size() - 1).getOutput(time));
	}
	

	public static void outputInputChain(BlockInterface bi, PrintStream out) {
		Stack<BlockInterface> stack = new Stack<BlockInterface>();
		outputInputChainHelper(stack, bi);
		while(!stack.isEmpty()) {
			BlockInterface block = stack.pop();
			out.print(block.getName());
			if(!stack.isEmpty()) out.print(" -> ");
		}
		out.println();
	}
	
	private static void outputInputChainHelper(Stack<BlockInterface> stack, BlockInterface bi) {
		stack.push(bi);
		for(BlockInterface block : bi.getInputList()) {
			outputInputChainHelper(stack, block);
		}
	}
}
