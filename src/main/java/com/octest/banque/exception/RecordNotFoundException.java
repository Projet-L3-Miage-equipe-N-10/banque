package com.octest.banque.exception;

/**
 * RecordNotFoundException thrown when a record not found occurred
 */

public class RecordNotFoundException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            error message
	 */
	public RecordNotFoundException(String msg) {
		super(msg);

	}
}
