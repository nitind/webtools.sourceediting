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
import org.eclipse.wst.sse.core.text.ITextRegion;
import org.eclipse.wst.xml.core.document.InvalidCharacterException;
import org.eclipse.wst.xml.core.document.XMLGenerator;
import org.eclipse.wst.xml.core.document.XMLModel;
import org.eclipse.wst.xml.core.document.XMLText;
import org.eclipse.wst.xml.core.parser.XMLRegionContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 * TextImpl class
 */
public class TextImpl extends CharacterDataImpl implements XMLText {

	private String source = null;

	/**
	 */
	private class StringPair {
		private String first = null;
		private String second = null;

		StringPair(String first, String second) {
			this.first = first;
			this.second = second;
		}

		String getFirst() {
			return this.first;
		}

		String getSecond() {
			return this.second;
		}
	}

	/**
	 * TextImpl constructor
	 */
	protected TextImpl() {
		super();
	}

	/**
	 * TextImpl constructor
	 * @param that TextImpl
	 */
	protected TextImpl(TextImpl that) {
		super(that);

		if (that != null) {
			this.source = that.getSource();
		}
	}

	/**
	 * appendData method
	 * @param arg java.lang.String
	 */
	public void appendData(String arg) throws DOMException {
		if (arg == null || arg.length() == 0)
			return;

		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		String newSource = getSource(arg);
		if (newSource == null)
			return;
		String source = getSource();
		if (source != null)
			setTextSource(source + newSource);
		else
			setTextSource(newSource);
	}

	/**
	 */
	IStructuredDocumentRegion appendStructuredDocumentRegion(IStructuredDocumentRegion newStructuredDocumentRegion) {
		if (newStructuredDocumentRegion == null)
			return null;

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null) {
			setStructuredDocumentRegion(newStructuredDocumentRegion);
			return newStructuredDocumentRegion;
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			container.appendStructuredDocumentRegion(newStructuredDocumentRegion);
		}
		else {
			StructuredDocumentRegionContainer container = new StructuredDocumentRegionContainer();
			container.appendStructuredDocumentRegion(flatNode);
			container.appendStructuredDocumentRegion(newStructuredDocumentRegion);
			setStructuredDocumentRegion(container);
		}

		return newStructuredDocumentRegion;
	}

	/**
	 * appendText method
	 * @param text org.w3c.dom.Text
	 */
	public void appendText(Text newText) {
		if (newText == null)
			return;

		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		TextImpl text = (TextImpl) newText;
		String newSource = text.getSource();
		if (newSource == null && newSource.length() == 0)
			return;
		String source = getSource();
		if (source != null)
			setTextSource(source + newSource);
		else
			setTextSource(newSource);
	}

	/**
	 * cloneNode method
	 * @return org.w3c.dom.Node
	 * @param deep boolean
	 */
	public Node cloneNode(boolean deep) {
		TextImpl cloned = new TextImpl(this);
		return cloned;
	}

	/**
	 * deleteData method
	 * @param offset int
	 * @param count int
	 */
	public void deleteData(int offset, int count) throws DOMException {
		if (count == 0)
			return;
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		if (count < 0 || offset < 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		String source = getSource();
		if (source == null || source.length() == 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		StringPair pair = substringSourceExcluded(source, offset, count);
		if (pair == null)
			return;
		source = null;
		String first = pair.getFirst();
		if (first != null)
			source = first;
		String second = pair.getSecond();
		if (second != null) {
			if (source != null)
				source += second;
			else
				source = second;
		}
		if (source == null)
			source = new String(); // delete all
		setTextSource(source);
	}

	/**
	 * getData method
	 * @return java.lang.String
	 */
	public String getData() throws DOMException {
		if (this.source != null)
			return getData(this.source);
		String data = super.getData();
		if (data != null)
			return data;
		return getData(getStructuredDocumentRegion());
	}

	/**
	 */
	private String getData(IStructuredDocumentRegion flatNode) {
		if (flatNode == null)
			return new String();

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int length = container.getLength();
			if (length < 16)
				length = 16; // default
			StringBuffer buffer = new StringBuffer(length);
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				String data = getData(content);
				if (data == null)
					continue;
				buffer.append(data);
			}
			return buffer.toString();
		}

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			return flatNode.getText();
		}

		ITextRegion region = StructuredDocumentRegionUtil.getFirstRegion(flatNode);
		if (region != null) {
			String regionType = region.getType();
			if (regionType == XMLRegionContext.XML_ENTITY_REFERENCE || regionType == XMLRegionContext.XML_CHAR_REFERENCE) {
				String name = StructuredDocumentRegionUtil.getEntityRefName(flatNode, region);
				if (name != null) {
					DocumentImpl document = (DocumentImpl) getOwnerDocument();
					if (document != null) {
						String value = document.getCharValue(name);
						if (value != null)
							return value;
					}
				}
			}
		}

		return flatNode.getText();
	}

	/**
	 * Returns data for the source
	 */
	private String getData(String source) {
		if (source == null)
			return null;
		StringBuffer buffer = null;
		int offset = 0;
		int length = source.length();
		int ref = source.indexOf('&');
		while (ref >= 0) {
			int end = source.indexOf(';', ref + 1);
			if (end > ref + 1) {
				String name = source.substring(ref + 1, end);
				String value = getCharValue(name);
				if (value != null) {
					if (buffer == null)
						buffer = new StringBuffer(length);
					if (ref > offset)
						buffer.append(source.substring(offset, ref));
					buffer.append(value);
					offset = end + 1;
					ref = end;
				}
			}
			ref = source.indexOf('&', ref + 1);
		}
		if (buffer == null)
			return source;
		if (length > offset)
			buffer.append(source.substring(offset));
		return buffer.toString();
	}

	/**
	 * getFirstStructuredDocumentRegion method
	 */
	public IStructuredDocumentRegion getFirstStructuredDocumentRegion() {
		return StructuredDocumentRegionUtil.getFirstStructuredDocumentRegion(getStructuredDocumentRegion());
	}

	/**
	 * getLastStructuredDocumentRegion method
	 */
	public IStructuredDocumentRegion getLastStructuredDocumentRegion() {
		return StructuredDocumentRegionUtil.getLastStructuredDocumentRegion(getStructuredDocumentRegion());
	}

	/**
	 * getNodeName method
	 * @return java.lang.String
	 */
	public String getNodeName() {
		return "#text";//$NON-NLS-1$
	}

	/**
	 * getNodeType method
	 * @return short
	 */
	public short getNodeType() {
		return TEXT_NODE;
	}

	/**
	 */
	public String getSource() {
		if (this.source != null)
			return this.source;
		String data = super.getData();
		if (data != null && data.length() > 0) {
			String source = getSource(data);
			if (source != null)
				return source;
		}
		return super.getSource();
	}

	/**
	 * Returns source for the data
	 */
	private String getSource(String data) {
		if (data == null)
			return null;
		XMLModel model = getModel();
		if (model == null)
			return null; // error
		XMLGenerator generator = model.getGenerator();
		if (generator == null)
			return null; // error
		return generator.generateTextData(this, data);
	}

	/**
	 */
	String getTextSource() {
		return this.source;
	}

	/**
	 */
	public String getValueSource() {
		return getSource();
	}

	/**
	 */
	boolean hasStructuredDocumentRegion(IStructuredDocumentRegion askedStructuredDocumentRegion) {
		if (askedStructuredDocumentRegion == null)
			return false;

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null)
			return false;

		if (flatNode == askedStructuredDocumentRegion)
			return true;

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) flatNode;
			if (proxy.getStructuredDocumentRegion() == askedStructuredDocumentRegion)
				return true;
			return false;
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (content == null)
					continue;
				if (content == askedStructuredDocumentRegion)
					return true;
				if (content instanceof StructuredDocumentRegionProxy) {
					StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) content;
					if (proxy.getStructuredDocumentRegion() == askedStructuredDocumentRegion)
						return true;
				}
			}
			return false;
		}

		return false;
	}

	/**
	 * insertData method
	 * @param offset int
	 * @param arg java.lang.String
	 */
	public void insertData(int offset, String arg) throws DOMException {
		if (arg == null || arg.length() == 0)
			return;
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		if (offset < 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		String source = getSource();
		if (source == null || source.length() == 0) {
			if (offset > 0) {
				throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
			}
			source = getSource(arg);
			if (source != null)
				setTextSource(source);
			return;
		}

		StringPair pair = substringSourceExcluded(source, offset, 0);
		if (pair == null)
			return; // error
		StringBuffer buffer = new StringBuffer(source.length() + arg.length());
		String first = pair.getFirst();
		if (first != null)
			buffer.append(first);
		source = getSource(arg);
		if (source != null)
			buffer.append(source);
		String second = pair.getSecond();
		if (second != null)
			buffer.append(second);
		setTextSource(buffer.toString());
	}

	/**
	 */
	IStructuredDocumentRegion insertStructuredDocumentRegion(IStructuredDocumentRegion newStructuredDocumentRegion, IStructuredDocumentRegion nextStructuredDocumentRegion) {
		if (newStructuredDocumentRegion == null)
			return null;
		if (nextStructuredDocumentRegion == null)
			return appendStructuredDocumentRegion(newStructuredDocumentRegion);

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null)
			return null; // error

		if (flatNode == nextStructuredDocumentRegion) {
			StructuredDocumentRegionContainer container = new StructuredDocumentRegionContainer();
			container.appendStructuredDocumentRegion(newStructuredDocumentRegion);
			container.appendStructuredDocumentRegion(flatNode);
			setStructuredDocumentRegion(container);
			return newStructuredDocumentRegion;
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (content == nextStructuredDocumentRegion) {
					container.insertStructuredDocumentRegion(newStructuredDocumentRegion, i);
					return newStructuredDocumentRegion;
				}
			}
			return null; // error
		}

		return null; // error
	}

	/**
	 * insertText method
	 * @param text org.w3c.dom.Text
	 * @param offset int
	 */
	public void insertText(Text newText, int offset) throws DOMException {
		if (newText == null)
			return;
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		TextImpl text = (TextImpl) newText;
		String newSource = text.getSource();
		if (newSource == null && newSource.length() == 0)
			return;
		if (offset < 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		String source = getSource();
		if (source == null || source.length() == 0) {
			if (offset > 0) {
				throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
			}
			setTextSource(newSource);
			return;
		}

		StringPair pair = substringSourceExcluded(source, offset, 0);
		if (pair == null)
			return; // error
		StringBuffer buffer = new StringBuffer(source.length() + newSource.length());
		String first = pair.getFirst();
		if (first != null)
			buffer.append(first);
		buffer.append(newSource);
		String second = pair.getSecond();
		if (second != null)
			buffer.append(second);
		setTextSource(buffer.toString());
	}

	/**
	 * isCDATAContent method
	 * @return boolean
	 */
	public boolean isCDATAContent() {
		Node parent = getParentNode();
		if (parent == null || parent.getNodeType() != Node.ELEMENT_NODE)
			return false;
		ElementImpl element = (ElementImpl) parent;
		return element.isCDATAContainer();
	}

	/**
	 */
	public boolean isInvalid() {
		return isInvalid(getStructuredDocumentRegion());
	}

	/**
	 */
	private boolean isInvalid(IStructuredDocumentRegion flatNode) {
		if (flatNode == null)
			return false;

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (isInvalid(content))
					return true;
			}
			return false;
		}

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) flatNode;
			return isInvalid(proxy.getStructuredDocumentRegion());
		}

		String regionType = StructuredDocumentRegionUtil.getFirstRegionType(flatNode);
		if (regionType != XMLRegionContext.XML_CONTENT && regionType != JSP_CONTENT && regionType != XMLRegionContext.XML_ENTITY_REFERENCE && regionType != XMLRegionContext.XML_CHAR_REFERENCE && regionType != XMLRegionContext.BLOCK_TEXT && regionType != XMLRegionContext.WHITE_SPACE) {
			return true;
		}

		return false;
	}

	/**
	 */
	boolean isSharingStructuredDocumentRegion(IStructuredDocumentRegion sharedStructuredDocumentRegion) {
		if (sharedStructuredDocumentRegion == null)
			return false;

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null)
			return false;

		if (flatNode == sharedStructuredDocumentRegion)
			return false;

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) flatNode;
			if (proxy.getStructuredDocumentRegion() == sharedStructuredDocumentRegion)
				return true;
			return false;
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (content == null)
					continue;
				if (content == sharedStructuredDocumentRegion)
					return false;
				if (content instanceof StructuredDocumentRegionProxy) {
					StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) content;
					if (proxy.getStructuredDocumentRegion() == sharedStructuredDocumentRegion)
						return true;
				}
			}
			return false;
		}

		return false;
	}

	/**
	 */
	public boolean isWhitespace() {
		String data = getData();
		if (data == null)
			return true;
		int length = data.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(data.charAt(i)))
				return false;
		}
		return true;
	}

	/**
	 */
	IStructuredDocumentRegion removeStructuredDocumentRegion(IStructuredDocumentRegion oldStructuredDocumentRegion) {
		if (oldStructuredDocumentRegion == null)
			return null;

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null)
			return null; // error

		if (flatNode == oldStructuredDocumentRegion) {
			setStructuredDocumentRegion(null);
			return oldStructuredDocumentRegion;
		}

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) flatNode;
			if (proxy.getStructuredDocumentRegion() == oldStructuredDocumentRegion) {
				// removed with proxy
				setStructuredDocumentRegion(null);
				return oldStructuredDocumentRegion;
			}
			return null; // error
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (content == oldStructuredDocumentRegion) {
					container.removeStructuredDocumentRegion(i);
					if (container.getStructuredDocumentRegionCount() == 1) {
						// get back to single IStructuredDocumentRegion
						setStructuredDocumentRegion(container.getStructuredDocumentRegion(0));
					}
					return oldStructuredDocumentRegion;
				}

				if (content instanceof StructuredDocumentRegionProxy) {
					StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) content;
					if (proxy.getStructuredDocumentRegion() == oldStructuredDocumentRegion) {
						// removed with proxy
						container.removeStructuredDocumentRegion(i);
						if (container.getStructuredDocumentRegionCount() == 1) {
							// get back to single IStructuredDocumentRegion
							setStructuredDocumentRegion(container.getStructuredDocumentRegion(0));
						}
						return oldStructuredDocumentRegion;
					}
				}
			}
			return null; // error
		}

		return null; // error
	}

	/**
	 * replaceData method
	 * @param offset int
	 * @param count int
	 * @param arg java.lang.String
	 */
	public void replaceData(int offset, int count, String arg) throws DOMException {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		if (arg == null || arg.length() == 0) {
			deleteData(offset, count);
			return;
		}
		if (count == 0) {
			insertData(offset, arg);
			return;
		}
		if (offset < 0 || count < 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		String source = getSource();
		if (source == null || source.length() == 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		StringPair pair = substringSourceExcluded(source, offset, count);
		if (pair == null)
			return; // error
		StringBuffer buffer = new StringBuffer(source.length() + arg.length());
		String first = pair.getFirst();
		if (first != null)
			buffer.append(first);
		source = getSource(arg);
		if (source != null)
			buffer.append(source);
		String second = pair.getSecond();
		if (second != null)
			buffer.append(second);
		setTextSource(buffer.toString());
	}

	/**
	 */
	IStructuredDocumentRegion replaceStructuredDocumentRegion(IStructuredDocumentRegion newStructuredDocumentRegion, IStructuredDocumentRegion oldStructuredDocumentRegion) {
		if (oldStructuredDocumentRegion == null)
			return null;
		if (newStructuredDocumentRegion == null)
			return removeStructuredDocumentRegion(oldStructuredDocumentRegion);

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null)
			return null; // error

		if (flatNode == oldStructuredDocumentRegion) {
			setStructuredDocumentRegion(newStructuredDocumentRegion);
			return oldStructuredDocumentRegion;
		}

		if (flatNode instanceof StructuredDocumentRegionProxy) {
			StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) flatNode;
			if (proxy.getStructuredDocumentRegion() == oldStructuredDocumentRegion) {
				if (newStructuredDocumentRegion instanceof StructuredDocumentRegionProxy) {
					// proxy must not be nested
					setStructuredDocumentRegion(newStructuredDocumentRegion);
				}
				else {
					proxy.setStructuredDocumentRegion(newStructuredDocumentRegion);
				}
				return oldStructuredDocumentRegion;
			}
			return null; // error
		}

		if (flatNode instanceof StructuredDocumentRegionContainer) {
			StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
			int count = container.getStructuredDocumentRegionCount();
			for (int i = 0; i < count; i++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(i);
				if (content == null)
					continue; // error
				if (content == oldStructuredDocumentRegion) {
					container.replaceStructuredDocumentRegion(newStructuredDocumentRegion, i);
					return oldStructuredDocumentRegion;
				}

				if (content instanceof StructuredDocumentRegionProxy) {
					StructuredDocumentRegionProxy proxy = (StructuredDocumentRegionProxy) content;
					if (proxy.getStructuredDocumentRegion() == oldStructuredDocumentRegion) {
						if (newStructuredDocumentRegion instanceof StructuredDocumentRegionProxy) {
							// proxy must not be nested
							container.replaceStructuredDocumentRegion(newStructuredDocumentRegion, i);
						}
						else {
							proxy.setStructuredDocumentRegion(newStructuredDocumentRegion);
						}
						return oldStructuredDocumentRegion;
					}
				}
			}
			return null; // error
		}

		return null; // error
	}

	/**
	 */
	void resetStructuredDocumentRegions() {
		String source = getSource();
		if (source != null && source.length() > 0)
			this.source = source;
		super.resetStructuredDocumentRegions();
	}

	/**
	 * getData method
	 * @return java.lang.String
	 */
	public void setData(String data) throws DOMException {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		this.source = null;
		super.setData(data);
	}

	/**
	 */
	void setStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		super.setStructuredDocumentRegion(flatNode);
		if (flatNode != null)
			this.source = null;
	}

	/**
	 */
	public void setSource(String source) throws InvalidCharacterException {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		SourceValidator validator = new SourceValidator(this);
		if (validator.validateSource(source))
			setTextSource(source);
	}

	/**
	 */
	public void setTextSource(String source) {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		this.source = source;

		notifyValueChanged();
	}

	/**
	 */
	public void setValueSource(String source) {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}

		SourceValidator validator = new SourceValidator(this);
		setTextSource(validator.convertSource(source));
	}

	/**
	 * splitText method
	 * @return org.w3c.dom.Text
	 * @param offset int
	 */
	public Text splitText(int offset) throws DOMException {
		if (!isDataEditable()) {
			throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, new String());
		}
		if (offset < 0) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		int length = getLength();
		if (offset > length) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		Document document = getOwnerDocument();
		if (document == null)
			return null;

		String source = null;
		if (offset < length) {
			int count = length - offset;
			source = substringSource(offset, count);
			deleteData(offset, count);
		}
		TextImpl text = (TextImpl) document.createTextNode(null);
		if (source != null)
			text.setTextSource(source);

		Node parent = getParentNode();
		if (parent != null)
			parent.insertBefore(text, getNextSibling());

		return text;
	}

	/**
	 */
	Text splitText(IStructuredDocumentRegion nextStructuredDocumentRegion) {
		if (nextStructuredDocumentRegion == null)
			return null;

		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode == null || !(flatNode instanceof StructuredDocumentRegionContainer))
			return null; // error

		StructuredDocumentRegionContainer container = (StructuredDocumentRegionContainer) flatNode;
		int count = container.getStructuredDocumentRegionCount();
		int index = 0;
		for (; index < count; index++) {
			if (container.getStructuredDocumentRegion(index) == nextStructuredDocumentRegion)
				break;
		}
		if (index >= count) {
			// this is the case nextStructuredDocumentRegion is a new IStructuredDocumentRegion
			// search gap by offset
			int offset = nextStructuredDocumentRegion.getStart();
			for (index = 0; index < count; index++) {
				IStructuredDocumentRegion content = container.getStructuredDocumentRegion(index);
				if (content == null)
					continue; // error
				if (content.getStart() >= offset)
					break;
			}
			if (index >= count)
				return null; // error
		}
		if (index == 0)
			return this; // nothing to do

		Document document = getOwnerDocument();
		if (document == null)
			return null; // error
		Node parent = getParentNode();
		if (parent == null)
			return null; // error
		TextImpl nextText = (TextImpl) document.createTextNode(null);
		if (nextText == null)
			return null; // error

		for (; index < count; count--) {
			nextText.appendStructuredDocumentRegion(container.removeStructuredDocumentRegion(index));
		}

		// normalize IStructuredDocumentRegion
		if (index == 1) {
			setStructuredDocumentRegion(container.getStructuredDocumentRegion(0));
		}

		parent.insertBefore(nextText, getNextSibling());
		return nextText;
	}

	/**
	 * Retruns data for the range
	 */
	private String substringData(String data, int offset, int count) throws DOMException {
		// sure offset and count are non-negative
		if (count == 0)
			return new String();
		if (data == null) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		int length = data.length();
		if (offset > length) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		int end = offset + count;
		if (end > length) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}
		return data.substring(offset, end);
	}

	/**
	 * Returns source for the range specified by:
	 * offset: data offset
	 * count: data count
	 */
	private String substringSource(int offset, int count) throws DOMException {
		// sure offset and count are non-negative
		if (this.source != null)
			return substringSource(this.source, offset, count);

		String data = super.getData();
		if (data != null && data.length() > 0) {
			data = substringData(data, offset, count);
			if (data == null)
				return new String();
			String source = getSource(data);
			if (source != null)
				return source;
		}

		return substringSource(getSource(), offset, count);
	}

	/**
	 * Returns source for the range specified by:
	 * offset: data offset
	 * count: data count
	 */
	private String substringSource(String source, int offset, int count) throws DOMException {
		// sure offset and count are non-negative
		if (count == 0)
			return new String();
		if (source == null) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		int length = source.length();
		int end = offset + count;

		// find character reference
		int ref = source.indexOf('&');
		while (ref >= 0) {
			if (ref >= end)
				break;
			int refEnd = source.indexOf(';', ref + 1);
			if (refEnd > ref + 1) {
				String name = source.substring(ref + 1, refEnd);
				if (getCharValue(name) != null) {
					// found, shift for source offsets
					int refCount = refEnd - ref;
					if (ref < offset)
						offset += refCount;
					if (ref < end)
						end += refCount;
					ref = refEnd;
				}
			}
			ref = source.indexOf('&', ref + 1);
		}

		if (offset > length || end > length) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		return source.substring(offset, end);
	}

	/**
	 * Returns sources before and after the range specified by:
	 * offset: data offset
	 * count: data count
	 */
	private StringPair substringSourceExcluded(String source, int offset, int count) throws DOMException {
		// sure offset and count are non-negative
		if (source == null) {
			if (offset == 0 && count == 0)
				return new StringPair(null, null);
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		int length = source.length();
		int end = offset + count;

		// find character reference
		int ref = source.indexOf('&');
		while (ref >= 0) {
			if (ref >= end)
				break;
			int refEnd = source.indexOf(';', ref + 1);
			if (refEnd > ref + 1) {
				String name = source.substring(ref + 1, refEnd);
				if (getCharValue(name) != null) {
					// found, shift for source offsets
					int refCount = refEnd - ref;
					if (ref < offset)
						offset += refCount;
					if (ref < end)
						end += refCount;
					ref = refEnd;
				}
			}
			ref = source.indexOf('&', ref + 1);
		}

		if (offset > length || end > length) {
			throw new DOMException(DOMException.INDEX_SIZE_ERR, new String());
		}

		String first = (offset > 0 ? source.substring(0, offset) : null);
		String second = (end < length ? source.substring(end, length) : null);
		return new StringPair(first, second);
	}
}
