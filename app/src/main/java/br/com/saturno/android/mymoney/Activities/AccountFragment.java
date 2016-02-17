package br.com.saturno.android.mymoney.Activities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.saturno.android.database.datasources.AccountDataSource;
import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.mymoney.R;
import br.com.saturno.android.util.AccountAdapter;
import br.com.saturno.android.util.Bank;
import br.com.saturno.android.util.BankAdapter;
import br.com.saturno.android.util.BankXMLParser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    EditText accDescription;
    private Spinner bankSpinner;
    private Account currentItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        new GetBankListTask().execute();



        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAccountOperation();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onAccountOperation();
    }

    //Action to insert the account on the database. If no balance is defined
    //the balance will be set as ZERO.
    public void insertAccount(View view) {
        accDescription = (EditText) getView().findViewById(R.id.account_description);
        bankSpinner = (Spinner) getView().findViewById(R.id.account_bankSpinner);

        bankSpinner.getSelectedItem();

        if (accountNameValidator("TESTE")) {
            //Account newAcc = new Account(accName.getText().toString(),Double.parseDouble(accBalance.getText().toString()));

            ///new AddAccountTask().execute(newAcc);

            //updateFields("", "");
        }
    }

    /*public void editAccount(View view) {
        accName = (EditText) getView().findViewById(R.id.inputAccName);
        accBalance = (EditText) getView().findViewById(R.id.inputAccBalance);

        if (accountSelectionValidator() && accountNameValidator()) {

            if (accBalance.getText().length() == 0) {
                accBalance.setText("0");
            }

            Account selectedAcc = new Account(currentItem.getId(), accName
                    .getText().toString(), Double.parseDouble(accBalance
                    .getText().toString()));

            new UpdateAccountTask().execute();

            currentItem = null;
            updateFields("","");

        }
    }

    public void deleteAccount(View view) {
        accName = (EditText) getView().findViewById(R.id.inputAccName);
        //accBalance = (EditText) getView().findViewById(R.id.inputAccBalance);

        if (accountSelectionValidator()) {
            if (accountNameValidator()) {

                Account selectedAcc = new Account(currentItem.getId(), accName
                        .getText().toString(), Double.parseDouble(accBalance
                        .getText().toString()));

                new DeleteAccountTask().execute(selectedAcc);

                currentItem = null;
                updateFields("","");
            }
        }
    }*/

    /*public void updateFields(String name, String balance){
        accDescription = (EditText) getView().findViewById(R.id.inputAccName);
        accBalance = (EditText) getView().findViewById(R.id.inputAccBalance);
        accName.setText(name);
        accBalance.setText(balance);
    }*/

    public boolean accountNameValidator(String bankName) {
        accDescription = (EditText) getView().findViewById(R.id.account_description);
        if (accDescription.getText().length() == 0) {
            accDescription.setText(bankName);
            return false;
        }
        return true;
    }

    public boolean accountSelectionValidator() {
        if (currentItem == null) {
            Toast.makeText(getActivity(), "Please select account!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class AddAccountTask extends AsyncTask<Account, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Account... params) {
            AccountDataSource ads = new AccountDataSource(getActivity());

            Boolean result = false;
            for (Account accs:params){
                result = ads.add(accs);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(getActivity(), "Insert successful =)",
                        Toast.LENGTH_SHORT).show();

                mListener.onAccountOperation();
            }else {
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateAccountTask extends AsyncTask<Account, Void, Integer> {

        @Override
        protected Integer doInBackground(Account... params) {
            AccountDataSource ads = new AccountDataSource(getActivity());

            int result = 0;

            for (Account accs:params) {
                result = ads.update(accs);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result > 0) {
                Toast.makeText(getActivity(), "Update successful =)",
                        Toast.LENGTH_SHORT).show();

                mListener.onAccountOperation();
            } else{
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteAccountTask extends AsyncTask<Account, Void, Integer> {

        @Override
        protected Integer doInBackground(Account... params) {
            AccountDataSource ads = new AccountDataSource(getActivity());
            int result = 0;

            for (Account accs:params) {
                result = ads.delete(accs);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result > 0) {
                Toast.makeText(getActivity(), "Delete successful =)",
                        Toast.LENGTH_SHORT).show();

                mListener.onAccountOperation();
            } else{
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetBankListTask extends AsyncTask<Void, Void, List<Bank>> {

        @Override
        protected List<Bank> doInBackground(Void... params) {
            List<Bank> banks = null;
            InputStream istream = null;
            BankXMLParser parser = new BankXMLParser();
            try {
                istream = getActivity().getResources().openRawResource(R.raw.bank_list);
                banks = parser.parse(istream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (istream != null) {
                    try {
                        istream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return banks;
        }

        @Override
        protected void onPostExecute(List<Bank> result) {
            // Inflate the layout for this fragment
            // Typecasting the variable here
            bankSpinner = (Spinner) getActivity().findViewById(R.id.account_bankSpinner);
            // Setting a Custom Adapter to the Spinner
            bankSpinner.setAdapter(new BankAdapter(getActivity(),result));
        }
    }

    public void setCurrentItem(Account currentItem) {
        this.currentItem = currentItem;
    }
}
