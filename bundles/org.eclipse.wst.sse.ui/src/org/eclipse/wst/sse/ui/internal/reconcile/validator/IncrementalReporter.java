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
package org.eclipse.wst.sse.ui.internal.reconcile.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.IMessage;
import org.eclipse.wst.validation.IMessageAccess;
import org.eclipse.wst.validation.IReporter;
import org.eclipse.wst.validation.IValidator;

/**
 * Right now we'll only use one reporter per validator.
 */
public class IncrementalReporter implements IReporter {
	private HashMap messages = new HashMap();
	private IProgressMonitor fProgressMonitor;

	public IncrementalReporter(IProgressMonitor progressMonitor) {
		super();
		fProgressMonitor = progressMonitor;
	}

	public void addMessage(IValidator validator, IMessage message) {
		Object existingValue = messages.get(validator);
		if (existingValue != null) {
			((List) existingValue).add(message);
		}
		else {
			List newValue = new ArrayList(1);
			newValue.add(message);
			messages.put(validator, newValue);
		}
	}

	public void displaySubtask(IValidator validator, IMessage message) {
		if ((message == null) || (message.equals(""))) { //$NON-NLS-1$
			return;
		}
		if (fProgressMonitor != null) {
			fProgressMonitor.subTask(message.getText(validator.getClass().getClassLoader()));
		}
	}

	public IMessageAccess getMessageAccess() {
		// we may want to use this eventually
		return null;
	}

	public HashMap getMessages() {
		return messages;
	}

	public boolean isCancelled() {
		if (fProgressMonitor == null)
			return false;
		return fProgressMonitor.isCanceled();
	}

	public void removeAllMessages(IValidator validator) {
		messages.clear();
	}

	public void removeAllMessages(IValidator validator, Object object) {
		removeAllMessages(validator);
	}

	// group names are unsupported
	public void removeMessageSubset(IValidator validator, Object obj, String groupName) {
		removeAllMessages(validator);
	}
}
