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
package org.eclipse.wst.sse.core.document;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.jface.text.IDocument;

/**
 * A java.io.Reader that can operate off of an IDocument.
 */
public class DocumentReader extends Reader {
	private IDocument fDocument = null;
	private int position = 0;
	private int mark = 0;

	public DocumentReader() {
		super();
	}

	public DocumentReader(IDocument document) {
		this(document, 0);
	}

	public DocumentReader(IDocument document, int initialPosition) {
		super();
		fDocument = document;
		position = initialPosition;
	}

	public void close() throws IOException {
		// do nothing
	}

	/**
	 * @return
	 */
	public IDocument getDocument() {
		return fDocument;
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		char[] readChars = null;
		try {
			if (position >= fDocument.getLength())
				return -1;
			// the IDocument is likely using a GapTextStore, so we can't
			// retrieve a char[] directly
			if (position + len > fDocument.getLength())
				readChars = fDocument.get(position, fDocument.getLength() - position).toCharArray();
			else
				readChars = fDocument.get(position, len).toCharArray();
			System.arraycopy(readChars, 0, cbuf, off, readChars.length);
			//				System.out.println("" + position + ":" + readChars.length + " "
			// + StringUtils.escape(new String(readChars)));
			position += readChars.length;
			return readChars.length;
		}
		catch (Exception e) {
			throw new IOException("Exception while reading from IDocument: " + e); //$NON-NLS-1$
		}
	}

	public void reset(IDocument document, int initialPosition) {
		fDocument = document;
		position = initialPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Reader#skip(long)
	 */
	public long skip(long n) throws IOException {
		long skipped = n;
		if (position + n > fDocument.getLength()) {
			skipped = fDocument.getLength() - position;
			position = fDocument.getLength();
		}
		else {
			position += n;
		}
		return skipped;
	}

	public boolean markSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#mark(int)
	 */
	public void mark(int readAheadLimit) throws IOException {
		mark = position;
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#reset()
	 */
	public void reset() throws IOException {
		position = mark;
	}
	/* (non-Javadoc)
	 * @see java.io.Reader#reset()
	 */
	public void reset(int pos) throws IOException {
		position = pos;
	}
}
