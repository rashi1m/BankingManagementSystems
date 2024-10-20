package com.bankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner scanner;
	
	public User(Connection connection, Scanner scanner) {
		this.connection= connection;
		this.scanner= scanner;
	
	}


	public void register() {
	
		scanner.nextLine();
		System.out.print("Full Name:");
		String full_name = scanner.nextLine();
		System.out.print("Email:");
		String email = scanner.nextLine();
		System.out.print("Password:");
		String password = scanner.nextLine();
		if(user_exist(email))
		{
			System.out.println("User already exist for this email address!");
			return;
		}
		try {
		String register_query = "Insert into user(full_name,email,password)values(?,?,?)";
		PreparedStatement preparedstatement = connection.prepareStatement(register_query);
		preparedstatement.setString(1, full_name);
		preparedstatement.setString(2, email);
		preparedstatement.setString(3, password);
		int affectedrows = preparedstatement.executeUpdate();
		if(affectedrows>0) {
			System.out.println("Registration successfull!");
		}else {
			System.out.println("Registration failed!");
		}
	}catch(SQLException e)
		{
		e.printStackTrace();
		}
	}

	public String login()
	{
		scanner.nextLine();
		System.out.print("Email:");
		String email = scanner.nextLine();
		System.out.print("Password:");
		String password = scanner.nextLine();
		String login_query = "select * from  user where email=? and password=?";
		
		try
		{
			
		PreparedStatement preparedstatement = connection.prepareStatement(login_query);
		preparedstatement.setString(1, email);
		preparedstatement.setString(2, password);
		ResultSet resultset = preparedstatement.executeQuery();
		if(resultset.next())
		{
			return email;
		}
		else {
			
		
		   return null ;
		
		}
	}catch(SQLException e) {
		
		e.printStackTrace();
	}
		return null;
		
		
	}
	
public boolean user_exist(String email) {
	
		String query = "select * from user where email=?" ;
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


	


