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
package org.eclipse.wst.sse.ui.util;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;


/**
 * Utility to display (and/or clear) messages on the status line.
 * @author pavery
 */
public class PlatformStatusLineUtil {
	
	private class ClearErrorMessage implements Runnable {
		public void run() {
			displayMessage(null);
		}
	}
	/**
	 * Used to clear message on focus loss, change of selection, key type, etc...
	 */
	private class OneTimeListener extends FocusAdapter implements VerifyKeyListener, SelectionListener, MouseListener {

		private Runnable fRunner = null;
		private StyledText fStyledText;

		public OneTimeListener(StyledText target, Runnable r) {
			fStyledText = target;
			fRunner = r;
			fStyledText.addVerifyKeyListener(this);
			fStyledText.addFocusListener(this);
			fStyledText.addSelectionListener(this);
			fStyledText.addMouseListener(this);
		}

		public void focusLost(FocusEvent e) {
			unhookAndRun();
		}
		public void mouseDoubleClick(MouseEvent e) {
			unhookAndRun();
		}
		public void mouseDown(MouseEvent e) {
			unhookAndRun();
		}
		public void mouseUp(MouseEvent e) {
			//
		}

		private void unhookAndRun() {
			fStyledText.removeVerifyKeyListener(this);
			fStyledText.removeFocusListener(this);
			fStyledText.removeSelectionListener(this);
			fStyledText.removeMouseListener(this);
			fStyledText.getDisplay().asyncExec(fRunner);
		}

		public void verifyKey(VerifyEvent event) {
			unhookAndRun();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			unhookAndRun();
		}

		public void widgetSelected(SelectionEvent e) {
			unhookAndRun();
		}
	}
	// end OneTimeListener
	
	private static PlatformStatusLineUtil singleton = null;
	
	private PlatformStatusLineUtil() {
		// force use of singleton
	}
	
	private static PlatformStatusLineUtil getInstance() {
		if(singleton == null)
			singleton = new PlatformStatusLineUtil();
		return singleton;
	}

	private static void addOneTimeClearListener(StructuredTextEditor editor) {
		if(editor != null) {
			ITextViewer viewer = editor.getTextViewer();
			if(viewer != null)
				addOneTimeClearListener(viewer.getTextWidget());
		}
	}
	private static void addOneTimeClearListener(StyledText widget) {
		getInstance().new OneTimeListener(widget, getInstance().new ClearErrorMessage());
	}
	private static IEditorPart getActiveEditor() {
		
		IEditorPart editor = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(window == null) {
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
			if(windows.length > 0)
				window = windows[0];
		}
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null)
				editor = page.getActiveEditor();
		}
		return editor;
	}
	
	/**
	 * Status line will be cleared w/ key type, or selection change
	 * @param widget
	 */
	public static void addOneTimeClearListener() {
		IEditorPart editor = getActiveEditor();
		if(editor != null && editor instanceof StructuredTextEditor)
			addOneTimeClearListener((StructuredTextEditor)editor);
	}

	/**
	 * Clears the status line immediately
	 */
	public static void clearStatusLine() {
		displayMessage(null);
	}
	
	/**
	 * Display a message on the status line (no beep)
	 * @param msg
	 */
	public static void displayMessage(String msg) {
		
		IEditorPart editor = getActiveEditor();
		if (editor != null) 
			editor.getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(msg);
	
	}
	/**
	 * Display a message on the status line (with a beep)
	 * @param msg
	 */
	public static void displayErrorMessage(String msg) {
		
		displayMessage(msg);
		PlatformUI.getWorkbench().getDisplay().beep();
	}
}
