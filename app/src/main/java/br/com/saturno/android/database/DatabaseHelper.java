package br.com.saturno.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

public class DatabaseHelper extends SQLiteOpenHelper{
	// All Static variables

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "myMoney";

	// Table names
	protected static final String TABLE_CATEGORY = "category";
	protected static final String TABLE_TRANSACTION = "movement";
	protected static final String TABLE_ACCOUNT = "account";
    protected static final String TABLE_CARD = "card";
    protected static final String TABLE_GOAL = "goal";

	// Category Table Columns names
	protected static final String CATEGORY_KEY_ID = "id";
	protected static final String CATEGORY_KEY_NAME = "name";

	// Account Table Columns names
	protected static final String ACCOUNT_KEY_ID = "id";
	protected static final String ACCOUNT_KEY_BANK = "bank";
	protected static final String ACCOUNT_KEY_BALANCE = "balance";

	// Transaction Table Columns names
	protected static final String TRANSACTION_KEY_ID = "id";
	protected static final String TRANSACTION_KEY_DATE = "date";
	protected static final String TRANSACTION_KEY_VALUE = "value";
	protected static final String TRANSACTION_KEY_DESCRIPTION = "description";
	protected static final String TRANSACTION_KEY_CATEGORYID = "categoryId";
	protected static final String TRANSACTION_KEY_CARDID = "cardId";

    // Card Table Columns names
    protected static final String CARD_KEY_ID = "id";
    protected static final String CARD_KEY_LASTDIGITS = "lastDigits";
    protected static final String CARD_KEY_TYPE = "type";
    protected static final String CARD_KEY_BALANCE = "balance";
    protected static final String CARD_KEY_ACCOUNTID = "accountId";

    // Goal Table Columns names
    protected static final String GOAL_KEY_ID = "id";
    protected static final String GOAL_KEY_START_DATE = "startDate";
    protected static final String GOAL_KEY_END_DATE = "endDate";
    protected static final String GOAL_KEY_VALUE = "value";
    protected static final String GOAL_KEY_CARDID = "bank";


	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("Database", "calls onCreate");
		try {
			db.execSQL(createTableCategory());
			db.execSQL(createTableAccount());
			db.execSQL(createTableTransaction());
            db.execSQL(createTableCard());
            db.execSQL(createTableGoal());
		} catch (SQLiteException e) {
			Log.i("Database", e + "Error while creating tables");
		}
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Database", "calls onUpdate");
		System.out.println("calls onUpdate");
		// Drop older table if existed
		onDelete(db);
		// Create tables again
		onCreate(db);
	}

	public void onDelete(SQLiteDatabase db) {
		Log.i("Database", "calls onDelete");
		System.out.println("calls onDelete");
		// Drop tables if existed
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
			Log.i("Database", "Droped CATEGORY");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
			Log.i("Database", "Droped ACCOUNT");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
			Log.i("Database", "Droped TRANSACTION");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);
            Log.i("Database", "Droped CARD");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
            Log.i("Database", "Droped GOAL");
		} catch (SQLiteException e) {
			Log.i("Database", e + "Error while removing tables");
		}

	}

	private String createTableCategory() {
		String CREATE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_CATEGORY + "("
                + CATEGORY_KEY_ID + " INTEGER PRIMARY KEY,"
                + CATEGORY_KEY_NAME + " TEXT UNIQUE NOT NULL"
				+ ");";

		return CREATE_CATEGORY_TABLE;
	}

	private String createTableAccount() {
		String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_ACCOUNT + "("
                + ACCOUNT_KEY_ID + " INTEGER PRIMARY KEY,"
                + ACCOUNT_KEY_BANK + " TEXT UNIQUE NOT NULL,"
				+ ACCOUNT_KEY_BALANCE + " REAL NOT NULL"
                + ");";

		return CREATE_ACCOUNT_TABLE;
	}

	private String createTableTransaction() {
		String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_TRANSACTION + "("
                + TRANSACTION_KEY_ID + " INTEGER PRIMARY KEY,"
                + TRANSACTION_KEY_DATE + " TEXT NOT NULL,"
				+ TRANSACTION_KEY_VALUE + " REAL NOT NULL,"
                + TRANSACTION_KEY_DESCRIPTION  + " TEXT,"
                + TRANSACTION_KEY_CATEGORYID + " INTEGER,"
				+ TRANSACTION_KEY_CARDID + " INTEGER"
                + ");";

		return CREATE_TRANSACTION_TABLE;
	}

    private String createTableCard() {
        String CREATE_CARD_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CARD + "("
                + CARD_KEY_ID + " INTEGER PRIMARY KEY,"
                + CARD_KEY_LASTDIGITS+ " INTEGER UNIQUE,"
                + CARD_KEY_TYPE + " TEXT NOT NULL,"
                + CARD_KEY_BALANCE + " REAL NOT NULL,"
                + CARD_KEY_ACCOUNTID + " INTEGER"
                + ");";

        return CREATE_CARD_TABLE;
    }

    private String createTableGoal() {
        String CREATE_GOAL_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_GOAL + "("
                + GOAL_KEY_ID + " INTEGER PRIMARY KEY,"
                + GOAL_KEY_START_DATE + " TEXT NOT NULL,"
                + GOAL_KEY_END_DATE + " TEXT NOT NULL,"
                + GOAL_KEY_VALUE+ " REAL NOT NULL,"
                + GOAL_KEY_CARDID + " INTEGER" + ");";

        return CREATE_GOAL_TABLE;
    }

}
