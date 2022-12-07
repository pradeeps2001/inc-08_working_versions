package myutil;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MyUtil {
	
	public static void checkNull(Object input) throws CustomException {
		if (Objects.isNull(input)) {
			throw new CustomException("Input is null. Enter a valid input !");
		}
	}
	
	public static void validDeposit(double amount) throws CustomException {
		if(amount < 0) {
			throw new CustomException("Enter a valid amount.");
		} else if(amount > 250000) {
			throw new CustomException("Deposit limit is Rs.2,50,000.00");
		}
	}
	
	public static void validWithdraw(double balance, double amount) throws CustomException {
		if(amount > balance) {
			throw new CustomException("Insufficient balance.");
		}
	}
	
	public static void validTransfer(double balance, double amount, long sender, long receiver) throws CustomException {
		if(amount > balance) {
			throw new CustomException("Insufficient balance.");
		} else if (sender == receiver) {
			throw new CustomException("Cannot transfer to same account.");
		}
	}
	
	public static void checkInBound(List<?> input, int index1, int index2) throws CustomException {
		if (index1 < 0 || index2 > input.size()) {
			throw new CustomException("Index is out of bound. Enter a valid input !");
		}
	}
	
	public static int checkInt(Scanner scan) {
		boolean condition = true;
		int option = 0;
		while (condition) {
			if (scan.hasNextInt()) {
				option = scan.nextInt();
				scan.nextLine();
				if (option < 0) {
					System.out.println("Enter a positive integer");
				} else {
					condition = false;
				}
			} else {
				System.out.println("Enter a valid integer !");
				scan.next();
			}
		}
		return option;
	}
	
	public static long checkLong(Scanner scan) {
		boolean condition = true;
		long option = 0;
		while (condition) {
			if (scan.hasNextLong()) {
				option = scan.nextLong();
				scan.nextLine();
				if (option < 0) {
					System.out.println("Enter a positive integer");
				} else {
					condition = false;
				}
			} else {
				System.out.println("Enter a valid long !");
				scan.next();
			}
		}
		return option;
	}
	
	public static double checkDouble(Scanner scan) {
		boolean condition = true;
		double option = 0;
		while (condition) {
			if (scan.hasNextDouble()) {
				option = scan.nextDouble();
				scan.nextLine();
				if (option < 0) {
					System.out.println("Enter a positive integer");
				} else {
					condition = false;
				}
			} else {
				System.out.println("Enter a valid long !");
				scan.next();
			}
		}
		return option;
	}
	
	public static Long[] getLongInput(Scanner scan, int number) {
		Long[] input = new Long[number];
		for (int i = 0; i < number; i++) {
			input[i] = scan.nextLong();
		}
		return input;
	}
}
