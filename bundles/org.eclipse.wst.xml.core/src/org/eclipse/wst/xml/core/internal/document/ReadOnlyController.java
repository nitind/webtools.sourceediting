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
package org.eclipse.wst.xml.core.internal.document;



import org.eclipse.wst.sse.core.text.IStructuredDocument;
import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.text.ITextRegion;
import org.eclipse.wst.sse.core.text.ITextRegionList;
import org.eclipse.wst.xml.core.document.XMLElement;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.eclipse.wst.xml.core.document.XMLText;
import org.eclipse.wst.xml.core.parser.XMLRegionContext;
import org.w3c.dom.Node;


class ReadOnlyController {
	static synchronized ReadOnlyController getInstance() {
		if (fInstance == null) {
			fInstance = new ReadOnlyController();
		}
		return fInstance;
	}

	class Span {
		int offset;
		int length;

		Span(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}
	}

	/**
	 * This method is used from parent's setChildEditable()
	 *
	 * case 1:
	 * <parent><node attr="value"/><node2></parent>
	 *        x######x          x##x
	 * case 2:
	 * <parent><node attr="value"><child></child></node></parent>
	 *        x######x          x#?             ?#######x
	 *
	 * (? : editable if node.isEditable() == true)
	 */
	void lockNode(XMLNode node) {
		if (node == null) {
			return;
		}
		if (!node.isDataEditable()) {
			lockBoth(node);
			return;
		}

		IStructuredDocumentRegion flatNode;
		boolean canInsert = false;

		// end node (element)
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			flatNode = node.getEndStructuredDocumentRegion();
			if (flatNode != null) {
				canInsert = node.isChildEditable();
				lock(flatNode, canInsert, false);
			}
		}
		// start node
		flatNode = node.getStartStructuredDocumentRegion();
		if (flatNode != null) {
			Span span = getDataSpan(node);
			if (0 <= span.length) {
				IStructuredDocument structuredDocument = flatNode.getParentDocument();
				int offset, length;
				offset = flatNode.getStart();
				length = span.offset;
				lock(structuredDocument, offset, length, false, false);
				offset = offset + span.offset + span.length;
				length = flatNode.getEnd() - offset;
				lock(structuredDocument, offset, length, canInsert, false);
			}
			else {
				lock(flatNode, false, canInsert);
			}
		}
	}

	void unlockNode(XMLNode node) {
		if (node == null) {
			return;
		}

		IStructuredDocumentRegion flatNode;
		// end node
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			flatNode = node.getEndStructuredDocumentRegion();
			if (flatNode != null) {
				unlock(flatNode);
			}
		}

		// start node
		flatNode = node.getStartStructuredDocumentRegion();
		if (flatNode != null) {
			if (node.isDataEditable()) {
				unlock(flatNode);
			}
			else {
				Span span = getDataSpan(node);
				if (span.length <= 0) {
					unlock(flatNode);
				}
				else {
					IStructuredDocument structuredDocument = flatNode.getParentDocument();
					int offset, length;
					offset = flatNode.getStart();
					length = span.offset - offset;
					unlock(structuredDocument, offset, length);
					offset = span.offset + span.length;
					length = flatNode.getEnd() - span.offset;
					unlock(structuredDocument, offset, length);
				}
			}
		}
	}

	void lockData(XMLNode node) {
		if (node == null) {
			return;
		}

		Span span = getDataSpan(node);
		if (0 <= span.length) {
			lock(node.getModel().getStructuredDocument(), node.getStartOffset() + span.offset, span.length, false, false);
		}
	}

	void unlockData(XMLNode node) {
		if (node == null) {
			return;
		}

		Span span = getDataSpan(node);
		if (0 <= span.length) {
			unlock(node.getModel().getStructuredDocument(), span.offset, span.length);
		}
	}

	/**
	 * This method is used from parent's setChildEditable()
	 *
	 * case 1:
	 * <parent><node attr="value"/><node2></parent>
	 *        x####################x
	 * case 2:
	 * <parent><node attr="value"><child></child></node></parent>
	 *        x###################?             ?#######x
	 *
	 * (? : editable if node.isEditable() == true)
	 */
	void lockBoth(XMLNode node) {
		if (node == null) {
			return;
		}

		IStructuredDocumentRegion flatNode;
		boolean canInsert = false;

		// end node (element)
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			flatNode = node.getEndStructuredDocumentRegion();
			if (flatNode != null) {
				canInsert = node.isChildEditable();
				lock(flatNode, canInsert, false);
			}
		}
		// start node
		flatNode = node.getStartStructuredDocumentRegion();
		if (flatNode != null) {
			lock(flatNode, false, canInsert);
		}
	}

	void unlockBoth(XMLNode node) {
		if (node == null) {
			return;
		}

		IStructuredDocumentRegion flatNode;
		// start node
		flatNode = node.getStartStructuredDocumentRegion();
		if (flatNode != null) {
			unlock(flatNode);
		}
		// end node
		flatNode = node.getEndStructuredDocumentRegion();
		if (flatNode != null) {
			unlock(flatNode);
		}
	}

	/**
	 * lock itself and all descendants
	 */
	void lockDeep(XMLNode node) {
		if (node == null) {
			return;
		}

		int offset = node.getStartOffset();
		int length = node.getEndOffset() - offset;

		boolean canInsert = true;
		XMLNode parent = (XMLNode) node.getParentNode();
		if (parent != null && !parent.isChildEditable()) {
			canInsert = false;
		}
		lock(node.getStructuredDocument(), offset, length, canInsert, canInsert);
	}

	void unlockDeep(XMLNode node) {
		if (node == null) {
			return;
		}

		int offset = node.getStartOffset();
		int length = node.getEndOffset() - offset;

		unlock(node.getStructuredDocument(), offset, length);
	}

	static private void lock(IStructuredDocumentRegion node, boolean canInsertBefore, boolean canInsertAfter) {
		if (node == null) {
			return;
		}
		IStructuredDocument doc = node.getParentDocument();
		if (doc == null) {
			return;
		}
		doc.makeReadOnly(node.getStart(), node.getLength());
	}

	static private void lock(IStructuredDocument doc, int offset, int length, boolean canInsertBefore, boolean canInsertAfter) {
		if (doc == null) {
			return;
		}
		doc.makeReadOnly(offset, length);
	}

	static private void unlock(IStructuredDocumentRegion node) {
		if (node == null) {
			return;
		}
		IStructuredDocument doc = node.getParentDocument();
		if (doc == null) {
			return;
		}
		doc.clearReadOnly(node.getStart(), node.getLength());
	}

	private void unlock(IStructuredDocument doc, int offset, int length) {
		if (doc == null) {
			return;
		}
		doc.clearReadOnly(offset, length);
	}

	private Span getDataSpan(XMLNode node) {
		switch (node.getNodeType()) {
			case Node.ELEMENT_NODE :
				return getDataSpanForElement((XMLElement) node);
			case Node.TEXT_NODE :
				return getDataSpanForText((XMLText) node);
			default :
				return new Span(0, -1);
		}
	}

	private Span getDataSpanForElement(XMLElement node) {
		IStructuredDocumentRegion docRegion = node.getStartStructuredDocumentRegion();
		if (docRegion == null) {
			return new Span(0, -1);
		}

		ITextRegionList regions = docRegion.getRegions();
		if (regions == null) {
			return new Span(0, -1);
		}

		String startType;
		String endType;
		if (node.isCommentTag()) {
			startType = XMLRegionContext.XML_COMMENT_OPEN;
			endType = XMLRegionContext.XML_COMMENT_CLOSE;
		}
		else {
			startType = XMLRegionContext.XML_TAG_NAME;
			endType = XMLRegionContext.XML_TAG_CLOSE;
		}

		int startOffset = -1;
		int endOffset = -1;
		ITextRegion prevRegion = null;
		ITextRegion region;
		for (int i = 0; i < regions.size(); i++) {
			region = regions.get(i);
			String type = region.getType();
			if (type == startType) {
				startOffset = region.getEnd();
			}
			else if (type == endType && prevRegion != null) {
				endOffset = prevRegion.getTextEnd();
			}
			prevRegion = region;
		}

		if (0 <= startOffset && 0 <= endOffset) {
			return new Span(startOffset, endOffset - startOffset);
		}
		else {
			return new Span(0, -1);
		}
	}

	private Span getDataSpanForText(XMLText node) {
		IStructuredDocumentRegion docRegion = ((NodeImpl) node).getStructuredDocumentRegion();
		if (docRegion == null) {
			return new Span(0, -1);
		}
		return new Span(0, docRegion.getLength());
	}

	private ReadOnlyController() {
		super();
	}

	private static ReadOnlyController fInstance;
}
