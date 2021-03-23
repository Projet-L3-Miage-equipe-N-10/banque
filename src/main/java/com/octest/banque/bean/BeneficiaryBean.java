package com.octest.banque.bean;

/**
 * Beneficiary JavaBean encapsulates TimeTable attributes
 * 
 * 
 */
public class BeneficiaryBean extends BaseBean {
	
	/**
	 * Account  No of Beneficiary
	 */
	private long acc_No;

	/**
	 * Bank Name of Beneficiary
	 */
	private String bankName;
	/**
	 * Name  of Beneficiary
	 */
	private String  name;
	/**
	 * Confirm Account No of Beneficiary
	 */
	private String confirmAccNo;
	/**
	 * User Id of Beneficiary
	 */
	private long userId;
	
	// IFSC Code of Beneficiary
	 
	private String IBAN;
	
	
	
	
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfirmAccNo() {
		return confirmAccNo;
	}

	public void setConfirmAccNo(String confirmAccNo) {
		this.confirmAccNo = confirmAccNo;
	}

	public long getAcc_No() {
		return acc_No;
	}

	public void setAcc_No(long acc_No) {
		this.acc_No = acc_No;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}


	public String getKey() {
		return null;
	}

	public String getValue() {
		return null;
	}

}
