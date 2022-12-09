package com.banking.localcache;

import java.util.Map;

import com.banking.methods.AdminOperations;
import com.banking.methods.CustomerOperations;
import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.RequestDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;

import myutil.CustomException;

public class StorageLayer {

	// customer
	public Map<Long,CustomerDetails> customerDetails;
	public Map<Long,Map<Long,AccountDetails>> accountDetails;
	public Map<Long,Map<Long,TransactionDetails>> transactionDetails;
	
	// admin
	public Map<Long, UserDetails> userDetails;
	public Map<Long,RequestDetails> requestDetails;
	
	AdminOperations admin = new AdminOperations();
	CustomerOperations customer = new CustomerOperations();
	
	public void setAllData() throws CustomException {
		setCustomerDetails();
		setAccountDetails();
		setTransactionDetails();
		setUserDetails();
		setRequestDetails();
	}
	
	public Map<Long, CustomerDetails> getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails() throws CustomException {
		customerDetails = customer.getAllCustomerInfo();
	}
	public Map<Long, Map<Long, AccountDetails>> getAccountDetails() {
		return accountDetails;
	}
	public void setAccountDetails() throws CustomException {
		accountDetails = customer.getAllAccountDetails();
	}
	public Map<Long, Map<Long, TransactionDetails>> getTransactionDetails() {
		return transactionDetails;
	}
	public void setTransactionDetails() throws CustomException {
		transactionDetails = customer.getAllTransactionDetails();
	}
	public Map<Long, UserDetails> getUserDetails() {
		return userDetails;
	}
	public void setUserDetails() throws CustomException {
		userDetails = admin.showAllUser();
	}
	public Map<Long, RequestDetails> getRequestDetails() {
		return requestDetails;
	}
	public void setRequestDetails() throws CustomException {
		requestDetails = admin.getAllRequests();
	}
	
	
}
