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
package org.eclipse.wst.sse.core.builder.participants;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.sse.core.builder.IBuilderModelProvider;
import org.eclipse.wst.sse.core.builder.IBuilderParticipant;
import org.eclipse.wst.sse.core.text.IStructuredDocument;
import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.text.ITextRegion;
import org.eclipse.wst.sse.core.text.ITextRegionList;


/**
 * Publically usable implementor of IBuilderParticipant. Includes pre- and
 * post-build hooks and iteration through the Structured Document
 */

public class AbstractBuilderParticipant implements IBuilderParticipant {

	private IFile fCurrentFile;
	private IBuilderModelProvider fModelProvider;

	public AbstractBuilderParticipant() {
		super();
	}

	protected boolean build(IFile file, IProject project, IBuilderModelProvider provider, IProgressMonitor monitor) {
		preBuild(file, provider, monitor);
		if (getCurrentFile().isAccessible() && !monitor.isCanceled()) {
			IStructuredDocument document = provider.getDocument(getCurrentFile());
			if (document != null) {
				IStructuredDocumentRegion region = document.getFirstStructuredDocumentRegion();
				while (region != null && !monitor.isCanceled()) {
					build(region);
					region = region.getNext();
				}
			}
		}
		postBuild(file, provider, monitor);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public boolean build(IResource[] resources, IProject project, IBuilderModelProvider provider, IProgressMonitor monitor) {
		for (int i = 0; i < resources.length; i++) {
			if (!monitor.isCanceled()) {
				build((IFile) resources[i], project, provider, monitor);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public boolean build(IResourceDelta delta, IProject project, IBuilderModelProvider provider, IProgressMonitor monitor) {
		return build((IFile) delta.getResource(), project, provider, monitor);
	}

	protected void build(IStructuredDocumentRegion region) {
		doBuildFor(region);
		ITextRegionList list = region.getRegions();
		for (int i = 0; i < list.size(); i++) {
			doBuildFor(region, list.get(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public boolean cleanup(IResource[] resources, IProject project, IBuilderModelProvider provider, IProgressMonitor monitor) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public boolean cleanup(IResourceDelta delta, IProject project, IBuilderModelProvider provider, IProgressMonitor monitor) {
		return false;
	}

	protected void doBuildFor(IStructuredDocumentRegion region) {
	}

	protected void doBuildFor(IStructuredDocumentRegion region, ITextRegion textRegion) {
	}

	/**
	 * @return Returns the currentFile.
	 */
	public IFile getCurrentFile() {
		return fCurrentFile;
	}

	/**
	 * @return Returns the modelProvider.
	 */
	public IBuilderModelProvider getModelProvider() {
		return fModelProvider;
	}

	protected void postBuild(IFile file, IBuilderModelProvider provider, IProgressMonitor monitor) {
		setCurrentFile(null);
		setModelProvider(null);
	}

	protected void preBuild(IFile file, IBuilderModelProvider provider, IProgressMonitor monitor) {
		setCurrentFile(file);
		setModelProvider(provider);
	}

	/**
	 * @param currentFile
	 *            The currentFile to set.
	 */
	protected void setCurrentFile(IFile currentFile) {
		fCurrentFile = currentFile;
	}

	/**
	 * @param modelProvider
	 *            The modelProvider to set.
	 */
	protected void setModelProvider(IBuilderModelProvider modelProvider) {
		fModelProvider = modelProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public void shutdown(IProject project) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public void startup(IProject project, int kind, Map args) {
	}
}
