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
package org.eclipse.wst.dtd.core.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.dtd.core.Attribute;
import org.eclipse.wst.dtd.core.AttributeEnumList;
import org.eclipse.wst.dtd.core.AttributeList;
import org.eclipse.wst.dtd.core.DTDFile;
import org.eclipse.wst.dtd.core.DTDNode;
import org.eclipse.wst.dtd.core.Entity;
import org.eclipse.wst.dtd.core.Notation;


public class DTDNotationReferenceRemover {

	public DTDNotationReferenceRemover() {

	}

	protected Object requestor;
	protected Notation notation;
	protected String notationName;

	public void notationAboutToBeDeleted(Object requestor, Notation notation) {
		this.requestor = requestor;
		this.notation = notation;
		notationName = notation.getName();

		visit(notation.getDTDFile());
	}

	public void visit(DTDFile file) {
		List nodes = file.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			DTDNode currentNode = (DTDNode) nodes.get(i);
			if (currentNode instanceof Entity) {
				visitEntity((Entity) currentNode);
			}
			else if (currentNode instanceof AttributeList) {
				visitAttributeList((AttributeList) currentNode);
			}
		}
	}

	public void visitEntity(Entity entity) {
		if (entity.getNotationName().equals(notationName)) {
			entity.setNotationName(requestor, ""); //$NON-NLS-1$
		}
	}

	public void visitAttributeList(AttributeList attList) {
		Attribute attr = (Attribute) attList.getFirstChild();
		while (attr != null) {
			visitAttribute(attr);
			attr = (Attribute) attr.getNextSibling();
		}
	}

	public void visitAttribute(Attribute attr) {
		if (attr.getType().equals(Attribute.ENUMERATED_NOTATION)) {
			AttributeEnumList enumList = attr.getEnumList();
			List notationNames = enumList.getItems();
			Iterator iter = notationNames.iterator();
			boolean updateRequired = false;
			while (iter.hasNext()) {
				String notation = (String) iter.next();
				if (notation.equals(notationName)) {
					updateRequired = true;
					iter.remove();
				}
			}
			if (updateRequired) {
				String[] newItems = new String[notationNames.size()];
				notationNames.toArray(newItems);
				enumList.setItems(requestor, newItems);
			}
		}
	}

}// DTDNotationRemover
