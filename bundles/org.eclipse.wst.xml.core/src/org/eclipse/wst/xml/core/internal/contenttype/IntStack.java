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
package org.eclipse.wst.xml.core.internal.contenttype;

/*
 * 
 * A non-resizable class implementing the behavior of java.util.Stack, but
 * directly for the <code> integer </code> primitive.
 */
import java.util.EmptyStackException;

public class IntStack {

	private int size = 0;
	private int[] list = null;

	public IntStack() {
		this(100);
	}

	public IntStack(int maxdepth) {
		super();
		list = new int[maxdepth];
		initialize();
	}

	public boolean empty() {
		return size == 0;
	}

	public int get(int slot) {
		return list[slot];
	}

	private void initialize() {
		for (int i = 0; i < list.length; i++)
			list[i] = -1;
	}

	/**
	 * Returns the int at the top of the stack without removing it
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int peek() {
		if (size == 0)
			throw new EmptyStackException();
		return list[size - 1];
	}

	/**
	 * Removes and returns the int at the top of the stack
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int pop() {
		int value = peek();
		list[size - 1] = -1;
		size--;
		return value;
	}

	/**
	 * Pushes an item onto the top of this stack.
	 * 
	 * @param newValue -
	 *            the int to be pushed onto this stack.
	 * @return the <code>newValue</code> argument.
	 */
	public int push(int newValue) {
		if (size == list.length) {
			throw new StackOverflowError();
		}
		list[size++] = newValue;
		return newValue;
	}

	public int size() {
		return list.length;
	}

	public void clear() {
		initialize();
	}
}
