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
package org.eclipse.wst.xml.ui.ui.actions;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.xml.ui.ui.XMLCommonResources;
import org.eclipse.wst.xml.ui.ui.dialogs.EditProcessingInstructionDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;



/**
 * EditProcessingInstructionAction
 */
public class EditProcessingInstructionAction extends NodeAction {
	protected AbstractNodeActionManager manager;
	protected Node parent;
	protected Node childRef;
	protected ProcessingInstruction pi;
	protected String title;

	/**
	 *  This constructor is used to add a new ProcessingInstruction
	 */
	public EditProcessingInstructionAction(AbstractNodeActionManager manager, Node parent, Node childRef, String actionLabel, String title) {
		setText(actionLabel);
		this.manager = manager;
		this.parent = parent;
		this.childRef = childRef;
		this.title = title;
	}

	/**
	 *  This constructor is used to edit a ProcessingInstruction
	 */
	public EditProcessingInstructionAction(AbstractNodeActionManager manager, ProcessingInstruction pi, String actionLabel, String title) {
		setText(actionLabel);
		this.manager = manager;
		this.pi = pi;
		this.parent = pi.getParentNode();
		this.title = title;
	}

	public String getUndoDescription() {
		return title;
	}

	public void run() {
		manager.beginNodeAction(this);
		Shell shell = XMLCommonResources.getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();

		EditProcessingInstructionDialog dialog = null;
		if (pi != null) {
			dialog = new EditProcessingInstructionDialog(shell, pi);
		}
		else {
			dialog = new EditProcessingInstructionDialog(shell, XMLCommonResources.getInstance().getString("_UI_PI_TARGET_VALUE"), XMLCommonResources.getInstance().getString("_UI_PI_DATA_VALUE")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		dialog.create();
		dialog.getShell().setText(title);
		dialog.setBlockOnOpen(true);
		dialog.open();

		if (dialog.getReturnCode() == Window.OK) {
			if (pi != null) {
				childRef = pi;
			}

			Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document) parent : parent.getOwnerDocument();
			Node newNode = document.createProcessingInstruction(dialog.getTarget(), dialog.getData());
			parent.insertBefore(newNode, childRef);

			if (pi != null) {
				parent.removeChild(pi);
			}

			manager.reformat(newNode, false);
			manager.setViewerSelection(newNode);
		}
		manager.endNodeAction(this);
	}
}
