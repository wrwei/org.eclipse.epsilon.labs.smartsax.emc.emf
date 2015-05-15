package org.eclipse.epsilon.labs.smartsax.emc.emf.benchmark;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.analysis.optimisation.loading.context.LoadingOptimisationAnalysisContext;
import org.eclipse.epsilon.eol.analysis.optimisation.loading.impl.LoadingOptimisationAnalyser;
import org.eclipse.epsilon.eol.ast2eol.Ast2EolContext;
import org.eclipse.epsilon.eol.metamodel.EolElement;
import org.eclipse.epsilon.eol.parse.Eol_EolParserRules.elseStatement_return;
import org.eclipse.epsilon.eol.visitor.resolution.type.tier1.impl.TypeResolver;
import org.eclipse.epsilon.eol.visitor.resolution.variable.impl.VariableResolver;
import org.eclipse.epsilon.labs.smartsax.emc.emf.EmfGreedyModel;
import org.eclipse.epsilon.labs.smartsax.emc.emf.EmfSmartModel;

public class Test_Harness {
	
	public static void main(String[] args) throws Exception {
		Test_Harness test_Harness = new Test_Harness();
//		test_Harness.runSet0();
//		test_Harness.runSet1();
//		test_Harness.runSet2();
//		test_Harness.runSet3();
//		test_Harness.runSet4();
		
//		test_Harness.runSet0CU();
//		test_Harness.runSet1CU();
//		test_Harness.runSet2CU();
//		test_Harness.runSet3CU();
//		test_Harness.runSet4CU();
		
		test_Harness.runSet0grabats();
//		test_Harness.runSet1grabats();
//		test_Harness.runSet2grabats();
//		test_Harness.runSet3grabats();
//		test_Harness.runSet4grabats();
		
	}
	
	public void runSet0grabats() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v3("test/JDTAST.ecore", "test/set0.xmi", "test/grabats.eol", "m"));	
		generateCSV_v2(result, "set0grab", 1);
	}
	
	public void runSet1grabats() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v3("test/JDTAST.ecore", "test/set1.xmi", "test/grabats.eol", "m"));	
		generateCSV_v2(result, "set1grab", 1);
	}
	
	public void runSet2grabats() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v3("test/JDTAST.ecore", "test/set2.xmi", "test/grabats.eol", "m"));	
		generateCSV_v2(result, "set2grab", 1);
	}
	
	public void runSet3grabats() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v3("test/JDTAST.ecore", "test/set3.xmi", "test/grabats.eol", "m"));	
		generateCSV_v2(result, "set3grab", 1);
	}
	
	public void runSet4grabats() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v3("test/JDTAST.ecore", "test/set4.xmi", "test/grabats.eol", "m"));	
		generateCSV_v2(result, "set4grab", 1);
	}

	
	public void runSet(int setNumber, int percentage) throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		result.add(test_v2("test/JDTAST.ecore", "test/set"+ setNumber + ".xmi", "test/set"+ setNumber + "_" + percentage + "percent.eol", "m"));	
	}

	
	
	public void runSet0CU() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set0.xmi", "test/set0_" + i*2 + "0percent_cu.eol", "m"));	
		}
		generateCSV_v2(result, "set0", 5);
	}

	public void runSet1CU() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set1.xmi", "test/set0_" + i*2 + "0percent_cu.eol", "m"));	
		}
		generateCSV_v2(result, "set1", 5);
	}
	
	public void runSet2CU() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set2.xmi", "test/set2_" + i*2 + "0percent_cu.eol", "m"));	
		}
		generateCSV_v2(result, "set2", 5);
	}
	
	public void runSet3CU() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set3.xmi", "test/set3_" + i*2 + "0percent_cu.eol", "m"));	
		}
		generateCSV_v2(result, "set3", 5);
	}

	public void runSet4CU() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set4.xmi", "test/set4_" + i*2 + "0percent_cu.eol", "m"));	
		}
		generateCSV_v2(result, "set4", 5);
	}

	
	public void runSet0() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set0.xmi", "test/set0_" + i*2 + "0percent.eol", "m"));	
		}
		generateCSV_v2(result, "set0", 5);
	}
	
	public void runSet1() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();
		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set1.xmi", "test/set1_" + i*2 + "0percent.eol", "m"));	
		}
		generateCSV_v2(result, "set1", 5);
	}
	
	public void runSet2() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();

		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set2.xmi", "test/set2_" + i*2 + "0percent.eol", "m"));
		}
		generateCSV_v2(result, "set2", 5);

	}
		
	public void runSet3() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();

		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set3.xmi", "test/set3_" + i*2 + "0percent.eol", "m"));
		}
		
		generateCSV_v2(result, "set3", 5);

	}
	
	public void runSet4() throws Exception
	{
		ArrayList<ArrayList<Long>> result = new ArrayList<ArrayList<Long>>();

		for(int i = 1; i <= 5; i++)
		{
			result.add(test_v3("test/JDTAST.ecore", "test/set4.xmi", "test/set4_" + i*2 + "0percent.eol", "m"));
		}
		generateCSV_v2(result, "set4", 5);

	}
	
	public void generateCSV_v2(ArrayList<ArrayList<Long>> input, String name, int entries)
	{
		
		try
		{
			
			FileWriter writer = null;
			
			if (entries == 10) {
				writer = new FileWriter("test/"+name +".csv");
			}
			else {
				writer = new FileWriter("test/"+name +"CU.csv");
			}
	 
		    writer.append("Normal loading time");
		    writer.append(',');
		    writer.append("Normal execution time");
		    writer.append(',');
		    writer.append("Normal memory consumption");
		    writer.append(',');
		    writer.append("Partial loading time");
		    writer.append(',');
		    writer.append("Partial execution time");
		    writer.append(',');
		    writer.append("Partial memory consumption");
//		    writer.append(',');
//		    writer.append("Normal Total");
//		    writer.append(',');
//		    writer.append("Smart Total");
//		    writer.append(',');
//		    writer.append("Partial Total");
//		    writer.append(',');
//		    writer.append("Smart Partial Total");
//		    writer.append(',');
//		    writer.append("Greedy Total");
//		    writer.append(',');
//		    writer.append("Greedy memory consumption");
		    writer.append('\n');
		    
		    for(int i = 0; i < entries; i++)
		    {
		    	ArrayList<Long> temp = input.get(i);
		    	for(int j = 0; j < 5; j++)
		    	{
		    		writer.append(temp.get(j)+"");
		    		writer.append(',');
		    	}
		    	writer.append(temp.get(5)+"\n");
		    }	 
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}

	
	public void generateCSV(ArrayList<ArrayList<Long>> input, String name, int entries)
	{
		
		try
		{
			
			FileWriter writer = null;
			
			if (entries == 10) {
				writer = new FileWriter("test/"+name +".csv");
			}
			else {
				writer = new FileWriter("test/"+name +"_CU.csv");
			}
	 
		    writer.append("Normal loading time");
		    writer.append(',');
		    writer.append("Normal execution time");
		    writer.append(',');
		    writer.append("Normal memory consumption");
		    writer.append(',');
		    writer.append("Smart loading time");
		    writer.append(',');
		    writer.append("Smart execution time");
		    writer.append(',');
		    writer.append("Smart memory consumption");
		    writer.append(',');
		    writer.append("Partial loading time");
		    writer.append(',');
		    writer.append("Partial execution time");
		    writer.append(',');
		    writer.append("Partial memory consumption");
		    writer.append(',');
		    writer.append("Smart Partial loading time");
		    writer.append(',');
		    writer.append("Smart Partial execution time");
		    writer.append(',');
		    writer.append("Smart Partial memory consumption");
		    writer.append(',');
		    writer.append("Greedy loading time");
		    writer.append(',');
		    writer.append("Greedy execution time");
		    writer.append(',');
		    writer.append("Greedy memory consumption");
//		    writer.append(',');
//		    writer.append("Normal Total");
//		    writer.append(',');
//		    writer.append("Smart Total");
//		    writer.append(',');
//		    writer.append("Partial Total");
//		    writer.append(',');
//		    writer.append("Smart Partial Total");
//		    writer.append(',');
//		    writer.append("Greedy Total");
//		    writer.append(',');
//		    writer.append("Greedy memory consumption");
		    writer.append('\n');
		    
		    for(int i = 0; i < entries; i++)
		    {
		    	ArrayList<Long> temp = input.get(i);
		    	for(int j = 0; j < 14; j++)
		    	{
		    		writer.append(temp.get(j)+"");
		    		writer.append(',');
		    	}
		    	writer.append(temp.get(14)+"\n");
		    }	 
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	public ArrayList<Long> test_v2(String metamodel, String model, String eolFile, String modelName) throws Exception
	{
		ArrayList<Long> result = new ArrayList<Long>();
		
		//prepare data holders
		ArrayList<Long> normalLoad = new ArrayList<Long>();
		ArrayList<Long> normalExecute = new ArrayList<Long>();
		ArrayList<Long> normalMemory = new ArrayList<Long>();

		
		ArrayList<Long> smartLoad = new ArrayList<Long>();
		ArrayList<Long> smartExecute = new ArrayList<Long>();
		ArrayList<Long> smartMemory = new ArrayList<Long>();

		
		ArrayList<Long> partialLoad = new ArrayList<Long>();
		ArrayList<Long> partialExecute = new ArrayList<Long>();
		ArrayList<Long> partialMemory = new ArrayList<Long>();


		ArrayList<Long> smartPartialLoad = new ArrayList<Long>();
		ArrayList<Long> smartPartialExecute = new ArrayList<Long>();
		ArrayList<Long> smartPartialMemory = new ArrayList<Long>();


		ArrayList<Long> greedyLoad = new ArrayList<Long>();
		ArrayList<Long> greedyExecute = new ArrayList<Long>();
		ArrayList<Long> greedyMemory = new ArrayList<Long>();


		//specify the iteration and disregard
		final int iteration = 2;
		final int disregard = 1;
		
		//run normal
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 0, modelName, false, false);
			normalLoad.add(tempResult.get(0));
			normalExecute.add(tempResult.get(1));
			normalMemory.add(tempResult.get(2));
			System.gc();
		}
		//run smart
		for(int i = 0; i < iteration; i++)
		{		
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 1, modelName, true, false);
			smartLoad.add(tempResult.get(0));
			smartExecute.add(tempResult.get(1));
			smartMemory.add(tempResult.get(2));
			System.gc();
		}
		//run partial
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 1, modelName, false, true);
			partialLoad.add(tempResult.get(0));
			partialExecute.add(tempResult.get(1));
			partialMemory.add(tempResult.get(2));
			System.gc();
		}		
		//run smart partial
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 1, modelName, true, true);
			smartPartialLoad.add(tempResult.get(0));
			smartPartialExecute.add(tempResult.get(1));
			smartPartialMemory.add(tempResult.get(2));
			System.gc();
		}	
		//run greedy
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 2, modelName, false, false);
			greedyLoad.add(tempResult.get(0));
			greedyExecute.add(tempResult.get(1));
			greedyMemory.add(tempResult.get(2));
			System.gc();
		}
		
		long normalLoadTime = 0;
		long normalExecutionTime = 0;
		long normalMemoryConsumption = 0;
		
		long smartLoadTime = 0;
		long smartExecutionTime = 0;
		long smartMemoryConsumption = 0;

		long partialLoadTime = 0;
		long partialExecutionTime = 0;
		long partialMemoryConsumption = 0;

		long smartPartialLoadTime = 0;
		long smartPartialExecutionTime = 0;
		long smartPartialMemoryConsumption = 0;
		
		long greedyLoadTime = 0;
		long greedyExecutionTime = 0;
		long greedyMemoryConsumption = 0;

		Collections.sort(normalLoad);
		Collections.sort(normalExecute);
		Collections.sort(normalMemory);
		
		Collections.sort(smartLoad);
		Collections.sort(smartExecute);
		Collections.sort(smartMemory);
		
		Collections.sort(partialLoad);
		Collections.sort(partialExecute);
		Collections.sort(partialMemory);
		
		Collections.sort(smartPartialLoad);
		Collections.sort(smartPartialExecute);
		Collections.sort(smartPartialMemory);
		
		Collections.sort(greedyLoad);
		Collections.sort(greedyExecute);
		Collections.sort(greedyMemory);
		
		int pivot;
		if ((iteration - disregard)/2 == 0) {
			pivot = 1;
		}
		else {
			pivot = (iteration-disregard)/2;
		}
		
		for(int i = 0; i < iteration; i++)
		{
			if (i < pivot) {
				
			}
			else {
				if (i < pivot+disregard) {
					if (i > pivot) {
						normalLoadTime = (normalLoadTime + normalLoad.get(i))/2;
						normalExecutionTime = (normalExecutionTime + normalExecute.get(i))/2;
						normalMemoryConsumption = (normalMemoryConsumption + normalMemory.get(i))/2;
						
						smartLoadTime = (smartLoadTime + smartLoad.get(i))/2;
						smartExecutionTime = (smartExecutionTime + smartExecute.get(i))/2;
						smartMemoryConsumption = (smartMemoryConsumption + smartMemory.get(i))/2;
						
						partialLoadTime = (partialLoadTime + partialLoad.get(i))/2;
						partialExecutionTime = (partialExecutionTime + partialExecute.get(i))/2;
						partialMemoryConsumption = (partialMemoryConsumption + partialMemory.get(i))/2;
						
						smartPartialLoadTime = (smartPartialLoadTime + smartPartialLoad.get(i))/2;
						smartPartialExecutionTime = (smartPartialExecutionTime + smartPartialExecute.get(i))/2;
						smartPartialMemoryConsumption = (smartPartialMemoryConsumption + smartPartialMemory.get(i))/2;
						
						greedyLoadTime = (greedyLoadTime + greedyLoad.get(i))/2;
						greedyExecutionTime = (greedyExecutionTime + greedyExecute.get(i))/2;
						greedyMemoryConsumption = (greedyMemoryConsumption + greedyMemory.get(i))/2;

					}
					else {
						normalLoadTime = normalLoad.get(i);
						normalExecutionTime = normalExecute.get(i);
						normalMemoryConsumption = normalMemory.get(i);

						smartLoadTime = smartLoad.get(i);
						smartExecutionTime = smartExecute.get(i);
						smartMemoryConsumption = smartMemory.get(i);
						
						partialLoadTime = partialLoad.get(i);
						partialExecutionTime = partialExecute.get(i);
						partialMemoryConsumption = partialMemory.get(i);
						
						smartPartialLoadTime = smartPartialLoad.get(i);
						smartPartialExecutionTime = smartPartialExecute.get(i);
						smartPartialMemoryConsumption = smartPartialMemory.get(i);
						
						greedyLoadTime = greedyLoad.get(i);
						greedyExecutionTime = greedyExecute.get(i);
						greedyMemoryConsumption = greedyMemory.get(i);
					}
				}
			}
		}
		
				
		result.add(normalLoadTime);
		result.add(normalExecutionTime);
		result.add(normalMemoryConsumption);
		
		result.add(smartLoadTime);
		result.add(smartExecutionTime);
		result.add(smartMemoryConsumption);
		
		result.add(partialLoadTime);
		result.add(partialExecutionTime);
		result.add(partialMemoryConsumption);
		
		result.add(smartPartialLoadTime);
		result.add(smartPartialExecutionTime);
		result.add(smartPartialMemoryConsumption);
		
		result.add(greedyLoadTime);
		result.add(greedyExecutionTime);
		result.add(greedyMemoryConsumption);
		
		System.out.println("normal loading time average: " + normalLoadTime);
		System.out.println("normal execution time average: " + normalExecutionTime);
		System.out.println("normal memory consumption: " + normalMemoryConsumption);
		
		System.out.println("smart loading time average: " + smartLoadTime);
		System.out.println("smart execution time average: " + smartExecutionTime);
		System.out.println("smart memory consumption: " + smartMemoryConsumption);
		
		System.out.println("partial loading time average: " + partialLoadTime);
		System.out.println("partial execution time average: " + partialExecutionTime);
		System.out.println("partial memory consumption: " + partialMemoryConsumption);
		
		System.out.println("smart partial loading time average: " + smartPartialLoadTime);
		System.out.println("smart partial execution time average: " + smartPartialExecutionTime);
		System.out.println("smart partial memory consumption: " + smartPartialMemoryConsumption);

		System.out.println("greedy loading time average: " + greedyLoadTime);
		System.out.println("greedy execution time average: " + greedyExecutionTime);
		System.out.println("greedy memory consumption: " + greedyMemoryConsumption);
		
		try
		{
			FileWriter writer = new FileWriter(eolFile.substring(0, eolFile.length()-4) + ".csv");
	 
		    writer.append("Normal loading time");
		    writer.append(',');
		    writer.append("Normal execution time");
		    writer.append(',');
		    writer.append("Normal memory consumption");
		    writer.append(',');
		    writer.append("Smart loading time");
		    writer.append(',');
		    writer.append("Smart execution time");
		    writer.append(',');
		    writer.append("Smart memory consumption");
		    writer.append(',');
		    writer.append("Partial loading time");
		    writer.append(',');
		    writer.append("Partial execution time");
		    writer.append(',');
		    writer.append("Partial memory consumption");
		    writer.append(',');
		    writer.append("Smart Partial loading time");
		    writer.append(',');
		    writer.append("Smart Partial execution time");
		    writer.append(',');
		    writer.append("Smart Partial memory consumption");
		    writer.append(',');
		    writer.append("Greedy loading time");
		    writer.append(',');
		    writer.append("Greedy execution time");
		    writer.append(',');
		    writer.append("Greedy memory consumption");
		    writer.append('\n');
		    
		    writer.append(normalLoadTime+"");
		    writer.append(',');
		    writer.append(normalExecutionTime+"");
		    writer.append(',');
		    writer.append(normalMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(smartLoadTime+"");
		    writer.append(',');
		    writer.append(smartExecutionTime+"");
		    writer.append(',');
		    writer.append(smartMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(partialLoadTime+"");
		    writer.append(',');
		    writer.append(partialExecutionTime+"");
		    writer.append(',');
		    writer.append(partialMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(smartPartialLoadTime+"");
		    writer.append(',');
		    writer.append(smartPartialExecutionTime+"");
		    writer.append(',');
		    writer.append(smartPartialMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(greedyLoadTime+"");
		    writer.append(',');
		    writer.append(greedyExecutionTime+"");
		    writer.append(',');
		    writer.append(greedyMemoryConsumption+"");
		    writer.append('\n');
	 
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
		
		return result;
	
	}
	
	public ArrayList<Long> test_v3(String metamodel, String model, String eolFile, String modelName) throws Exception
	{
		ArrayList<Long> result = new ArrayList<Long>();
		
		//prepare data holders
		ArrayList<Long> normalLoad = new ArrayList<Long>();
		ArrayList<Long> normalExecute = new ArrayList<Long>();
		ArrayList<Long> normalMemory = new ArrayList<Long>();
		
		ArrayList<Long> partialLoad = new ArrayList<Long>();
		ArrayList<Long> partialExecute = new ArrayList<Long>();
		ArrayList<Long> partialMemory = new ArrayList<Long>();

		//specify the iteration and disregard
		final int iteration = 2;
		final int disregard = 1;
		
		//run normal
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 0, modelName, false, false);
			normalLoad.add(tempResult.get(0));
			normalExecute.add(tempResult.get(1));
			normalMemory.add(tempResult.get(2));
			System.gc();
		}
		//run partial
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 1, modelName, false, true);
			partialLoad.add(tempResult.get(0));
			partialExecute.add(tempResult.get(1));
			partialMemory.add(tempResult.get(2));
			System.gc();
		}		
		
		long normalLoadTime = 0;
		long normalExecutionTime = 0;
		long normalMemoryConsumption = 0;
		
		long partialLoadTime = 0;
		long partialExecutionTime = 0;
		long partialMemoryConsumption = 0;

		Collections.sort(normalLoad);
		Collections.sort(normalExecute);
		Collections.sort(normalMemory);
		
		Collections.sort(partialLoad);
		Collections.sort(partialExecute);
		Collections.sort(partialMemory);
		
		int pivot;
		if ((iteration - disregard)/2 == 0) {
			pivot = 1;
		}
		else {
			pivot = (iteration-disregard)/2;
		}
		
		for(int i = 0; i < iteration; i++)
		{
			if (i < pivot) {
				
			}
			else {
				if (i < pivot+disregard) {
					if (i > pivot) {
						normalLoadTime = (normalLoadTime + normalLoad.get(i))/2;
						normalExecutionTime = (normalExecutionTime + normalExecute.get(i))/2;
						normalMemoryConsumption = (normalMemoryConsumption + normalMemory.get(i))/2;
						
						partialLoadTime = (partialLoadTime + partialLoad.get(i))/2;
						partialExecutionTime = (partialExecutionTime + partialExecute.get(i))/2;
						partialMemoryConsumption = (partialMemoryConsumption + partialMemory.get(i))/2;
						
					}
					else {
						normalLoadTime = normalLoad.get(i);
						normalExecutionTime = normalExecute.get(i);
						normalMemoryConsumption = normalMemory.get(i);

						partialLoadTime = partialLoad.get(i);
						partialExecutionTime = partialExecute.get(i);
						partialMemoryConsumption = partialMemory.get(i);
						
					}
				}
			}
		}
		
				
		result.add(normalLoadTime);
		result.add(normalExecutionTime);
		result.add(normalMemoryConsumption);
		
		result.add(partialLoadTime);
		result.add(partialExecutionTime);
		result.add(partialMemoryConsumption);
		
		System.out.println("normal loading time average: " + normalLoadTime);
		System.out.println("normal execution time average: " + normalExecutionTime);
		System.out.println("normal memory consumption: " + normalMemoryConsumption);
		
		System.out.println("partial loading time average: " + partialLoadTime);
		System.out.println("partial execution time average: " + partialExecutionTime);
		System.out.println("partial memory consumption: " + partialMemoryConsumption);
		
		
		try
		{
			FileWriter writer = new FileWriter(eolFile.substring(0, eolFile.length()-4) + ".csv");
	 
		    writer.append("Normal loading time");
		    writer.append(',');
		    writer.append("Normal execution time");
		    writer.append(',');
		    writer.append("Normal memory consumption");
		    writer.append(',');
		    writer.append("Partial loading time");
		    writer.append(',');
		    writer.append("Partial execution time");
		    writer.append(',');
		    writer.append("Partial memory consumption");
		    writer.append('\n');
		    
		    writer.append(normalLoadTime+"");
		    writer.append(',');
		    writer.append(normalExecutionTime+"");
		    writer.append(',');
		    writer.append(normalMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(partialLoadTime+"");
		    writer.append(',');
		    writer.append(partialExecutionTime+"");
		    writer.append(',');
		    writer.append(partialMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
		
		return result;
	
	}

	
	public void test(String metamodel, String model, String eolFile, String modelName) throws Exception
	{
		//prepare data holders
		ArrayList<ArrayList<Long>> normalData = new ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Long>> smartData = new ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Long>> partialData = new ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Long>> smartPartialData = new ArrayList<ArrayList<Long>>();
		ArrayList<ArrayList<Long>> greedyData = new ArrayList<ArrayList<Long>>();
		
		//specify the iteration and disregard
		final int iteration = 1;
		final int disregard = 0;
		
		//run normal
		for(int i = 0; i < iteration; i++)
		{
			normalData.add(testModel(metamodel, model, eolFile, 0, modelName, false, false));
		}

		//run smart
		for(int i = 0; i < iteration; i++)
		{
			smartData.add(testModel(metamodel, model, eolFile, 1, modelName, true, false));
		}
		
		//run partial
		for(int i = 0; i < iteration; i++)
		{
			partialData.add(testModel(metamodel, model, eolFile, 1, modelName, false, true));
		}		
		
		//run smart partial
		for(int i = 0; i < iteration; i++)
		{
			smartPartialData.add(testModel(metamodel, model, eolFile, 1, modelName, true, true));
		}	
		
		//run greedy
		for(int i = 0; i < iteration; i++)
		{
			greedyData.add(testModel(metamodel, model, eolFile, 2, modelName, false, false));
		}
		
		
		//gc
		System.gc();
		
		//set counter
		int counter = 0;
		
		//normal results
		String normalResult = "";
		long normalLoadTime = 0;
		long normalExecutionTime = 0;
		long normalMemoryConsumption = 0;
		
		for(ArrayList<Long> result : normalData)
		{
			if (counter < disregard) {
				//if still in disregard range, ignore
			}
			else {
				normalLoadTime += result.get(0);
				normalExecutionTime += result.get(1);
				normalMemoryConsumption += result.get(2);
				normalResult += "|"+result.get(0) + ", " + result.get(1) + ", " + result.get(2);	
			}
			counter ++;
		}
		counter = 0;

		String smartResult = "";
		long smartLoadTime = 0;
		long smartExecutionTime = 0;
		long smartMemoryConsumption = 0;
		
		for(ArrayList<Long> result : smartData)
		{
			if (counter < disregard) {
				
			}
			else {
				smartLoadTime += result.get(0);
				smartExecutionTime += result.get(1);
				smartMemoryConsumption += result.get(2);
				smartResult += "|" + result.get(0) + ", " + result.get(1) + ", " + result.get(2);	
			}
			counter ++;
		}
		
		counter = 0;
		
		String partialResult = "";
		long partialLoadTime = 0;
		long partialExecutionTime = 0;
		long partialMemoryConsumption = 0;
		
		for(ArrayList<Long> result : partialData)
		{
			if (counter < disregard) {
				
			}
			else {
				partialLoadTime += result.get(0);
				partialExecutionTime += result.get(1);
				partialMemoryConsumption += result.get(2);
				partialResult += "|" + result.get(0) + ", " + result.get(1) + ", " + result.get(2);	
			}
			counter ++;
		}
		
		counter = 0;
		
		String smartPartialResult = "";
		long smartPartialLoadTime = 0;
		long smartPartialExecutionTime = 0;
		long smartPartialMemoryConsumption = 0;
		
		for(ArrayList<Long> result : smartPartialData)
		{
			if (counter < disregard) {
				
			}
			else {
				smartPartialLoadTime += result.get(0);
				smartPartialExecutionTime += result.get(1);
				smartPartialMemoryConsumption += result.get(2);
				smartPartialResult += "|" + result.get(0) + ", " + result.get(1) + ", " + result.get(2);	
			}
			counter ++;
		}
		
		counter = 0;
		
		String greedyResult = "";
		long greedyLoadTime = 0;
		long greedyExecutionTime = 0;
		long greedyMemoryConsumption = 0;
		for(ArrayList<Long> result : greedyData)
		{
			if (counter < disregard) {
				
			}
			else {
				greedyLoadTime += result.get(0);
				greedyExecutionTime += result.get(1);
				greedyMemoryConsumption += result.get(2);
				
				greedyResult += "|" + result.get(0) + ", " + result.get(1) + ", " + result.get(2);	
			}
			counter ++;
			
		}
				
		System.out.println("Normal: " + normalResult);
		System.out.println("Smart: " + smartResult);
		System.out.println("Partial: " + partialResult);
		System.out.println("Smart partial:" + smartPartialResult);
		System.out.println("Greedy: " + greedyResult);
		
		
		System.out.println("normal loading time average: " + normalLoadTime/(iteration-disregard));
		System.out.println("normal execution time average: " + normalExecutionTime/(iteration-disregard));
		System.out.println("normal memory consumption: " + normalMemoryConsumption/(iteration-disregard));
		
		System.out.println("smart loading time average: " + smartLoadTime/(iteration-disregard));
		System.out.println("smart execution time average: " + smartExecutionTime/(iteration-disregard));
		System.out.println("smart memory consumption: " + smartMemoryConsumption/(iteration-disregard));
		
		System.out.println("partial loading time average: " + partialLoadTime/(iteration-disregard));
		System.out.println("partial execution time average: " + partialExecutionTime/(iteration-disregard));
		System.out.println("partial memory consumption: " + partialMemoryConsumption/(iteration-disregard));
		
		System.out.println("smart partial loading time average: " + smartPartialLoadTime/(iteration-disregard));
		System.out.println("smart partial execution time average: " + smartPartialExecutionTime/(iteration-disregard));
		System.out.println("smart partial memory consumption: " + smartPartialMemoryConsumption/(iteration-disregard));

		System.out.println("greedy loading time average: " + greedyLoadTime/(iteration-disregard));
		System.out.println("greedy execution time average: " + greedyExecutionTime/(iteration-disregard));
		System.out.println("greedy memory consumption: " + greedyMemoryConsumption/(iteration-disregard));
		
		try
		{
			FileWriter writer = new FileWriter(eolFile.substring(0, eolFile.length()-4) + ".csv");
	 
		    writer.append("Normal loading time");
		    writer.append(',');
		    writer.append("Normal execution time");
		    writer.append(',');
		    writer.append("Normal memory consumption");
		    writer.append(',');
		    writer.append("Smart loading time");
		    writer.append(',');
		    writer.append("Smart execution time");
		    writer.append(',');
		    writer.append("Smart memory consumption");
		    writer.append(',');
		    writer.append("Partial loading time");
		    writer.append(',');
		    writer.append("Partial execution time");
		    writer.append(',');
		    writer.append("Partial memory consumption");
		    writer.append(',');
		    writer.append("Smart Partial loading time");
		    writer.append(',');
		    writer.append("Smart Partial execution time");
		    writer.append(',');
		    writer.append("Smart Partial memory consumption");
		    writer.append(',');
		    writer.append("Greedy loading time");
		    writer.append(',');
		    writer.append("Greedy execution time");
		    writer.append(',');
		    writer.append("Greedy memory consumption");
		    writer.append('\n');
		    
		    writer.append(normalLoadTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(normalExecutionTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(normalMemoryConsumption/(iteration-disregard)+"");
		    writer.append(',');
		    
		    writer.append(smartLoadTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(smartExecutionTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(smartMemoryConsumption/(iteration-disregard)+"");
		    writer.append(',');
		    
		    writer.append(partialLoadTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(partialExecutionTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(partialMemoryConsumption/(iteration-disregard)+"");
		    writer.append(',');
		    
		    writer.append(smartPartialLoadTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(smartPartialExecutionTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(smartPartialMemoryConsumption/(iteration-disregard)+"");
		    writer.append(',');
		    
		    writer.append(greedyLoadTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(greedyExecutionTime/(iteration-disregard)+"");
		    writer.append(',');
		    writer.append(greedyMemoryConsumption/(iteration-disregard)+"");
		    writer.append('\n');
	 
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	
	}
	
	public ArrayList<Long> testModel(String metamodel, 
			String model, 
			String eolFile, 
			int type, 
			String modelName, 
			boolean smartLoading, 
			boolean partialLoading) throws Exception {
		
		Ast2EolContext ast2EolContext;
		EolElement dom;
		VariableResolver vr;
		TypeResolver tr;
		LoadingOptimisationAnalyser loa;
		LoadingOptimisationAnalysisContext loaContext;

		
		//prepare result
		ArrayList<Long> result = new ArrayList<Long>();
		//prepare eolModule
		EolModule eolModule = new EolModule();
		eolModule.parse(new File(eolFile));
		
		loadEPackageFromFile(metamodel);
		
		//prepare model type
		String modelType = "";
		//prepare emf model
		EmfModel emfModel = null;
		
		
		if (type == 0) {
			emfModel = new EmfModel();
			modelType = "Normal";
		}
		if (type == 1) {
			emfModel = new EmfSmartModel();
			if (smartLoading && !partialLoading) {
				modelType = "Smart";	
			}
			else if (smartLoading && partialLoading) {
				modelType = "Smart Partial";
			}
			else if (!smartLoading && partialLoading) {
				modelType = "Partial";
			}
			
			
			ast2EolContext = new Ast2EolContext();
			dom = ast2EolContext.getEolElementCreatorFactory().createDomElement(eolModule.getAst(), null, ast2EolContext);
			
			vr = new VariableResolver();
			vr.run(dom);
			
			tr = new TypeResolver();
			tr.getTypeResolutionContext().setModule(eolModule);
			tr.run(dom);
			
			loa = new LoadingOptimisationAnalyser();
			loa.run(dom);
			
			loaContext = (LoadingOptimisationAnalysisContext) loa.getTypeResolutionContext();
			((EmfSmartModel)emfModel).setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			((EmfSmartModel)emfModel).setSmartLoading(smartLoading);
			((EmfSmartModel)emfModel).setPartialLoading(partialLoading);
			((EmfSmartModel)emfModel).setLoadAllAttributes(false);
		}
		if (type == 2) {
			emfModel = new EmfGreedyModel();
			modelType = "Greedy";
		}
		
		emfModel.setName(modelName);
		emfModel.setMetamodelFile(new File(metamodel).getAbsolutePath());
		emfModel.setModelFile(new File(model).getAbsolutePath());
		
		
		if (type == 1 && partialLoading) {
			((EmfSmartModel)emfModel).preProcess();
		}
		

		
		
		//System.gc();
		
		synchronized (Test_Harness.class) {
			System.out.println(modelType + " model prepared, loading...");
			
			long init = System.nanoTime();
			long memoryConsumptionStart = Runtime.getRuntime().freeMemory();
			emfModel.load();
			long result1 = (System.nanoTime()-init)/1000000;

			result.add(result1);
			System.out.println("(took ~" + result1 + "ms to load)");
			long memoryConsumptionEnd = Runtime.getRuntime().freeMemory();
			
			init = System.nanoTime();
			
			eolModule.getContext().getModelRepository().addModel(emfModel);
			eolModule.execute();
			
			long result2 = (System.nanoTime() - init)/1000000;
			result.add(result2);
			System.out.println("(took ~" + result2 + "ms to run)");
			
			
			result.add(memoryConsumptionStart-memoryConsumptionEnd);
			eolModule.getContext().getModelRepository().dispose();
		}
		
		
		return result;
	}

//	public static ArrayList<Long> testSmart(String set, String file) throws Exception
//	{
//		ArrayList<Long> result = new ArrayList<Long>();
//		EolModule eolModule = new EolModule();
//		eolModule.parse(new File(file));
//		//eolModule.parse(new File("test/set2_50percent.eol"));
//		
//		EmfSmartModel smartModel = new EmfSmartModel();
//		smartModel.setName("DOM");
//		smartModel.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
//		//smartModel.setModelFile(new File("test/set2.xmi").getAbsolutePath());
//		smartModel.setModelFile(new File(set).getAbsolutePath());
//		
//		System.out.println("emf smart model prepared, loading...");
//		
//		loadEPackageFromFile("test/JDTAST.ecore");
//		
//		Ast2EolContext ast2EolContext = new Ast2EolContext();
//		EolElement dom = ast2EolContext.getEolElementCreatorFactory().createDomElement(eolModule.getAst(), null, ast2EolContext);
//		
//		VariableResolver vr = new VariableResolver();
//		vr.run(dom);
//		
//		TypeResolver tr = new TypeResolver();
//		tr.getTypeResolutionContext().setModule(eolModule);
//		tr.run(dom);
//		
//		LoadingOptimisationAnalyser loa = new LoadingOptimisationAnalyser();
//		loa.run(dom);
//		
//		LoadingOptimisationAnalysisContext loaContext = (LoadingOptimisationAnalysisContext) loa.getTypeResolutionContext();
//		System.out.println(loaContext.getModelContainers().get(0).getModelElementsAllOfKind().size() +loaContext.getModelContainers().get(1).getModelElementsAllOfKind().size());
//		smartModel.setModelContainers(loaContext.getModelContainers());
//		
//		long memoryConsumptionStart = Runtime.getRuntime().freeMemory();
//		
//		long init = System.nanoTime();
//		smartModel.load();
//		long result1 = (System.nanoTime()-init)/1000000;
//		result.add(result1);
//		System.out.println("(took ~" + result1 + "ms to load)");
//		
//		init = System.nanoTime();
//		eolModule.getContext().getModelRepository().addModel(smartModel);
//		eolModule.execute();
//		long result2 = (System.nanoTime() - init)/1000000;
//		result.add(result2);
//		System.out.println("(took ~" + result2 + "ms to run)");
//		long memoryConsumptionEnd = Runtime.getRuntime().freeMemory();
//		
//		result.add(memoryConsumptionStart-memoryConsumptionEnd);
//		eolModule.getContext().getModelRepository().dispose();
//		return result;
//	}
//	
//	public static ArrayList<Long> testNormal(String set, String file) throws Exception
//	{
//		ArrayList<Long> result = new ArrayList<Long>();
//		EolModule eolModule = new EolModule();
//		eolModule.parse(new File(file));
//		//eolModule.parse(new File("test/set2_50percent.eol"));
//		
//		EmfModel normalModel = new EmfModel();
//		normalModel.setName("DOM");
//		normalModel.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
//		normalModel.setModelFile(new File(set).getAbsolutePath());
//		//normalModel.setModelFile(new File("test/set2.xmi").getAbsolutePath());
//		
//		loadEPackageFromFile("test/JDTAST.ecore");
//		
//		System.out.println("emf normal model prepared, loading...");
//		
//		long memoryConsumptionStart = Runtime.getRuntime().freeMemory();
//
//		long init = System.nanoTime();
//		normalModel.load();
//		
//		long result1 = (System.nanoTime()-init)/1000000;
//		result.add(result1);
//		System.out.println("(took ~" + result1 + "ms to load)");
//		
//		init = System.nanoTime();
//		eolModule.getContext().getModelRepository().addModel(normalModel);
//		eolModule.execute();
//		
//		long result2 = (System.nanoTime() - init)/1000000;
//		result.add(result2);
//		System.out.println("(took ~" + result2 + "ms to run)");
//		
//		long memoryConsumptionEnd = Runtime.getRuntime().freeMemory();
//		
//		result.add(memoryConsumptionStart-memoryConsumptionEnd);
//
//		eolModule.getContext().getModelRepository().dispose();
//		return result;
//	}
//	
//	public static ArrayList<Long> testGreedy(String set, String file) throws Exception
//	{
//		ArrayList<Long> result = new ArrayList<Long>();
//		EolModule eolModule = new EolModule();
//		eolModule.parse(new File(file));
//		//eolModule.parse(new File("test/set2_50percent.eol"));
//		
//		EmfGreedyModel normalModel = new EmfGreedyModel();
//		normalModel.setName("DOM");
//		normalModel.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
//		normalModel.setModelFile(new File(set).getAbsolutePath());
//		//normalModel.setModelFile(new File("test/set2.xmi").getAbsolutePath());
//		
//		loadEPackageFromFile("test/JDTAST.ecore");
//		
//		System.out.println("emf greedy model prepared, loading...");
//		long memoryConsumptionStart = Runtime.getRuntime().freeMemory();
//
//		long init = System.nanoTime();
//		normalModel.load();
//		
//		long result1 = (System.nanoTime()-init)/1000000;
//		result.add(result1);
//		System.out.println("(took ~" + result1 + "ms to load)");
//		
//		init = System.nanoTime();
//		eolModule.getContext().getModelRepository().addModel(normalModel);
//		eolModule.execute();
//		
//		long result2 = (System.nanoTime() - init)/1000000;
//		result.add(result2);
//		System.out.println("(took ~" + result2 + "ms to run)");
//		long memoryConsumptionEnd = Runtime.getRuntime().freeMemory();
//		
//		result.add(memoryConsumptionStart-memoryConsumptionEnd);
//
//		eolModule.getContext().getModelRepository().dispose();
//		return result;
//	}

	
	public static EPackage loadEPackageFromFile(String fileName)
	{
		EPackage result = null;
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		URI uri = URI.createFileURI(new File(fileName).getAbsolutePath());
		Resource resource = resourceSet.getResource(uri, true);
		for(EObject obj: resource.getContents())
		{
			if(obj instanceof EPackage)
			{
				EPackage.Registry.INSTANCE.put(((EPackage) obj).getNsURI(), obj);
				result = (EPackage) obj;
			}
		}
		return result;
	}

}
