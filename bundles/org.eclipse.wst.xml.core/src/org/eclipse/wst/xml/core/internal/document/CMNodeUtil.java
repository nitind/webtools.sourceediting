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
package org.eclipse.wst.xml.core.internal.document;



import org.eclipse.wst.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.contentmodel.CMElementDeclaration;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 */
public class CMNodeUtil {

	/**
	 */
	public static CMAttributeDeclaration getAttributeDeclaration(Attr attr) {
		if (attr == null)
			return null;
		return ((AttrImpl) attr).getDeclaration();
	}

	/**
	 */
	public static CMElementDeclaration getElementDeclaration(Element element) {
		if (element == null)
			return null;
		return ((ElementImpl) element).getDeclaration();
	}
}
