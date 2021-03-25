package com.octest.banque.bean;
/**
 * Customer  JavaBean encapsulates TimeTable attributes
 * 
 * 
 */

public class CustomerBean extends BaseBean {

	/**
	 * Mobile No of Customer
	 */
	private String mobileNo;
	/**
	 * Email of Customer
	 */
	private String emailId;
	/**
	 * Name of Customer
	 */
	private String name;
	/**
	 * Account No of Customer
	 */
	private long acc_No;
	/**
	 * Address of Customer
	 */
	private String address;
	/**
	 * User Id of Customer
	 */
	private long userId;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAcc_No() {
		return acc_No;
	}

	public void setAcc_No(long acc_No) {
		this.acc_No = acc_No;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getKey() {
		return id + "";
	}

	public String getValue() {

		return name;
	}
}
