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
package org.eclipse.wst.dtd.ui.views.contentoutline.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.dtd.core.CMGroupNode;
import org.eclipse.wst.dtd.core.DTDNode;
import org.eclipse.wst.dtd.core.Element;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class AddElementToContentModelAction extends BaseAction {

	public AddElementToContentModelAction(StructuredTextEditor editor, String label) {
		super(editor, label);
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean rc = super.updateSelection(selection);
		DTDNode node = getFirstNodeSelected(selection);
		if (node instanceof CMGroupNode) {
			setEnabled(true);
		}
		else {
			setEnabled(false);
		}
		return rc;
	}

	public void run() {
		DTDNode node = getFirstNodeSelected();

		if (node instanceof CMGroupNode) {
			((CMGroupNode) node).addChild();
		}
		else if (node instanceof Element) {
			((Element) node).addChild();
		}
	}
}
