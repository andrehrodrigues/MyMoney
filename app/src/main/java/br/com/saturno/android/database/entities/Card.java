package br.com.saturno.android.database.entities;

/**
 * Created by andre on 04/11/2015.
 */
public class Card extends Entity{
    // private variables
    int _lastdigit;
    String _type;
    double _balance;
    int _accountId;

    // Empty constructor
    public Card() {
    }

    public Card(int _id, int _lastdigit, String _type, double _balance, int _accountId) {
        super(_id);
        this._lastdigit = _lastdigit;
        this._type = _type;
        this._balance = _balance;
        this._accountId = _accountId;
    }

    public Card(int _lastdigit, String _type, double _balance, int _accountId) {
        this._lastdigit = _lastdigit;
        this._type = _type;
        this._balance = _balance;
        this._accountId = _accountId;
    }

    public int getLastdigit() {
        return _lastdigit;
    }

    public void setLastdigit(int _lastdigit) {
        this._lastdigit = _lastdigit;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public double getBalance() {
        return _balance;
    }

    public void setBalance(double _balance) {
        this._balance = _balance;
    }

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int _accountId) {
        this._accountId = _accountId;
    }
}
