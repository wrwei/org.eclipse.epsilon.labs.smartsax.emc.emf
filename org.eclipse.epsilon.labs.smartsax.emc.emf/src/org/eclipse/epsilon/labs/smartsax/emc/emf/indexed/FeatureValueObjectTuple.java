package org.eclipse.epsilon.labs.smartsax.emc.emf.indexed;

import org.eclipse.emf.ecore.EObject;

public class FeatureValueObjectTuple {

	protected Object value;
	protected EObject object;
	
	public FeatureValueObjectTuple(Object value, EObject object)
	{
		this.value = value;
		this.object = object;
	}
	
	
}
