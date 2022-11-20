package com.banking.storageprotocol;

import java.util.Map;

import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.RequestDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;

import myutil.CustomException;

public interface AdminInterface {

	Map<Long,CustomerDetails> showUser(Long... userId) throws CustomException;
	
	Map<Long, AccountDetails> showAccounts(Long... customerId) throws CustomException;
	
	Map<Long,Map<Long,TransactionDetails>> getTransactionDetails(long customerId, String time, Long... accNo) throws CustomException;
	
	Map<Long, RequestDetails> getRequests(long customerId) throws CustomException;
	
	long createCustomer(CustomerDetails customer) throws CustomException;
	
	long createAccount(AccountDetails account) throws CustomException;
	
	void acceptRequest(long customerId, long accNo, long reqId) throws CustomException;
	
	void rejectRequest(long customerId, long accNo, long reqId) throws CustomException;

	void activateCustomer(long customerId) throws CustomException;
	
	void deactivateCustomer(long customerId) throws CustomException;
	
	void activateAccount(long customerId, long accNo) throws CustomException;
	
	void deactivateAccount(long customerId, long accNo) throws CustomException;

	void editAdminInfo(long customerId, UserDetails user) throws CustomException;
}
