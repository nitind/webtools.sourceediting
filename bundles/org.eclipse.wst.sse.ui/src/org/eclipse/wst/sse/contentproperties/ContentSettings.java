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



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ContentSettings implements IContentSettings {

	private static final IContentSettings singleton = new ContentSettings();

	private static SimpleNodeOperator domOperator;
	private static final String contentSettingsName = ".contentsettings";//$NON-NLS-1$
	private String contentSettingsPath;

	private static IProject preProject;
	private IProject currProject;



	private final String rootElementName = "contentsettings";//$NON-NLS-1$
	private final String projectElementName = "project";//$NON-NLS-1$
	private final String fileElementName = "file";//$NON-NLS-1$
	private final String PATHATTR = "path"; //$NON-NLS-1$



	private ContentSettings() {
		currProject = null;
		contentSettingsPath = null;
	}

	public final String getPathAttr() {
		return PATHATTR;
	}

	public static final String getContentSettingsName() {
		return contentSettingsName;
	}

	public synchronized static IContentSettings getInstance() {
		return singleton;
	}


	private String getProjectOf(IResource resource) {
		//if (resource==null) return null;
		currProject = resource.getProject();
		//String projectPath=null;

		IPath path = currProject.getLocation();
		if (path == null) {
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			if (path != null) {
				path = path.addTrailingSeparator();
				path = path.append(currProject.getName());
			}
		}
		return (path != null) ? path.toString() : null;

	}

	private String getRelativePathFromProject(IResource resource) {
		if (resource == null)
			return null;

		IPath path = resource.getProjectRelativePath();
		if (path == null)
			return null; // if resource is project or workspace root
		String resourcePath = path.toString();

		return resourcePath;

	}


	private boolean existsContentSettings() {
		if (contentSettingsPath == null)
			return false;

		IResource file = currProject.getFile(contentSettingsName);
		if (file == null)
			return false;
		if (file.isAccessible())
			return true;
		else
			return false;

	}


	private void createDOMTree() throws SimpleNodeOperator.ReadContentSettingsFailureException {
		if (domOperator == null || (currProject != null && (!currProject.equals(preProject)) && contentSettingsPath != null))
			domOperator = new SimpleNodeOperator(contentSettingsPath);


	}

	/*
	 * private void createNewDOMTree() throws
	 * SimpleNodeOperator.CreateContentSettingsFailureException{ // create New
	 * document when no file exists. DOMImplementation impl =
	 * DOMImplementationImpl.getDOMImplementation(); Document document =
	 * impl.createDocument(null,rootElementName,null); domOperator = new
	 * SimpleNodeOperator(document); }
	 */
	private void createNewDOMTree() throws SimpleNodeOperator.CreateContentSettingsFailureException {
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e) {
			Logger.logException("exception creating document", e); //$NON-NLS-1$
		}
		catch (FactoryConfigurationError e) {
			Logger.logException("exception creating document", e); //$NON-NLS-1$
		}
		document.appendChild(document.createElement(rootElementName));
		domOperator = new SimpleNodeOperator(document);
	}

	private void writeDOMDocument() throws SimpleNodeOperator.WriteContentSettingsFailureException {
		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			domOperator.writeDocument(outputStream);
			outputStream.flush();
			outputStream.close();

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

			IFile outputFile = currProject.getFile(contentSettingsName);
			if (outputFile.exists())
				outputFile.setContents(inputStream, true, true, null);
			else
				outputFile.create(inputStream, true, null);

			inputStream.close();
		}
		catch (CoreException e) {
			Logger.logException(e);
			throw new SimpleNodeOperator.WriteContentSettingsFailureException("invalid outputFile in writeDOMDocument()");//$NON-NLS-1$
		}
		catch (IOException e) {
			Logger.logException(e);
			throw new SimpleNodeOperator.WriteContentSettingsFailureException("invalid outputStream or inputStream in writeDOMDocument()");//$NON-NLS-1$
		}
	}

	private String getContentSettingsPath(IResource resource) {
		String projectPath = getProjectOf(resource);
		StringBuffer strbuf = new StringBuffer(""); //$NON-NLS-1$
		if (projectPath != null) {
			strbuf.append(projectPath);
			strbuf.append(IPath.SEPARATOR);
			strbuf.append(contentSettingsName);
		}
		else
			return null;

		String str = strbuf.toString();
		strbuf.delete(0, strbuf.length());
		return str;
	}

	public synchronized String getProperty(final IResource resource, final String propertyName) {
		if (resource == null)
			return null;

		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return null;

		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return null;

		if (!existsContentSettings()) {
			return null; // when .contentsettings.xml is NOT exist.
		}


		try {
			createDOMTree();
		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e1) {
			Logger.logException(e1);
			// create DOM tree for new XML Document
			try {
				createNewDOMTree();
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.CreateContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return null;
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return null;
			}

		}


		Element e = null;
		if (resource.getType() == IResource.PROJECT)
			// select project element and get attribute
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
		else if (resource.getType() == IResource.FILE)
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));


		if (e != null) {
			String result = e.getAttribute(propertyName);
			preProject = currProject;
			return result;
		}
		else {
			preProject = currProject;
			return null;// when project or file element is NOT exist.
		}
	}


	private void _setProperty(final IResource resource, final String propertyName, final String propertyValue) {
		if (resource == null || propertyName == null)
			return;
		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return;

		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return;

		try {

			if (!existsContentSettings()) {
				// create DOM tree for new XML Document
				createNewDOMTree();
			}
			else {
				// create DOM tree from existing contentsettings.
				createDOMTree();
			}

		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e) {
			Logger.logException(e);
			try {
				createNewDOMTree();
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.CreateContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}

		}
		catch (SimpleNodeOperator.CreateContentSettingsFailureException e) {
			Logger.logException(e);
			preProject = currProject;
			return;
		}

		Element e = null;
		if (resource.getType() == IResource.PROJECT) {
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
			if (e == null) {
				// create project Element and add it into tree
				e = (Element) domOperator.addElementUnderRoot(this.projectElementName);
			}
		}
		else if (resource.getType() == IResource.FILE) {
			// check exists file Element
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));
			if (e == null) {
				// create file Element and add path into it.
				e = (Element) domOperator.addElementUnderRoot(this.fileElementName);
				domOperator.addAttributeAt(e, getPathAttr(), getRelativePathFromProject(resource));
			}
		}

		// check exists propertyName attribute

		Map attrList = domOperator.getAttributesOf(e);
		if (attrList == null || attrList.isEmpty() || (String) attrList.get(propertyName) == null)
			// create propertyName attribute and add
			domOperator.addAttributeAt(e, propertyName, propertyValue);
		else
			// set attribute value
			domOperator.updateAttributeAt(e, propertyName, propertyValue);


		// write dom tree into .contentsettings
		try {
			writeDOMDocument();
		}
		catch (SimpleNodeOperator.WriteContentSettingsFailureException ex) {
			Logger.logException(ex);
			preProject = currProject;
			return;
		}

		preProject = currProject;


	}

	public synchronized void setProperty(final IResource resource, final String propertyName, final String propertyValue) {
		// deny to set "path" attribute value.
		if (this.getPathAttr().equals(propertyName))
			return;
		this._setProperty(resource, propertyName, propertyValue);
	}

	public synchronized void deleteProperty(final IResource resource, final String propertyName) {
		if (resource == null)
			return;
		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return;
		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return;

		if (!existsContentSettings()) {
			return; // when .contentsettings.xml is NOT exist.
		}
		try {
			createDOMTree();
		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e) {
			Logger.logException(e);
			try {
				createNewDOMTree();
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.CreateContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}
		}

		Element e = null;
		if (resource.getType() == IResource.PROJECT)
			// select project element and get attribute
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
		else if (resource.getType() == IResource.FILE)
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));


		if (e != null) {
			domOperator.removeAttributeAt(e, propertyName);
			// write dom tree into .contentsettings
			try {
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException ex) {
				Logger.logException(ex);
				preProject = currProject;
				return;
			}
		}

		preProject = currProject;

	}

	public synchronized void releaseCache() {
		domOperator = null;
	}


	/**
	 * 
	 */
	public synchronized void deleteAllProperties(final IResource deletedFile) {
		if (deletedFile == null)
			return;
		//	if (deletedFile.exists()) return;
		if ((deletedFile).getType() != IResource.FILE && (deletedFile).getType() != IResource.PROJECT)
			return;


		contentSettingsPath = getContentSettingsPath(deletedFile);//getProjectOf((IResource)deletedFile)
		// +
		// IPath.SEPARATOR
		// +
		// contentSettingsName;
		if (contentSettingsPath == null)
			return;
		if (!existsContentSettings())
			return;

		try {
			createDOMTree();
		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e) {
			Logger.logException(e);
			return;
		}

		Element e = null;
		if (deletedFile.getType() == IResource.PROJECT)
			// select project element and get attribute
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
		else if (deletedFile.getType() == IResource.FILE)
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(deletedFile));
		if (e == null) {
			preProject = currProject;
			return;
		}


		// when deletedFile entry exists.
		if (deletedFile.getType() == IResource.PROJECT)
			domOperator.removeElementWith(this.projectElementName);
		else if (deletedFile.getType() == IResource.FILE)
			domOperator.removeElementWith(getPathAttr(), getRelativePathFromProject(deletedFile));


		// write dom tree into .contentsettings
		try {
			writeDOMDocument();
		}
		catch (SimpleNodeOperator.WriteContentSettingsFailureException ex) {
			Logger.logException(ex);
			preProject = currProject;
			return;
		}

		preProject = currProject;


	}


	private void _setProperties(final IResource resource, final Map properties) {
		if (resource == null || properties == null || properties.isEmpty())
			return;
		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return;

		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return;

		try {

			if (!existsContentSettings()) {
				// create DOM tree for new XML Document
				createNewDOMTree();
			}
			else {
				// create DOM tree from existing contentsettings.
				createDOMTree();
			}

		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e) {
			Logger.logException(e);
			try {
				createNewDOMTree();
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.CreateContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return;
			}

		}
		catch (SimpleNodeOperator.CreateContentSettingsFailureException e) {
			Logger.logException(e);
			preProject = currProject;
			return;
		}

		Element e = null;
		if (resource.getType() == IResource.PROJECT) {
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
			if (e == null) {
				// create project Element and add it into tree
				e = (Element) domOperator.addElementUnderRoot(this.projectElementName);
			}
		}
		else if (resource.getType() == IResource.FILE) {
			// check exists file Element
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));
			if (e == null) {
				// create file Element and add path into it.
				e = (Element) domOperator.addElementUnderRoot(this.fileElementName);
				domOperator.addAttributeAt(e, getPathAttr(), getRelativePathFromProject(resource));
			}
		}

		// check exists propertyName attribute
		Map attrList = domOperator.getAttributesOf(e);
		boolean hasAttr = true;
		if (attrList == null || attrList.isEmpty())
			hasAttr = false;
		Set keys = properties.keySet();
		Iterator ii = keys.iterator();
		while (ii.hasNext()) {
			String propertyName = (String) ii.next();
			String propertyValue = (String) properties.get(propertyName);


			if (!hasAttr || (String) attrList.get(propertyName) == null)
				// create propertyName attribute and add
				domOperator.addAttributeAt(e, propertyName, propertyValue);
			else
				// set attribute value
				domOperator.updateAttributeAt(e, propertyName, propertyValue);
		}

		// write dom tree into .contentsettings
		try {
			writeDOMDocument();
		}
		catch (SimpleNodeOperator.WriteContentSettingsFailureException ex) {
			Logger.logException(ex);
			preProject = currProject;
			return;
		}

		preProject = currProject;


	}

	public synchronized void setProperties(final IResource resource, final Map properties) {
		// deny to set "path" attribute value.
		Set keys = properties.keySet();
		Iterator ii = keys.iterator();
		while (ii.hasNext()) {
			if (this.getPathAttr().equals(ii.next()))
				return;
		}
		this._setProperties(resource, properties);
	}


	public synchronized Map getProperties(final IResource resource) {
		if (resource == null)
			return null;

		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return null;

		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return null;

		if (!existsContentSettings()) {
			return null; // when .contentsettings.xml is NOT exist.
		}


		try {
			createDOMTree();
		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e1) {
			Logger.logException(e1);
			// create DOM tree for new XML Document
			try {
				createNewDOMTree();
				writeDOMDocument();
			}
			catch (SimpleNodeOperator.CreateContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return null;
			}
			catch (SimpleNodeOperator.WriteContentSettingsFailureException e2) {
				Logger.logException(e2);
				preProject = currProject;
				return null;
			}

		}


		Element e = null;
		if (resource.getType() == IResource.PROJECT)
			// select project element and get attribute
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
		else if (resource.getType() == IResource.FILE)
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));


		if (e != null) {

			Map properties = domOperator.getAttributesOf(e);
			preProject = currProject;
			if (properties == null)
				return null;
			if (properties.isEmpty())
				return null;
			properties.remove(getPathAttr());
			return properties;
		}
		else {
			preProject = currProject;
			return null;// when project or file element is NOT exist.
		}
	}

	public boolean existsProperties(IResource resource) {
		if (resource == null)
			return false;

		if (!(resource.getType() == IResource.PROJECT || resource.getType() == IResource.FILE))
			return false;

		contentSettingsPath = getContentSettingsPath(resource);
		if (contentSettingsPath == null)
			return false;

		if (!existsContentSettings())
			return false; // when .contentsettings.xml is NOT exist.

		try {
			createDOMTree();
		}
		catch (SimpleNodeOperator.ReadContentSettingsFailureException e1) {
			return false;
		}

		Element e = null;
		if (resource.getType() == IResource.PROJECT)
			// select project element and get attribute
			e = (Element) domOperator.getElementWithNodeName(this.projectElementName);
		else if (resource.getType() == IResource.FILE)
			e = (Element) domOperator.getElementWithAttribute(getPathAttr(), getRelativePathFromProject(resource));

		preProject = currProject;
		if (e == null)
			return false;

		Map properties = domOperator.getAttributesOf(e);
		if (properties == null)
			return false;
		properties.remove(getPathAttr());// if IFile,removed
		if (properties.isEmpty())
			return false;
		return true;

	}

}
