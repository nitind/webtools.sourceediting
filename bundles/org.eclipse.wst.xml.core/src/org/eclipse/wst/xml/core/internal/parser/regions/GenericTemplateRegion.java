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
package org.eclipse.wst.xml.core.internal.parser.regions;

import org.eclipse.wst.sse.core.events.StructuredDocumentEvent;
import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.text.ITextRegion;
import org.eclipse.wst.xml.core.parser.XMLRegionContext;


/**
 * 
 * This class is not intended to be used, its just present to server
 * as a generic starting point for adding new specific region types.
 */

public class GenericTemplateRegion implements ITextRegion {
	// specify correct type
	static private final String fType = XMLRegionContext.UNDEFINED;
	private int fTextLength;
	private int fLength;
	private int fStart;


	public GenericTemplateRegion() {
		super();
	}

	public GenericTemplateRegion(int start, int textLength, int length) {
		this();
		fStart = start;
		fTextLength = textLength;
		fLength = length;
	}

	/**
	 * @deprecated
	 */
	public void adjust(int i) {
		fStart += i;

	}

	public void adjustLengthWith(int i) {
		fLength += i;

	}

	public void adjustStart(int i) {
		fStart += i;

	}

	public void equatePositions(ITextRegion region) {
		fStart = region.getStart();
		fLength = region.getLength();
		fTextLength = region.getTextLength();
	}

	public int getEnd() {
		return fStart + fLength;
	}

	public int getLength() {
		return fLength;
	}

	public int getStart() {
		return fStart;
	}

	public int getTextEnd() {
		return fStart + fTextLength;
	}

	public int getTextLength() {
		return fTextLength;
	}

	public String getType() {
		return fType;
	}

	public StructuredDocumentEvent updateModel(Object requester, IStructuredDocumentRegion parent, String changes, int requestStart, int lengthToReplace) {
		// can never be updated
		return null;
	}

	public String toString() {
		return RegionToStringUtil.toString(this);
	}

	/* (non-Javadoc)
	 */
	public void adjustTextLength(int i) {
		fTextLength += 1;

	}

}
