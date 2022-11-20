package com.banking.servlets;

import java.io.IOException;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.banking.localcache.StorageLayer;
import com.banking.methods.AdminOperations;
import com.banking.methods.CustomerOperations;
import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.RequestDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;
import com.banking.storageprotocol.AdminInterface;
import com.banking.storageprotocol.CustomerInterface;

import myutil.CustomException;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public MyServlet() {	

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException {
		try {
			doProcess(request,responce);
		} catch (ServletException | IOException | CustomException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException {
		try {
			doProcess(request, responce);
		} catch (ServletException | IOException | CustomException e) {
			e.printStackTrace();
		}
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException, CustomException {

		AdminInterface admin = new AdminOperations();
		CustomerInterface customer = new CustomerOperations();
		StorageLayer storage = new StorageLayer();
		
		storage.setAllData();
		
		Map<Long,UserDetails> userDetailsMap = storage.getUserDetails();
		Map<Long,CustomerDetails> customerDetailsMap = storage.getCustomerDetails();
		Map<Long,Map<Long,AccountDetails>> accountDetailsMap = storage.getAccountDetails();
		Map<Long,Map<Long,TransactionDetails>> transactionDetailsMap = storage.getTransactionDetails();
		Map<Long,RequestDetails> requestDetailsMap = storage.getRequestDetails();

		String data = request.getParameter("data");
		System.out.println(data);

		HttpSession session = request.getSession(true);

		responce.setContentType("text/html");

		switch (data) {
		case "Login": {

			String userIdStr = request.getParameter("user");
			String pass = request.getParameter("pass");
			long userId = Long.parseLong(userIdStr);

			if(userDetailsMap.containsKey(userId)) {
				UserDetails userObj = userDetailsMap.get(userId);
				long dbUserId = userObj.getUserId();
				String dbPass = userObj.getPassword();
				String dbName = userObj.getUserName();

				if(userId==dbUserId && pass.equals(dbPass)) {
					session.setAttribute("userId", dbUserId);
					session.setAttribute("userName", dbName);

					if(userObj.getRole().equals("CUSTOMER")) {
						CustomerDetails customerObj = customerDetailsMap.get(userId);
						String status = customerObj.getStatus();
						// setting account details map for getting account num and account info for this session
						if(status.equals("ACTIVE")) {
							Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get(userId);
							session.setAttribute("accountMap", cusAccMap);
							session.setAttribute("accountSet", cusAccMap.keySet().toArray());
							RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerPage.jsp");
							reqDp.include(request, responce);

						} else {
							request.setAttribute("error", "Customer is not Active.");
							RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/LoginPage.jsp");
							reqDp.include(request, responce);
						}

					} else if(userObj.getRole().equals("ADMIN")) {
						RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminPage.jsp");
						reqDp.forward(request, responce);
					}

					else {
						request.setAttribute("error", "Invalid Login Credentials");
						RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/LoginPage.jsp");
						reqDp.include(request, responce);
					}

				} else {
					request.setAttribute("error", "Invalid Login Credentials");
					RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/LoginPage.jsp");
					reqDp.include(request, responce);
				}
			} else {
				request.setAttribute("error", "Invalid Login Credentials");
				RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/LoginPage.jsp");
				reqDp.include(request, responce);
			}
			break;
		}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		// customer menu
		case "Home": {
			storage.setAllData();
			request.setAttribute("accounts", accountDetailsMap.get(session.getAttribute("userId")));
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerInitialContent.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Account Info":{
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			request.setAttribute("accounts", cusAccMap);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/AccountInfo.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Deposit Cash":{
			request.setAttribute("heading", "Deposit");
			request.setAttribute("transaction", "Deposit");
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/DepositWithdraw.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Withdraw Cash":{
			request.setAttribute("heading", "Withdraw");
			request.setAttribute("transaction", "Withdraw");
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/DepositWithdraw.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Proceed": {
			long userId = (long) session.getAttribute("userId");
			String mode = request.getParameter("transaction");
			String accNoStr = request.getParameter("accountDrop");
			long accNo = Long.parseLong(accNoStr);
			String amountStr = request.getParameter("amount");
			double amount = Double.parseDouble(amountStr);
			if(mode.equals("Deposit")) {
				try {
					customer.deposit(userId, accNo, amount);
					System.out.println("Deposit "+amount);
					request.setAttribute("success", "Deposit Successful.");
				} catch (CustomException e) {
					request.setAttribute("transaction", "Deposit");
					request.setAttribute("error", e.getMessage());
				}
				request.setAttribute("transaction", "Deposit");
				request.setAttribute("heading", "Deposit :");
				request.setAttribute("accountDrop", accNo);
				RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/DepositWithdraw.jsp");
				reqDp.include(request, responce);
			} else {
				try {
					customer.requestWithdraw(userId, accNo, amount);
					System.out.println("Withdraw requested for "+amount);
					request.setAttribute("success", "Withdraw Requested.");
				} catch(CustomException e) {
					request.setAttribute("transaction", "Withdraw");
					request.setAttribute("error", e.getMessage());
				}
				request.setAttribute("transaction", "Withdraw");
				request.setAttribute("heading", "Withdraw :");
				request.setAttribute("accountDrop", accNo);
				RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/DepositWithdraw.jsp");
				reqDp.include(request, responce);
			}
			storage.setAllData();
			break;
		}
		case "Transfer Cash":{
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/Transfer.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Transfer": {
			long senderId = (long) session.getAttribute("userId");
			String senderAccStr = request.getParameter("accountDrop");
			long senderAcc = Long.parseLong(senderAccStr);
			String receiverAccStr = request.getParameter("receiverAcc");
			long receiverAcc = Long.parseLong(receiverAccStr);
			String amountStr = request.getParameter("amount");
			double amount = Double.parseDouble(amountStr);
			try {
				customer.transfer(senderId, senderAcc, receiverAcc, amount);
				System.out.println("Transferred "+amount+" to "+receiverAcc);
				request.setAttribute("success", "Money Transfer Successful.");
			} catch (CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			request.setAttribute("accountDrop", senderAcc);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/Transfer.jsp");
			reqDp.include(request, responce);
			storage.setAllData();
			break;
		}
		case "Transaction History":{
			request.setAttribute("hide", "hide");
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			request.setAttribute("accountSet", cusAccMap.keySet().toArray());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/TransactionHistory.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Confirm": {
			String accDrop = request.getParameter("accountDrop");
			System.out.println(accDrop);
			long accNo = Long.parseLong(accDrop);
			Map<Long,TransactionDetails> transDetails = transactionDetailsMap.get(accNo);
			request.setAttribute("accountDrop", accNo);
			request.setAttribute("transMap", transDetails);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/TransactionHistory.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Edit Customer Details":{
			long customerId = (long)session.getAttribute("userId");
			CustomerDetails customerObj = customerDetailsMap.get(customerId);
			request.setAttribute("customerId", customerObj.getUserId());
			request.setAttribute("name", customerObj.getUserName());
			request.setAttribute("dob", customerObj.getDOB());
			request.setAttribute("mail", customerObj.geteMail());
			request.setAttribute("mobile", customerObj.getMobileNum());
			request.setAttribute("aadhar", customerObj.getAadharNum());
			request.setAttribute("pan", customerObj.getPanNum());
			
			session.setAttribute("email", customerObj.geteMail());
			session.setAttribute("mobNum", customerObj.getMobileNum());
			
			System.out.println(customerObj.getUserName());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/CustomerProfile.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Update Details": {
			long userId = (long) session.getAttribute("userId");
			String eMail = request.getParameter("email");
			String mobile = request.getParameter("mobile");
			UserDetails user = new UserDetails();
			user.setUserId(userId);
			user.seteMail(eMail);
			user.setMobileNum(mobile);
			
			String oldMail = (String) session.getAttribute("email");
			String oldNum = (String) session.getAttribute("mobNum");
			
			if(!(user.geteMail().equals(oldMail) && user.getMobileNum().equals(oldNum))) {
				try {
					customer.editCustomerInfo(userId, user);
					request.setAttribute("success", "Profile Edited Successfully.");
					storage.setAllData();
				} catch(CustomException e) {
					request.setAttribute("error", e.getMessage());
				}
			} else {
				CustomerDetails customerObj = customerDetailsMap.get(userId);
				request.setAttribute("customerId", customerObj.getUserId());
				request.setAttribute("name", customerObj.getUserName());
				request.setAttribute("dob", customerObj.getDOB());
				request.setAttribute("mail", customerObj.geteMail());
				request.setAttribute("mobile", customerObj.getMobileNum());
				request.setAttribute("aadhar", customerObj.getAadharNum());
				request.setAttribute("pan", customerObj.getPanNum());
				RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/CustomerProfile.jsp");
				reqDp.forward(request, responce);
			}
			// populate
			long customerId = (long)session.getAttribute("userId");
			Map<Long,CustomerDetails> userMap = storage.getCustomerDetails();
			CustomerDetails customerObj = userMap.get(customerId);
			request.setAttribute("customerId", customerObj.getUserId());
			request.setAttribute("name", customerObj.getUserName());
			request.setAttribute("dob", customerObj.getDOB());
			request.setAttribute("mail", customerObj.geteMail());
			request.setAttribute("mobile", customerObj.getMobileNum());
			request.setAttribute("aadhar", customerObj.getAadharNum());
			request.setAttribute("pan", customerObj.getPanNum());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/CustomerActions/CustomerProfile.jsp");
			reqDp.include(request, responce);
			break;
		}
		
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		// common
		case "Password Change": {
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/PasswordChange.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Change Password": {
			long userId = (Long) session.getAttribute("userId");
			UserDetails passObj = userDetailsMap.get(userId);
			String actualPass = passObj.getPassword();
			String inputOldPass = request.getParameter("oldPass");
			String inputNewPass = request.getParameter("newPass");
			if(inputOldPass.equals(actualPass)) {
				customer.changePassword(userId, inputNewPass);
				System.out.println("Password changed - "+inputNewPass);
				storage.setAllData();
				request.setAttribute("success", "Password Changed.");
			} else {
				request.setAttribute("error", "Old password is incorrect.");
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/PasswordChange.jsp");
			reqDp.include(request, responce);
			break;
		}
		// logout
		case "LOGOUT":{
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/LoginPage.jsp");
			reqDp.forward(request, responce);
			session.invalidate();
			break;
		}
		
//-------------------------------------------------------------------------------------------------------------------------------------------------------		
		// admin menu
		case "Home Page":{
			storage.setAllData();
			request.setAttribute("requestMap", requestDetailsMap);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminInitialContent.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Show Customer Info":{
			Map<Long, CustomerDetails> map = admin.showUser();
			request.setAttribute("cusInfoMap", map);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowCustomerInfo.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Show Customer": {
			String customerIdStr = request.getParameter("customerId");
			long customerId = Long.parseLong(customerIdStr);
			if(customerDetailsMap.containsKey(customerId)) {
				Map<Long, CustomerDetails> map = admin.showUser(customerId);
				request.setAttribute("cusInfoMap", map);
				request.setAttribute("customerId", customerId);
			}
			else {
				System.out.println("Show customer info failed.");
				request.setAttribute("hide", "hide");
				request.setAttribute("error", "Customer ID Not Found.");
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowCustomerInfo.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Show Account Info":{
			Map<Long, AccountDetails> accountMap = admin.showAccounts();
			request.setAttribute("accounts", accountMap);
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowAccountInfo.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Show Accounts": {
			String customerIdStr = request.getParameter("customerId");
			long customerId = Long.parseLong(customerIdStr);
			if(accountDetailsMap.containsKey(customerId)) {
				Map<Long,AccountDetails> accounts = accountDetailsMap.get(customerId);
				request.setAttribute("customerId", customerId);
				request.setAttribute("accounts", accounts);
			} else {
				request.setAttribute("error", "Customer ID Not Found.");
				request.setAttribute("hide", "hide");
				request.setAttribute("error", "Customer ID Not Found.");
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowAccountInfo.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Show Transaction Info":{
			request.setAttribute("hide", "hide");
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowTransactionInfo.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Show Statement": {
			String customerIdStr = request.getParameter("customerId");
			long customerId = Long.parseLong(customerIdStr);
			String time = request.getParameter("time");
			System.out.println(time);
			if(customerDetailsMap.containsKey(customerId)) {
				Map<Long,Map<Long,TransactionDetails>> transMap = admin.getTransactionDetails(customerId, time);
				if(transMap.size()==0) {
					request.setAttribute("hide", "hide");
					request.setAttribute("error", "No recent transactions.");
				}
				request.setAttribute("customerId", customerId);
				request.setAttribute("time", time);
				request.setAttribute("transMap", transMap);
			} else {
				System.out.println("Show Transaction Details Failed.");
				request.setAttribute("error", "Customer ID Not Found.");
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/ShowTransactionInfo.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Create Customer": {
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CreateCustomer.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Add Customer": {
			CustomerDetails newCustomer = new CustomerDetails();
			String name = (String) request.getParameter("name");
			String dob = request.getParameter("dob");
			String email = (String) request.getParameter("email");
			String mobileNum = (String) request.getParameter("mobile");
			String password = (String) request.getParameter("password");
			String aadharStr = (String) request.getParameter("aadhar");
			long aadhar = Long.parseLong(aadharStr);
			String pan = (String) request.getParameter("pan");
			newCustomer.setUserName(name);
			newCustomer.setDOB(dob);
			newCustomer.seteMail(email);
			newCustomer.setMobileNum(mobileNum);
			newCustomer.setRole("CUSTOMER");
			newCustomer.setPassword(password);
			newCustomer.setAadharNum(aadhar);
			newCustomer.setPanNum(pan);
			newCustomer.setStatus("ACTIVE");
			try{
				long newUserId = admin.createCustomer(newCustomer);
				storage.setAllData();
				request.setAttribute("success", "Customer Created with User ID "+newUserId+".");
			} catch (CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CreateCustomer.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Create Account":{
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CreateAccount.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Add Account": {
			AccountDetails newAccount = new AccountDetails();
			String customerIdStr = (String) request.getParameter("customerId");
			long customerId = Long.parseLong(customerIdStr);
			String accType = (String) request.getParameter("type");
			String branch = (String) request.getParameter("branch");
			String balanceStr = (String) request.getParameter("balance");
			double balance = Double.parseDouble(balanceStr);
			newAccount.setCustomerId(customerId);
			newAccount.setAccountType(accType.toUpperCase());
			newAccount.setBranch(branch.toUpperCase());
			newAccount.setBalance(balance);
			newAccount.setStatus("ACTIVE");
			try {
			long newAccNo = admin.createAccount(newAccount);
			storage.setAllData();
			request.setAttribute("success", "Account Created with Account No "+newAccNo+".");
			} catch(CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CreateAccount.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Request Approval": {
			request.setAttribute("requestMap", requestDetailsMap);
			System.out.println(requestDetailsMap.size()+" pending withdrawals.");
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/RequestApproval.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Approve Table":{
			String tableIdStr = request.getParameter("tableCusId");
			long customerId = Long.parseLong(tableIdStr);
			String tableAccStr = request.getParameter("tableAccNo");
			long accNo = Long.parseLong(tableAccStr);
			String tableReqStr = request.getParameter("tableReqId");
			long reqId = Long.parseLong(tableReqStr);
			admin.acceptRequest(customerId, accNo, reqId);
			storage.setAllData();
			request.setAttribute("requestMap", storage.getRequestDetails());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/RequestApproval.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Reject Table":{
			String tableIdStr = request.getParameter("tableCusId");
			long customerId = Long.parseLong(tableIdStr);
			String tableAccStr = request.getParameter("tableAccNo");
			long accNo = Long.parseLong(tableAccStr);
			String tableReqStr = request.getParameter("tableReqId");
			long reqId = Long.parseLong(tableReqStr);
			admin.rejectRequest(customerId, accNo, reqId);
			storage.setAllData();
			request.setAttribute("requestMap", storage.getRequestDetails());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/RequestApproval.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Customer Activation": {
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CustomerActivation.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Activate Customer": {
			String cusIdStr = request.getParameter("customerId");
			long cusId = Long.parseLong(cusIdStr);
			try {
				admin.activateCustomer(cusId);
				storage.setAllData();
				request.setAttribute("activate", "Customer " + cusId + " is Activated Successfully.");
			} catch(CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CustomerActivation.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Deactivate Customer": {
			String cusIdStr = request.getParameter("customerId");
			long cusId = Long.parseLong(cusIdStr);
			try {
				admin.deactivateCustomer(cusId);
				storage.setAllData();
				request.setAttribute("deactivate", "Customer " + cusId + " is De-activated Successfully.");
			} catch(CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/CustomerActivation.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Account Activation": {
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AccountActivation.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Activate Account": {
			String cusIdStr = request.getParameter("customerId");
			long cusId = Long.parseLong(cusIdStr);
			String accNoStr = request.getParameter("accountNo");
			long accNo = Long.parseLong(accNoStr);
			try {
				admin.activateAccount(cusId, accNo);
				storage.setAllData();
				request.setAttribute("activate", "Account " + accNo + " is Activated Successfully.");
			} catch(CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AccountActivation.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Deactivate Account": {
			String cusIdStr = request.getParameter("customerId");
			long cusId = Long.parseLong(cusIdStr);
			String accNoStr = request.getParameter("accountNo");
			long accNo = Long.parseLong(accNoStr);
			try {
				admin.deactivateAccount(cusId, accNo);
				storage.setAllData();
				request.setAttribute("deactivate", "Account " + accNo + " is De-activated Successfully.");
			} catch(CustomException e) {
				request.setAttribute("error", e.getMessage());
			}
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AccountActivation.jsp");
			reqDp.include(request, responce);
			break;
		}
		case "Edit Admin Details":{
			long userId = (long)session.getAttribute("userId");
			UserDetails userObj = userDetailsMap.get(userId);
			request.setAttribute("customerId", userObj.getUserId());
			request.setAttribute("name", userObj.getUserName());
			request.setAttribute("dob", userObj.getDOB());
			request.setAttribute("mail", userObj.geteMail());
			request.setAttribute("mobile", userObj.getMobileNum());
			
			session.setAttribute("email", userObj.geteMail());
			session.setAttribute("mobNum", userObj.getMobileNum());
			
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AdminProfile.jsp");
			reqDp.forward(request, responce);
			break;
		}
		case "Update Admin Details": {
			long userId = (long) session.getAttribute("userId");
			String eMail = request.getParameter("email");
			String mobile = request.getParameter("mobile");
			UserDetails user = new UserDetails();
			user.setUserId(userId);
			user.seteMail(eMail);
			user.setMobileNum(mobile);
			
			String oldMail = (String) session.getAttribute("email");
			String oldNum = (String) session.getAttribute("mobNum");
			
			if(!(user.geteMail().equals(oldMail) && user.getMobileNum().equals(oldNum))) {
				try {
					admin.editAdminInfo(userId, user);
					request.setAttribute("success", "Profile Edited Successfully.");
					storage.setAllData();
				} catch(CustomException e) {
					request.setAttribute("error", e.getMessage());
				}
			} else {
				UserDetails userObj = userDetailsMap.get(userId);
				request.setAttribute("customerId", userObj.getUserId());
				request.setAttribute("name", userObj.getUserName());
				request.setAttribute("dob", userObj.getDOB());
				request.setAttribute("mail", userObj.geteMail());
				request.setAttribute("mobile", userObj.getMobileNum());
				session.setAttribute("email", userObj.geteMail());
				session.setAttribute("mobNum", userObj.getMobileNum());
				RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AdminProfile.jsp");
				reqDp.forward(request, responce);
			}
			// populate
			long adminId = (long)session.getAttribute("userId");
			Map<Long,UserDetails> userMap = storage.getUserDetails();
			UserDetails userObj = userMap.get(adminId);
			request.setAttribute("customerId", userObj.getUserId());
			request.setAttribute("name", userObj.getUserName());
			request.setAttribute("dob", userObj.getDOB());
			request.setAttribute("mail", userObj.geteMail());
			request.setAttribute("mobile", userObj.getMobileNum());
			System.out.println(userObj.getUserName());
			RequestDispatcher reqDp = request.getRequestDispatcher("/jsp/AdminActions/AdminProfile.jsp");
			reqDp.include(request, responce);
			break;
		}
		default: 
			break;
		}
	}

}
