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
package org.eclipse.wst.sse.ui.extension;



import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * IExtendedMarkupEditor
 */
public interface IExtendedMarkupEditor extends IExtendedSimpleEditor {


	public Node getCaretNode();

	public List getSelectedNodes();

	public Document getDOMDocument();
}
