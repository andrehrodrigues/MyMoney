package br.com.saturno.android.database.entities;

public class Transaction extends Entity {
	// private variables
	String _date;
	double _value;
	String _description;
	Integer _categoryId;
	Integer _accountId;

	// Empty constructor
	public Transaction() {

	}

	// constructor
	public Transaction(int id, String date, double value, String comment, Integer catId,
					   Integer accId) {
		super(id);
		this._date = date;
		this._value = value;
		this._description = comment;
		this._categoryId = catId;
		this._accountId = accId;
	}

	// constructor
	public Transaction(String date, Double value, String comment, Integer catId, Integer accId) {
		this._date = date;
		this._value = value;
		this._description = comment;
		this._categoryId = catId;
		this._accountId = accId;
	}

	// getting date
	public String getDate() {
		return this._date;
	}

	// setting date
	public void setDate(String date) {
		this._date = date;
	}

	// getting value
	public double getValue() {
		return this._value;
	}

	// setting value
	public void setValue(double value) {
		this._value = value;
	}

	// getting comment
	public String getDescription() {
		return this._description;
	}

	// setting comment
	public void setDescription(String comment) {
		this._description = comment;
	}

	// getting categoryId
	public Integer getCategoryId() {
		return this._categoryId;
	}

	// setting categoryId
	public void setCategoryId(Integer categoryId) {
		this._categoryId = categoryId;
	}

	// getting accountId
	public Integer getAccountId() {
		return this._accountId;
	}

	// setting accountId
	public void setAccountId(Integer accountId) {
		this._accountId = accountId;
	}

}
