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
package org.eclipse.wst.xml.ui.reconcile;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.ui.internal.reconcile.AbstractStructuredTextReconcilingStrategy;

/**
 * 
 * @author pavery
 *
 */
public class StructuredTextReconcilingStrategyForMarkup extends AbstractStructuredTextReconcilingStrategy {

	public StructuredTextReconcilingStrategyForMarkup(ITextEditor editor) {
		super(editor);
	}

	public void createReconcileSteps() {
		// only one step, to check syntax
		fFirstStep = new ReconcileStepForMarkup();
	}
}
