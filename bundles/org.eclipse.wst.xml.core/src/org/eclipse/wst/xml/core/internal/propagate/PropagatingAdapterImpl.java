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
package org.eclipse.wst.xml.core.internal.propagate;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.sse.core.AdapterFactory;
import org.eclipse.wst.sse.core.INodeNotifier;
import org.eclipse.wst.sse.core.PropagatingAdapter;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class PropagatingAdapterImpl implements PropagatingAdapter {

	public static final Class PropagatingAdapterClass = PropagatingAdapter.class;
	private List adaptOnCreateFactories = new ArrayList();

	/**
	 * AbstractPropagatingAdapterImpl constructor comment.
	 */
	public PropagatingAdapterImpl() {
		super();
	}

	/**
	 * Allowing the INodeAdapter to compare itself against the type
	 * allows it to return true in more than one case.
	 */
	public boolean isAdapterForType(Object type) {
		return type.equals(PropagatingAdapterClass);
	}

	/**
	 */
	public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
		// DMW, 2002.8.10. I changed this so we only propagate to Elements ... not attributes too!
		// I'm assuming this will help performance and memory, but don't know if anyone was depending on 
		// this being proagate to attributes.
		if (eventType == INodeNotifier.ADD && isInteresting(newValue)) {
			propagateTo((XMLNode) newValue);
		}
		//	else if (eventType == INodeNotifier.CONTENT_CHANGED) {
		//		notifier.getAdapterFor(PropagatingAdapterClass);
		//	}
		//	else if (eventType == INodeNotifier.CHANGE) {
		//	}
		//		else if (eventType == INodeNotifier.REMOVE && isInteresting(oldValue)) {
		//			unadaptOnRemove((XMLNode)oldValue);
		//		}
		//	else if (eventType == INodeNotifier.STRUCTURE_CHANGED) {
		//	}
	}

	protected void propagateTo(XMLNode node) {
		// get adapter to ensure its created
		node.getAdapterFor(PropagatingAdapterClass);
		adaptOnCreate(node);
		propagateToChildren(node);
	}

	protected void propagateToChildren(XMLNode parent) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			propagateTo((XMLNode) child);
		}
	}

	/**
	 * This mechanism can be made "easier to use" later.
	 */
	public void addAdaptOnCreateFactory(AdapterFactory factory) {
		adaptOnCreateFactories.add(factory);
	}

	/**
	 * @see PropagatingAdapter#release()
	 */
	public void release() {
		if (adaptOnCreateFactories != null) {
			Iterator iterator = adaptOnCreateFactories.iterator();
			while (iterator.hasNext()) {
				AdapterFactory factory = (AdapterFactory) iterator.next();
				factory.release();
			}
		}
	}

	protected void adaptOnCreate(XMLNode node) {
		// give each of the factories a chance to adapt the node, if it
		// chooses to
		Iterator iterator = adaptOnCreateFactories.iterator();
		while (iterator.hasNext()) {
			AdapterFactory factory = (AdapterFactory) iterator.next();
			factory.adapt(node);
		}

	}

	//	protected void unadaptOnRemove(INodeNotifier node) {
	//		// give each of the factories a chance to process remove event
	//		// This is a bit out of the normal adapter pattern, but I couldn't 
	//		// think of a better way to "remove" pageDirectiveWatchers, if and 
	//		// when the page directive was 'removed' (edited). 
	//		// 
	//		Iterator iterator = adaptOnCreateFactories.iterator();
	//		while (iterator.hasNext()) {
	//			AdapterFactory factory = (AdapterFactory) iterator.next();
	//			if (factory instanceof PropagatingAdapterFactory) {
	//				((PropagatingAdapterFactory)factory).unadapt(node);
	//			}
	//		}
	//
	//	}

	/**
	 * @see PropagatingAdapter#initializeForFactory(AdapterFactory, INodeNotifier)
	 */
	public void initializeForFactory(AdapterFactory factory, INodeNotifier node) {
		// we're DOM specific implimentation
		if (node instanceof XMLNode) {
			XMLNode xmlNode = (XMLNode) node;
			propagateTo(xmlNode);
		}
	}

	/**
	 * Gets the adaptOnCreateFactories.
	 * @return Returns a List
	 */
	public List getAdaptOnCreateFactories() {
		return adaptOnCreateFactories;
	}

	protected boolean isInteresting(Object newValue) {
		return (newValue != null && (newValue instanceof Element || newValue instanceof Document || newValue instanceof DocumentType));
	}

}
