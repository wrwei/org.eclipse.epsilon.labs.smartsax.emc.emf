package org.eclipse.epsilon.labs.emc.smart.emf;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
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

public class EmfPrecachedModel_v4 extends EmfModel{
	

	
	protected ArrayList<EffectiveMetamodel> effectiveMetamodels = new ArrayList<EffectiveMetamodel>();

	protected HashMap<EPackage, HashMap<EClass, ArrayList<EReference>>> traversalPlans = new HashMap<EPackage, HashMap<EClass,ArrayList<EReference>>>();

	protected HashMap<String, ArrayList<String>> placeHolderObjects = new HashMap<String, ArrayList<String>>();

	
	protected ArrayList<EClass> visitedClasses = new ArrayList<EClass>();

	HashSet<EClass> allOfKinds = new HashSet<EClass>();
	HashSet<EClass> allOfTypes = new HashSet<EClass>();

	protected boolean smartLoading = true;

	public void setEffectiveMetamodels(
			ArrayList<EffectiveMetamodel> effectiveMetamodels) {
		this.effectiveMetamodels = effectiveMetamodels;
	}
	
	public void setSmartLoading(boolean smartLoading) {
		this.smartLoading = smartLoading;
	}

	
	public static void main(String[] args) throws URISyntaxException, Exception {
		for(int i = 0; i < 1; i++)
		{
			EolModule eolModule = new EolModule();
			eolModule.parse(new File("test/set0_10percent.eol"));
			
			EmfPrecachedModel_v3 smartModel = new EmfPrecachedModel_v3();
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
			
			smartModel.preProcess();

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

	public void preProcess() throws EolModelLoadingException
	{
		ResourceSet resourceSet = createResourceSet();
		
        // Check if global package registry contains the EcorePackage
		if (EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI) == null) {
			EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		}
		
		determinePackagesFrom(resourceSet);
		reconcile();

	}

	
	public void reconcile()
	{
		//for each epackage, add to 'actualObjectToLoad' considering 
		for(EPackage ePackage: packages)
		{
			//for each eclassifier
			for(EClassifier eClassifier: ePackage.getEClassifiers())
			{
				//if eclassifier is a eclass
				if (eClassifier instanceof EClass) {
					
					//cast to eClass
					EClass eClass = (EClass) eClassifier;
					
					//clear visited class
					visitedClasses.clear();
					
					//visit EClass
					planTraversal(eClass);
				}
			}
		}
		
		
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

	public void populateCaches() throws Exception
	{
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

		//reconcile();
		for(Resource resource : getResources())
		{
			for(EObject eObject: resource.getContents())
			{
				visitEObject(eObject);
			}
		}
	}
	
	public ArrayList<EReference> getReferencesToVisit(EClass eClass)
	{
		EPackage ePackage = eClass.getEPackage();
		HashMap<EClass, ArrayList<EReference>> subMap = traversalPlans.get(ePackage);
		if (subMap != null) {
			return subMap.get(eClass);
		}
		return null;
	}

	
	public void visitEObject(EObject eObject)
	{
	
		if (traversalPlanContains(eObject.eClass())) {
			processEObject(eObject);
			
			for(EObject obj: eObject.eContents())
			{
				visitEObject(obj);
			}
		}
	}
	
	public boolean traversalPlanContains(EClass eClass)
	{
		EPackage ePackage = eClass.getEPackage();
		HashMap<EClass, ArrayList<EReference>> subMap = traversalPlans.get(ePackage);
		if (subMap != null) {
			if (subMap.containsKey(eClass)) {
				return true;
			}
		}
		return false;
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



	
	public void planTraversal(EClass eClass)
	{
		//add this class to the visited
		visitedClasses.add(EcoreUtil.copy(eClass));
		
		//if this one is a live class, should addRef()
		if (liveClass(eClass.getEPackage(), eClass.getName())) {
			//add class to objectsAndRefNamesToVisit
			addToTraversalPlan(eClass, null);
			//add to placeholder if necessary
			insertPlaceHolderOjbects(eClass.getEPackage(), eClass);
		}
		
		//for each reference
		for(EReference eReference: eClass.getEAllReferences())
		{
			//if the etype of the reference has not been visited
			if (!visitedEClass((EClass) eReference.getEType())) {
				//visit the etype
				planTraversal((EClass) eReference.getEType());
			}
			
			//if is live reference
			if (liveReference(eReference)) {
				//add class and reference to objectsAndRefNamesToVisit
				addToTraversalPlan(eClass, eReference);
				//insert placeholder if necessary
				insertPlaceHolderOjbects(eClass.getEPackage(), eClass);
			}
		}
		
		//for every eclassifier
		for(EClassifier every: eClass.getEPackage().getEClassifiers())
		{
			if (every instanceof EClass) {
				EClass theClass = (EClass) every;
				
				if (theClass.getEAllSuperTypes().contains(eClass)) {
					for(EReference eReference: theClass.getEAllReferences())
					{
						if (!visitedEClass((EClass) eReference.getEType())) {
							planTraversal((EClass) eReference.getEType());
						}
						
						if (liveReference(eReference)) {
							addToTraversalPlan(theClass, eReference);
							insertPlaceHolderOjbects(theClass.getEPackage(), theClass);
						}
					}
				}
			}
		}
	}

	
	//determines if a class is live class, this is used to generate the traversal path
	public boolean liveClass(EPackage ePackage, String className)
	{
		//for each effective metamodel
		for(EffectiveMetamodel em: effectiveMetamodels)
		{
			//get the package first
			if (em.getName().equalsIgnoreCase(ePackage.getName())) {
				
				//for all of kinds
				for(EffectiveType et: em.getAllOfKind())
				{
					//the element name
					String elementName = et.getName();
					//if name equals return true
					if (className.equals(elementName)) {
						return true;
					}
					
					//get the eclass for the mec
					EClass kind = (EClass) ePackage.getEClassifier(elementName);
					//get the eclass for the current class under question
					EClass actual = (EClass) ePackage.getEClassifier(className);
					//if the current class under question is a sub class of the mec, should return true
					
					if(actual.getEAllSuperTypes().contains(kind))
					{
						return true;
					}
					//if the current class under question is a super class of the mec, should also return true
					if (kind.getEAllSuperTypes().contains(actual)) 
					{
						return true;
					}
				}
				
				for(EffectiveType et: em.getAllOfType())
				{
					//the element n ame
					String elementName = et.getName();
					//if name equals return true
					if (className.equals(elementName)) {
						return true;
					}
					
					//get the eclass for the mec
					EClass type = (EClass) ePackage.getEClassifier(elementName);
					//get the eclass for the class under question
					EClass actual = (EClass) ePackage.getEClassifier(className);
					

					//if the class under question is a super class of the mec, should return true
					if (type.getEAllSuperTypes() != null && type.getEAllSuperTypes().contains(actual)) 
					{
						return true;
					}
				}
				
				for(EffectiveType et: em.getTypes())
				{
					//the element n ame
					String elementName = et.getName();
					//if name equals return true
					if (className.equals(elementName)) {
						return true;
					}
					
					//get the eclass for the mec
					EClass type = (EClass) ePackage.getEClassifier(elementName);
					//get the eclass for the class under question
					EClass actual = (EClass) ePackage.getEClassifier(className);
					//if the class under question is a super class of the mec, should return true
					if (type.getEAllSuperTypes() != null && type.getEAllSuperTypes().contains(actual)) 
					{
						return true;
					}
				}
				
				ArrayList<String> placeHolders = placeHolderObjects.get(em.getName());
				
				if (placeHolders != null) {
					for(String t : placeHolderObjects.get(em.getName()))
					{
						if (t.equals(className)) {
							return true;
						}
						
						//get the eclass for the mec
						EClass type = (EClass) ePackage.getEClassifier(t);
						//get the eclass for the class under question
						EClass actual = (EClass) ePackage.getEClassifier(className);
						//if the class under question is a super class of the mec, should return true
						if (type.getEAllSuperTypes().contains(actual)) 
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public void addToTraversalPlan(EClass eClass, EReference eReference)
	{
		EPackage epackage = eClass.getEPackage();
		//get the submap with the epackage name
		HashMap<EClass, ArrayList<EReference>> subMap = traversalPlans.get(epackage);
		//if sub map is null
		if (subMap == null) {
			//create new sub map
			subMap = new HashMap<EClass, ArrayList<EReference>>();
			//create new refs for the map
			ArrayList<EReference> refs = new ArrayList<EReference>();
			//if eReference is not null
			if (eReference != null) {
				//add the eReference to the ref
				refs.add(eReference);
			}
			//add the ref to the sub map
			subMap.put(eClass, refs);
			//add the sub map to objectsAndRefNamesToVisit
			traversalPlans.put(epackage, subMap);
		}
		else {
			//if sub map is not null, get the refs by class name
			ArrayList<EReference> refs = subMap.get(eClass);

			//if refs is null, create new refs and add the ref and then add to sub map
			if (refs == null) {
				refs = new ArrayList<EReference>();
				if(eReference != null)
				{
					refs.add(eReference);
				}
				subMap.put(eClass, refs);
			}
			//if ref is not null, add the ref
			else {
				if (eReference != null) {
					if (!refs.contains(eReference)) {
						refs.add(eReference);	
					}
				}
			}
		}
	}	
	
	public void insertPlaceHolderOjbects(EPackage ePackage, EClass eClass)
	{
		//inserted 
		boolean inserted = false;
		//for each effective metamodel
		for(EffectiveMetamodel em: effectiveMetamodels)
		{
			//get the matching package
			if (em.getName().equals(ePackage.getName())) {
				
				//for each type in all of kind
				for(EffectiveType et: em.getAllOfKind())
				{
					//if types match, return
					if (et.getName().equals(eClass.getName())) {
						inserted = true;
						return;
					}
					
					//if types match return
					EClass kind = (EClass) ePackage.getEClassifier(et.getName());
					for(EClass superClass: eClass.getEAllSuperTypes())
					{
						if (kind.getName().equals(superClass.getName())) {
							inserted = true;
							return;
						}
					}
				}
				
				//for each type in all of type
				for(EffectiveType et: em.getAllOfType())
				{
					//if types match, return
					if (et.getName().equals(eClass.getName())) {
						inserted = true;
						return;
					}
				}
				
				ArrayList<String> placeHolders = placeHolderObjects.get(em.getName());
				
				if (placeHolders != null) {
					for(String t : placeHolderObjects.get(em.getName()))
					{
						if (t.equals(eClass.getName())) {
							inserted = true;
							return;
						}
					}
				}
				
				
				
				//if not inserted, add to types
				if (!inserted) {
					inserted = true;
					addPlaceHolderObject(ePackage.getName(), eClass.getName());
					return;
				}
			}
		}
		//if not inserted, create effective metamodel and add to types
		if (!inserted) {
			addPlaceHolderObject(ePackage.getName(), eClass.getName());
			EffectiveMetamodel newEffectiveMetamodel = new EffectiveMetamodel(ePackage.getName());
			effectiveMetamodels.add(newEffectiveMetamodel);
		}
	}
	
	public void addPlaceHolderObject(String ePackage, String type)
	{
		if (placeHolderObjects.containsKey(ePackage)) {
			ArrayList<String> metamodel = placeHolderObjects.get(ePackage);
			if (metamodel.contains(type)) {
				return;
			}
			else {
				metamodel.add(type);
			}
		}
		else {
			ArrayList<String> metamodel = new ArrayList<String>();
			metamodel.add(type);
			placeHolderObjects.put(ePackage, metamodel);
		}
	}

	//test to see if the class has been visited
	public boolean visitedEClass(EClass eClass)
	{
		for(EClass clazz: visitedClasses)
		{
			if (clazz.getName().equals(eClass.getName())) {
				return true;
			}
		}
		return false;
	}


	//returns true if the reference is live
	public boolean liveReference(EReference eReference)
	{
		//if reference is containment, we are not looking into non-containment references
		if(eReference.isContainment())
		{
			//get the etype
			EClassifier eClassifier = eReference.getEType();
			EClass etype = (EClass) eClassifier;
			
			//if etype is a live class, return true
			if (liveClass(etype.getEPackage(), etype.getName())) {
				return true;
			}
			
			return false;
		}
		return false;
		
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
