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
package org.eclipse.wst.sse.core.contentmodel;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.wst.contentmodel.CMDocument;
import org.eclipse.wst.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.contentmodel.CMNamespace;
import org.eclipse.wst.contentmodel.CMNode;

/**
 * This class can be used to intialize specific 
 * variables that need a content model, until the 
 * true content model is available. This prevents 
 * having to do lots of null checks.
 */
public class NullContentModel implements CMDocument {

	private class NullCMNamedNodeMap implements CMNamedNodeMap {
		public int getLength() {
			return 0;
		}

		public CMNode getNamedItem(String name) {
			return null;
		}

		public CMNode item(int index) {
			return null;
		}

		public Iterator iterator() {
			return new NullIterator();
		}

	}

	private class NullIterator implements Iterator {
		public NullIterator() {
		}

		public boolean hasNext() {
			return false;
		}

		public Object next() {
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException("can not remove regions via iterator"); //$NON-NLS-1$

		}

	}

	public NullContentModel() {
		super();
	}

	public CMNamedNodeMap getElements() {
		return new NullCMNamedNodeMap();
	}

	public CMNamedNodeMap getEntities() {
		return new NullCMNamedNodeMap();
	}

	public CMNamespace getNamespace() {
		return null;
	}

	public String getNodeName() {
		return null;
	}

	public int getNodeType() {
		return 0;
	}

	public Object getProperty(String propertyName) {
		return null;
	}

	public boolean supports(String propertyName) {
		return false;
	}

}
