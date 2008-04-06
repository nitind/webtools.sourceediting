/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.xsl.core.internal.text.rules;

import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredTextPartitioner;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.core.internal.text.rules.StructuredTextPartitionerForXML;
import org.eclipse.wst.xml.core.text.IXMLPartitions;
import org.eclipse.wst.xsl.core.internal.text.IXSLPartitions;

public class StructuredTextPartitionerForXSL extends StructuredTextPartitionerForXML implements IStructuredTextPartitioner {

	private final static String[] configuredContentTypes = new String[]{IXMLPartitions.XML_DEFAULT, IXMLPartitions.XML_CDATA, IXMLPartitions.XML_PI, IXMLPartitions.XML_DECLARATION, IXMLPartitions.XML_COMMENT, IXMLPartitions.DTD_SUBSET, IXSLPartitions.XSL_XPATH};

	public StructuredTextPartitionerForXSL() {
		super();
	}

	@Override
	public String getPartitionType(ITextRegion region, int offset) {
		String result = null;
		if (region.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {
			// Check to see what namespace the region falls under
			// If the namespace
			
			result = IXSLPartitions.XSL_XPATH;
		} else {
			result = super.getPartitionType(region, offset);
		}
		return result;
	}
	
	public static String[] getConfiguredContentTypes() {
		return configuredContentTypes;
	}
	
	@Override
	public IDocumentPartitioner newInstance() {
		StructuredTextPartitionerForXML instance = new StructuredTextPartitionerForXML();
		return instance;
	}
}
