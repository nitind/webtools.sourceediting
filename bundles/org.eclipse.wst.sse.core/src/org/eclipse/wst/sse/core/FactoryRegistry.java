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
package org.eclipse.wst.sse.core;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.sse.core.internal.Logger;


/**
 * This class simply maintains the list of factories and
 * returns singleton instances of them. Some "built in"
 * types are automatically created form FactoryConfig,
 * if not found registerd, but normally clients can/should
 * register their own factories.
 * 
 */
public class FactoryRegistry implements IFactoryRegistry {

	private List factories;

	/**
	 * 
	 */
	public FactoryRegistry() {
		super();

	}

	public void addFactory(AdapterFactory factory) {
		_getFactories().add(factory);
	}

	protected List _getFactories() {

		if (factories == null) {
			// may need to use java.util.Collections.synchronizedList() if syncronization becomes
			// necessary (and if so, remember to synchronize on factories)
			factories = new ArrayList();
		}
		return factories;

	}

	/** 
	 * This method is a not a pure resistry. Factories retrieved based on their response
	 * to "isFactoryForType(type)". Note that if there is more than one factory that can
	 * answer 'true' that the most recently added factory is used.
	 */
	public AdapterFactory getFactoryFor(Object type) {

		AdapterFactory result = null;
		if (factories == null)
			return null;
		int listSize = factories.size();
		for (int i = listSize - 1; i >= 0; i--) {
			// It is the adapter factories responsibility to answer isFactoryForType so it gets choosen.
			// Notice we are going through the list backwards to get the factory added last.
			AdapterFactory a = (AdapterFactory) factories.get(i);
			if (a.isFactoryForType(type)) {
				result = a;
				break;
			}
		}
		return result;

	}

	/**
	 *
	 */
	public void release() {
		// modified to work on copy of list, for V5PTF1
		// send release to local copy of list 
		// of factories, since some factories, during 
		// their release function, may remove
		// themselves from the registry.
		List localList = getFactories();
		Iterator factoryIterator = localList.iterator();
		while (factoryIterator.hasNext()) {
			AdapterFactory a = (AdapterFactory) factoryIterator.next();
			// to help bullet proof code, we'll catch and log 
			// any messages thrown by factories during release, 
			// but we'll attempt to keep going. 
			// In nearly all cases, though, such errors are 
			// severe for product/client, and need to be fixed.
			try {
				a.release();
			}
			catch (Exception e) {
				Logger.logException("Program problem releasing factory" + a, e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Removes a factory if it can be retrieved by getFactoryFor(type). If there
	 * is more than one, all are removed. If there is none, the call simply returns
	 * (that is, it is not considered an error).
	 */
	public void removeFactoriesFor(java.lang.Object type) {
		if (factories != null) {
			int listSize = factories.size();
			// we'll go backwards through list, since we're removing, so 'size' change won't matter.
			// Note: I'm assuming other items in the collection do not change position
			// simply because another was removed.
			for (int i = listSize - 1; i >= 0; i--) {
				// It is the adapter factories responsibility to answer isFactoryForType so it gets choosen.
				AdapterFactory a = (AdapterFactory) factories.get(i);
				if (a.isFactoryForType(type)) {
					factories.remove(a);
				}
			}
		}
	}

	public void removeFactory(AdapterFactory factory) {
		_getFactories().remove(factory);

	}

	/**
	 * Returns a shallow cloned list of the factories 
	 * in the registry. Note: this can not be used to 
	 * add/remove factories. Its primarily provided for 
	 * those few cases where a list of factories must be 
	 * copied from one model and added to another.
	 */
	public List getFactories() {
		// note: for object integrity, we don't let anyone get 
		// our main list (so they have to add through addFactory), 
		// but we will return a shallow "cloned" list.
		List factoryList = new ArrayList();
		List internalList = _getFactories();
		Iterator internalListIterator = internalList.iterator();
		while (internalListIterator.hasNext()) {
			// we assume they are all already AdapterFactories, 
			// but no profit in casting here
			Object factory = internalListIterator.next();
			factoryList.add(factory);
		}
		return factoryList;
	}

	/*
	 * @see IFactoryRegistry#contains(Object)
	 */
	public boolean contains(Object type) {
		boolean result = false;
		// note: we're not using cloned list, so stricly speaking
		// is not thread safe.
		List internalList = _getFactories();
		Iterator internalListIterator = internalList.iterator();
		while (internalListIterator.hasNext()) {
			AdapterFactory factory = (AdapterFactory) internalListIterator.next();
			if (factory.isFactoryForType(type)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public void clearFactories() {
		factories.clear();
	}

}
