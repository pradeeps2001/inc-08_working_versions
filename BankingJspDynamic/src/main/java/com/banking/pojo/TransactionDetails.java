package com.banking.pojo;

import java.io.Serializable;

public class TransactionDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	private long userId;
	private long accountNo;
	private long referenceId;
	private long transferId;
	private long time;
	private String mode;
	private String type;
	private double amount;
	private long receivedFrom;
	private long sendTo;
	private double balance;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	public long getTransferId() {
		return transferId;
	}
	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getReceivedFrom() {
		return receivedFrom;
	}
	public void setReceivedFrom(long receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
	public long getSendTo() {
		return sendTo;
	}
	public void setSendTo(long sendTo) {
		this.sendTo = sendTo;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
