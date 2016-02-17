package br.com.saturno.android.mymoney.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import br.com.saturno.android.database.DatabaseHelper;
import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.database.datasources.AccountDataSource;
import br.com.saturno.android.mymoney.R;

public class MainActivity extends Activity {

	private EditText dateInput, commentInput, valueInput;
	private ImageButton btnChangeDate;
	private Spinner spinnerAcc, spinnerCat;
	DataSource ds;

	static final int DATE_DIALOG_ID = 999;

	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//deleteDatabase();
		//createDatabase();
		Log.i("Database", "Database created");


	}

	//When the activity is restarted the values on the spinners are refreshed.
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_account:
			//manageAccounts();
			return true;
		case R.id.action_categories:
			//manageCategories();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}



	//Calls the Account Activity
	public void manageAccounts(View view) {
		Intent intent = new Intent(MainActivity.this, AccountActivity.class);
		startActivity(intent);
	}

	//Calls the Account Activity
	public void callTransAct(View view) {
		Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
		startActivity(intent);
	}


	//Calls the Category Activity
	public void manageCategories(View view) {
		Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
		startActivity(intent);
	}

	//Calls the All Movements Activity
	public void manageAllMovements(View view) {
		Intent intent = new Intent(MainActivity.this,
				ViewAllTransactionsActivity.class);
		startActivity(intent);
	}


	public void alterAccountValue(Account acc, double value) {
		double balanceFinal = acc.getBalance() + value;
		acc.setBalance(balanceFinal);
		ds = new AccountDataSource(this);
		ds.update(acc);
	}

	/*
	 * Method to check the existence of the Database on the phone. Will be used
	 * in the future when the app is altered to save the DB on the SDCard
	 * private boolean checkDataBase() { //
	 * http://stackoverflow.com/questions/8007993
	 * /in-android-check-if-sqlite-database-exists-fails-from-time-to-time File
	 * database = getApplication().getDatabasePath("myMoney.db");
	 * System.out.println("Filename:" + database.getAbsolutePath()); if
	 * (!database.exists()) { // Database does not exist so copy it from assets
	 * here Log.i("Database", "Not Found"); return false; } Log.i("Database",
	 * "Found"); return true; }
	 */

	// Try to create the database on the mobile. If the database already exists
	// no alteration is made.
	public void createDatabase() {
		DatabaseHelper db = new DatabaseHelper(this);
		SQLiteDatabase dbWritable = db.getWritableDatabase();
		db.onCreate(dbWritable);
		db.close();
	}

	// Delete all the database.
	public void deleteDatabase() {
		DatabaseHelper db = new DatabaseHelper(this);
		SQLiteDatabase dbWritable = db.getWritableDatabase();
		db.onDelete(dbWritable);
		db.close();
	}



}
