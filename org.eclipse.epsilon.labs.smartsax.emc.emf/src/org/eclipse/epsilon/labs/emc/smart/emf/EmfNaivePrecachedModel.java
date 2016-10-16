package org.eclipse.epsilon.labs.emc.smart.emf;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
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
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.metamodel.EolElement;
import org.eclipse.epsilon.eol.visitor.resolution.type.tier1.impl.TypeResolver;
import org.eclipse.epsilon.eol.visitor.resolution.variable.impl.VariableResolver;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveMetamodel;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveType;
import org.eclipse.epsilon.labs.smartsax.emc.emf.EmfSmartModel;

public class EmfNaivePrecachedModel extends EmfModel{

	protected ArrayList<EffectiveMetamodel> effectiveMetamodels = new ArrayList<EffectiveMetamodel>();	//protected ModelContainer modelContainer;
	protected boolean smartLoading = true;
	
	public void setEffectiveMetamodels(
			ArrayList<EffectiveMetamodel> effectiveMetamodels) {
		this.effectiveMetamodels = effectiveMetamodels;
	}
	
	public void setSmartLoading(boolean smartLoading) {
		this.smartLoading = smartLoading;
	}
	
	public boolean isSmartLoading() {
		return smartLoading;
	}
	
	public void loadModelFromUri() throws EolModelLoadingException {
		super.loadModelFromUri();
		
		if (effectiveMetamodels.size() != 0) {
			try {
				if (smartLoading) {
					populateCaches();	
				}
			} catch (EolModelElementTypeNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}

	}
	
	
	public void populateCaches() throws EolModelElementTypeNotFoundException
	{
		ArrayList<EClass> allOfKinds = new ArrayList<EClass>();
		ArrayList<EClass> allOfTypes = new ArrayList<EClass>();
		
		for(EffectiveMetamodel mc: effectiveMetamodels)
		{
			for(EffectiveType mec: mc.getAllOfKind())
			{
				EClass eClass = classForName(mec.getName());
				if (eClass != null) {
					allOfKinds.add(eClass);
					cachedKinds.add(eClass);	
				}
				else {
					System.out.println("eclass is null!");
				}
			}
			
			for(EffectiveType mec: mc.getAllOfType())
			{
				EClass eClass = classForName(mec.getName());
				allOfTypes.add(eClass);
				cachedTypes.add(eClass);
			}
		}
		
		Long start = System.nanoTime();
		for (EObject eObject : (Collection<EObject>)allContents()) {
			for(EClass eClass : allOfKinds)
			{
				if (eClass.isInstance(eObject)) {
					kindCache.put(eClass, eObject);
				}
			}
			for(EClass eClass : allOfTypes)
			{
				if (eObject.eClass() == eClass){
					typeCache.put(eClass, eObject);
				}
			}
		}
		System.out.println("(populateCaches took ~"
				+ (System.nanoTime() - start) / 1000000 + "ms to run)");
	}
	
	public static void main(String[] args) throws URISyntaxException, Exception {
//		EolModule eolModule = new EolModule();
//		eolModule.parse(new File("test/grabats.eol"));
//		
//		EmfNaivePrecachedModel model = new EmfNaivePrecachedModel();
//		model.setModelFile(new File("test/set4.xmi").getAbsolutePath());
//		model.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
//		model.setName("DOM");
//		
//		long init = System.nanoTime();
//
//		model.load();
//		System.out.println("(took ~" + (System.nanoTime() - init)
//				/ 1000000 + "ms to load)");
//		init = System.nanoTime();
//		eolModule.getContext().getModelRepository().addModel(model);
//		eolModule.execute();
//		System.out.println("(took ~" + (System.nanoTime() - init)
//				/ 1000000 + "ms to run)");
		
		for(int i = 0; i < 1; i++)
		{
			EolModule eolModule = new EolModule();
			eolModule.parse(new File("test/grabats.eol"));
			
			EmfNaivePrecachedModel smartModel = new EmfNaivePrecachedModel();
			smartModel.setName("m");
			smartModel.setModelFile(new File("test/set2.xmi").getAbsolutePath());
			smartModel.setMetamodelFile(new File("test/JDTAST.ecore").getAbsolutePath());
			
			loadEPackageFromFile("test/JDTAST.ecore");
			
			Ast2EolContext ast2EolContext = new Ast2EolContext();
			EolElement dom = ast2EolContext.getEolElementCreatorFactory().createDomElement(eolModule.getAst(), null, ast2EolContext);
			
			VariableResolver vr = new VariableResolver();
			vr.run(dom);
			
			TypeResolver tr = new TypeResolver();
			tr.getTypeResolutionContext().setModule(eolModule);
			tr.run(dom);
			
			
			LoadingOptimisationAnalyser loa = new LoadingOptimisationAnalyser();
			loa.run(dom);
			loa.getTypeResolutionContext().print();
			
			LoadingOptimisationAnalysisContext loaContext = (LoadingOptimisationAnalysisContext) loa.getTypeResolutionContext();
			
			smartModel.setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			smartModel.setSmartLoading(true);
			long init = System.nanoTime();

			smartModel.load();
			System.out.println("(took ~" + (System.nanoTime() - init) / 1000000 + "ms to load");
			init = System.nanoTime();
			
			eolModule.getContext().getModelRepository().addModel(smartModel);
			eolModule.execute();
			System.out.println("(took ~" + (System.nanoTime() - init)
					/ 1000000 + "ms to run)");
			eolModule.getContext().getModelRepository().dispose();
		}

	}

	public static EPackage loadEPackageFromFile(String fileName) {
		EPackage result = null;
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("ecore", new EcoreResourceFactoryImpl());
		URI uri = URI.createFileURI(new File(fileName).getAbsolutePath());
		Resource resource = resourceSet.getResource(uri, true);
		for (EObject obj : resource.getContents()) {
			if (obj instanceof EPackage) {
				EPackage.Registry.INSTANCE
						.put(((EPackage) obj).getNsURI(), obj);
				result = (EPackage) obj;
				// break;
			}
		}
		return result;
	}



}
