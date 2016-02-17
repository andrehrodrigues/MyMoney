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
import br.com.saturno.android.database.entities.Account;

public class AccountDataSource extends DatabaseHelper implements DataSource<Account>{

	public AccountDataSource(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// Adding new account
	@Override
	public boolean add(Account account) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ACCOUNT_KEY_BALANCE, account.getBalance()); // Account Value
		values.put(ACCOUNT_KEY_BANK, account.getBank()); // Account Name

        try {
            // Inserting Row
            db.insertOrThrow(TABLE_ACCOUNT, null, values);
        }catch (SQLException e){
            Log.e("AccountDataSource", "Insert Error - "+e);
			return false;
        }finally {
            db.close(); // Closing database connection
        }
		return true;
	}

	// Getting single account
    @Override
	public Account get(Account account) {
		SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        //There are two ways to search for an Account. If the account object have an ID the data will be searched based on the ID.
        //Else the account will be searched using the BANK field.
        if(account.getId() != 0) {
             cursor = db.query(TABLE_ACCOUNT, new String[]{ACCOUNT_KEY_ID,
                            //ACCOUNT_KEY_BANK, ACCOUNT_KEY_BALANCE }, ACCOUNT_KEY_ID + "=?",
                            ACCOUNT_KEY_BANK}, ACCOUNT_KEY_ID + "=?",
                    new String[]{String.valueOf(account.getId())}, null, null, null, null);
        }else{
            if(account.getBank() != null){
                cursor = db.query(TABLE_ACCOUNT, new String[]{ACCOUNT_KEY_ID,
                                ACCOUNT_KEY_BANK, ACCOUNT_KEY_BALANCE}, ACCOUNT_KEY_BANK
                                + "=?", new String[]{String.valueOf(account.getBank())}, null, null,
                        null, null);
            }
        }

		if (cursor.moveToFirst()) {
            account.setId(cursor.getInt(cursor.getColumnIndex(ACCOUNT_KEY_ID)));
            account.setBank(cursor.getString(cursor.getColumnIndex(ACCOUNT_KEY_BANK)));
            account.setBalance(cursor.getDouble(cursor.getColumnIndex(ACCOUNT_KEY_BALANCE)));
        }
		db.close();

		return account;
	}


	// Getting All accounts
    @Override
	public List<Account> getAll() {
		List<Account> accountList = new ArrayList<Account>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Account account = new Account();
				account.setId(cursor.getInt(cursor.getColumnIndex(ACCOUNT_KEY_ID)));
				account.setBank(cursor.getString(cursor.getColumnIndex(ACCOUNT_KEY_BANK)));
				account.setBalance(cursor.getDouble(cursor.getColumnIndex(ACCOUNT_KEY_BALANCE)));
				// Adding account to list
				accountList.add(account);
			} while (cursor.moveToNext());
		}
		db.close();
		// return account list
		return accountList;
	}

	// Updating single account
    @Override
	public int update(Account account) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ACCOUNT_KEY_BANK, account.getBank());
		values.put(ACCOUNT_KEY_BALANCE, account.getBalance());

		// updating row
		int affectedRows = db.update(TABLE_ACCOUNT, values, ACCOUNT_KEY_ID + " = ?",
                new String[]{String.valueOf(account.getId())});
		db.close();
		return affectedRows;
	}

	// Deleting single account
    @Override
	public int delete(Account account) {
		SQLiteDatabase db = this.getWritableDatabase();
		int affectedRows = db.delete(TABLE_ACCOUNT, ACCOUNT_KEY_ID + " = ?",
                new String[]{String.valueOf(account.getId())});
		db.close();
		return affectedRows;
	}

	// Getting categories Count
    @Override
	public int getCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ACCOUNT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();
	}

}
