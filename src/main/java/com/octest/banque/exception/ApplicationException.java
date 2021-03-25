package com.octest.banque.exception;

/**
 * ApplicationException is propogated from Service classes when an business
 * logic exception occurered.
 * 
 */

public class ApplicationException  extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            : Error message
	 */
	public ApplicationException(String msg) {
		super(msg);
	}
}
