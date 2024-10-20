package com.bankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection connection, Scanner scanner) {

		this.connection = connection;
		this.scanner = scanner;

	}

	public void credit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter security_pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedstatement = connection.prepareStatement("Select * from account where account_number = ? and security_pin = ?");
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, security_pin);
				ResultSet resultset = preparedstatement.executeQuery();

				if (resultset.next()) {

					String credit_query = "Update account Set balance = balance + ? where account_number=?";
					PreparedStatement preparedstatement1 = connection.prepareStatement(credit_query);
					preparedstatement1.setDouble(1, amount);
					preparedstatement1.setLong(2, account_number);
					int affectedrows = preparedstatement1.executeUpdate();
					if (affectedrows > 0) {
						System.out.println("Rs." + amount + "credited successfully");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction failed!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Insufficient balance");
				}
			} else {
				System.out.println("Invalid pin!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter security_pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedstatement = connection.prepareStatement("Select * from account where account_number = ? and security_pin = ? ");
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, security_pin);
				ResultSet resultset = preparedstatement.executeQuery();

				if (resultset.next()) {
				
					double current_balance = resultset.getDouble(4);
					if (amount <= current_balance) {
						String debit_query = "Update account Set balance = balance - ? where account_number = ?";
						PreparedStatement preparedstatement2 = connection.prepareStatement(debit_query);
						preparedstatement2.setDouble(1, amount);
						preparedstatement2.setLong(2, account_number);
						int affectedrows = preparedstatement2.executeUpdate();
						if (affectedrows > 0) {
							System.out.println("Rs." + amount + "debited successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction failed!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient balance");
					}
				} else {
					System.out.println("Invalid pin!");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws SQLException {

		scanner.nextLine();
		System.out.print("Enter Receiver account number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		System.out.print("Enter Security pin:");
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (receiver_account_number != 0 && sender_account_number != 0) {
				PreparedStatement preparedstatement = connection.prepareStatement("select*from account where account_number =? and security_pin = ?");
				preparedstatement.setLong(1, receiver_account_number);
				preparedstatement.setString(2, security_pin);
				ResultSet resultset = preparedstatement.executeQuery();
				if (resultset.next()) {
					
					double current_balance = resultset.getDouble(4);
					if (amount <= current_balance) {
						String debit_query = "Update account Set balance= balance -? where account_number=?";
						String credit_query = "Update account Set balance = balance + ? where account number=?";
						PreparedStatement creditpreparedstatement = connection.prepareStatement(credit_query);
						PreparedStatement debitpreparedstatement = connection.prepareStatement(debit_query);
						creditpreparedstatement.setDouble(1, amount);
						creditpreparedstatement.setLong(2, receiver_account_number);

						debitpreparedstatement.setDouble(1, amount);
						debitpreparedstatement.setLong(2, sender_account_number);

						int affectedrows1 = creditpreparedstatement.executeUpdate();
						int affectedrows2 = debitpreparedstatement.executeUpdate();
						if (affectedrows1 > 0 && affectedrows2 > 0) {
							System.out.println("Transaction successful!");
							System.out.println("Rs." + amount + "Transferred successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction failed");
							connection.rollback();
							connection.setAutoCommit(true);

						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid security pin");
				}
			} else {
				System.out.println("Invalid Account number");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void get_balance(long account_number) {
		scanner.nextLine();
		System.out.print("Enter security pin: ");
		String security_pin = scanner.nextLine();
		try {
			PreparedStatement preparedstatement = connection
					.prepareStatement("Select balance from account where account_number=?and security_pin=?");
			preparedstatement.setLong(1, account_number);
			preparedstatement.setString(2, security_pin);
			ResultSet resultset = preparedstatement.executeQuery();
			if (resultset.next()) {
				double balance = resultset.getDouble("balance");
				System.out.println("Balance: " + balance);
			} else {
				System.out.println("Invalid pin");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
