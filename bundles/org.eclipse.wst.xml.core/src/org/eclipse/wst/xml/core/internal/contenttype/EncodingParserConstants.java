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
package org.eclipse.wst.xml.core.internal.contenttype;


public interface EncodingParserConstants {

	final String EOF = "EOF"; //$NON-NLS-1$
	final String StringValue = "strval"; //$NON-NLS-1$
	final String MAX_CHARS_REACHED = "MAX_CHARS_REACHED"; //$NON-NLS-1$
	final String UnDelimitedStringValue = "UnDelimitedStringValue"; //$NON-NLS-1$
	final String InvalidTermintatedUnDelimitedStringValue = "InvalidTermintatedUnDelimitedStringValue"; //$NON-NLS-1$
	final String InvalidTerminatedStringValue = "InvalidTerminatedStringValue"; //$NON-NLS-1$


	String UTF83ByteBOM = "UTF83ByteBOM"; //$NON-NLS-1$
	String UTF16LE = "UTF16LE"; //$NON-NLS-1$
	String UTF16BE = "UTF16BE"; //$NON-NLS-1$

}
