package com.octest.banque.bean;

import java.util.Date;

/**
 * Transaction  JavaBean encapsulates TimeTable attributes
 * 
 * 
 */

public class TransactionBean extends BaseBean {

	/**
	 * Transaction Id of Transaction
	 */
	private long trasactionId;
	/**
	 * Transaction Date of Transaction
	 */
	private Date trasactionDate;
	/**
	 * Transaction Type of Transaction
	 */
	private String transactionType;
	/**
	 * Transaction Amount of Transaction
	 */
	private double transactionAmount;
	/**
	 * Description of Transaction
	 */
	private String description;
	/**
	 * To Account No of Transaction
	 */
	private long toAccountNo;
	/**
	 * From Account No of Transaction
	 */
	private long fromAccountNo;
	
	
	public long getTrasactionId() {
		return trasactionId;
	}

	public void setTrasactionId(long trasactionId) {
		this.trasactionId = trasactionId;
	}

	public Date getTrasactionDate() {
		return trasactionDate;
	}

	public void setTrasactionDate(Date trasactionDate) {
		this.trasactionDate = trasactionDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(long toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public long getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(long fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getKey() {
		return null;
	}

	public String getValue() {
		return null;
	}

}
