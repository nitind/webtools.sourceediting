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
package org.eclipse.wst.sse.core;



/**
 * This interface defines two new model level events. In order to avoid 
 * unnecessary last minute changes from clients, this interface was created 
 * as a "stop gap" measure. Eventually, it will become part of the normal 
 * ModelStateListener interface.
 */
public interface IModelStateListenerExtended extends IModelStateListener {


	void modelAboutToBeReinitialized(IStructuredModel structuredModel);

	void modelReinitialized(IStructuredModel structuredModel);

}
