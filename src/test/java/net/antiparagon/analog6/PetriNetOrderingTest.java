package net.antiparagon.analog6;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.antiparagon.analog6.block.AnalogComputer;
import net.antiparagon.analog6.block.IBlock;
import net.antiparagon.analog6.block.integrator.Euler;
import net.antiparagon.analog6.block.math.Gain;
import net.antiparagon.analog6.block.source.Constant;

import org.junit.Test;

public class PetriNetOrderingTest {

	@Test
	public void SequentialOrderTest() {
		Constant constant1 = new Constant("Constant1", 2.0);
		Gain gain1 = new Gain("Gain1", 3.0);
		gain1.addInput(constant1);
		Euler euler1 = new Euler("Euler1", 0.0);
		euler1.addInput(gain1);
		
		AnalogComputer computer = new AnalogComputer("Analog Computer");
		
		computer.addBlock(constant1);
		computer.addBlock(gain1);
		computer.addBlock(euler1);
		
		computer.orderBlocks();
		
		List<IBlock> blocks = computer.getBlocks();
		assertEquals(constant1.getName(), blocks.get(0).getName());
		assertEquals(gain1.getName(), blocks.get(1).getName());
		assertEquals(euler1.getName(), blocks.get(2).getName());
		
	}
	
	@Test
	public void SecondOrderOrderingTest() {
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
		
		euler1.addInput(gain1);
		
		AnalogComputer computer = new AnalogComputer("Analog Computer");
		
		computer.addBlock(euler1);
		computer.addBlock(euler2);
		computer.addBlock(gain1);
		computer.addBlock(gain2);
				
		computer.orderBlocks();
		
		List<IBlock> order = computer.getBlocks();
		for(int i = 0; i < order.size(); ++i) {
			System.out.println((i + 1) + ". " + order.get(i).getName());
		}
		
	}
}

