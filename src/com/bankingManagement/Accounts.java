package com.bankingManagement;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {

	private Connection connection;
	private Scanner scanner;
	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
		
	}
	
	public long open_account(String email)
	{
		if(!account_exist(email)) {
			String open_account_query = "insert into account (account_number,full_name,email,balance,security_pin ) values (?,?,?,?,?)";
			scanner.nextLine();
			System.out.print("Full name: ");
			String full_name = scanner.nextLine();
			System.out.print("Enter initial amount: ");
			Double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.print("Enter security pin: ");
			String security_pin = scanner.nextLine();
			try {
				long account_number = generateAccountnumber();
				PreparedStatement preparedstatement = connection.prepareStatement(open_account_query);
				preparedstatement.setLong(1, account_number);
				preparedstatement.setString(2, full_name);
				preparedstatement.setString(3, email);
				preparedstatement.setDouble(4,balance);
				preparedstatement.setString(5, security_pin);
				int affectedrows = preparedstatement.executeUpdate();
				if(affectedrows>0)
				{
					return account_number;
				}else
				{
					throw new RuntimeException("Account creation failed!");
				}
			}catch(SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		throw new RuntimeException("account already exist!");
	}
			
		
		
		
		
	public long getAccount_number(String email) 
	{
		String query= "select account_number from account where email=?";
		try {
			PreparedStatement preparedstatement = connection.prepareStatement(query);
			preparedstatement.setString(1, email);
			ResultSet resultset = preparedstatement.executeQuery();
			if(resultset.next()) {
				return resultset.getLong("account_number");
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		throw new RuntimeException("account number doesn't exist");
		
	}
	
	private long generateAccountnumber()
	{
		try
		{
		Statement statement = connection.createStatement();
		ResultSet resultset = statement.executeQuery("select account_number from account order by account_number desc limit 1");
		if(resultset.next())
		{
			long last_account_number =resultset.getLong("account_number");
			return last_account_number+1;
		}else
		{
			return 10000100;
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 10000100;
				
	}
	public boolean account_exist(String email)
	{
		String query = "select account_number from account where email=?" ;
		try {
			PreparedStatement preparedstatement = connection.prepareStatement(query);
			preparedstatement.setString(1, email);
			ResultSet resultset = preparedstatement.executeQuery();
			if(resultset.next())
			{
				return true;
				
			}
			else {
				return false;
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return false;
	}
	

	
}
