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
/* The following code was generated by JFlex 1.2.2 on 4/6/04 11:13 PM */
package org.eclipse.wst.xml.core.internal.contenttype;

import java.io.IOException;
import java.io.Reader;



/**
 * This class is a scanner generated by 
 * <a href="http://www.informatik.tu-muenchen.de/~kleing/jflex/">JFlex</a> 1.2.2
 * on 4/6/04 11:13 PM from the specification file
 * <tt>file:/D:/DevTimeSupport/HeadParsers/XMLHeadTokenizer/XMLHeadTokenizer.jflex</tt>
 */
public class XMLHeadTokenizer {

	/** this character denotes the end of file */
	final public static int YYEOF = -1;

	/** lexical states */
	final public static int YYINITIAL = 0;
	final public static int UnDelimitedString = 10;
	final public static int DQ_STRING = 6;
	final public static int SQ_STRING = 8;
	final public static int ST_XMLDecl = 2;
	final public static int QuotedAttributeValue = 4;

	/**
	 * YY_LEXSTATE[l] is the state in the DFA for the lexical state l
	 * YY_LEXSTATE[l+1] is the state in the DFA for the lexical state l
	 *                  at the beginning of a line
	 * l is of the form l = 2*k, k a non negative integer
	 */
	private final static int YY_LEXSTATE[] = {0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6};

	/** 
	 * Translates characters to character classes
	 */
	final private static String yycmap_packed = "\11\0\1\6\1\7\2\0\1\11\22\0\1\6\1\0\1\27\2\0" + "\1\31\1\0\1\30\24\0\1\12\1\10\1\26\1\13\3\0\1\21" + "\1\23\1\17\1\0\1\25\1\0\1\24\2\0\1\16\1\15\1\20" + "\1\22\10\0\1\14\12\0\1\21\1\23\1\17\1\0\1\25\1\0" + "\1\24\2\0\1\16\1\15\1\20\1\22\10\0\1\14\102\0\1\4" + "\3\0\1\5\17\0\1\3\16\0\1\1\20\0\1\3\16\0\1\1" + "\1\2\170\0\1\2\ufe87\0";

	/** 
	 * Translates characters to character classes
	 */
	final private static char[] yycmap = yy_unpack_cmap(yycmap_packed);


	/* error codes */
	final private static int YY_UNKNOWN_ERROR = 0;
	final private static int YY_ILLEGAL_STATE = 1;
	final private static int YY_NO_MATCH = 2;
	final private static int YY_PUSHBACK_2BIG = 3;

	/* error messages for the codes above */
	final private static String YY_ERROR_MSG[] = {"Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"};

	/** the input device */
	private java.io.Reader yy_reader;

	/** the current state of the DFA */
	private int yy_state;

	/** the current lexical state */
	private int yy_lexical_state = YYINITIAL;

	/** this buffer contains the current text to be matched and is
	 the source of the yytext() string */
	private char yy_buffer[] = new char[16384];

	/** the textposition at the last accepting state */
	private int yy_markedPos;

	/** the textposition at the last state to be included in yytext */
	private int yy_pushbackPos;

	/** the current text position in the buffer */
	private int yy_currentPos;

	/** startRead marks the beginning of the yytext() string in the buffer */
	private int yy_startRead;

	/** endRead marks the last character in the buffer, that has been read
	 from input */
	private int yy_endRead;

	/** number of newlines encountered up to the start of the matched text */
	private int yyline;

	/** the number of characters up to the start of the matched text */
	private int yychar;

	/**
	 * the number of characters from the last newline up to the start of the 
	 * matched text
	 */
	private int yycolumn;

	/** 
	 * yy_atBOL == true <=> the scanner is currently at the beginning of a line
	 */
	private boolean yy_atBOL;

	/** yy_atEOF == true <=> the scanner has returned a value for EOF */
	private boolean yy_atEOF;

	/** denotes if the user-EOF-code has already been executed */
	private boolean yy_eof_done;

	/* user code: */


	private boolean hasMore = true;
	private final static int MAX_TO_SCAN = 1000;
	StringBuffer string = new StringBuffer();
	// state stack for easier state handling
	private IntStack fStateStack = new IntStack();
	private String valueText = null;


	public XMLHeadTokenizer() {
		super();
	}

	public void reset(Reader in) {
		/* the input device */
		yy_reader = in;

		/* the current state of the DFA */
		yy_state = 0;

		/* the current lexical state */
		yy_lexical_state = YYINITIAL;

		/* this buffer contains the current text to be matched and is
		 the source of the yytext() string */
		java.util.Arrays.fill(yy_buffer, (char) 0);

		/* the textposition at the last accepting state */
		yy_markedPos = 0;

		/* the textposition at the last state to be included in yytext */
		yy_pushbackPos = 0;

		/* the current text position in the buffer */
		yy_currentPos = 0;

		/* startRead marks the beginning of the yytext() string in the buffer */
		yy_startRead = 0;

		/** 
		 * endRead marks the last character in the buffer, that has been read
		 * from input 
		 */
		yy_endRead = 0;

		/* number of newlines encountered up to the start of the matched text */
		yyline = 0;

		/* the number of characters up to the start of the matched text */
		yychar = 0;

		/**
		 * the number of characters from the last newline up to the start
		 * of the matched text
		 */
		yycolumn = 0;

		/** 
		 * yy_atBOL == true <=> the scanner is currently at the beginning 
		 * of a line
		 */
		yy_atBOL = false;

		/* yy_atEOF == true <=> the scanner has returned a value for EOF */
		yy_atEOF = false;

		/* denotes if the user-EOF-code has already been executed */
		yy_eof_done = false;


		fStateStack.clear();

		hasMore = true;

		// its a little wasteful to "throw away" first char array generated
		// by class init (via auto generated code), but we really do want 
		// a small buffer for our head parsers. 
		if (yy_buffer.length != MAX_TO_SCAN) {
			yy_buffer = new char[MAX_TO_SCAN];
		}


	}


	public final HeadParserToken getNextToken() throws IOException {
		String context = null;
		context = primGetNextToken();
		HeadParserToken result = null;
		if (valueText != null) {
			result = createToken(context, yychar, valueText);
			valueText = null;
		}
		else {
			result = createToken(context, yychar, yytext());
		}
		return result;
	}

	public final boolean hasMoreTokens() {
		return hasMore && yychar < MAX_TO_SCAN;
	}

	private void pushCurrentState() {
		fStateStack.push(yystate());

	}

	private void popState() {
		yybegin(fStateStack.pop());
	}

	private HeadParserToken createToken(String context, int start, String text) {
		return new HeadParserToken(context, start, text);
	}



	/**
	 * Creates a new scanner
	 * There is also a java.io.InputStream version of this constructor.
	 *
	 * @param   in  the java.io.Reader to read input from.
	 */
	public XMLHeadTokenizer(java.io.Reader in) {
		this.yy_reader = in;
	}

	/**
	 * Creates a new scanner.
	 * There is also java.io.Reader version of this constructor.
	 *
	 * @param   in  the java.io.Inputstream to read input from.
	 */
	public XMLHeadTokenizer(java.io.InputStream in) {
		this(new java.io.InputStreamReader(in));
	}

	/** 
	 * Unpacks the compressed character translation table.
	 *
	 * @param packed   the packed character translation table
	 * @return         the unpacked character translation table
	 */
	private static char[] yy_unpack_cmap(String packed) {
		char[] map = new char[0x10000];
		int i = 0; /* index in packed string  */
		int j = 0; /* index in unpacked array */
		while (i < 128) {
			int count = packed.charAt(i++);
			char value = packed.charAt(i++);
			do
				map[j++] = value;
			while (--count > 0);
		}
		return map;
	}


	/**
	 * Gets the next input character.
	 *
	 * @return      the next character of the input stream, EOF if the
	 *              end of the stream is reached.
	 * @exception   IOException  if any I/O-Error occurs
	 */
	private int yy_advance() throws java.io.IOException {

		/* standard case */
		if (yy_currentPos < yy_endRead)
			return yy_buffer[yy_currentPos++];

		/* if the eof is reached, we don't need to work hard */
		if (yy_atEOF)
			return YYEOF;

		/* otherwise: need to refill the buffer */

		/* first: make room (if you can) */
		if (yy_startRead > 0) {
			System.arraycopy(yy_buffer, yy_startRead, yy_buffer, 0, yy_endRead - yy_startRead);

			/* translate stored positions */
			yy_endRead -= yy_startRead;
			yy_currentPos -= yy_startRead;
			yy_markedPos -= yy_startRead;
			yy_pushbackPos -= yy_startRead;
			yy_startRead = 0;
		}

		/* is the buffer big enough? */
		if (yy_currentPos >= yy_buffer.length) {
			/* if not: blow it up */
			char newBuffer[] = new char[yy_currentPos * 2];
			System.arraycopy(yy_buffer, 0, newBuffer, 0, yy_buffer.length);
			yy_buffer = newBuffer;
		}

		/* finally: fill the buffer with new input */
		int numRead = yy_reader.read(yy_buffer, yy_endRead, yy_buffer.length - yy_endRead);

		if (numRead == -1)
			return YYEOF;

		yy_endRead += numRead;

		return yy_buffer[yy_currentPos++];
	}


	/**
	 * Closes the input stream.
	 */
	final public void yyclose() throws java.io.IOException {
		yy_atEOF = true; /* indicate end of file */
		yy_endRead = yy_startRead; /* invalidate buffer    */
		yy_reader.close();
	}


	/**
	 * Returns the current lexical state.
	 */
	final public int yystate() {
		return yy_lexical_state;
	}

	/**
	 * Enters a new lexical state
	 *
	 * @param newState the new lexical state
	 */
	final public void yybegin(int newState) {
		yy_lexical_state = newState;
	}


	/**
	 * Returns the text matched by the current regular expression.
	 */
	final public String yytext() {
		return new String(yy_buffer, yy_startRead, yy_markedPos - yy_startRead);
	}

	/**
	 * Returns the length of the matched text region.
	 */
	final public int yylength() {
		return yy_markedPos - yy_startRead;
	}


	/**
	 * Reports an error that occured while scanning.
	 *
	 * @param   errorCode  the code of the errormessage to display
	 */
	private void yy_ScanError(int errorCode) {
		try {
			System.out.println(YY_ERROR_MSG[errorCode]);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(YY_ERROR_MSG[YY_UNKNOWN_ERROR]);
		}

		// System.exit(1);
	}


	/**
	 * Pushes the specified amount of characters back into the input stream.
	 *
	 * They will be read again by then next call of the scanning method
	 *
	 * @param number  the number of characters to be read again.
	 *                This number must not be greater than yylength()!
	 */
	private void yypushback(int number) {
		if (number > yylength())
			yy_ScanError(YY_PUSHBACK_2BIG);

		yy_markedPos -= number;
	}


	/**
	 * Contains user EOF-code, which will be executed exactly once,
	 * when the end of file is reached
	 */
	private void yy_do_eof() {
		if (!yy_eof_done) {
			yy_eof_done = true;
			hasMore = false;

		}
	}


	/**
	 * Resumes scanning until the next regular expression is matched,
	 * the end of input is encountered or an I/O-Error occurs.
	 *
	 * @return      the next token
	 * @exception   IOException  if any I/O-Error occurs
	 */
	public String primGetNextToken() throws java.io.IOException {
		int yy_input;
		int yy_action;


		while (true) {

			yychar += yylength();

			yy_atBOL = yy_markedPos <= 0 || yy_buffer[yy_markedPos - 1] == '\n';
			if (!yy_atBOL && yy_buffer[yy_markedPos - 1] == '\r') {
				yy_atBOL = yy_advance() != '\n';
				if (!yy_atEOF)
					yy_currentPos--;
			}

			yy_action = -1;

			yy_currentPos = yy_startRead = yy_markedPos;

			if (yy_atBOL)
				yy_state = YY_LEXSTATE[yy_lexical_state + 1];
			else
				yy_state = YY_LEXSTATE[yy_lexical_state];


			yy_forAction : {
				while (true) {

					yy_input = yy_advance();

					if (yy_input == YYEOF)
						break yy_forAction;

					yy_input = yycmap[yy_input];

					boolean yy_isFinal = false;
					boolean yy_noLookAhead = false;

					yy_forNext : {
						switch (yy_state) {
							case 0 :
								switch (yy_input) {
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 7;
										break yy_forNext;
								}

							case 1 :
								switch (yy_input) {
									case 1 :
										yy_isFinal = true;
										yy_state = 8;
										break yy_forNext;
									case 2 :
										yy_isFinal = true;
										yy_state = 9;
										break yy_forNext;
									case 3 :
										yy_isFinal = true;
										yy_state = 10;
										break yy_forNext;
									case 6 :
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_state = 11;
										break yy_forNext;
									case 10 :
										yy_isFinal = true;
										yy_state = 12;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 7;
										break yy_forNext;
								}

							case 2 :
								switch (yy_input) {
									case 11 :
										yy_isFinal = true;
										yy_state = 13;
										break yy_forNext;
									case 15 :
										yy_isFinal = true;
										yy_state = 14;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 7;
										break yy_forNext;
								}

							case 3 :
								switch (yy_input) {
									case 6 :
									case 9 :
										yy_isFinal = true;
										yy_state = 16;
										break yy_forNext;
									case 7 :
										yy_isFinal = true;
										yy_state = 17;
										break yy_forNext;
									case 23 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 18;
										break yy_forNext;
									case 24 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 19;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 15;
										break yy_forNext;
								}

							case 4 :
								switch (yy_input) {
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 21;
										break yy_forNext;
									case 11 :
										yy_isFinal = true;
										yy_state = 22;
										break yy_forNext;
									case 23 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 23;
										break yy_forNext;
									case 24 :
										yy_isFinal = true;
										yy_state = 24;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 20;
										break yy_forNext;
								}

							case 5 :
								switch (yy_input) {
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 21;
										break yy_forNext;
									case 24 :
										yy_isFinal = true;
										yy_state = 25;
										break yy_forNext;
									case 25 :
										yy_isFinal = true;
										yy_state = 26;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 20;
										break yy_forNext;
								}

							case 6 :
								switch (yy_input) {
									case 11 :
										yy_isFinal = true;
										yy_state = 26;
										break yy_forNext;
									case 6 :
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 27;
										break yy_forNext;
									case 23 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 28;
										break yy_forNext;
									case 24 :
										yy_isFinal = true;
										yy_state = 29;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 20;
										break yy_forNext;
								}

							case 8 :
								switch (yy_input) {
									case 2 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 30;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 9 :
								switch (yy_input) {
									case 1 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 31;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 10 :
								switch (yy_input) {
									case 4 :
										yy_state = 32;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 11 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_state = 33;
										break yy_forNext;
									case 10 :
										yy_state = 34;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 12 :
								switch (yy_input) {
									case 11 :
										yy_state = 35;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 13 :
								switch (yy_input) {
									case 22 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 36;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 14 :
								switch (yy_input) {
									case 16 :
										yy_state = 37;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 16 :
								switch (yy_input) {
									case 6 :
									case 9 :
										yy_isFinal = true;
										yy_state = 16;
										break yy_forNext;
									case 7 :
										yy_state = 38;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 15;
										break yy_forNext;
								}

							case 17 :
								switch (yy_input) {
									case 6 :
									case 9 :
										yy_isFinal = true;
										yy_state = 16;
										break yy_forNext;
									case 7 :
										yy_state = 38;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 15;
										break yy_forNext;
								}

							case 22 :
								switch (yy_input) {
									case 22 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 39;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 24 :
								switch (yy_input) {
									case 10 :
										yy_state = 40;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 25 :
								switch (yy_input) {
									case 10 :
										yy_state = 40;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 26 :
								switch (yy_input) {
									case 22 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 41;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 29 :
								switch (yy_input) {
									case 10 :
										yy_state = 40;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 32 :
								switch (yy_input) {
									case 5 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 42;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 33 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_state = 33;
										break yy_forNext;
									case 10 :
										yy_state = 34;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 34 :
								switch (yy_input) {
									case 11 :
										yy_state = 35;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 35 :
								switch (yy_input) {
									case 12 :
										yy_state = 43;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 37 :
								switch (yy_input) {
									case 17 :
										yy_state = 44;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 38 :
								switch (yy_input) {
									case 6 :
									case 9 :
										yy_isFinal = true;
										yy_state = 16;
										break yy_forNext;
									case 7 :
										yy_state = 38;
										break yy_forNext;
									default :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 15;
										break yy_forNext;
								}

							case 40 :
								switch (yy_input) {
									case 24 :
										yy_isFinal = true;
										yy_noLookAhead = true;
										yy_state = 21;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 43 :
								switch (yy_input) {
									case 13 :
										yy_state = 45;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 44 :
								switch (yy_input) {
									case 18 :
										yy_state = 46;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 45 :
								switch (yy_input) {
									case 14 :
										yy_state = 47;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 46 :
								switch (yy_input) {
									case 19 :
										yy_state = 48;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 47 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_state = 49;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 48 :
								switch (yy_input) {
									case 20 :
										yy_state = 50;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 49 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_state = 49;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 50 :
								switch (yy_input) {
									case 16 :
										yy_state = 51;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 51 :
								switch (yy_input) {
									case 21 :
										yy_state = 52;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 52 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_state = 52;
										break yy_forNext;
									case 8 :
										yy_isFinal = true;
										yy_state = 53;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							case 53 :
								switch (yy_input) {
									case 6 :
									case 7 :
									case 9 :
										yy_isFinal = true;
										yy_state = 53;
										break yy_forNext;
									default :
										break yy_forAction;
								}

							default :
								yy_ScanError(YY_ILLEGAL_STATE);
								break;
						}
					}

					if (yy_isFinal) {
						yy_action = yy_state;
						yy_markedPos = yy_currentPos;
						if (yy_noLookAhead)
							break yy_forAction;
					}

				}
			}


			switch (yy_action) {

				case 25 :
					{
						popState();
						valueText = string.toString();
						return EncodingParserConstants.StringValue;
					}
				case 55 :
					break;
				case 21 :
					{
						yypushback(1);
						popState();
						valueText = string.toString();
						return EncodingParserConstants.InvalidTerminatedStringValue;
					}
				case 56 :
					break;
				case 15 :
				case 16 :
					{
						yypushback(1);
						yybegin(UnDelimitedString);
						string.setLength(0);
					}
				case 57 :
					break;
				case 28 :
				case 29 :
					{
						yypushback(1);
						popState();
						valueText = string.toString();
						return EncodingParserConstants.InvalidTermintatedUnDelimitedStringValue;
					}
				case 58 :
					break;
				case 39 :
					{
						yypushback(2);
						popState();
						valueText = string.toString();
						return EncodingParserConstants.InvalidTerminatedStringValue;
					}
				case 59 :
					break;
				case 41 :
					{
						yypushback(2);
						popState();
						valueText = string.toString();
						return EncodingParserConstants.InvalidTerminatedStringValue;
					}
				case 60 :
					break;
				case 7 :
				case 8 :
				case 9 :
				case 10 :
				case 11 :
				case 12 :
				case 13 :
				case 14 :
				case 17 :
					{
						if (yychar > MAX_TO_SCAN) {
							hasMore = false;
							return EncodingParserConstants.MAX_CHARS_REACHED;
						}
					}
				case 61 :
					break;
				case 30 :
					{
						if (yychar == 0) {
							hasMore = false;
							return EncodingParserConstants.UTF16BE;
						}
					}
				case 62 :
					break;
				case 31 :
					{
						if (yychar == 0) {
							hasMore = false;
							return EncodingParserConstants.UTF16LE;
						}
					}
				case 63 :
					break;
				case 42 :
					{
						if (yychar == 0) {
							hasMore = false;
							return EncodingParserConstants.UTF83ByteBOM;
						}
					}
				case 64 :
					break;
				case 49 :
					{
						if (yychar == 0) {
							yybegin(ST_XMLDecl);
							return XMLHeadTokenizerConstants.XMLDeclStart;
						}
					}
				case 65 :
					break;
				case 36 :
					{
						yybegin(YYINITIAL);
						hasMore = false;
						return XMLHeadTokenizerConstants.XMLDeclEnd;
					}
				case 66 :
					break;
				case 53 :
					{
						pushCurrentState();
						yybegin(QuotedAttributeValue);
						return XMLHeadTokenizerConstants.XMLDelEncoding;
					}
				case 67 :
					break;
				case 23 :
					{
						popState();
						valueText = string.toString();
						return EncodingParserConstants.StringValue;
					}
				case 68 :
					break;
				case 20 :
				case 22 :
				case 24 :
				case 26 :
					{
						string.append(yytext());
					}
				case 69 :
					break;
				case 19 :
					{
						yybegin(SQ_STRING);
						string.setLength(0);
					}
				case 70 :
					break;
				case 18 :
					{
						yybegin(DQ_STRING);
						string.setLength(0);
					}
				case 71 :
					break;
				case 27 :
					{
						yypushback(1);
						popState();
						valueText = string.toString();
						return EncodingParserConstants.UnDelimitedStringValue;
					}
				case 72 :
					break;
				default :
					if (yy_input == YYEOF && yy_startRead == yy_currentPos) {
						yy_atEOF = true;
						yy_do_eof();
						{
							hasMore = false;
							return EncodingParserConstants.EOF;
						}
					}
					else {
						yy_ScanError(YY_NO_MATCH);
					}
			}
		}
	}

	/**
	 * Runs the scanner on input files.
	 *
	 * This main method is the debugging routine for the scanner.
	 * It prints each returned token to System.out until the end of
	 * file is reached, or an error occured.
	 *
	 * @param argv   the command line, contains the filenames to run
	 *               the scanner on.
	 */
	public static void main(String argv[]) {
		for (int i = 0; i < argv.length; i++) {
			XMLHeadTokenizer scanner = null;
			try {
				scanner = new XMLHeadTokenizer(new java.io.FileReader(argv[i]));
			}
			catch (java.io.FileNotFoundException e) {
				System.out.println("File not found : \"" + argv[i] + "\"");
				System.exit(1);
			}
//			catch (java.io.IOException e) {
//				System.out.println("Error opening file \"" + argv[i] + "\"");
//				System.exit(1);
//			}
			catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Usage : java XMLHeadTokenizer <inputfile>");
				System.exit(1);
			}

			try {
				do {
					System.out.println(scanner.primGetNextToken());
				}
				while (!scanner.yy_atEOF);

			}
			catch (java.io.IOException e) {
				System.out.println("An I/O error occured while scanning :");
				System.out.println(e);
				System.exit(1);
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}


}
