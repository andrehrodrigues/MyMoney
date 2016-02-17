package br.com.saturno.android.database.entities;

import java.util.ArrayList;
import java.util.List;

public class Category extends Entity{
	// private variables
	String _name;

	// Empty constructor
	public Category() {

	}

	// constructor
	public Category(int id, String name) {
		super(id);
		this._name = name;
	}

	// constructor
	public Category(String name) {
		this._name = name;
	}

	// getting name
	public String getName() {
		return this._name;
	}

	// setting name
	public void setName(String name) {
		this._name = name;
	}

	// Select the names to be inserted in the dropdown list of categories
	public static List<String> convertCategoryList(List<Category> catList) {
		List<String> list = new ArrayList<String>();
		for (Category cn : catList) {
			list.add(cn.getName());
		}
		return list;
	}
}
