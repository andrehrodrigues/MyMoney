package br.com.saturno.android.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.mymoney.R;

public class AccountAdapter extends ArrayAdapter<Account> {

	List<Account> accountList;
	Context context;

	public AccountAdapter(Context context, List<Account> accList) {
		super(context, R.layout.account_list_item, R.id.layAccName, accList);
		this.accountList = accList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.accountList.size();
	}

	@Override
	public Account getItem(int arg0) {
		// TODO Auto-generated method stub
		return accountList.get(arg0);
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
			arg1 = inflater.inflate(R.layout.account_list_item, arg2, false);
		}

		TextView accName = (TextView) arg1.findViewById(R.id.layAccName);
		TextView accBalance = (TextView) arg1.findViewById(R.id.layAccBalance);

		Account account = this.accountList.get(arg0);

		accName.setText(account.getBank());
		accBalance.setText(String.valueOf(account.getBalance()));

		return arg1;
	}

}
