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
package org.eclipse.wst.dtd.ui.views.contentoutline;

import java.text.Collator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.wst.dtd.ui.DTDEditorPlugin;
import org.eclipse.wst.dtd.ui.internal.editor.DTDEditorPluginImageHelper;
import org.eclipse.wst.dtd.ui.internal.editor.DTDEditorPluginImages;
import org.eclipse.wst.sse.ui.views.contentoutline.PropertyChangeUpdateAction;


/**
 */
class SortAction extends PropertyChangeUpdateAction {
	private TreeViewer treeViewer;

	public SortAction(TreeViewer viewer, IPreferenceStore store, String preferenceKey) {
		super(DTDEditorPlugin.getResourceString("_UI_BUTTON_SORT_ITEMS"), store, preferenceKey, false); //$NON-NLS-1$
		setImageDescriptor(DTDEditorPluginImageHelper.getInstance().getImageDescriptor(DTDEditorPluginImages.IMG_OBJ_SORT));
		setToolTipText(getText());
		treeViewer = viewer;
		if (isChecked()) {
			treeViewer.setSorter(new ViewerSorter(Collator.getInstance()));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IUpdate#update()
	 */
	public void update() {
		super.update();
		treeViewer.getControl().setVisible(false);
		Object[] expandedElements = treeViewer.getExpandedElements();
		if (isChecked()) {
			treeViewer.setSorter(new ViewerSorter(Collator.getInstance()));
		}
		else {
			treeViewer.setSorter(null);
		}
		treeViewer.setInput(treeViewer.getInput());
		treeViewer.setExpandedElements(expandedElements);
		treeViewer.getControl().setVisible(true);
	}
}
