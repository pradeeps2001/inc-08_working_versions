package com.banking.storageprotocol;
	
import java.util.Map;

import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;

import myutil.CustomException;
	
public interface CustomerInterface {
	
	Map<Long,CustomerDetails> getCustomerInfo(long customerId) throws CustomException;
	
	Map<Long,Map<Long,AccountDetails>> getAccountDetails(long customerId, Long... accountNo) throws CustomException;
	
	Map<Long,Map<Long,TransactionDetails>> getTransactionDetails(long customerId, long accNo) throws CustomException;
	
	AccountDetails getBalance(long accNo) throws CustomException;
	
	void deposit(long userId, long accNo, double amount) throws CustomException;
	
	public void requestWithdraw(long customerId, long accNo, double amount) throws CustomException;
	
	void transfer(long senderId, long senderAcc, long receiverAcc, double amount) throws CustomException;
	
	void updateTransaction(TransactionDetails obj) throws CustomException;
	
	void changePassword(Long userId, String newPassword) throws CustomException;

	void editCustomerInfo(long customerId, UserDetails user) throws CustomException;
	
}	