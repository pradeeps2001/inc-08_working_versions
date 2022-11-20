package com.banking.servlets;

import java.io.IOException;


import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.banking.localcache.StorageLayer;
import com.banking.pojo.AccountDetails;

import myutil.CustomException;

@WebServlet("/servlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse responce) {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
		
		StorageLayer storage = new StorageLayer();
		
		try {
			storage.setAllData();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		
		Map<Long,Map<Long,AccountDetails>> accountDetailsMap = storage.getAccountDetails();
		
		String customerIdStr = request.getParameter("custId");
		long customerId = Long.parseLong(customerIdStr);
		if(accountDetailsMap.containsKey(customerId)) {
			Map<Long,AccountDetails> accounts = accountDetailsMap.get(customerId);
			System.out.println(accounts.size());
			JSONObject jsonResponce = new JSONObject(accounts);
			String json = jsonResponce.toString();
			responce.getWriter().write(json);
		} else {
			System.out.println("No");
		}

	}

}
