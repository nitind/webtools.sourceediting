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
package org.eclipse.wst.sse.core.undo;



public interface CommandCursorPosition {

	/**
	 * Returns the cursor position to be set to after this command is redone.
	 * @return int
	 */
	int getRedoCursorPosition();

	/**
	 * Returns the length of text to be selected after this command is redone.
	 * @return int
	 */
	int getRedoSelectionLength();

	/**
	 * Returns the cursor position to be set to after this command is undone.
	 * @return int
	 */
	int getUndoCursorPosition();

	/**
	 * Returns the length of text to be selected after this command is undone.
	 * @return int
	 */
	int getUndoSelectionLength();

	/**
	 * Sets the cursor position to be used after this command is redone.
	 */
	void setRedoCursorPosition(int cursorPosition);

	/**
	 * Sets the length of text to be selected after this command is redone.
	 */
	void setRedoSelectionLength(int selectionLength);

	/**
	 * Sets the cursor position to be used after this command is undone.
	 */
	void setUndoCursorPosition(int cursorPosition);

	/**
	 * Sets the length of text to be selected after this command is undone.
	 */
	void setUndoSelectionLength(int selectionLength);
}
