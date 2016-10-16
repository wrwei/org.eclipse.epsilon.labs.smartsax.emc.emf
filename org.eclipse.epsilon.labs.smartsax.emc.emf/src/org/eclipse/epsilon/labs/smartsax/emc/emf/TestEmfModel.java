package org.eclipse.epsilon.labs.smartsax.emc.emf;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;

public class TestEmfModel {

	public static void main(String[] args) throws URISyntaxException, Exception {
		EolModule eolModule = new EolModule();
		eolModule.parse(new File("test/grabats.eol"));
		
		EmfModel greedyModel = new EmfModel();
		greedyModel.setModelFile(new File("test/set2.xmi").getAbsolutePath());
		greedyModel.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
		greedyModel.setName("DOM");
		
		long init = System.nanoTime();

		greedyModel.load();
		System.out.println("(took ~" + (System.nanoTime() - init)
				/ 1000000 + "ms to load)");
		init = System.nanoTime();
		eolModule.getContext().getModelRepository().addModel(greedyModel);
		eolModule.execute();
		System.out.println("(took ~" + (System.nanoTime() - init)
				/ 1000000 + "ms to run)");

	}
}
