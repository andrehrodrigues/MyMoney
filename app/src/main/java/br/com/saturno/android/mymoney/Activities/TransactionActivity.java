package br.com.saturno.android.mymoney.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import br.com.saturno.android.mymoney.R;

public class TransactionActivity extends Activity implements TransactionFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
