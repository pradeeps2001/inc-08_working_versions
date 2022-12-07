package com.banking.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

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
public class AjaxServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public AjaxServlet() {	

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

		HttpSession session = request.getSession(true);
		
		responce.setContentType("text/html");
		
		String data;
		JSONObject jsonObject;
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
			jb.append(line);
		} catch (Exception e) { 
			
		}
		try {
			jsonObject =  new JSONObject(jb.toString());
			data = jsonObject.optString("name");
			System.out.println("Function : "+data);
		} catch (JSONException e) {
			throw new IOException("Error parsing JSON request string",e.getCause());
		}
		
		switch (data) {
		case "Login": {
			
			String userIdStr = jsonObject.optString("custId");
			String pass = jsonObject.optString("pass");
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
							jsonObject.accumulate("url", "views/CustomerPage.html");
							String json = jsonObject.toString();
							responce.getWriter().write(json);

						} else {
							jsonObject.accumulate("error", "Customer is not Active.");
							jsonObject.accumulate("url", "/BankingAjax");
							String json = jsonObject.toString();
							responce.getWriter().write(json);
						}

					} else if(userObj.getRole().equals("ADMIN")) {
						jsonObject.accumulate("url", "views/AdminPage.html");
						String json = jsonObject.toString();
						responce.getWriter().write(json);
					}

					else {
						jsonObject.accumulate("error", "Invalid Login Credentials");
						jsonObject.accumulate("url", "/BankingAjax");
						String json = jsonObject.toString();
						responce.getWriter().write(json);
					}

				} else {
					jsonObject.accumulate("error", "Invalid Login Credentials");
					jsonObject.accumulate("url", "/BankingAjax");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			} else {
				jsonObject.accumulate("error", "Invalid Login Credentials");
				jsonObject.accumulate("url", "/BankingAjax");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		// customer menu
		case "Home": {
			storage.setAllData();
			Map<Long,AccountDetails> balanceMap = accountDetailsMap.get(session.getAttribute("userId"));
			jsonObject =  new JSONObject(balanceMap);
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Account Info":{
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			jsonObject =  new JSONObject(cusAccMap);
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Deposit Cash":{
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			jsonObject =  new JSONObject();
			jsonObject.accumulate("accountList", cusAccMap.keySet().toArray());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Withdraw Cash":{
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			jsonObject =  new JSONObject();
			jsonObject.accumulate("accountList", cusAccMap.keySet().toArray());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Proceed": {
			long userId = (long) session.getAttribute("userId");
			String mode = jsonObject.optString("mode");
			String accNoStr = jsonObject.optString("accNo");
			long accNo = Long.parseLong(accNoStr);
			String amountStr = jsonObject.optString("amount");
			double amount = Double.parseDouble(amountStr);
			if(mode.equals("Deposit")) {
				try {
					customer.deposit(userId, accNo, amount);
					System.out.println("Deposit "+amount);
					jsonObject = new JSONObject();
					jsonObject.accumulate("success", "Deposit Successful.");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				} catch (CustomException e) {
					jsonObject = new JSONObject();
					jsonObject.accumulate("error", e.getMessage());
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			} else if(mode.equals("Withdraw")) {
				try {
					customer.requestWithdraw(userId, accNo, amount);
					System.out.println("Withdraw requested for "+amount);
					jsonObject = new JSONObject();
					jsonObject.accumulate("success", "Withdraw Requested.");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				} catch(CustomException e) {
					jsonObject = new JSONObject();
					jsonObject.accumulate("error", e.getMessage());
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			}
			storage.setAllData();
			break;
		}
		case "Transfer Cash":{
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			jsonObject =  new JSONObject();
			jsonObject.accumulate("accountList", cusAccMap.keySet().toArray());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Transfer": {
			long senderId = (long) session.getAttribute("userId");
			String senderAccStr = jsonObject.optString("accNo");
			long senderAcc = Long.parseLong(senderAccStr);
			String receiverAccStr = jsonObject.optString("receiver");
			long receiverAcc = Long.parseLong(receiverAccStr);
			String amountStr = jsonObject.optString("amount");
			double amount = Double.parseDouble(amountStr);
			try {
				customer.transfer(senderId, senderAcc, receiverAcc, amount);
				System.out.println("Transferred "+amount+" to "+receiverAcc);
				jsonObject = new JSONObject();
				jsonObject.accumulate("success", "Money Transfer Successful.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch (CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			storage.setAllData();
			break;
		}
		case "Transaction History":{
			request.setAttribute("hide", "hide");
			Map<Long,AccountDetails> cusAccMap = accountDetailsMap.get((long)session.getAttribute("userId"));
			jsonObject =  new JSONObject();
			jsonObject.accumulate("accountList", cusAccMap.keySet().toArray());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Confirm": {
			storage.setAllData();
			String accDrop = jsonObject.optString("accNo");
			System.out.println(accDrop);
			long accNo = Long.parseLong(accDrop);
			Map<Long,TransactionDetails> transDetails = transactionDetailsMap.get(accNo);
			if(transDetails == null) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "No Recent Transactions.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} else {
				jsonObject =  new JSONObject(transDetails);
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Edit Customer Details":{
			long customerId = (long)session.getAttribute("userId");
			CustomerDetails customerObj = customerDetailsMap.get(customerId);
			jsonObject =  new JSONObject();
			jsonObject.put("custId", customerObj.getUserId());
			jsonObject.put("name", customerObj.getUserName());
			jsonObject.put("dob", customerObj.getDOB());
			jsonObject.put("mail", customerObj.getEmail());
			jsonObject.put("mobile", customerObj.getMobileNum());
			jsonObject.put("aadhar", customerObj.getAadharNum());
			jsonObject.put("pan", customerObj.getPanNum());
			session.setAttribute("email", customerObj.getEmail());
			session.setAttribute("mobNum", customerObj.getMobileNum());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Update Details": {
			long userId = (long) session.getAttribute("userId");
			String eMail = jsonObject.optString("email");
			String mobile = jsonObject.optString("mobile");
			UserDetails user = new UserDetails();
			user.setUserId(userId);
			user.setEmail(eMail);
			user.setMobileNum(mobile);
			
			String oldMail = (String) session.getAttribute("email");
			String oldNum = (String) session.getAttribute("mobNum");
			
			if(!(user.getEmail().equals(oldMail) && user.getMobileNum().equals(oldNum))) {
				try {
					customer.editCustomerInfo(userId, user);
					jsonObject = new JSONObject();
					jsonObject.accumulate("success", "Profile Edited Successfully.");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
					storage.setAllData();
				} catch(CustomException e) {
					System.out.println(e.getMessage());
					jsonObject = new JSONObject();
					jsonObject.accumulate("error", e.getMessage());
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			} else {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Cannot update original values.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		// common
		case "Password Change": {
			System.out.println("Entered Password Change.");
			break;
		}
		case "Change Password": {
			long userId = (Long) session.getAttribute("userId");
			UserDetails passObj = userDetailsMap.get(userId);
			String actualPass = passObj.getPassword();
			String inputOldPass = jsonObject.optString("oldPass");
			String inputNewPass = jsonObject.optString("newPass");
			if(inputOldPass.equals(actualPass)) {
				customer.changePassword(userId, inputNewPass);
				System.out.println("Password changed - "+inputNewPass);
				jsonObject = new JSONObject();
				jsonObject.accumulate("success", "Password Changed.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
				storage.setAllData();
			} else {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Old password is incorrect.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		// logout
		case "LOGOUT":{
			request.getSession(false);
			session.invalidate();
			break;
		}
		
//-------------------------------------------------------------------------------------------------------------------------------------------------------		
		// admin menu
		case "AdminHome":{
			storage.setAllData();
			Map<Long, RequestDetails> reqDetails = requestDetailsMap;
			jsonObject =  new JSONObject(reqDetails);
			System.out.println(reqDetails.size() + " pending requests.");
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Show Customer Info":{
			Map<Long, CustomerDetails> allCustomer = admin.showUser();
			jsonObject =  new JSONObject(allCustomer);
			System.out.println(allCustomer.size() + " total customers.");
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Show Customer": {
			String customerIdStr = jsonObject.optString("custId");
			long customerId = Long.parseLong(customerIdStr);
			if(customerDetailsMap.containsKey(customerId)) {
				Map<Long, CustomerDetails> map = admin.showUser(customerId);
				jsonObject = new JSONObject(map);
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			else {
				System.out.println("Show customer info failed.");
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Customer ID Not Found.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Show Account Info":{
			Map<Long, AccountDetails> accountMap = admin.showAccounts();
			jsonObject =  new JSONObject(accountMap);
			System.out.println(accountMap.size() + " total accounts.");
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Show Accounts": {
			String customerIdStr = jsonObject.optString("custId");
			long customerId = Long.parseLong(customerIdStr);
			if(accountDetailsMap.containsKey(customerId)) {
				Map<Long,AccountDetails> accounts = accountDetailsMap.get(customerId);
				jsonObject = new JSONObject(accounts);
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} else {
				System.out.println("Show customer info failed.");
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Customer ID Not Found.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Show Transaction Info":{
			request.setAttribute("hide", "hide");
			System.out.println("Entered into transactions.");
			break;
		}
		case "Show Statement": {
			String customerIdStr = jsonObject.optString("custId");
			long customerId = Long.parseLong(customerIdStr);
			String time = jsonObject.optString("time");
			System.out.println(time);
			if(customerDetailsMap.containsKey(customerId)) {
				Map<Long,Map<Long,TransactionDetails>> transMap = admin.getTransactionDetails(customerId, time);
				if(transMap.size()==0) {
					jsonObject = new JSONObject();
					jsonObject.accumulate("error", "No recent transactions.");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				} else {
					jsonObject = new JSONObject(transMap);
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			} else {
				System.out.println("Show Transaction Details Failed.");
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Customer ID Not Found.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Create Customer": {
			System.out.println("Entered Customer Creation.");
			break;
		}
		case "Add Customer": {
			CustomerDetails newCustomer = new CustomerDetails();
			String name = jsonObject.optString("userName");
			String dob = jsonObject.optString("dob");
			String email = jsonObject.optString("email");
			String mobileNum = jsonObject.optString("mobile");
			String password = jsonObject.optString("pass");
			String aadharStr = jsonObject.optString("aadhar");
			long aadhar = Long.parseLong(aadharStr);
			String pan = jsonObject.optString("pan");
			newCustomer.setUserName(name);
			newCustomer.setDOB(dob);
			newCustomer.setEmail(email);
			newCustomer.setMobileNum(mobileNum);
			newCustomer.setRole("CUSTOMER");
			newCustomer.setPassword(password);
			newCustomer.setAadharNum(aadhar);
			newCustomer.setPanNum(pan);
			newCustomer.setStatus("ACTIVE");
			try{
				long newUserId = admin.createCustomer(newCustomer);
				System.out.println("customer created");
				System.out.println(newUserId);
				storage.setAllData();
				jsonObject = new JSONObject();
				jsonObject.accumulate("success", "Customer Created with User ID "+newUserId+".");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch (CustomException e) {
				System.out.println(e.getMessage());
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Create Account":{
			System.out.println("Entered Account Creation.");
			break;
		}
		case "Add Account": {
			AccountDetails newAccount = new AccountDetails();
			String customerIdStr = jsonObject.optString("custId");
			long customerId = Long.parseLong(customerIdStr);
			String accType = jsonObject.optString("type");
			String branch = jsonObject.optString("branch");
			String balanceStr = jsonObject.optString("balance");
			double balance = Double.parseDouble(balanceStr);
			newAccount.setCustomerId(customerId);
			newAccount.setAccountType(accType.toUpperCase());
			newAccount.setBranch(branch.toUpperCase());
			newAccount.setBalance(balance);
			newAccount.setStatus("ACTIVE");
			try {
			long newAccNo = admin.createAccount(newAccount);
			storage.setAllData();
			jsonObject = new JSONObject();
			jsonObject.accumulate("success", "Account Created with Account No "+newAccNo+".");
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			} catch(CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Request Approval": {
			Map<Long, RequestDetails> allReq = requestDetailsMap;
			System.out.println(requestDetailsMap.size()+" pending withdrawals.");
			jsonObject =  new JSONObject(allReq);
			System.out.println(allReq.size() + " total pending requests.");
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Approve Table":{
			String tableIdStr = jsonObject.optString("cusId");
			long customerId = Long.parseLong(tableIdStr);
			String tableAccStr = jsonObject.optString("accNo");
			long accNo = Long.parseLong(tableAccStr);
			String tableReqStr = jsonObject.optString("reqId");
			long reqId = Long.parseLong(tableReqStr);
			admin.acceptRequest(customerId, accNo, reqId);
			storage.setAllData();
			break;
		}
		case "Reject Table":{
			String tableIdStr = jsonObject.optString("cusId");
			long customerId = Long.parseLong(tableIdStr);
			String tableAccStr = jsonObject.optString("accNo");
			long accNo = Long.parseLong(tableAccStr);
			String tableReqStr = jsonObject.optString("reqId");
			long reqId = Long.parseLong(tableReqStr);
			admin.rejectRequest(customerId, accNo, reqId);
			storage.setAllData();
			break;
		}
		case "Customer Activation": {
			System.out.println("Entered Customer Activation.");
			break;
		}
		case "Activate Customer": {
			String cusIdStr = jsonObject.optString("custId");
			long cusId = Long.parseLong(cusIdStr);
			try {
				admin.activateCustomer(cusId);
				System.out.println(cusId + "Customer Activated");
				storage.setAllData();
				jsonObject = new JSONObject();
				jsonObject.accumulate("activated", "Customer " + cusId + " is Activated Successfully.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch(CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Deactivate Customer": {
			String cusIdStr = jsonObject.optString("custId");
			long cusId = Long.parseLong(cusIdStr);
			try {
				admin.deactivateCustomer(cusId);
				System.out.println(cusId + "Customer Deactivated");
				storage.setAllData();
				jsonObject = new JSONObject();
				jsonObject.accumulate("deactivated", "Customer " + cusId + " is De-activated Successfully.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch(CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Account Activation": {
			System.out.println("Entered Account Activation.");
			break;
		}
		case "Activate Account": {
			String cusIdStr = jsonObject.optString("custId");
			long cusId = Long.parseLong(cusIdStr);
			String accNoStr = jsonObject.optString("accNo");
			long accNo = Long.parseLong(accNoStr);
			try {
				admin.activateAccount(cusId, accNo);
				System.out.println("Account Activated"+cusId);
				storage.setAllData();
				jsonObject = new JSONObject();
				jsonObject.accumulate("activated", "Account " + accNo + " is Activated Successfully.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch(CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Deactivate Account": {
			String cusIdStr = jsonObject.optString("custId");
			long cusId = Long.parseLong(cusIdStr);
			String accNoStr = jsonObject.optString("accNo");
			long accNo = Long.parseLong(accNoStr);
			try {
				admin.deactivateAccount(cusId, accNo);
				System.out.println("Account Deactivated"+cusId);
				storage.setAllData();
				jsonObject = new JSONObject();
				jsonObject.accumulate("deactivated", "Account " + accNo + " is De-activated Successfully.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			} catch(CustomException e) {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", e.getMessage());
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		case "Edit Admin Details":{
			long userId = (long)session.getAttribute("userId");
			UserDetails userObj = userDetailsMap.get(userId);
			jsonObject =  new JSONObject();
			jsonObject.put("custId", userObj.getUserId());
			jsonObject.put("name", userObj.getUserName());
			jsonObject.put("dob", userObj.getDOB());
			jsonObject.put("mail", userObj.getEmail());
			jsonObject.put("mobile", userObj.getMobileNum());
			session.setAttribute("email", userObj.getEmail());
			session.setAttribute("mobNum", userObj.getMobileNum());
			String json = jsonObject.toString();
			responce.getWriter().write(json);
			break;
		}
		case "Update Admin Details": {
			long userId = (long) session.getAttribute("userId");
			String eMail = jsonObject.optString("email");
			String mobile = jsonObject.optString("mobile");
			UserDetails user = new UserDetails();
			user.setUserId(userId);
			user.setEmail(eMail);
			user.setMobileNum(mobile);
			
			String oldMail = (String) session.getAttribute("email");
			String oldNum = (String) session.getAttribute("mobNum");
			
			if(!(user.getEmail().equals(oldMail) && user.getMobileNum().equals(oldNum))) {
				try {
					admin.editAdminInfo(userId, user);
					System.out.println("Admin Profile Edited Successfully.");
					jsonObject = new JSONObject();
					jsonObject.accumulate("success", "Profile Edited Successfully.");
					String json = jsonObject.toString();
					responce.getWriter().write(json);
					storage.setAllData();
				} catch(CustomException e) {
					jsonObject = new JSONObject();
					jsonObject.accumulate("error", e.getMessage());
					String json = jsonObject.toString();
					responce.getWriter().write(json);
				}
			} else {
				jsonObject = new JSONObject();
				jsonObject.accumulate("error", "Cannot update original values.");
				String json = jsonObject.toString();
				responce.getWriter().write(json);
			}
			break;
		}
		default: 
			break;
		}
	}

}
