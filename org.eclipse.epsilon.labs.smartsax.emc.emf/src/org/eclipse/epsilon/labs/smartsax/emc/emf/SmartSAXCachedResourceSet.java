package org.eclipse.epsilon.labs.smartsax.emc.emf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;
import org.eclipse.epsilon.labs.effectivemetamodel.impl.EffectiveMetamodel;
import org.eclipse.epsilon.labs.smartsax.SmartSAXResourceFactory;
import org.eclipse.epsilon.labs.smartsax.SmartSAXXMIResource;

public class SmartSAXCachedResourceSet extends CachedResourceSet{
	
	public static final String OPTION_EFFECTIVE_METAMODELS = "effective-metamodels";
	public static final String OPTION_RECONCILE = "reconcile";
	public static final String OPTION_LOAD_ALL_ATTRIBUTES = "load-all-attributes";
	public static final String OPTION_EFFECTIVE_METAMODEL_RECONCILER = "effective-metamodel-reconciler";


	protected ArrayList<EffectiveMetamodel> effectiveMetamodels = new ArrayList<EffectiveMetamodel>();	//protected ModelContainer modelContainer;

	
	public void setEffectiveMetamodels(
			ArrayList<EffectiveMetamodel> effectiveMetamodels) {
		this.effectiveMetamodels = effectiveMetamodels;
	}
	
	public Resource createResource(URI uri) {
		Resource cachedResource = getCache().checkoutResource(uri);
		if (cachedResource == null) {
			cachedResource = createNewResource(uri);
			cachedResource.setTrackingModification(false);
			if (cachedResource instanceof XMLResource) {
				configure((XMLResource) cachedResource);
			}
			getResources().add(cachedResource);
			getCache().cacheResource(uri, cachedResource);
		}
		return cachedResource;
	}
	
	public Resource createNewResource(URI uri) {
		Resource resource = new SmartSAXResourceFactory().createResource(uri);
		
		return resource;
	}
	
	public void configure(XMLResource resource) {
		Map<Object, Object> loadOptions = resource.getDefaultLoadOptions();
		loadOptions.put(OPTION_EFFECTIVE_METAMODELS, effectiveMetamodels);
		loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
		loadOptions.put(XMLResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
		loadOptions.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
	}

}
