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



import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * NodeContainer class
 */
public abstract class NodeContainer extends NodeImpl implements Node, NodeList {

	private NodeImpl firstChild = null;
	private NodeImpl lastChild = null;
	private NodeList childNodesCache = null;

	/**
	 */
	private class ChildNodesCache implements NodeList {
		private int length = 0;
		private int curIndex = -1;
		private Node curChild = null;

		ChildNodesCache() {
			for (Node child = firstChild; child != null; child = child.getNextSibling()) {
				this.length++;
			}
		}

		public int getLength() {
			return this.length;
		}

		public Node item(int index) {
			if (this.length == 0)
				return null;
			if (index < 0)
				return null;
			if (index >= this.length)
				return null;

			if (this.curIndex < 0) { // first time
				if (index * 2 >= this.length) { // search from the last
					this.curIndex = this.length - 1;
					this.curChild = lastChild;
				}
				else { // search from the first
					this.curIndex = 0;
					this.curChild = firstChild;
				}
			}

			if (index == this.curIndex)
				return this.curChild;

			if (index > this.curIndex) {
				while (index > this.curIndex) {
					this.curIndex++;
					this.curChild = this.curChild.getNextSibling();
				}
			}
			else { // index < this.curIndex
				while (index < this.curIndex) {
					this.curIndex--;
					this.curChild = this.curChild.getPreviousSibling();
				}
			}

			return this.curChild;
		}
	}

	/**
	 * NodeContainer constructor
	 */
	protected NodeContainer() {
		super();
	}

	/**
	 * NodeContainer constructor
	 * @param that NodeContainer
	 */
	protected NodeContainer(NodeContainer that) {
		super(that);
	}

	/**
	 * appendChild method
	 * @return org.w3c.dom.Node
	 * @param newChild org.w3c.dom.Node
	 */
	public Node appendChild(Node newChild) throws DOMException {
		return insertBefore(newChild, null);
	}

	/**
	 * cloneChildNodes method
	 * @param container org.w3c.dom.Node
	 * @param deep boolean
	 */
	protected void cloneChildNodes(Node newParent, boolean deep) {
		if (newParent == null || newParent == this)
			return;
		if (!(newParent instanceof NodeContainer))
			return;

		NodeContainer container = (NodeContainer) newParent;
		container.removeChildNodes();

		for (Node child = getFirstChild(); child != null; child = child.getNextSibling()) {
			Node cloned = child.cloneNode(deep);
			if (cloned != null)
				container.appendChild(cloned);
		}
	}

	/**
	 * getChildNodes method
	 * @return org.w3c.dom.NodeList
	 */
	public NodeList getChildNodes() {
		return this;
	}

	/**
	 * getFirstChild method
	 * @return org.w3c.dom.Node
	 */
	public Node getFirstChild() {
		return this.firstChild;
	}

	/**
	 * getLastChild method
	 * @return org.w3c.dom.Node
	 */
	public Node getLastChild() {
		return this.lastChild;
	}

	/**
	 * getLength method
	 * @return int
	 */
	public int getLength() {
		if (this.firstChild == null)
			return 0;
		if (this.childNodesCache == null)
			this.childNodesCache = new ChildNodesCache();
		return this.childNodesCache.getLength();
	}

	/**
	 */
	public String getSource() {
		StringBuffer buffer = new StringBuffer();

		IStructuredDocumentRegion startStructuredDocumentRegion = getStartStructuredDocumentRegion();
		if (startStructuredDocumentRegion != null) {
			String source = startStructuredDocumentRegion.getText();
			if (source != null)
				buffer.append(source);
		}

		for (NodeImpl child = firstChild; child != null; child = (NodeImpl) child.getNextSibling()) {
			String source = child.getSource();
			if (source != null)
				buffer.append(source);
		}

		IStructuredDocumentRegion endStructuredDocumentRegion = getEndStructuredDocumentRegion();
		if (endStructuredDocumentRegion != null) {
			String source = endStructuredDocumentRegion.getText();
			if (source != null)
				buffer.append(source);
		}

		return buffer.toString();
	}

	/**
	 * hasChildNodes method
	 * @return boolean
	 */
	public boolean hasChildNodes() {
		return (this.firstChild != null);
	}

	/**
	 * insertBefore method
	 * @return org.w3c.dom.Node
	 * @param newChild org.w3c.dom.Node
	 * @param refChild org.w3c.dom.Node
	 */
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		if (newChild == null)
			return null; // nothing to do
		if (refChild != null && refChild.getParentNode() != this) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, new String());
		}
		if (!isChildEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		if (newChild == refChild)
			return newChild; // nothing to do

		if (newChild.getNodeType() == DOCUMENT_FRAGMENT_NODE) {
			// insert child nodes instead
			for (Node child = newChild.getFirstChild(); child != null; child = newChild.getFirstChild()) {
				newChild.removeChild(child);
				insertBefore(child, refChild);
			}
			return newChild;
		}

		this.childNodesCache = null; // invalidate child nodes cache

		NodeImpl child = (NodeImpl) newChild;
		NodeImpl next = (NodeImpl) refChild;
		NodeImpl prev = null;
		Node oldParent = child.getParentNode();
		if (oldParent != null)
			oldParent.removeChild(child);
		if (next == null) {
			prev = this.lastChild;
			this.lastChild = child;
		}
		else {
			prev = (NodeImpl) next.getPreviousSibling();
			next.setPreviousSibling(child);
		}
		if (prev == null)
			this.firstChild = child;
		else
			prev.setNextSibling(child);
		child.setPreviousSibling(prev);
		child.setNextSibling(next);
		child.setParentNode(this);
		// make sure having the same owner document
		if (child.getOwnerDocument() == null) {
			if (getNodeType() == DOCUMENT_NODE) {
				child.setOwnerDocument((Document) this);
			}
			else {
				child.setOwnerDocument(getOwnerDocument());
			}
		}

		notifyChildReplaced(child, null);

		return child;
	}

	/**
	 * isContainer method
	 * @return boolean
	 */
	public boolean isContainer() {
		return true;
	}

	/**
	 * item method
	 * @return org.w3c.dom.Node
	 * @param index int
	 */
	public Node item(int index) {
		if (this.firstChild == null)
			return null;
		if (this.childNodesCache == null)
			this.childNodesCache = new ChildNodesCache();
		return this.childNodesCache.item(index);
	}

	/**
	 * notifyChildReplaced method
	 * @param newChild org.w3c.dom.Node
	 * @param oldChild org.w3c.dom.Node
	 */
	protected void notifyChildReplaced(Node newChild, Node oldChild) {
		DocumentImpl document = (DocumentImpl) getContainerDocument();
		if (document == null)
			return;

		syncChildEditableState(newChild);

		XMLModelImpl model = (XMLModelImpl) document.getModel();
		if (model == null)
			return;
		model.childReplaced(this, newChild, oldChild);
	}

	protected void syncChildEditableState(Node child) {
		ReadOnlyController roc = ReadOnlyController.getInstance();
		if (fChildEditable) {
			roc.unlockNode((NodeImpl) child);
		}
		else {
			roc.lockNode((NodeImpl) child);
		}
	}

	/**
	 * removeChild method
	 * @return org.w3c.dom.Node
	 * @param oldChild org.w3c.dom.Node
	 */
	public Node removeChild(Node oldChild) throws DOMException {
		if (oldChild == null)
			return null;
		if (oldChild.getParentNode() != this) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, new String());
		}

		if (!isChildEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		this.childNodesCache = null; // invalidate child nodes cache

		NodeImpl child = (NodeImpl) oldChild;
		NodeImpl prev = (NodeImpl) child.getPreviousSibling();
		NodeImpl next = (NodeImpl) child.getNextSibling();

		child.setEditable(true, true); // clear ReadOnly flags

		if (prev == null)
			this.firstChild = next;
		else
			prev.setNextSibling(next);
		if (next == null)
			this.lastChild = prev;
		else
			next.setPreviousSibling(prev);
		child.setPreviousSibling(null);
		child.setNextSibling(null);
		child.setParentNode(null);

		notifyChildReplaced(null, child);

		return child;
	}

	/**
	 * removeChildNodes method
	 */
	public void removeChildNodes() {
		if (!isChildEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		Node nextChild = null;
		for (Node child = getFirstChild(); child != null; child = nextChild) {
			nextChild = child.getNextSibling();
			removeChild(child);
		}
	}

	/**
	 * removeChildNodes method
	 * @return org.w3c.dom.DocumentFragment
	 * @param firstChild org.w3c.dom.Node
	 * @param lastChild org.w3c.dom.Node
	 */
	public DocumentFragment removeChildNodes(Node firstChild, Node lastChild) {
		if (!hasChildNodes())
			return null;
		if (!isChildEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		Document document = null;
		if (getNodeType() == DOCUMENT_NODE)
			document = (Document) this;
		else
			document = getOwnerDocument();
		if (document == null)
			return null;
		DocumentFragment fragment = document.createDocumentFragment();
		if (fragment == null)
			return null;

		if (firstChild == null)
			firstChild = getFirstChild();
		if (lastChild == null)
			lastChild = getLastChild();
		Node nextChild = null;
		for (Node child = firstChild; child != null; child = nextChild) {
			nextChild = child.getNextSibling();
			removeChild(child);
			fragment.appendChild(child);
			if (child == lastChild)
				break;
		}

		return fragment;
	}

	/**
	 * replaceChild method
	 * @return org.w3c.dom.Node
	 * @param newChild org.w3c.dom.Node
	 * @param oldChild org.w3c.dom.Node
	 */
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		if (!isChildEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		if (oldChild == null)
			return newChild;
		if (newChild != null)
			insertBefore(newChild, oldChild);
		return removeChild(oldChild);
	}

	public boolean isChildEditable() {
		if (!fChildEditable) {
			XMLModelImpl model = (XMLModelImpl) getModel();
			if (model != null && model.isReparsing()) {
				return true;
			}
		}
		return fChildEditable;
	}

	public void setChildEditable(boolean editable) {
		if (fChildEditable == editable) {
			return;
		}

		ReadOnlyController roc = ReadOnlyController.getInstance();
		Node node;
		if (editable) {
			for (node = getFirstChild(); node != null; node = node.getNextSibling()) {
				roc.unlockNode((XMLNode) node);
			}
		}
		else {
			for (node = getFirstChild(); node != null; node = node.getNextSibling()) {
				roc.lockNode((XMLNode) node);
			}
		}

		fChildEditable = editable;
		notifyEditableChanged();
	}

	private boolean fChildEditable = true;
}
