package br.com.saturno.android.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.database.datasources.AccountDataSource;
import br.com.saturno.android.database.entities.Category;
import br.com.saturno.android.database.datasources.CategoryDataSource;
import br.com.saturno.android.database.entities.Transaction;
import br.com.saturno.android.mymoney.R;

public class MovementAdapter extends ArrayAdapter<Transaction> {

	List<Transaction> movList;
	Context context;
	DataSource ds;

	public MovementAdapter(Context context, List<Transaction> movList) {
		super(context, R.layout.movement_list, R.id.layMovDate, movList);
		this.movList = movList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.movList.size();
	}

	@Override
	public Transaction getItem(int arg0) {
		// TODO Auto-generated method stub
		return movList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.movement_list, arg2, false);
		}

		TextView movDate = (TextView) arg1.findViewById(R.id.layMovDate);
		TextView movValue = (TextView) arg1.findViewById(R.id.layMovValue);
		TextView movCategory = (TextView) arg1.findViewById(R.id.layMovCat);
		TextView movAcc = (TextView) arg1.findViewById(R.id.layMovAcc);
		TextView movComment = (TextView) arg1.findViewById(R.id.layMovComment);

		Transaction transaction = this.movList.get(arg0);
		ds = new AccountDataSource(context);

		movDate.setText(transaction.getDate());
		movValue.setText(String.valueOf(transaction.getValue()));
		if (transaction.getAccountId() != null) {
			Account acc = new Account(transaction.getAccountId(),null,0.0);
                ds.get(acc);
			movAcc.setText(acc.getBank());
		} else {
			movAcc.setText("");
		}
		ds = new CategoryDataSource(context);
		if (transaction.getCategoryId() != null) {
			Category cat = new Category(transaction.getCategoryId(),null);
                ds.get(cat);
			movCategory.setText(cat.getName());
		} else {
			movCategory.setText("");
		}

		movComment.setText(transaction.getDescription());

		return arg1;
	}

}
