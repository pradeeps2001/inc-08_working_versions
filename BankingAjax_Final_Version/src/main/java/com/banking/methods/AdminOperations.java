package com.banking.methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.RequestDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;
import com.banking.storageprotocol.AdminInterface;

import myutil.CustomException;
import myutil.MyUtil;

public class AdminOperations implements AdminInterface {

	private final String url = "jdbc:mysql://localhost/Lucas_Federal_Bank";
	private final String user = "root";
	private final String password = "Root@123";

	private Connection getConnection() throws CustomException {
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (SQLException e) {
			throw new CustomException("Error occured while establishing connection.", e);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public Map<Long, CustomerDetails> showUser(Long... userId) throws CustomException {

		Map<Long, CustomerDetails> map = new LinkedHashMap<>();
		int len = userId.length;

		if (len == 0) {
			String query = "SELECT USER_DETAILS.*,CUSTOMER_DETAILS.* FROM USER_DETAILS LEFT JOIN CUSTOMER_DETAILS "
					+ "ON USER_DETAILS.USER_ID=CUSTOMER_DETAILS.CUSTOMER_ID WHERE USER_DETAILS.ROLE = 'CUSTOMER'";
			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query)) {
				try (ResultSet result = prepStat.executeQuery();) {
					map = setCustomerDetails(result, map);
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		} else {
			String query = "SELECT USER_DETAILS.*,CUSTOMER_DETAILS.* FROM USER_DETAILS LEFT JOIN CUSTOMER_DETAILS "
					+ "ON USER_DETAILS.USER_ID=CUSTOMER_DETAILS.CUSTOMER_ID WHERE USER_DETAILS.ROLE = 'CUSTOMER' AND USER_ID = ?";
			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query)) {
				for (Long id : userId) {
					if (!hasCustomerId(id)) {
						throw new CustomException("No User ID Found");
					}
					prepStat.setLong(1, id);
					try (ResultSet result = prepStat.executeQuery();) {
						map = setCustomerDetails(result, map);
					}
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		}
		return map;
	}

	public Map<Long, UserDetails> showAllUser() throws CustomException {

		Map<Long, UserDetails> map = new LinkedHashMap<>();

		String query = "SELECT USER_DETAILS.*,CUSTOMER_DETAILS.* FROM USER_DETAILS LEFT JOIN CUSTOMER_DETAILS "
				+ "ON USER_DETAILS.USER_ID=CUSTOMER_DETAILS.CUSTOMER_ID";
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query)) {
			try (ResultSet result = prepStat.executeQuery();) {
				map = setUserDetails(result, map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while establishing connection.", e);
		}
		return map;
	}

	public Map<Long, UserDetails> setUserDetails(ResultSet result, Map<Long, UserDetails> inputMap)
			throws CustomException {

		try {
			while (result.next()) {
				UserDetails obj = new UserDetails();
				obj.setUserId(result.getLong("USER_ID"));
				obj.setUserName(result.getString("NAME"));
				obj.setDOB(result.getString("DOB"));
				obj.setEmail(result.getString("E_MAIL"));
				obj.setMobileNum(result.getString("MOBILE"));
				obj.setRole(result.getString("ROLE"));
				obj.setPassword(result.getString("PASSWORD"));
				inputMap.put(obj.getUserId(), obj);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return inputMap;
	}

	public Map<Long, CustomerDetails> setCustomerDetails(ResultSet result, Map<Long, CustomerDetails> inputMap)
			throws CustomException {

		try {
			while (result.next()) {
				CustomerDetails obj = new CustomerDetails();
				obj.setUserId(result.getLong("USER_ID"));
				obj.setUserName(result.getString("NAME"));
				obj.setDOB(result.getString("DOB"));
				obj.setEmail(result.getString("E_MAIL"));
				obj.setMobileNum(result.getString("MOBILE"));
				obj.setRole(result.getString("ROLE"));
				obj.setPassword(result.getString("PASSWORD"));
				obj.setAadharNum(result.getLong("AADHAAR_NUMBER"));
				obj.setPanNum(result.getString("PAN_NUMBER"));
				inputMap.put(obj.getUserId(), obj);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return inputMap;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public Map<Long, AccountDetails> showAccounts(Long... customerId) throws CustomException {

		Map<Long, AccountDetails> map = new LinkedHashMap<>();
		int len = customerId.length;
		if (len == 0) {
			String query = "SELECT * FROM ACCOUNT_DETAILS";

			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query)) {
				try (ResultSet result = prepStat.executeQuery();) {
					map = setAccountDetails(result, map);
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		} else {
			String query = "SELECT * FROM ACCOUNT_DETAILS WHERE CUSTOMER_ID = ?";
			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query)) {
				for (Long id : customerId) {
					if (!hasCustomerId(id)) {
						throw new CustomException("No User ID Found");
					}
					prepStat.setLong(1, id);
					try (ResultSet result = prepStat.executeQuery();) {
						map = setAccountDetails(result, map);
					}
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		}
		return map;
	}

	public Map<Long, AccountDetails> setAccountDetails(ResultSet result, Map<Long, AccountDetails> inputMap)
			throws CustomException {

		try {
			while (result.next()) {
				AccountDetails obj = new AccountDetails();
				obj.setCustomerId(result.getLong("CUSTOMER_ID"));
				obj.setAccountNo(result.getLong("ACCOUNT_NO"));
				obj.setAccountType(result.getString("ACCOUNT_TYPE"));
				obj.setBranch(result.getString("BRANCH"));
				obj.setBalance(result.getDouble("BALANCE"));
				obj.setStatus(result.getString("STATUS"));
				inputMap.put(obj.getAccountNo(), obj);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return inputMap;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public long createCustomer(CustomerDetails customer) throws CustomException {

		// changing date format to dd-mm-yyyy
		String nonFormatDate = customer.getDOB();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Date resultDate = null;
		try {
			resultDate = formater.parse(nonFormatDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat newFormater = new SimpleDateFormat("dd-MM-yyyy");
		String dob = newFormater.format(resultDate);
		String userQuery = "INSERT INTO USER_DETAILS (NAME, DOB, E_MAIL, MOBILE, ROLE, PASSWORD) VALUES (?,?,?,?,?,?)";
		String customerQuery = "INSERT INTO CUSTOMER_DETAILS (CUSTOMER_ID,AADHAAR_NUMBER,PAN_NUMBER,STATUS) VALUES (?,?,?,?)";
		long generatedId = 0;

		if (checkEmail(customer.getEmail())) {
			if (checkMobileNo(customer.getMobileNum())) {
				if (checkAadhar(customer.getAadharNum())) {
					if (checkPan(customer.getPanNum())) {
						if (checkPassword(customer.getPassword())) {
							try (Connection con = getConnection();) {
								Savepoint save = null;
								con.setAutoCommit(false);
								try (PreparedStatement prepStat1 = con.prepareStatement(userQuery,
										Statement.RETURN_GENERATED_KEYS);
										PreparedStatement prepStat2 = con.prepareStatement(customerQuery);) {
									prepStat1.setString(1, customer.getUserName());
									prepStat1.setString(2, dob);
									prepStat1.setString(3, customer.getEmail());
									prepStat1.setString(4, customer.getMobileNum());
									prepStat1.setString(5, customer.getRole());
									prepStat1.setString(6, customer.getPassword());
									prepStat1.addBatch();
									prepStat1.execute();
									save = con.setSavepoint();
									try (ResultSet result = prepStat1.getGeneratedKeys()) {
										while (result.next()) {
											generatedId = result.getLong(1);
											prepStat2.setLong(1, generatedId);
										}
									}
									prepStat2.setLong(2, customer.getAadharNum());
									prepStat2.setString(3, customer.getPanNum());
									prepStat2.setString(4, customer.getStatus());
									prepStat2.addBatch();
									prepStat2.execute();
									con.commit();
								} catch (SQLException e) {
									try {
										con.rollback(save);
									} catch (SQLException e1) {
										throw new CustomException("Rollback Failed", e1);
									}
									throw new CustomException("Duplicate entry found.", e);
								}
							} catch (SQLException e3) {
								throw new CustomException("Failed to establish connection.", e3);
							}
						} else {
							throw new CustomException("A minimum 8 characters password should contain an uppercase and "
									+ "lowercase letters, atleast one number and special characters are required.");
						}
					} else {
						throw new CustomException("Invalid PAN Number.");
					}
				} else {
					throw new CustomException("Invalid Aadhaar Number.");
				}
			} else {
				throw new CustomException("Mobile number should contain only 10 numbers.");
			}
		} else {
			throw new CustomException("You have entered an incorrect email.");
		}
		return generatedId;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public long createAccount(AccountDetails account) throws CustomException {

		String userQuery = "INSERT INTO ACCOUNT_DETAILS (CUSTOMER_ID, ACCOUNT_TYPE, BRANCH, BALANCE, STATUS) VALUES (?,?,?,?,?)";
		long generatedAccount = 0;
		if (hasUserId(account.getCustomerId())) {
			try (Connection con = getConnection(); PreparedStatement prepStat1 = con.prepareStatement(userQuery,
					Statement.RETURN_GENERATED_KEYS);) {
				prepStat1.setLong(1, account.getCustomerId());
				prepStat1.setString(2, account.getAccountType());
				prepStat1.setString(3, account.getBranch());
				prepStat1.setDouble(4, account.getBalance());
				prepStat1.setString(5, account.getStatus());
				prepStat1.addBatch();
				prepStat1.execute();
				try (ResultSet result = prepStat1.getGeneratedKeys()) {
					while (result.next()) {
						generatedAccount = result.getLong(1);
					}
				}

			} catch (SQLException e) {
				throw new CustomException("Error occured while execution of prepared statement.", e);
			}
		} else {
			throw new CustomException("Cannot create new account. UserId does not exist.");
		}
		return generatedAccount;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public Map<Long, Map<Long, TransactionDetails>> getTransactionDetails(long customerId, String time,
			Long... accounts) throws CustomException {

		Map<Long, Map<Long, TransactionDetails>> map = new LinkedHashMap<>();
		int len = accounts.length;
		long beforeAWeek = beforeWeekTime(time);

		if (len == 0) {
			String query = "SELECT * FROM TRANSACTION_DETAILS WHERE CUSTOMER_ID = ? AND TIME > ? ORDER BY TIME DESC";
			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
				if (!hasCustomerId(customerId)) {
					throw new CustomException("No User ID Found");
				}
				prepStat.setLong(1, customerId);
				prepStat.setLong(2, beforeAWeek);
				try (ResultSet result = prepStat.executeQuery();) {
					map = transactionPrintRset(result, map);
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while execution of prepared statement.", e);
			}
		} else {
			String query = "SELECT * FROM TRANSACTION_DETAILS WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ? AND TIME > ? ORDER BY TIME DESC limit 5";

			try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
				if (!hasCustomerId(customerId)) {
					throw new CustomException("No User ID Found");
				}
				prepStat.setLong(1, customerId);
				for (Long accNo : accounts) {
					if (!hasAccount(accNo)) {
						throw new CustomException("No Account Found");
					}
					prepStat.setLong(2, accNo);
					try (ResultSet result = prepStat.executeQuery();) {
						map = transactionPrintRset(result, map);
					}
				}
				prepStat.setLong(3, beforeAWeek);
			} catch (SQLException e) {
				throw new CustomException("Error occured while execution of prepared statement.", e);
			}
		}
		return map;
	}

	public Map<Long, Map<Long, TransactionDetails>> transactionPrintRset(ResultSet result,
			Map<Long, Map<Long, TransactionDetails>> input) throws CustomException {

		Map<Long, TransactionDetails> innerMap = new LinkedHashMap<>();

		try {
			while (result.next()) {
				TransactionDetails obj = new TransactionDetails();
				obj.setUserId(result.getLong("CUSTOMER_ID"));
				obj.setAccountNo(result.getLong("ACCOUNT_NO"));
				obj.setTransferId(result.getLong("REFERENCE_ID"));
				obj.setReferenceId(result.getInt("TRANSACTION_ID"));
				obj.setTime(result.getLong("TIME"));
				obj.setMode(result.getString("MODE_OF_TRANSACTION"));
				obj.setType(result.getString("TYPE"));
				obj.setAmount(result.getDouble("AMOUNT"));
				obj.setReceivedFrom(result.getLong("RECEIVED_FROM"));
				obj.setSendTo(result.getLong("SEND_TO"));
				obj.setBalance(result.getDouble("CLOSING_BALANCE"));
				innerMap.put(result.getLong("REFERENCE_ID"), obj);
				input.put(result.getLong("ACCOUNT_NO"), innerMap);
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return input;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	public Map<Long, RequestDetails> getRequests(long customerId) throws CustomException {

		Map<Long, RequestDetails> map = new LinkedHashMap<>();
		String query = "SELECT * FROM REQUEST_DETAILS WHERE CUSTOMER_ID = ? AND REQUEST_STATUS = 'PENDING'";

		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, customerId);
			try (ResultSet result = prepStat.executeQuery();) {
				map = requestPrintRset(result, map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return map;
	}

	public Map<Long, RequestDetails> getAllRequests() throws CustomException {

		Map<Long, RequestDetails> map = new LinkedHashMap<>();
		String query = "SELECT * FROM REQUEST_DETAILS WHERE REQUEST_STATUS = 'PENDING'";

		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			try (ResultSet result = prepStat.executeQuery();) {
				map = requestPrintRset(result, map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured.", e);
		}
		return map;
	}

	public Map<Long, RequestDetails> requestPrintRset(ResultSet result, Map<Long, RequestDetails> map)
			throws CustomException {

		try {
			while (result.next()) {
				RequestDetails obj = new RequestDetails();
				obj.setRequestId(result.getLong("REQUEST_ID"));
				obj.setCustomerId(result.getLong("CUSTOMER_ID"));
				obj.setAccountNo(result.getLong("ACCOUNT_NO"));
				obj.setAmount(result.getDouble("AMOUNT"));
				obj.setReqTime(result.getLong("REQUEST_TIME"));
				obj.setReqStatus(result.getString("REQUEST_STATUS"));
				map.put(result.getLong("REQUEST_ID"), obj);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return map;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	// withdraw
	public void withdraw(long userId, long accNo, double amount) throws CustomException {

		AccountDetails oldBalance = getBalance(accNo);
		double newBalance = oldBalance.getBalance() - amount;

		MyUtil.validWithdraw(oldBalance.getBalance(), amount);

		String query = "UPDATE ACCOUNT_DETAILS SET BALANCE = ? WHERE ACCOUNT_NO = ?";
		String reference = "INSERT INTO TRANSACTION_REFERENCE (MODE) VALUES ('WITHDRAW')";
		String lastIndex = "SELECT REFERENCE_ID FROM TRANSACTION_REFERENCE ORDER BY REFERENCE_ID DESC LIMIT 1";
		int referenceId = 0;
		try (Connection con = getConnection(); PreparedStatement prepStat1 = con.prepareStatement(query);
				PreparedStatement prepStat2 = con.prepareStatement(reference);
				PreparedStatement prepStat3 = con.prepareStatement(lastIndex);) {
			prepStat1.setDouble(1, newBalance);
			prepStat1.setLong(2, accNo);
			prepStat1.addBatch();
			prepStat1.execute();
			prepStat2.execute();
			try (ResultSet result = prepStat3.executeQuery()) {
				while (result.next()) {
					referenceId = result.getInt("REFERENCE_ID");
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		updateTransaction(setWithdrawTransaction(userId, accNo, referenceId, amount, newBalance));
	}

	public TransactionDetails setWithdrawTransaction(long userId, long accNo, int reference, double amount,
			double balance) {

		TransactionDetails obj = new TransactionDetails();
		long time = getCurrentTime();
		obj.setUserId(userId);
		obj.setAccountNo(accNo);
		obj.setReferenceId(reference);
		obj.setTime(time);
		obj.setMode("WITHDRAW");
		obj.setType("DEBIT");
		obj.setAmount(amount);
		obj.setBalance(balance);
		return obj;
	}

	public void updateTransaction(TransactionDetails obj) throws CustomException {

		String query = "INSERT INTO TRANSACTION_DETAILS (CUSTOMER_ID,ACCOUNT_NO,REFERENCE_ID,TIME,MODE_OF_TRANSACTION,TYPE,AMOUNT,RECEIVED_FROM,SEND_TO,CLOSING_BALANCE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = getConnection(); PreparedStatement prepStat3 = con.prepareStatement(query);) {

			prepStat3.setLong(1, obj.getUserId());
			prepStat3.setLong(2, obj.getAccountNo());
			prepStat3.setLong(3, obj.getReferenceId());
			prepStat3.setLong(4, obj.getTime());
			prepStat3.setString(5, obj.getMode());
			prepStat3.setString(6, obj.getType());
			prepStat3.setDouble(7, obj.getAmount());
			prepStat3.setLong(8, obj.getReceivedFrom());
			prepStat3.setLong(9, obj.getSendTo());
			prepStat3.setDouble(10, obj.getBalance());
			prepStat3.addBatch();
			prepStat3.execute();
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public void acceptRequest(long customerId, long accNo, long reqId) throws CustomException {

		String money = "SELECT AMOUNT FROM REQUEST_DETAILS WHERE REQUEST_STATUS = 'PENDING' AND CUSTOMER_ID = ? AND ACCOUNT_NO = ? AND REQUEST_ID = ?";
		String query = "UPDATE REQUEST_DETAILS SET REQUEST_STATUS = ? WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ? AND REQUEST_ID = ?";
		try (Connection con = getConnection(); PreparedStatement prepStat1 = con.prepareStatement(money);
				PreparedStatement prepStat2 = con.prepareStatement(query);) {
			prepStat1.setLong(1, customerId);
			prepStat1.setLong(2, accNo);
			prepStat1.setLong(3, reqId);

			prepStat2.setString(1, "APPROVED");
			prepStat2.setLong(2, customerId);
			prepStat2.setLong(3, accNo);
			prepStat2.setLong(4, reqId);
			try (ResultSet result = prepStat1.executeQuery();) {
				while (result.next()) {
					double amount = result.getDouble("AMOUNT");
					withdraw(customerId, accNo, amount);
				}
			}
			prepStat2.execute();
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public void rejectRequest(long customerId, long accNo, long reqId) throws CustomException {

		String query = "UPDATE REQUEST_DETAILS SET REQUEST_STATUS = ? WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ? AND REQUEST_ID = ?";
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setString(1, "REJECTED");
			prepStat.setLong(2, customerId);
			prepStat.setLong(3, accNo);
			prepStat.setLong(4, reqId);
			prepStat.execute();
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	// Status Activation
	@Override
	public void activateCustomer(long customerId) throws CustomException {

		if (hasUserId(customerId)) {
			if (!isActive(customerId)) {
				String query = "UPDATE CUSTOMER_DETAILS SET STATUS = ? WHERE CUSTOMER_ID = ?";
				try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
					prepStat.setString(1, "ACTIVE");
					prepStat.setLong(2, customerId);
					prepStat.execute();
				} catch (SQLException e) {
					throw new CustomException("Error occured while execution of prepared statement.", e);
				}
			} else {
				throw new CustomException("Customer is already active.");
			}
		} else {
			throw new CustomException("Customer ID not found.");
		}
	}

	@Override
	public void deactivateCustomer(long customerId) throws CustomException {

		if (hasUserId(customerId)) {
			if (isActive(customerId)) {
				String query = "UPDATE CUSTOMER_DETAILS SET STATUS = ? WHERE CUSTOMER_ID = ?";
				try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
					prepStat.setString(1, "INACTIVE");
					prepStat.setLong(2, customerId);
					prepStat.execute();
				} catch (SQLException e) {
					throw new CustomException("Error occured while execution of prepared statement.", e);
				}
			} else {
				throw new CustomException("Customer is already inactive.");
			}
		} else {
			throw new CustomException("Customer ID not found.");
		}
	}

	@Override
	public void activateAccount(long customerId, long accNo) throws CustomException {

		if (hasCustomerId(customerId) && hasAccount(accNo)) {
			if (!isAccountActive(accNo)) {
				String query = "UPDATE ACCOUNT_DETAILS SET STATUS = ? WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ?";
				try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
					prepStat.setString(1, "ACTIVE");
					prepStat.setLong(2, customerId);
					prepStat.setLong(3, accNo);
					prepStat.execute();
				} catch (SQLException e) {
					throw new CustomException("Error occured while execution of prepared statement.", e);
				}
			} else {
				throw new CustomException("Account is already active.");
			}
		} else {
			throw new CustomException("Customer ID and Account Number doesn't match (or) invalid.");
		}
	}

	@Override
	public void deactivateAccount(long customerId, long accNo) throws CustomException {

		if (hasCustomerId(customerId) && hasAccount(accNo)) {
			if (isAccountActive(accNo)) {
				String query = "UPDATE ACCOUNT_DETAILS SET STATUS = ? WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ?";
				try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
					prepStat.setString(1, "INACTIVE");
					prepStat.setLong(2, customerId);
					prepStat.setLong(3, accNo);
					prepStat.execute();
				} catch (SQLException e) {
					throw new CustomException("Error occured while execution of prepared statement.", e);
				}
			} else {
				throw new CustomException("Account is already inactive.");
			}
		} else {
			throw new CustomException("Customer ID and Account Number doesn't match (or) invalid.");
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	// edit profile
	@Override
	public void editAdminInfo(long customerId, UserDetails user) throws CustomException {

		String mail = user.getEmail();
		String mobileNum = user.getMobileNum();

		if (mail.length() != 0) {
			if (mobileNum.length() != 0) {
				if (checkEmail(mail)) {
					if (checkMobileNo(mobileNum)) {
						String query = "UPDATE USER_DETAILS SET E_MAIL=?, MOBILE=? WHERE USER_ID=?;";
						try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
							prepStat.setString(1, mail);
							prepStat.setString(2, mobileNum);
							prepStat.setLong(3, customerId);
							prepStat.execute();
						} catch (SQLException e) {
							throw new CustomException("Duplicate entry found.", e);
						}
					} else {
						throw new CustomException("Mobile number should contain only 10 numbers.");
					}
				} else {
					throw new CustomException("You have entered an incorrect email.");
				}
			} else {
				throw new CustomException("Mobile number cannot be empty.");
			}
		} else {
			throw new CustomException("E-mail id cannot be empty.");
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	// get balance
	public AccountDetails getBalance(long accNo) throws CustomException {

		String query = "SELECT BALANCE FROM ACCOUNT_DETAILS WHERE ACCOUNT_NO = ?";
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, accNo);
			try (ResultSet result = prepStat.executeQuery();) {
				while (result.next()) {
					obj.setBalance(result.getDouble(1));
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return obj;
	}

	// current time
	public long getCurrentTime() {
		long milli = System.currentTimeMillis();
		return milli;
	}

	// get time before 7 day

	private Calendar resetTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	public long beforeWeekTime(String choice) {

		Calendar today = Calendar.getInstance();
		today = resetTime(today);

		Calendar timePeriod = Calendar.getInstance();
		if (choice.equals("1 Week")) {
			timePeriod.add(Calendar.DAY_OF_YEAR, -7);
			timePeriod = resetTime(timePeriod);
		} else if (choice.equals("1 Month")) {
			timePeriod.add(Calendar.MONTH, -1);
			timePeriod = resetTime(timePeriod);
		} else if (choice.equals("6 Month")) {
			timePeriod.add(Calendar.MONTH, -6);
			timePeriod = resetTime(timePeriod);
		}
		return timePeriod.getTimeInMillis();
	}

	public boolean hasUserId(long customerId) throws CustomException {

		String query = "SELECT CUSTOMER_ID FROM CUSTOMER_DETAILS";
		Map<Long, CustomerDetails> map = new LinkedHashMap<>();
		CustomerDetails obj = new CustomerDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			try (ResultSet result = prepStat.executeQuery()) {
				while (result.next()) {
					obj.setUserId(result.getLong("CUSTOMER_ID"));
					map.put(obj.getUserId(), obj);
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return map.containsKey(customerId);
	}

	public boolean hasCustomerId(long customerId) throws CustomException {

		String query = "SELECT CUSTOMER_ID FROM ACCOUNT_DETAILS";
		Map<Long, AccountDetails> map = new LinkedHashMap<>();
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			try (ResultSet result = prepStat.executeQuery()) {
				while (result.next()) {
					obj.setCustomerId(result.getLong("CUSTOMER_ID"));
					map.put(obj.getCustomerId(), obj);
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return map.containsKey(customerId);
	}

	public boolean hasAccount(long customerId) throws CustomException {

		String query = "SELECT ACCOUNT_NO FROM ACCOUNT_DETAILS";
		Map<Long, AccountDetails> map = new LinkedHashMap<>();
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			try (ResultSet result = prepStat.executeQuery()) {
				while (result.next()) {
					obj.setAccountNo(result.getLong("ACCOUNT_NO"));
					map.put(obj.getAccountNo(), obj);
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return map.containsKey(customerId);
	}

	public boolean isActive(long customerId) throws CustomException {

		String query = "SELECT STATUS FROM CUSTOMER_DETAILS WHERE CUSTOMER_ID = ?";
		CustomerDetails customer = new CustomerDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, customerId);
			try (ResultSet result = prepStat.executeQuery()) {
				while (result.next()) {
					customer.setStatus(result.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return customer.getStatus().equals("ACTIVE");
	}

	public boolean isAccountActive(long accNo) throws CustomException {

		String query = "SELECT STATUS FROM ACCOUNT_DETAILS WHERE ACCOUNT_NO = ?";
		AccountDetails account = new AccountDetails();
		try (Connection con = getConnection(); PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, accNo);
			try (ResultSet result = prepStat.executeQuery()) {
				while (result.next()) {
					account.setStatus(result.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement.", e);
		}
		return account.getStatus().equals("ACTIVE");
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// regex
	public boolean checkMobileNo(String number) throws CustomException {
		MyUtil.checkNull(number);
		String regex = "^\\d{10}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public boolean checkPassword(String inputPassword) throws CustomException {
		MyUtil.checkNull(inputPassword);
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputPassword);
		return matcher.matches();
	}

	public boolean checkEmail(String email) throws CustomException {
		MyUtil.checkNull(email);
		String regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean checkAadhar(Long input) throws CustomException {
		String aadhar = input.toString();
		String regex = "^\\d{12}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(aadhar);
		return matcher.matches();
	}

	public boolean checkPan(String input) throws CustomException {
		MyUtil.checkNull(input);
		String regex = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
}
