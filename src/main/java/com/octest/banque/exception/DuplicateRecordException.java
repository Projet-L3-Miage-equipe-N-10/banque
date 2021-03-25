package com.octest.banque.exception;

/**
 * DuplicateRecordException thrown when a duplicate record occurred
 */
public class DuplicateRecordException  extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            error message
	 */
	public DuplicateRecordException(String msg) {
		super(msg);
	}
}
