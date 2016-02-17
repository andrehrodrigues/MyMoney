package br.com.saturno.android.mymoney.Activities;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;

import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.mymoney.R;


public class AccountActivity extends Activity implements AccountFragment.OnFragmentInteractionListener,AccountListFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}*/

	@Override
	public void onAccountOperation() {
		AccountListFragment accListFrag = (AccountListFragment) getFragmentManager().findFragmentById(R.id.account_list_fragment);

		if (accListFrag != null) {
            accListFrag.updateList();
		}
	}


    @Override
    public void onItemSelected(Account account) {
        AccountFragment accFrag = (AccountFragment) getFragmentManager().findFragmentById(R.id.account_fragment);
        accFrag.setCurrentItem(account);
		//accFrag.updateFields(account.getBank(),String.valueOf(account.getBalance()));
    }
}
