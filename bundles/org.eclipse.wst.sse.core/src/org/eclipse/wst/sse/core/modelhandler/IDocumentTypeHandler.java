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
package org.eclipse.wst.sse.core.modelhandler;

import org.eclipse.wst.sse.core.document.IDocumentCharsetDetector;
import org.eclipse.wst.sse.core.document.IDocumentLoader;

/**
 * Interface to allow custom content types to be defined as 
 * extension points in plugins.
 */
public interface IDocumentTypeHandler {

	/**
	 * The Loader is reponsible for decoding the Resource, 
	 */
	IDocumentLoader getDocumentLoader();

	/**
	 * @deprecated - likely to go away, so I marked
	 * as deprecated to discoursage use
	 */
	IDocumentCharsetDetector getEncodingDetector();

	/**
	 * Must return unique ID that is the 
	 * same as identified in plugin registry
	 */
	String getId();



}
