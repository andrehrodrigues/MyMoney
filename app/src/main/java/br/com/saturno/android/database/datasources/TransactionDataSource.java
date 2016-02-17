package br.com.saturno.android.database.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.saturno.android.database.DatabaseHelper;
import br.com.saturno.android.database.entities.Transaction;

public class TransactionDataSource extends DatabaseHelper implements DataSource<Transaction>{

	public TransactionDataSource(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// Adding new transaction
    @Override
	public boolean add(Transaction transaction) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TRANSACTION_KEY_DATE, transaction.getDate());
		values.put(TRANSACTION_KEY_VALUE, transaction.getValue());
		values.put(TRANSACTION_KEY_DESCRIPTION, transaction.getDescription());
		values.put(TRANSACTION_KEY_CATEGORYID, transaction.getCategoryId());
		values.put(TRANSACTION_KEY_CARDID, transaction.getAccountId());

		// Inserting Row
        try {
            db.insertOrThrow(TABLE_TRANSACTION, null, values);
        }catch (SQLException e){
            Log.e("TransactionDataSource","Insert Error - "+e);
            return false;
        }
        finally {
            db.close(); // Closing database connection
        }
        return true;
	}

	// Getting single transaction
    @Override
	public Transaction get(Transaction transaction) {
		SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_TRANSACTION, new String[]{
                            TRANSACTION_KEY_ID, TRANSACTION_KEY_DATE, TRANSACTION_KEY_VALUE,
                            TRANSACTION_KEY_DESCRIPTION, TRANSACTION_KEY_CATEGORYID,
                            TRANSACTION_KEY_CARDID}, TRANSACTION_KEY_ID + "=?",
                    new String[]{String.valueOf(transaction.getId())}, null, null, null, null);

            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_ID)) == transaction.getId()) {
                    transaction.setDate(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DATE)));
                    transaction.setValue(cursor.getDouble(cursor.getColumnIndex(TRANSACTION_KEY_VALUE)));
                    transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DESCRIPTION)));
                    transaction.setCategoryId(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_CATEGORYID)).isEmpty() ? null : cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_CATEGORYID)));
                    transaction.setAccountId(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_CARDID)).isEmpty() ? null : cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_CARDID)));
                }
            }
        }finally {
            db.close();
        }

		return transaction;
	}

    // Getting All transactions
    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_ID)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DATE)));
                transaction.setValue(cursor.getDouble(cursor.getColumnIndex(TRANSACTION_KEY_VALUE)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DESCRIPTION)));
                if(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_CATEGORYID)) != null){
                    transaction.setCategoryId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_CATEGORYID)));
                }
                if(cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_CARDID)) != null){
                    transaction.setAccountId(cursor.getInt(cursor.getColumnIndex(TRANSACTION_KEY_CARDID)));
                }
                // Adding contact to list
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        db.close();
        // return movement list
        return transactionList;
    }

    // Updating single transaction
    @Override
    public int update(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRANSACTION_KEY_DATE, transaction.getDate());
        values.put(TRANSACTION_KEY_VALUE, transaction.getValue());
        values.put(TRANSACTION_KEY_DESCRIPTION, transaction.getDescription());
        values.put(TRANSACTION_KEY_CATEGORYID, transaction.getCategoryId());
        values.put(TRANSACTION_KEY_CARDID, transaction.getAccountId());

        // updating row
        int affectedRows = db.update(TABLE_TRANSACTION, values, TRANSACTION_KEY_ID + " = ?",
                new String[]{String.valueOf(transaction.getId())});
        db.close();
        return affectedRows;
    }

    // Deleting single transaction
    @Override
    public int delete(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE_TRANSACTION, TRANSACTION_KEY_ID + " = ?",
                new String[]{String.valueOf(transaction.getId())});
        db.close();
        return affectedRows;
    }

    // Getting transactions Count
    @Override
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        db.close();
        // return count
        return cursor.getCount();
    }

}
