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
package org.eclipse.wst.sse.ui.internal.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.ui.Logger;
import org.eclipse.wst.sse.ui.nls.ResourceHandler;


/**
 * ManageBreakpointAction - Enables and Disables
 */
public class ManageBreakpointAction extends BreakpointRulerAction {

	protected IBreakpoint[] breakpoints = null;
	private boolean doEnable = true;

	/**
	 * @param editor
	 * @param rulerInfo
	 */
	public ManageBreakpointAction(ITextEditor editor, IVerticalRuler rulerInfo) {
		super(editor, rulerInfo);
		setEnabled(true);
	}

	/**
	 *  
	 */
	protected void disableBreakpoints(IBreakpoint[] breakpoints) {
		for (int i = 0; i < breakpoints.length; i++) {
			try {
				breakpoints[i].setEnabled(false);
			}
			catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void enableBreakpoints(IBreakpoint[] breakpoints) {
		for (int i = 0; i < breakpoints.length; i++) {
			try {
				breakpoints[i].setEnabled(true);
			}
			catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run() {
		if (doEnable)
			enableBreakpoints(breakpoints);
		else
			disableBreakpoints(breakpoints);
	}

	public void update() {
		// doEnable means "enable" instead of "disable"
		doEnable = true;
		breakpoints = getBreakpoints(getMarkers());
		for (int i = 0; doEnable && i < breakpoints.length; i++) {
			IBreakpoint breakpoint = breakpoints[i];
			try {
				if (breakpoint.isEnabled()) {
					doEnable = false;
				}
			}
			catch (CoreException e) {
				Logger.logException("breakpoint not responding to isEnabled: " + breakpoint, e); //$NON-NLS-1$
			}
		}
		setEnabled(breakpoints != null && breakpoints.length > 0);
		if (doEnable)
			setText(ResourceHandler.getString("ManageBreakpointAction.0")); //$NON-NLS-1$
		else
			setText(ResourceHandler.getString("ManageBreakpointAction.1")); //$NON-NLS-1$
	}

}
