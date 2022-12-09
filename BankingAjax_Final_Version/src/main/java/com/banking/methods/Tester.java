package com.banking.methods;

import com.banking.localcache.StorageLayer;
import com.banking.pojo.CustomerDetails;

import myutil.CustomException;

public class Tester {

	public static void main(String[] args) {
		 StorageLayer storage = new StorageLayer();
		 AdminOperations admin = new AdminOperations();
		CustomerOperations customer = new CustomerOperations();
		//
		 try {
		 storage.setAllData();
		 storage.setTransactionDetails();
		 } catch (CustomException e1) {
		 e1.printStackTrace();
		 }
		 CustomerDetails user = new CustomerDetails();
		 user.setUserName("Ram");
		 user.setUserId(800101L);
		 user.setDOB("2001-02-14");
		 user.setMobileNum("9985458525");
		 user.setAadharNum(889565325698L);
		 user.setPanNum("FJGIK7845P");
		 user.setRole("CUSTOMER");
		 user.setStatus("ACTIVE");
		 user.setPassword("Ram@34563");
		 user.setEmail("ram@gmail.com");
		 
		 try {
			admin.createCustomer(user);
		} catch (CustomException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
