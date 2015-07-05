package org.eclipse.epsilon.labs.emc.smart.emf;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.epsilon.emc.emf.DefaultXMIResource;
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

public class EmfPrecachedModel extends EmfModel{

	protected ArrayList<EffectiveMetamodel> effectiveMetamodels = new ArrayList<EffectiveMetamodel>();	//protected ModelContainer modelContainer;

	protected ArrayList<EClass> eClassesToVisit = new ArrayList<EClass>();
	ArrayList<EClass> allOfKinds = new ArrayList<EClass>();
	ArrayList<EClass> allOfTypes = new ArrayList<EClass>();

	
	protected boolean smartLoading = true;
	
	public void setEffectiveMetamodels(
			ArrayList<EffectiveMetamodel> effectiveMetamodels) {
		this.effectiveMetamodels = effectiveMetamodels;
	}
	
	public void setSmartLoading(boolean smartLoading) {
		this.smartLoading = smartLoading;
	}
	
	public void loadModelFromUri() throws EolModelLoadingException {
		ResourceSet resourceSet = createResourceSet();
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("model", new DefaultXMIResource.Factory());
		
        // Check if global package registry contains the EcorePackage
		if (EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI) == null) {
			EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		}
		
		determinePackagesFrom(resourceSet);
		
		// Note that AbstractEmfModel#getPackageRegistry() is not usable yet, as modelImpl is not set
		for (EPackage ep : packages) {
			String nsUri = ep.getNsURI();
			if (nsUri == null || nsUri.trim().length() == 0) {
				nsUri = ep.getName();
			}
			resourceSet.getPackageRegistry().put(nsUri, ep);
		}
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		Resource model = resourceSet.createResource(modelUri);
		
		if (this.readOnLoad) {
			try {
				model.load(null);
				if (expand) {
					EcoreUtil.resolveAll(model);
				}
			} catch (IOException e) {
				throw new EolModelLoadingException(e, this);
			}
		}
		modelImpl = model;
		
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
		
		effectiveMetamodels.clear();
		effectiveMetamodels = null;

	}
	
//	public void addToTraversalPlan(EClass eClass, EStructuralFeature feature)
//	{
//		EPackage ePackage = eClass.getEPackage();
//		String packageName = ePackage.getName();
//		HashMap<String, ArrayList<String>> subMap = traversalPlan.get(packageName);
//		if (subMap != null) {
//			ArrayList<String> features = subMap.get(eClass.getName());
//			if (features != null) {
//				if (feature != null) {
//					features.add(feature.getName());	
//				}
//			}
//			else {
//				if (feature != null) {
//					features = new ArrayList<String>();
//					features.add(feature.getName());
//					subMap.put(eClass.getName(), features);
//				}
//			}
//		}
//		else {
//			subMap = new HashMap<String, ArrayList<String>>();
//			ArrayList<String> features = new ArrayList<String>();
//			if (feature != null) {
//				features.add(feature.getName());
//			}
//			subMap.put(eClass.getName(), value)
//		}
//	}
	
	public void expandEffectiveMetamodel()
	{
		for(EffectiveMetamodel em: effectiveMetamodels)
		{
			EPackage ePackage = getEPackage(em.getName());
			if (ePackage != null) {
				for(EffectiveType et: em.getAllOfKind())
				{
					EClass eClass = (EClass) ePackage.getEClassifier(et.getName());
					if (eClass != null) {
						
						allOfKinds.add(eClass);
						cachedKinds.add(eClass);	

						
						eClassesToVisit.add(eClass);
						for(EClassifier every: ePackage.getEClassifiers())
						{
							if (every instanceof EClass) {
								EClass e = (EClass) every;
								if (e.getEAllSuperTypes().contains(eClass)) {
									if (!eClassesToVisit.contains(e)) {
										eClassesToVisit.add(e);
									}
								}
							}
						}
					}
				}
				for(EffectiveType et: em.getAllOfType())
				{
					EClass eClass = (EClass) ePackage.getEClassifier(et.getName());
					if (eClass != null) {
						
						allOfTypes.add(eClass);
						cachedTypes.add(eClass);

						eClassesToVisit.add(eClass);
					}
				}
			}
		}
	}
	
	public boolean isTypeOrSubTypeOrSuperTypeOfAny(EClass eClass)
	{
		for(EClass every: eClassesToVisit)
		{
			if (eClass.equals(every)) {
				return true;
			}
			if (every.getEAllSuperTypes().contains(eClass)) {
				return true;
			}
			if (eClass.getEAllSuperTypes().contains(every)) {
				return true;
			}
		}
		return false;
	}
	
	public void planTraversal()
	{
//		ResourceSet resourceSet = createResourceSet();
//
//		if (EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI) == null) {
//			EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
//		}
//		
//		try {
//			determinePackagesFrom(resourceSet);
//		} catch (EolModelLoadingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		expandEffectiveMetamodel();
		for(EPackage ep: packages)
		{
			for(EClassifier eClassifier: ep.getEClassifiers())
			{
				if (eClassifier instanceof EClass) {
					EClass eClass = (EClass) eClassifier;
					if (!eClassesToVisit.contains(eClass)) {
						loop1:
						for(EReference eReference: eClass.getEAllReferences())
						{
							EClass eType = (EClass) eReference.getEType();
							if (isTypeOrSubTypeOrSuperTypeOfAny(eType)) {
								eClassesToVisit.add(eClass);
								break loop1;
							}
						}
					}
				}
			}
		}
	}
	
	public EPackage getEPackage(String name)
	{
		for(EPackage ep: packages)
		{
			if (ep.getName().equals(name)) {
				return ep;
			}
		}
		return null;
	}
	
	public void visitEObject(EObject eObject)
	{
		if (eClassesToVisit.contains(eObject.eClass())) {
			processEObject(eObject);
		}
		for(EObject content: eObject.eContents())
		{
			visitEObject(content);
		}
	}
	
	public void processEObject(EObject eObject)
	{
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
	
	
	public void populateCaches() throws Exception
	{
		
		planTraversal();
		for(Resource resource : getResources())
		{
			for(EObject eObject: resource.getContents())
			{
				visitEObject(eObject);
			}
		}
		
	}
	
	
	public static void main(String[] args) throws URISyntaxException, Exception {
		for(int i = 0; i < 1; i++)
		{
			EolModule eolModule = new EolModule();
			eolModule.parse(new File("test/grabats.eol"));
			
			EmfPrecachedModel smartModel = new EmfPrecachedModel();
			smartModel.setName("m");
			smartModel.setModelFile(new File("test/set0.xmi").getAbsolutePath());
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
			
			LoadingOptimisationAnalysisContext loaContext = (LoadingOptimisationAnalysisContext) loa.getTypeResolutionContext();
			
			smartModel.setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			smartModel.setSmartLoading(true);
			//smartModel.planTraversal();
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
				//break;
			}
		}
		return result;
	}


}
