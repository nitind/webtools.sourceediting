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
package org.eclipse.wst.dtd.core;

import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;

// base class for an Element's contentmodel
public abstract class CMNode extends DTDNode {

	public CMNode(DTDFile file, IStructuredDocumentRegion flatNode) {
		super(file, flatNode);
	}

	public static final String EMPTY = DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_EMPTY"); //$NON-NLS-1$
	public static final String ANY = DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_ANY"); //$NON-NLS-1$
	public static final String PCDATA = DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_PCDATA"); //$NON-NLS-1$
	public static final String MIXED = DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_MIX_CONTENT"); //$NON-NLS-1$
	public static final String CHILDREN = DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_CHILD_CONTENT"); //$NON-NLS-1$


	boolean rootElementContent;

	/**
	 * Get the value of rootElementContent.  This tells us whether this
	 * element content's parent is a direct decendent of the containing element
	 * @return value of rootElementContent.
	 */
	public boolean isRootElementContent() {
		return rootElementContent;
	}

	// this is only valid to ask if the content is a root element content
	abstract public String getType();

	/**
	 * Set the value of rootElementContent.
	 * @param v  Value to assign to rootElementContent.
	 */
	public void setRootElementContent(boolean v) {
		this.rootElementContent = v;
	}

	// if this is a root element, change the content to mixed
	// ie . (#PCDATA, child1)
	public void setMixedContent() {
		if (isRootElementContent()) {
			if (!getType().equals(MIXED)) {
				beginRecording(this, DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_SET_MIX_CONTENT")); //$NON-NLS-1$
				if (this instanceof CMBasicNode) {
					replaceText(this, getStartOffset(), getNodeLength(), "(#PCDATA | newChild)*"); //$NON-NLS-1$
				}
				else {
					// now must convert from children content to this one.  must
					// preserve the children
					CMGroupNode group = (CMGroupNode) this;
					group.setConnector(CMGroupNode.CHOICE);
					group.setOccurrence(CMRepeatableNode.ZERO_OR_MORE);
					CMNode firstChild = (CMNode) group.getFirstChild();
					if (!firstChild.getType().equals(PCDATA)) {
						group.insertChildNode("#PCDATA", 0); //$NON-NLS-1$
					}
				}
				endRecording(this);
			}
		}
	}

	// if this is a root element, change the content to children
	// ie . (child1)
	public void setChildrenContent(String newChild) {
		if (isRootElementContent()) {
			if (!newChild.equals("")) { //$NON-NLS-1$
				beginRecording(this, DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_SET_CHILD_CONTENT")); //$NON-NLS-1$
				replaceText(this, getStartOffset(), getNodeLength(), "(" + newChild + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				endRecording(this);
				return;
			}

			if (!getType().equals(CHILDREN)) {
				beginRecording(this, DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_SET_CHILD_CONTENT")); //$NON-NLS-1$
				if (this instanceof CMBasicNode) {
					replaceText(this, getStartOffset(), getNodeLength(), "(newChild)"); //$NON-NLS-1$
				}
				else {
					// now must convert from mixed content to this one.  must
					// preserve the remaining children
					CMGroupNode group = (CMGroupNode) this;
					CMNode firstChild = (CMNode) group.getFirstChild();
					if (firstChild.getType().equals(PCDATA)) {
						group.delete(firstChild);
					}
				}

				endRecording(this);
			}
		}
	}

	public void setContent(String content) {
		if (isRootElementContent()) {
			beginRecording(this, DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_SET") + " " + content + " " + DTDPlugin.getDTDString("_UI_LABEL_CM_NODE_CONTENT")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			replaceText(this, getStartOffset(), getNodeLength(), content);
			endRecording(this);
		}
	}

	//    public void delete()
	//    {
	//      if (isRootElementContent()) 
	//      {
	//        // then the superclasses delete will be fine
	//        super.delete();
	//        return;
	//      }

	//      CMGroupNode parent = (CMGroupNode) getParentNode();
	//      parent.removeChildNode(this);
	//    }


}
