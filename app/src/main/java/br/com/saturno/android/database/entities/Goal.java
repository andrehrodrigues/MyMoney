package br.com.saturno.android.database.entities;

/**
 * Created by andre on 05/11/2015.
 */
public class Goal extends Entity{

    String _startDate;
    String _endDate;
    double _value;
    int _cardId;

    public Goal(){

    }

    public Goal(int _id, String _startDate, String _endDate, double _value, int _cardId) {
        super(_id);
        this._startDate = _startDate;
        this._endDate = _endDate;
        this._value = _value;
        this._cardId = _cardId;
    }

    public Goal(String _startDate, String _endDate, double _value, int _cardId) {
        this._startDate = _startDate;
        this._endDate = _endDate;
        this._value = _value;
        this._cardId = _cardId;
    }

    public String getStartDate() {
        return _startDate;
    }

    public void setStartDate(String _startDate) {
        this._startDate = _startDate;
    }

    public String getEndDate() {
        return _endDate;
    }

    public void setEndDate(String _endDate) {
        this._endDate = _endDate;
    }

    public double getValue() {
        return _value;
    }

    public void setValue(double _value) {
        this._value = _value;
    }

    public int getCardId() {
        return _cardId;
    }

    public void setCardId(int _cardId) {
        this._cardId = _cardId;
    }
}
