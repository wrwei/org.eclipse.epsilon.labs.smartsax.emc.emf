package org.eclipse.epsilon.labs.smartsax.emc.emf.indexed;

import java.util.ArrayList;

public class FutureList<E> extends ArrayList<E> {

	protected String type;
	protected boolean allOfKind;
	protected boolean populated = false;
	
	public FutureList(String type, boolean allOfKind) {
		super();
		this.type = type;
		this.allOfKind = allOfKind;
		this.add(null);
	}
	
	public String getType() {
		return type;
	}

}
