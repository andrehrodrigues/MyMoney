package br.com.saturno.android.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.saturno.android.database.DatabaseHelper;
import br.com.saturno.android.database.entities.Card;

/**
 * Created by andre on 04/11/2015.
 */
public class CardDataSource extends DatabaseHelper implements DataSource<Card>{
    public CardDataSource(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new card
    @Override
    public boolean add(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CARD_KEY_LASTDIGITS, card.getLastdigit());
        values.put(CARD_KEY_TYPE, card.getType());
        values.put(CARD_KEY_BALANCE, card.getBalance());
        values.put(CARD_KEY_ACCOUNTID, card.getAccountId());

        try {
            // Inserting Row
            db.insertOrThrow(TABLE_CARD, null, values);
        }catch (SQLException e){
            Log.e("CardDataSource", "Insert Error - "+e);
            return false;
        }finally {
            db.close(); // Closing database connection
        }
        return true;
    }

    // Getting single card
    @Override
    public Card get(Card card) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(card.getId() != 0) {
             cursor = db.query(TABLE_CARD, new String[]{CARD_KEY_ID,
                            CARD_KEY_LASTDIGITS, CARD_KEY_TYPE, CARD_KEY_BALANCE, CARD_KEY_ACCOUNTID}, CARD_KEY_ID + "=?",
                    new String[]{String.valueOf(card.getId())}, null, null, null, null);
        }else{
            if(card.getLastdigit() != 0){
                cursor = db.query(TABLE_CARD, new String[]{CARD_KEY_ID,
                                CARD_KEY_LASTDIGITS, CARD_KEY_TYPE, CARD_KEY_BALANCE,CARD_KEY_ACCOUNTID}, CARD_KEY_LASTDIGITS + "=?",
                        new String[]{String.valueOf(card.getLastdigit())}, null, null, null, null);
            }
        }
        if (cursor.moveToFirst()) {
            card.setId(cursor.getInt(cursor.getColumnIndex(CARD_KEY_ID)));
            card.setLastdigit(cursor.getInt(cursor.getColumnIndex(CARD_KEY_LASTDIGITS)));
            card.setBalance(cursor.getInt(cursor.getColumnIndex(CARD_KEY_BALANCE)));
            card.setAccountId(cursor.getInt(cursor.getColumnIndex(CARD_KEY_ACCOUNTID)));
            card.setType(cursor.getString(cursor.getColumnIndex(CARD_KEY_TYPE)));
        }
        db.close();
        // return contact
        return card;
    }


    // Getting All cards
    @Override
    public List<Card> getAll() {
        List<Card> cardList = new ArrayList<Card>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card();
                card.setId(cursor.getInt(cursor.getColumnIndex(CARD_KEY_ID)));
                card.setLastdigit(cursor.getInt(cursor.getColumnIndex(CARD_KEY_LASTDIGITS)));
                card.setType(cursor.getString(cursor.getColumnIndex(CARD_KEY_TYPE)));
                card.setBalance(cursor.getDouble(cursor.getColumnIndex(CARD_KEY_BALANCE)));
                card.setAccountId(cursor.getInt(cursor.getColumnIndex(CARD_KEY_ACCOUNTID)));
                // Adding contact to list
                cardList.add(card);
            } while (cursor.moveToNext());
        }
        db.close();
        // return card list
        return cardList;
    }

    // Updating single card
    @Override
    public int update(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CARD_KEY_LASTDIGITS, card.getLastdigit());
        values.put(CARD_KEY_TYPE, card.getType());
        values.put(CARD_KEY_BALANCE, card.getBalance());
        values.put(CARD_KEY_ACCOUNTID, card.getAccountId());

        // updating row
        int affectedRows = db.update(TABLE_CARD, values, CARD_KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
        db.close();
        return affectedRows;
    }

    // Deleting single card
    @Override
    public int delete(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE_CARD, CARD_KEY_ID + " = ?",
                new String[] { String.valueOf(card.getId()) });
        db.close();
        return affectedRows;
    }

    // Getting categories Count
    @Override
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }

}
