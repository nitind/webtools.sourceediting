/*
* Copyright (c) 2002 IBM Corporation and others.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*   IBM - Initial API and implementation
*   Jens Lukowski/Innoopract - initial renaming/restructuring
* 
*/
package org.eclipse.wst.xml.core.internal.validate;



import org.eclipse.wst.sse.core.validate.ValidationAdapter;
import org.eclipse.wst.sse.core.validate.ValidationReporter;

public abstract class ValidationComponent implements ValidationAdapter {

	protected ValidationReporter reporter = null;

	/**
	 * ValidationComponent constructor comment.
	 */
	public ValidationComponent() {
		super();
	}

	/**
	 * Allowing the INodeAdapter to compare itself against the type
	 * allows it to return true in more than one case.
	 */
	public boolean isAdapterForType(Object type) {
		return (type == ValidationAdapter.class);
	}

	/**
	 */
	public void notifyChanged(org.eclipse.wst.sse.core.INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
		// This method will be implemented in the V2.
	}

	/**
	 */
	public void setReporter(ValidationReporter reporter) {
		this.reporter = reporter;
	}
}
