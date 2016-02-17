package br.com.saturno.android.mymoney.Activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Transaction;
import br.com.saturno.android.database.datasources.TransactionDataSource;
import br.com.saturno.android.mymoney.R;
import br.com.saturno.android.util.MovementAdapter;

public class ViewAllTransactionsActivity extends Activity {

	EditText movDate;
	EditText movValue;
	EditText movCat;
	EditText movAcc;
	EditText movComment;
	ListView movLV;
	MovementAdapter movArrayAdapter;
	Transaction currentItem;
	List<Transaction> movList;
	DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_transactions);

		populateListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_all_transactions, menu);
		return true;
	}

	public void populateListView() {
		movLV = (ListView) findViewById(R.id.allMovsLV);
		ds = new TransactionDataSource(this);
		List<Transaction> movList = ds.getAll();
		movArrayAdapter = new MovementAdapter(this, movList);
		movLV.setAdapter(movArrayAdapter);
	}
	
}
