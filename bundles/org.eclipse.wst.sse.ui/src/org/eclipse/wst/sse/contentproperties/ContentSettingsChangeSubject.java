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
package org.eclipse.wst.sse.contentproperties;



public class ContentSettingsChangeSubject extends AbstractSubject {


	private static INotify notify;
	private static ISubject subject;

	protected static INotify getNotify() {
		if (notify == null && subject == null) {
			synchronized (ContentSettingsChangeSubject.class) {
				if (notify == null && subject == null) {
					notify = new ContentSettingsChangeSubject();
					subject = (ISubject) notify;
				}
			}
		}

		return notify;
	}

	public static ISubject getSubject() {
		if (subject == null && notify == null) {
			synchronized (ContentSettingsChangeSubject.class) {
				if (subject == null && notify == null) {
					subject = new ContentSettingsChangeSubject();
					notify = (INotify) subject;
				}
			}
		}
		return subject;
	}

	public synchronized void notifyListeners(org.eclipse.core.resources.IResource changedResource) {
		super.notifyListeners(changedResource);
	}

	public synchronized void addListener(IContentSettingsListener listener) {
		super.addListener(listener);
	}

	public synchronized void removeListener(IContentSettingsListener listener) {
		super.removeListener(listener);
	}
}
