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
package org.eclipse.wst.sse.core.parser;

import org.eclipse.wst.sse.core.text.ITextRegion;

public class TagMarker {

	// a ITextRegion (meant to be updated with the model) marking the position
	// where this tagname becomes effective
	protected ITextRegion fMarker = null;

	// the tagname
	protected String fTagName = null;

	/**
	 *  
	 */
	public TagMarker() {
		super();
	}

	public TagMarker(String tagname) {
		super();
		setTagName(tagname);
	}

	public TagMarker(String tagname, ITextRegion marker) {
		super();
		setTagName(tagname);
		setMarker(marker);
	}

	/**
	 */
	public final ITextRegion getMarker() {
		return fMarker;
	}

	/**
	 * @return java.lang.String
	 */
	public final String getTagName() {
		return fTagName;
	}

	/**
	 * @return boolean
	 */
	public boolean isGlobal() {
		return fMarker == null;
	}

	/**
	 * @param newMarker
	 */
	public final void setMarker(ITextRegion newMarker) {
		fMarker = newMarker;
	}

	/**
	 * @param newTagname
	 *            java.lang.String
	 */
	public final void setTagName(String newTagName) {
		fTagName = newTagName;
	}
}
