package com.banking.pojo;

import java.io.Serializable;

public class AccountDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	private long customerId;
	private long accountNo;
	private String accountType;
	private String branch;
	private Double balance;
	private String status;
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long userId) {
		this.customerId = userId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
