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
package org.eclipse.wst.xml.ui.templates;

import org.eclipse.wst.xml.ui.nls.ResourceHandler;

/**
 * Templates of this context type apply to any tags within XML content type.
 */
public class TemplateContextTypeXMLTag extends TemplateContextTypeXML {

	public TemplateContextTypeXMLTag() {
		super(generateContextTypeId(TemplateContextTypeIds.TAG), ResourceHandler.getString("TemplateContextTypeXMLTag.0")); //$NON-NLS-1$
	}
}
