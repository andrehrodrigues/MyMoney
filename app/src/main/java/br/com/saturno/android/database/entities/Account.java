package br.com.saturno.android.database.entities;

import java.util.ArrayList;
import java.util.List;

public class Account extends Entity {
	// private variables
	private String _bank;
	private double _balance;

	// Empty constructor
	public Account() {
    }

	// constructor
	public Account(int id, String bank, double value) {
		super(id);
		this._bank = bank;
		this._balance = value;
	}

	// constructor
	public Account(String name, double value) {
		this._bank = name;
		this._balance = value;
	}

	// getting name
	public String getBank() {
		return this._bank;
	}

	// setting name
	public void setBank(String name) {
		this._bank = name;
	}

	// getting value
	public double getBalance() {
		return this._balance;
	}

	// setting value
	public void setBalance(double balance) {
		this._balance = balance;
	}

	// Select the names to be inserted in the dropdown list of accounts
	public static List<String> convertAccountList(List<Account> accList) {
		List<String> list = new ArrayList<String>();
		for (Account cn : accList) {
			list.add(cn.getBank());
		}
		return list;
	}
}
