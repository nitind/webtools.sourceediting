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
package org.eclipse.wst.sse.ui.views.properties;

import org.eclipse.jface.action.Action;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImageHelper;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImages;
import org.eclipse.wst.sse.ui.nls.ResourceHandler;


public class RemoveAction extends Action {
	private ConfigurablePropertySheetPage fPage;

	public RemoveAction(ConfigurablePropertySheetPage page) {
		super();
		fPage = page;
		setText(getText());
		setToolTipText(getText());
		setImageDescriptor(EditorPluginImageHelper.getInstance().getImageDescriptor(EditorPluginImages.IMG_ELCL_DELETE));
		setDisabledImageDescriptor(EditorPluginImageHelper.getInstance().getImageDescriptor(EditorPluginImages.IMG_DLCL_DELETE));
	}

	/**
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	public String getText() {
		return ResourceHandler.getString("RemoveAction.0"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		fPage.remove();
	}
}
