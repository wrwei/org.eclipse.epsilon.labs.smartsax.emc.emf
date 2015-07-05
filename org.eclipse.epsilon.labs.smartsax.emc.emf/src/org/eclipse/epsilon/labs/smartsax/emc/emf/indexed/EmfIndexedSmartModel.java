package org.eclipse.epsilon.labs.smartsax.emc.emf.indexed;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.analysis.optimisation.loading.context.LoadingOptimisationAnalysisContext;
import org.eclipse.epsilon.eol.analysis.optimisation.loading.impl.LoadingOptimisationAnalyser;
import org.eclipse.epsilon.eol.ast2eol.Ast2EolContext;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.AbstractOperation;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.declarative.IAbstractOperationContributorProvider;
import org.eclipse.epsilon.eol.execute.operations.declarative.SelectOperation;
import org.eclipse.epsilon.eol.metamodel.EolElement;
import org.eclipse.epsilon.eol.visitor.resolution.type.tier1.impl.TypeResolver;
import org.eclipse.epsilon.eol.visitor.resolution.variable.impl.VariableResolver;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveFeature;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveMetamodel;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveType;
import org.eclipse.epsilon.labs.smartsax.emc.emf.EmfSmartModel;

public class EmfIndexedSmartModel extends EmfSmartModel { 
//implements IAbstractOperationContributorProvider{

	protected HashMap<EClass, HashMap<String, HashMap<Object, EObject>>> index = new HashMap<EClass, HashMap<String,HashMap<Object,EObject>>>();	
	
	protected HashMap<EClass, EffectiveType> classToTypeMap = new HashMap<EClass, EffectiveType>();
	
	protected ArrayList<EffectiveType> typesToIndex = new ArrayList<EffectiveType>();
	
	public boolean shouldIndexEffectiveType(EffectiveType effectiveType)
	{
		for(EffectiveFeature ef: effectiveType.getAllFeatures())
		{
			if (ef.getUsage() > 1) {
				return true;
			}
		}
		return false;
	}
//	
//	public void insertSubtypesToClassToTypeMap(EClass eClass, EffectiveType et)
//	{
//		EPackage ePackage = eClass.getEPackage();
//		for(EClassifier eClassifier: ePackage.getEClassifiers())
//		{
//			if (eClassifier instanceof EClass) {
//				EClass type = (EClass) eClassifier;
//				if (type.getEAllSuperTypes().contains(eClass)) {
//					classToTypeMap.put(type, et);
//				}
//			}
//		}
//	}
	
	protected void inserIntoIndex(EClass eClass, EObject eObject)
	{
		EffectiveType et = classToTypeMap.get(eClass);
		if (et != null) {
			for(EffectiveFeature ef: et.getAllFeatures())
			{
				if (ef.getUsage() > 1) {
					String featureName = ef.getName();
					EStructuralFeature feature = eClass.getEStructuralFeature(featureName);
					Object value = eObject.eGet(feature);

					HashMap<String, HashMap<Object, EObject>> subMap = index.get(eClass);
					if (subMap != null) {
						HashMap<Object, EObject> subSubMap = subMap.get(featureName);
						if (subSubMap != null) {
							subSubMap.put(value, eObject);
						}
						else {
							subSubMap = new HashMap<Object, EObject>();
							subSubMap.put(value, eObject);
							subMap.put(featureName, subSubMap);
						}
					}
					else {
						subMap =new HashMap<String, HashMap<Object,EObject>>();
						HashMap<Object, EObject> subSubMap = new HashMap<Object, EObject>();
						subSubMap.put(value, eObject);
						subMap.put(featureName, subSubMap);
						index.put(eClass, subMap);
					}
				}
			}
		}
	}
	
	
	@Override
	public void populateCaches() throws Exception {

		ArrayList<EClass> allOfKinds = new ArrayList<EClass>();
		ArrayList<EClass> allOfTypes = new ArrayList<EClass>();
		
		for(EffectiveMetamodel em: effectiveMetamodels)
		{
			for(EffectiveType et: em.getAllOfKind())
			{
				EClass eClass = classForName(et.getName());
				if (eClass != null) {
					allOfKinds.add(eClass);
					cachedKinds.add(eClass);
					classToTypeMap.put(eClass, et);
				}
				else {
					throw new Exception("EClass: " + et.getName() + " cannot be found!");
				}
			}
			
			for(EffectiveType et: em.getAllOfType())
			{
				EClass eClass = classForName(et.getName());
				if (eClass != null) {
					allOfTypes.add(eClass);
					cachedTypes.add(eClass);
					classToTypeMap.put(eClass, et);
				}
				else {
					throw new Exception("EClass: " + et.getName() + " cannot be found!");
				}
			}
		}
		
		for (EObject eObject : (Collection<EObject>)allContents()) {
			for(EClass eClass : allOfKinds)
			{
				if (eClass.isInstance(eObject)) {
					kindCache.put(eClass, eObject);
					inserIntoIndex(eClass, eObject);
				}
			}
			for(EClass eClass : allOfTypes)
			{
				if (eObject.eClass() == eClass){
					typeCache.put(eClass, eObject);
					inserIntoIndex(eClass, eObject);
				}
			}
		}
	}

//	@Override
//	public IAbstractOperationContributor getAbstractOperationContributor(
//			Object target) {
//
//		
//		System.out.println("called");
//		if (target instanceof FutureList<?>) {
//			return new IAbstractOperationContributor() {
//				@Override
//				public AbstractOperation getAbstractOperation(String name) {
//					if (name.equalsIgnoreCase("select")) {
//						
//						return new SelectOperation() {
//							
//							@Override
//							public Object execute(Object target,
//									Variable iterator, Expression expression,
//									IEolContext context, boolean returnOnFirstMatch)
//									throws EolRuntimeException {
//								
//								FutureList<?> list = (FutureList<?>) target;
//								
//								if (expression instanceof EqualsOperatorExpression) {
//									EqualsOperatorExpression equalsOperatorExpression = (EqualsOperatorExpression) expression;
//									if (equalsOperatorExpression.getFirstOperand() instanceof PropertyCallExpression) {
//										PropertyCallExpression propertyCallExpression = (PropertyCallExpression) equalsOperatorExpression.getFirstOperand();
//										if (propertyCallExpression.getTargetExpression() instanceof NameExpression) {
//											ArrayList<Object> results = new ArrayList<Object>();
//											results.add(list.getType() + "." + propertyCallExpression.getPropertyNameExpression().getName() + "=" + 
//													context.getExecutorFactory().executeAST(equalsOperatorExpression.getSecondOperand(), context));
//											return results;
//										}
//									}
//								}
//								return new SelectOperation().execute(target, iterator, expression, context);
//							}
//						};
//					}
//					return null;
//				}
//			};
//		}
//		return null;
//	
//	}
	
	
	public static void main(String[] args) throws URISyntaxException, Exception {
		for(int i = 0; i < 1; i++)
		{
			EolModule eolModule = new EolModule();
			eolModule.parse(new File("test/grabats.eol"));
			
			EmfIndexedSmartModel smartModel = new EmfIndexedSmartModel();
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
			
			loa.getTypeResolutionContext().print();
			
			LoadingOptimisationAnalysisContext loaContext = (LoadingOptimisationAnalysisContext) loa.getTypeResolutionContext();
			
			smartModel.setEffectiveMetamodels(loaContext.getEffectiveMetamodels());
			smartModel.preProcess();
			smartModel.setSmartLoading(true);
			smartModel.setPartialLoading(false);
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

}
