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
import org.eclipse.epsilon.eol.visitor.resolution.type.tier1.impl.TypeResolver;
import org.eclipse.epsilon.eol.visitor.resolution.variable.impl.VariableResolver;
import org.eclipse.epsilon.labs.emc.smart.emf.EmfPrecachedModel_v3;
import org.eclipse.epsilon.labs.emc.smart.emf.EmfPrecachedModel_v4;
import org.eclipse.epsilon.labs.smartsax.emc.emf.EmfGreedyModel;

public class Test_Harness {
	
	public static void main(String[] args) throws Exception {
		Test_Harness test_Harness = new Test_Harness();
		test_Harness.runSet0();
		test_Harness.runSet1();
		test_Harness.runSet2();
		test_Harness.runSet3();
		test_Harness.runSet4();
		
		
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
		    writer.append(',');
		    writer.append("Greedy loading time");
		    writer.append(',');
		    writer.append("Greedy execution time");
		    writer.append(',');
		    writer.append("Greedy memory consumption");
		    writer.append(',');
		    writer.append("v2 loading time");
		    writer.append(',');
		    writer.append("v2 execution time");
		    writer.append(',');
		    writer.append("v2 memory consumption");
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
		    	for(int j = 0; j < 11; j++)
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
	
	public ArrayList<Long> test_v3(String metamodel, String model, String eolFile, String modelName) throws Exception
	{
		ArrayList<Long> result = new ArrayList<Long>();
		
		//prepare data holders
		ArrayList<Long> normalLoad = new ArrayList<Long>();
		ArrayList<Long> normalExecute = new ArrayList<Long>();
		ArrayList<Long> normalMemory = new ArrayList<Long>();
		
		ArrayList<Long> smartLoad = new ArrayList<Long>();
		ArrayList<Long> smartExecute = new ArrayList<Long>();
		ArrayList<Long> smartMemory = new ArrayList<Long>();
		
		ArrayList<Long> greedyLoad = new ArrayList<Long>();
		ArrayList<Long> greedyExecute = new ArrayList<Long>();
		ArrayList<Long> greedyMemory = new ArrayList<Long>();
		
		ArrayList<Long> v2Load = new ArrayList<Long>();
		ArrayList<Long> v2Execute = new ArrayList<Long>();
		ArrayList<Long> v2Memory = new ArrayList<Long>();
		

		//specify the iteration and disregard
		final int iteration = 3;
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
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 1, modelName, true, false);
			smartLoad.add(tempResult.get(0));
			smartExecute.add(tempResult.get(1));
			smartMemory.add(tempResult.get(2));
			System.gc();
		}		
		
		//run partial
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 2, modelName, false, false);
			greedyLoad.add(tempResult.get(0));
			greedyExecute.add(tempResult.get(1));
			greedyMemory.add(tempResult.get(2));
			System.gc();
		}	
		
		//run partial
		for(int i = 0; i < iteration; i++)
		{
			ArrayList<Long> tempResult = testModel(metamodel, model, eolFile, 3, modelName, true, false);
			v2Load.add(tempResult.get(0));
			v2Execute.add(tempResult.get(1));
			v2Memory.add(tempResult.get(2));
			System.gc();
		}	
		
		long normalLoadTime = 0;
		long normalExecutionTime = 0;
		long normalMemoryConsumption = 0;
		
		long smartLoadTime = 0;
		long smartExecutionTime = 0;
		long smartMemoryConsumption = 0;
		
		long greedyLoadTime = 0;
		long greedyExecutionTime = 0;
		long greedyMemoryConsumption = 0;
		
		long v2LoadTime = 0;
		long v2ExecutionTime = 0;
		long v2MemoryConsumption = 0;

		
		Collections.sort(normalLoad);
		Collections.sort(normalExecute);
		Collections.sort(normalMemory);
		
		Collections.sort(smartLoad);
		Collections.sort(smartExecute);
		Collections.sort(smartMemory);
		
		Collections.sort(greedyLoad);
		Collections.sort(greedyExecute);
		Collections.sort(greedyMemory);
		
		Collections.sort(v2Load);
		Collections.sort(v2Execute);
		Collections.sort(v2Memory);
		
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
						
						greedyLoadTime = (greedyLoadTime + greedyLoad.get(i))/2;
						greedyExecutionTime = (greedyExecutionTime + greedyExecute.get(i))/2;
						greedyMemoryConsumption = (greedyMemoryConsumption + greedyMemory.get(i))/2;
						
						v2LoadTime = (v2LoadTime + v2Load.get(i))/2;
						v2ExecutionTime = (v2ExecutionTime + v2Execute.get(i))/2;
						v2MemoryConsumption = (v2MemoryConsumption + v2Memory.get(i))/2;
						
					}
					else {
						normalLoadTime = normalLoad.get(i);
						normalExecutionTime = normalExecute.get(i);
						normalMemoryConsumption = normalMemory.get(i);

						smartLoadTime = smartLoad.get(i);
						smartExecutionTime = smartExecute.get(i);
						smartMemoryConsumption = smartMemory.get(i);
						
						greedyLoadTime = greedyLoad.get(i);
						greedyExecutionTime = greedyExecute.get(i);
						greedyMemoryConsumption = greedyMemory.get(i);

						v2LoadTime = v2Load.get(i);
						v2ExecutionTime = v2Execute.get(i);
						v2MemoryConsumption = v2Memory.get(i);
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
		
		result.add(greedyLoadTime);
		result.add(greedyExecutionTime);
		result.add(greedyMemoryConsumption);
		
		result.add(v2LoadTime);
		result.add(v2ExecutionTime);
		result.add(v2MemoryConsumption);
		
		System.out.println("normal loading time average: " + normalLoadTime);
		System.out.println("normal execution time average: " + normalExecutionTime);
		System.out.println("normal memory consumption: " + normalMemoryConsumption);
		
		System.out.println("partial loading time average: " + smartLoadTime);
		System.out.println("partial execution time average: " + smartExecutionTime);
		System.out.println("partial memory consumption: " + smartMemoryConsumption);
		
		System.out.println("greedy loading time average: " + greedyLoadTime);
		System.out.println("greedy execution time average: " + greedyExecutionTime);
		System.out.println("greedy memory consumption: " + greedyMemoryConsumption);
		
		System.out.println("v2 loading time average: " + v2LoadTime);
		System.out.println("v2 execution time average: " + v2ExecutionTime);
		System.out.println("v2 memory consumption: " + v2MemoryConsumption);
		
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
		    writer.append(',');
		    writer.append("greedy loading time");
		    writer.append(',');
		    writer.append("greedy execution time");
		    writer.append(',');
		    writer.append("greedy memory consumption");
		    writer.append(',');
		    writer.append("v2 loading time");
		    writer.append(',');
		    writer.append("v2 execution time");
		    writer.append(',');
		    writer.append("v2 memory consumption");
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
		    
		    writer.append(greedyLoadTime+"");
		    writer.append(',');
		    writer.append(greedyExecutionTime+"");
		    writer.append(',');
		    writer.append(greedyMemoryConsumption+"");
		    writer.append(',');
		    
		    writer.append(v2LoadTime+"");
		    writer.append(',');
		    writer.append(v2ExecutionTime+"");
		    writer.append(',');
		    writer.append(v2MemoryConsumption+"");
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
			emfModel = new EmfPrecachedModel_v3();
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
			((EmfPrecachedModel_v3)emfModel).setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			((EmfPrecachedModel_v3)emfModel).setSmartLoading(smartLoading);
		}
		if (type == 2) {
			emfModel = new EmfGreedyModel();
			modelType = "Greedy";
		}
		
		if (type == 3) {
			emfModel = new EmfPrecachedModel_v4();
			modelType = "eContents()";
			
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
			((EmfPrecachedModel_v4)emfModel).setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			((EmfPrecachedModel_v4)emfModel).setSmartLoading(smartLoading);

		}
		
		emfModel.setName(modelName);
		emfModel.setMetamodelFile(new File(metamodel).getAbsolutePath());
		emfModel.setModelFile(new File(model).getAbsolutePath());
		
		
		if (type == 1 && smartLoading) {
			((EmfPrecachedModel_v3)emfModel).preProcess();
		}
		
		if (type == 3) {
			((EmfPrecachedModel_v4)emfModel).preProcess();
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
