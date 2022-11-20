package com.banking.methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.banking.pojo.AccountDetails;
import com.banking.pojo.CustomerDetails;
import com.banking.pojo.TransactionDetails;
import com.banking.pojo.UserDetails;
import com.banking.storageprotocol.CustomerInterface;

import myutil.CustomException;
import myutil.MyUtil;

public class CustomerOperations implements CustomerInterface {

	private final String url = "jdbc:mysql://localhost/Lucas_Federal_Bank";
	private final String user = "root";
	private final String password = "Root@123";

	private Connection getConnection() throws CustomException {
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch(SQLException e) {
			throw new CustomException("Error occured while establishing connection.", e);
		}
	}

	// login
	public UserDetails getLoginDetails(long id) throws CustomException {

		String query = "SELECT * FROM USER_DETAILS WHERE USER_ID = ?";
		UserDetails obj = new CustomerDetails();

		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, id);
			try (ResultSet result = prepStat.executeQuery();) {
				obj = loginPrintresult(result);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." + e);
		}
		return obj;
	}

	public UserDetails loginPrintresult(ResultSet result) throws CustomException {

		UserDetails obj = new CustomerDetails();

		try {
			while(result.next()) {
				obj.setUserId(result.getInt("USER_ID"));
				obj.setUserName(result.getString("NAME"));
				obj.setRole(result.getString("ROLE"));
				//				obj.setStatus(result.getString("STATUS"));
				obj.setPassword(result.getString("PASSWORD"));
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured." , e);
		}
		return obj;
	}

	// user info
	@Override
	public Map<Long,CustomerDetails> getCustomerInfo(long id) throws CustomException{

		String query = "SELECT USER_DETAILS.*,CUSTOMER_DETAILS.* FROM USER_DETAILS JOIN CUSTOMER_DETAILS "
				+ "ON USER_DETAILS.USER_ID=CUSTOMER_DETAILS.CUSTOMER_ID WHERE USER_ID = ?";

		Map<Long,CustomerDetails> map = new LinkedHashMap<>();

		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);){
			prepStat.setLong(1, id);
			try (ResultSet result = prepStat.executeQuery();){
				map = userPrintresult(result,map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occurred while execution of prepared statement." , e);
		}
		return map;
	}

	public Map<Long,CustomerDetails> getAllCustomerInfo() throws CustomException{

		String query = "SELECT USER_DETAILS.*,CUSTOMER_DETAILS.* FROM USER_DETAILS JOIN CUSTOMER_DETAILS "
				+ "ON USER_DETAILS.USER_ID=CUSTOMER_DETAILS.CUSTOMER_ID";

		Map<Long,CustomerDetails> map = new LinkedHashMap<>();

		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);){
			try (ResultSet result = prepStat.executeQuery();){
				map = userPrintresult(result,map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occurred while execution of prepared statement." , e);
		}
		return map;
	}

	public Map<Long,CustomerDetails> userPrintresult(ResultSet result, Map<Long,CustomerDetails> map) throws CustomException {

		try {
			while(result.next()) {
				CustomerDetails obj = new CustomerDetails();
				obj.setUserId(result.getLong("USER_ID"));
				obj.setUserName(result.getString("NAME"));
				obj.setDOB(result.getString("DOB"));
				obj.seteMail(result.getString("E_MAIL"));
				obj.setMobileNum(result.getString("MOBILE"));
				obj.setRole(result.getString("ROLE"));
				obj.setPassword(result.getString("PASSWORD"));
				obj.setAadharNum(result.getLong("AADHAAR_NUMBER"));
				obj.setPanNum(result.getString("PAN_NUMBER"));
				obj.setStatus(result.getString("STATUS"));
				map.put(result.getLong(1), obj);
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured." , e);
		}
		return map;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// account info
	@Override
	public Map<Long,Map<Long,AccountDetails>> getAccountDetails(long customerId, Long... accountNo) throws CustomException {

		Map<Long,Map<Long,AccountDetails>> map = new LinkedHashMap<>();
		int len = accountNo.length;

		if(len == 0) {
			String query = "SELECT * FROM ACCOUNT_DETAILS WHERE CUSTOMER_ID = ?";
			try(PreparedStatement prepStat = getConnection().prepareStatement(query)){
				prepStat.setLong(1, customerId);
				try (ResultSet result = prepStat.executeQuery();){
					map = accountPrintresult(result,map);
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		} else {
			String query = "SELECT * FROM ACCOUNT_DETAILS WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ?";
			try(PreparedStatement prepStat = getConnection().prepareStatement(query)){
				prepStat.setLong(1, customerId);
				for(Long id : accountNo) {
					prepStat.setLong(2, id);
					try (ResultSet result = prepStat.executeQuery();){
						map = accountPrintresult(result,map);
					}
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while establishing connection.", e);
			}
		}
		return map;
	}

	public Map<Long,Map<Long,AccountDetails>> getAllAccountDetails() throws CustomException {

		Map<Long,Map<Long,AccountDetails>> map = new LinkedHashMap<>();

		String query = "SELECT * FROM ACCOUNT_DETAILS";
		try(PreparedStatement prepStat = getConnection().prepareStatement(query)){
			try (ResultSet result = prepStat.executeQuery();){
				map = accountPrintresult(result,map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while establishing connection.", e);
		}
		return map;
	}

	public Map<Long,Map<Long,AccountDetails>> accountPrintresult(ResultSet result, Map<Long,Map<Long,AccountDetails>> inputMap) throws CustomException {

		int i = 0;

		try {
			while(result.next()) {
				Map<Long,AccountDetails> innerMap = new LinkedHashMap<>();
				AccountDetails obj = new AccountDetails();
				obj.setCustomerId(result.getLong(1));
				obj.setAccountNo(result.getLong(2));
				obj.setAccountType(result.getString(3));
				obj.setBranch(result.getString(4));
				obj.setBalance(result.getDouble(5));
				obj.setStatus(result.getString(6));
				if(i>0 && inputMap.containsKey(obj.getCustomerId())) {
					inputMap.get(obj.getCustomerId()).put(obj.getAccountNo(), obj);
				} else {
					innerMap.put(result.getLong(2), obj);
					inputMap.put(result.getLong(1), innerMap);
				}
				i++;
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured." , e);
		}
		return inputMap;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// transaction details
	@Override
	public Map<Long,Map<Long,TransactionDetails>> getTransactionDetails(long customerId, long accNo) throws CustomException {

		String query = "SELECT * FROM TRANSACTION_DETAILS WHERE CUSTOMER_ID = ? AND ACCOUNT_NO = ? AND TIME > ? ORDER BY TIME DESC";
		long beforeAWeek = beforeWeekTime(getCurrentTime());
		Map<Long,Map<Long,TransactionDetails>> map = new LinkedHashMap<>();

		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);){
			prepStat.setLong(1, customerId);
			prepStat.setLong(2, accNo);
			prepStat.setLong(3, beforeAWeek);
			try (ResultSet result = prepStat.executeQuery();){
				map = transactionPrintresult(result, map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return map;
	}

	public Map<Long,Map<Long,TransactionDetails>> getAllTransactionDetails() throws CustomException {

		String query = "SELECT * FROM TRANSACTION_DETAILS WHERE TIME > ? ORDER BY TIME DESC";
		long beforeAWeek = beforeWeekTime(getCurrentTime());
		Map<Long,Map<Long,TransactionDetails>> map = new LinkedHashMap<>();

		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);){
			prepStat.setLong(1, beforeAWeek);
			try (ResultSet result = prepStat.executeQuery();){
				map = transactionPrintresult(result, map);
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return map;
	}

	public Map<Long,Map<Long,TransactionDetails>> transactionPrintresult(ResultSet result, Map<Long,Map<Long,TransactionDetails>> inputMap) throws CustomException {

		int i = 0;

		try {
			while(result.next()) {
				Map<Long,TransactionDetails> innerMap = new LinkedHashMap<>();
				TransactionDetails obj = new TransactionDetails();
				obj.setUserId(result.getLong("CUSTOMER_ID"));
				obj.setAccountNo(result.getLong("ACCOUNT_NO"));
				obj.setReferenceId(result.getInt("REFERENCE_ID"));
				obj.setTransferId(result.getLong("TRANSACTION_ID"));
				obj.setTime(result.getLong("TIME"));
				obj.setMode(result.getString("MODE_OF_TRANSACTION"));
				obj.setType(result.getString("TYPE"));
				obj.setAmount(result.getDouble("AMOUNT"));
				obj.setReceivedFrom(result.getLong("RECEIVED_FROM"));
				obj.setSendTo(result.getLong("SEND_TO"));
				obj.setBalance(result.getDouble("CLOSING_BALANCE"));
				if(i>0 && inputMap.containsKey(obj.getAccountNo())) {
					inputMap.get(obj.getAccountNo()).put(obj.getReferenceId(), obj);
				} else {
					innerMap.put(obj.getReferenceId(), obj);
					inputMap.put(obj.getAccountNo(), innerMap);
				}
				i++;
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured." , e);
		}
		return inputMap;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// deposit
	@Override
	public void deposit(long userId, long accNo, double amount) throws CustomException {

		if(isAccountActive(accNo)) {
			MyUtil.validDeposit(amount);
			AccountDetails oldBalance = getBalance(accNo);
			double newBalance = oldBalance.getBalance() + amount;
			String query = "UPDATE ACCOUNT_DETAILS SET BALANCE = ? WHERE ACCOUNT_NO = ?";
			String reference = "INSERT INTO TRANSACTION_REFERENCE (MODE) VALUES ('DEPOSIT')";
			String lastIndex = "SELECT REFERENCE_ID FROM TRANSACTION_REFERENCE ORDER BY REFERENCE_ID DESC LIMIT 1";
			int referenceId = 0;

			try (Connection con = getConnection(); 
					PreparedStatement prepStat = con.prepareStatement(query);
					PreparedStatement prepStat3 = getConnection().prepareStatement(reference);
					PreparedStatement prepStat4 = getConnection().prepareStatement(lastIndex);) {
				prepStat.setDouble(1, newBalance);
				prepStat.setLong(2, accNo);
				prepStat.addBatch();
				prepStat.execute();
				prepStat3.execute();
				try (ResultSet result = prepStat4.executeQuery()){
					while(result.next()) {
						referenceId = result.getInt(1);
					}
				}
			} catch (SQLException e) {
				throw new CustomException("Error occured while execution of prepared statement." , e);
			}
			updateTransaction(setDepositTransaction(userId, accNo, referenceId, amount, newBalance));
		} else {
			throw new CustomException("Cannot Deposit. This Account is inactive.");
		}
	}

	public TransactionDetails setDepositTransaction(long userId, long accNo, int reference, double amount, double balance) {

		TransactionDetails obj = new TransactionDetails();
		long time = getCurrentTime();
		obj.setUserId(userId);
		obj.setAccountNo(accNo);
		obj.setReferenceId(reference);
		obj.setTime(time);
		obj.setMode("DEPOSIT");
		obj.setType("CREDIT");
		obj.setAmount(amount);
		obj.setBalance(balance);
		return obj;
	}

	@Override
	public void requestWithdraw(long customerId, long accNo, double amount) throws CustomException {

		if(isAccountActive(accNo)) {
			AccountDetails oldBalance = getBalance(accNo);
			MyUtil.validWithdraw(oldBalance.getBalance(), amount);

			String query = "INSERT INTO REQUEST_DETAILS (CUSTOMER_ID,ACCOUNT_NO,AMOUNT,REQUEST_TIME,REQUEST_STATUS) VALUES (?,?,?,?,?)";
			long time = getCurrentTime();

			try(PreparedStatement prepStat = getConnection().prepareStatement(query)){;
			prepStat.setLong(1, customerId);
			prepStat.setLong(2, accNo);
			prepStat.setDouble(3, amount);
			prepStat.setLong(4, time);
			prepStat.setString(5, "PENDING");
			prepStat.execute();
			} catch (SQLException e) {
				throw new CustomException("Error occured while execution of prepared statement." , e);
			}
		} else {
			throw new CustomException("Cannot Withdraw. This Account is inactive.");
		}
	}

	public TransactionDetails setWithdrawTransaction(long userId, long accNo, int reference, double amount, double balance) {

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
	//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// transfer
	@Override
	public void transfer(long senderId, long senderAcc, long receiverAcc, double amount) throws CustomException {

		if(isAccountActive(senderAcc)) {
			if(hasAccount(receiverAcc)) {
				AccountDetails senderObj = getBalance(senderAcc);
				AccountDetails receiverObj = getBalance(receiverAcc);
				double senderBalance = senderObj.getBalance() - amount;
				double receiverBalance = receiverObj.getBalance() + amount;

				MyUtil.validTransfer(senderObj.getBalance(), amount, senderAcc, receiverAcc);
				AccountDetails receiver = getId(receiverAcc);
				String debitQuery = "UPDATE ACCOUNT_DETAILS SET BALANCE = ? WHERE ACCOUNT_NO = ?";
				String creditQuery = "UPDATE ACCOUNT_DETAILS SET BALANCE = ? WHERE ACCOUNT_NO = ?";
				String reference = "INSERT INTO TRANSACTION_REFERENCE (MODE) VALUES ('TRANSFER')";
				String lastIndex = "SELECT REFERENCE_ID FROM TRANSACTION_REFERENCE ORDER BY REFERENCE_ID DESC LIMIT 1";
				int referenceId = 0;

				try (Connection con = getConnection(); 
						PreparedStatement prepStat1 = con.prepareStatement(debitQuery);
						PreparedStatement prepStat2 = con.prepareStatement(creditQuery);
						PreparedStatement prepStat3 = con.prepareStatement(reference);
						PreparedStatement prepStat4 = con.prepareStatement(lastIndex);){
					prepStat1.setDouble(1, senderBalance);
					prepStat1.setLong(2, senderAcc);
					prepStat1.addBatch();
					prepStat1.execute();
					prepStat2.setDouble(1, receiverBalance);
					prepStat2.setLong(2, receiverAcc);
					prepStat2.addBatch();
					prepStat2.execute();
					prepStat3.execute();
					try (ResultSet result = prepStat4.executeQuery()){
						while(result.next()) {
							referenceId = result.getInt(1);
						}
					}
				} catch (SQLException e) {
					throw new CustomException("Error occured while execution of prepared statement." , e);
				}
				updateTransaction(setSenderTransaction(senderId, senderAcc, receiverAcc, referenceId, amount, senderBalance));
				updateTransaction(setReceiverTransaction(receiver.getCustomerId(), senderAcc, receiverAcc, referenceId, amount, receiverBalance));
			} else {
				throw new CustomException("Receiver account not found. Try again.");
			}
		} else {
			throw new CustomException("Cannot Transfer. This Account is inactive.");
		}
	}

	public TransactionDetails setSenderTransaction(long userId, long SenderAccNo, long ReceiverAccNo, int reference, double amount, double balance) {

		TransactionDetails obj = new TransactionDetails();
		long time = getCurrentTime();
		obj.setUserId(userId);
		obj.setAccountNo(SenderAccNo);
		obj.setReferenceId(reference);
		obj.setTime(time);
		obj.setMode("TRANSFER");
		obj.setType("DEBIT");
		obj.setAmount(amount);
		obj.setSendTo(ReceiverAccNo);
		obj.setBalance(balance);
		return obj;
	}

	public TransactionDetails setReceiverTransaction(long userId, long SenderAccNo, long ReceiverAccNo, int reference, double amount, double balance) {

		TransactionDetails obj = new TransactionDetails();
		long time = getCurrentTime();
		obj.setUserId(userId);
		obj.setAccountNo(ReceiverAccNo);
		obj.setReferenceId(reference);
		obj.setTime(time);
		obj.setMode("TRANSFER");
		obj.setType("CREDIT");
		obj.setAmount(amount);
		obj.setReceivedFrom(SenderAccNo);
		obj.setBalance(balance);
		return obj;
	}

	@Override
	public void updateTransaction(TransactionDetails obj) throws CustomException {

		String query = "INSERT INTO TRANSACTION_DETAILS (CUSTOMER_ID,ACCOUNT_NO,REFERENCE_ID,TIME,MODE_OF_TRANSACTION,TYPE,AMOUNT,RECEIVED_FROM,SEND_TO,CLOSING_BALANCE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepStat3 = getConnection().prepareStatement(query);){

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
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
	}

	//----------------------------------------------------------------------------------------------------------------------------------------------------------

	// Edit customer info
	@Override
	public void editCustomerInfo(long customerId, UserDetails user) throws CustomException {

		// changing date format to dd-mm-yyyy
		//		String nonFormatDate = user.getDOB();
		//		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		//		Date resultDate = null;
		//		try {
		//			resultDate = formater.parse(nonFormatDate);
		//		} catch (ParseException e) {
		//			e.printStackTrace();
		//		}
		//		SimpleDateFormat newFormater = new SimpleDateFormat("dd-MM-yyyy");
		//		String dob = newFormater.format(resultDate);

		String mail = user.geteMail();
		String mobileNum = user.getMobileNum();

		if(mail.length() != 0) {
			if(mobileNum.length() != 0) {
				if(checkEmail(mail)) {
					if(checkMobileNo(mobileNum)) {
						String query = "UPDATE USER_DETAILS SET E_MAIL=?, MOBILE=? WHERE USER_ID=?;";
						try (Connection con = getConnection(); 
								PreparedStatement prepStat = con.prepareStatement(query);) {
							prepStat.setString(1, user.geteMail());
							prepStat.setString(2, user.getMobileNum());
							prepStat.setLong(3, customerId);
							prepStat.execute();
						}catch (SQLException e) {
							throw new CustomException("Duplicate entry found." , e);
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

	public AccountDetails getId(long accNo) throws CustomException {

		String query = "SELECT CUSTOMER_ID FROM ACCOUNT_DETAILS WHERE ACCOUNT_NO = ?";
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, accNo);
			try(ResultSet result = prepStat.executeQuery()){
				while(result.next()) {
					obj.setCustomerId(result.getLong(1));
				}
			}

		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return obj;
	}

	// balance
	@Override
	public AccountDetails getBalance(long accNo) throws CustomException {

		String query = "SELECT BALANCE FROM ACCOUNT_DETAILS WHERE ACCOUNT_NO = ?";
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);){
			prepStat.setLong(1, accNo);
			try (ResultSet result = prepStat.executeQuery();){
				while(result.next()) {
					obj.setBalance(result.getDouble(1));
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return obj;
	}

	// change password
	@Override
	public void changePassword(Long userId, String newPassword) throws CustomException {

		String query = "UPDATE USER_DETAILS SET PASSWORD=? WHERE USER_ID=?;";
		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setString(1, newPassword);
			prepStat.setLong(2, userId);
			prepStat.execute();
		}catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
	}

	// current time
	public long getCurrentTime() {
		long milli = System.currentTimeMillis();
		return milli;
	}

	// get time before 7 day

	private Calendar resetTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c;
	}

	public long beforeWeekTime(long val) {

		Calendar today=Calendar.getInstance();
		today=resetTime(today);

		Calendar last7days=Calendar.getInstance();
		last7days.add(Calendar.DAY_OF_YEAR,-7);
		last7days = resetTime(last7days);

		return last7days.getTimeInMillis();
	}

	// print accounts
	public List<Long> printAccounts(Map<Long, Map<Long, AccountDetails>> input, Long userId) throws CustomException {

		MyUtil.checkNull(input);

		Map<Long, AccountDetails> innerMap = input.get(userId);
		List<Long> accountList = new ArrayList<>();
		for(Entry<Long, AccountDetails> entry : innerMap.entrySet()) {
			AccountDetails obj = entry.getValue();
			accountList.add(obj.getAccountNo());
		}
		return accountList;
	}

	public boolean hasAccount(long accNo) throws CustomException {

		String query = "SELECT ACCOUNT_NO FROM ACCOUNT_DETAILS";
		Map<Long,AccountDetails> map = new LinkedHashMap<>();
		AccountDetails obj = new AccountDetails();
		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);) {
			try(ResultSet result = prepStat.executeQuery()){
				while(result.next()) {
					obj.setAccountNo(result.getLong(1));
					map.put(obj.getAccountNo(), obj);
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return map.containsKey(accNo);
	}

	public boolean isAccountActive(long accNo) throws CustomException {

		String query = "SELECT STATUS FROM ACCOUNT_DETAILS WHERE ACCOUNT_NO = ?";
		AccountDetails account = new AccountDetails();
		try (Connection con = getConnection(); 
				PreparedStatement prepStat = con.prepareStatement(query);) {
			prepStat.setLong(1, accNo);
			try(ResultSet result = prepStat.executeQuery()){
				while(result.next()) {
					account.setStatus(result.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			throw new CustomException("Error occured while execution of prepared statement." , e);
		}
		return account.getStatus().equals("ACTIVE");
	}

	//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
}
