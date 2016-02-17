package br.com.saturno.android.mymoney.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.database.datasources.AccountDataSource;
import br.com.saturno.android.database.entities.Category;
import br.com.saturno.android.database.datasources.CategoryDataSource;
import br.com.saturno.android.database.entities.Transaction;
import br.com.saturno.android.database.datasources.TransactionDataSource;
import br.com.saturno.android.mymoney.R;
import br.com.saturno.android.util.DateUtil;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {

    //Context context;
    static final int DATE_DIALOG_ID = 999;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerAcc, spinnerCat;
    private EditText transactionValue, transactionDate, transactionDesc, trans;
    private Button datePickerBtn, incomeBtn, outcomeBtn;

    /** List of all accounts and categories retrieved in the fragment onStart().
     *  Kept so the ID values can be found easily using their names.
     */
    List<Account> listAcc;
    List<Category> listCat;

    DataSource ds;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);

        setCurrencyMask(rootView);
        setCurrentDateOnView(rootView);

        transactionValue = (EditText) rootView.findViewById(R.id.transaction_valueInput);
        transactionValue.setText("0.00");
        transactionValue.requestFocus();

        datePickerBtn = (Button) rootView.findViewById(R.id.transaction_datePickerBtn);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        incomeBtn = (Button) rootView.findViewById(R.id.transaction_btnIncome);
        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveIncome(rootView);
            }
        });

        outcomeBtn = (Button) rootView.findViewById(R.id.transaction_btnOutcome);
        outcomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveOutcome(rootView);
            }
        });

        // Inflate the layout for this fragment
        return rootView;//inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        new GetAccountsTask().execute();
        new GetCategoriesTask().execute();

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
        void onFragmentInteraction(Uri uri);
    }

    //Creates the currency mask to allow the automatic decimal for the value field.
    public void setCurrencyMask(View view) {
        transactionValue = (EditText) view.findViewById(R.id.transaction_valueInput);
        transactionValue.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    transactionValue.removeTextChangedListener(this);

                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }
                    //String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                    String formatted = String.valueOf(parsed / 100);

                    current = formatted;
                    transactionValue.setText(formatted);
                    transactionValue.setSelection(formatted.length());
                    transactionValue.addTextChangedListener(this);
                }
            }
        });

    }

    //Set the current date on the view when the screen is called.
    public void setCurrentDateOnView(View view) {
        transactionDate = (EditText) view.findViewById(R.id.transaction_dateInput);
        // set current date into textview
        transactionDate.setText(DateUtil.getCurrentDate());
    }

    //Class that implements the datepicker
    @SuppressLint("ValidFragment")
    private class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            transactionDate.setText(day+"/"+month+"/"+year);
        }

    }

    //Get the account list on the DB to fill the spinner
    private class GetAccountsTask extends AsyncTask<Void, Void, List> {

        @Override
        protected List doInBackground(Void... params) {
            AccountDataSource accOp = new AccountDataSource(getActivity());
            List<Account> accs = accOp.getAll();
            return accs;
        }

        @Override
        protected void onPostExecute(List result) {
            listAcc = result;
            List<String> listAcc = Account.convertAccountList(result);
            spinnerAcc = (Spinner) getView().findViewById(R.id.transaction_spinnerAccount);
            ArrayAdapter<String> accDataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listAcc);
            accDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAcc.setAdapter(accDataAdapter);
            if(!result.isEmpty()) {
                spinnerAcc.setSelection(0);
            }
        }
    }

    //Get the account list on the DB to fill the spinner
    private class GetCategoriesTask extends AsyncTask<Void, Void, List> {

        @Override
        protected List doInBackground(Void... params) {
            CategoryDataSource catOp = new CategoryDataSource(getActivity());
            List<Category> cats = catOp.getAll();
            return cats;
        }

        @Override
        protected void onPostExecute(List result) {
            listCat = result;
            List<String> listCat = Category.convertCategoryList(result);
            spinnerCat = (Spinner) getView().findViewById(R.id.transaction_spinnerCat);
            ArrayAdapter<String> catDataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listCat);
            catDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCat.setAdapter(catDataAdapter);
        }
    }

    // Validates if the field VALUE for the transaction is set
    public boolean transactionValueValidator() {
        transactionValue = (EditText) getActivity().findViewById(R.id.transaction_valueInput);
        if (transactionValue.getText().length() == 0) {
            transactionValue.setError("Please insert a value!");
            return false;
        }
        return true;
    }

    // Validates if the field DATE for the transaction is set
    public boolean transactionDateValidator() {
        transactionDate = (EditText) getActivity().findViewById(R.id.transaction_dateInput);
        if (transactionDate.getText().length() == 0) {
            transactionDate.setError("Please inform a date for the transaction!");
            return false;
        }
        return true;
    }

    // Test date and value validations together
    public boolean transactionValidator() {
        if (transactionValueValidator() && transactionDateValidator()) {
            return true;
        }
        return false;
    }

    //Changes the value to negative when the user defines the moviment as an outcome
    public void saveOutcome(View view) {
        if (transactionValueValidator()) {
            transactionValue = (EditText) view.findViewById(R.id.transaction_valueInput);
            Double minus = (-1)
                    * Double.parseDouble(transactionValue.getText().toString());
            transactionValue.setText(String.valueOf(minus));
            saveIncome(view);
        }
    }

    //Save the movement. If an account and/or category is not selected the values are set in the database as -1
    //In further development the definitions for the IDs will be changed to support NULL to be inserted in the database.
    public void saveIncome(View view) {
        if (transactionValidator()) {
            transactionDate = (EditText) view.findViewById(R.id.transaction_dateInput);
            transactionValue = (EditText) view.findViewById(R.id.transaction_valueInput);
            transactionDesc = (EditText) view.findViewById(R.id.transaction_descInput);
            spinnerAcc = (Spinner) view.findViewById(R.id.transaction_spinnerAccount);
            spinnerCat = (Spinner) view.findViewById(R.id.transaction_spinnerCat);

            Integer accId = null;
            Integer catId = null;
            if (spinnerAcc.getSelectedItemPosition() != -1) {
                Account acc = listAcc.get(listAcc.indexOf(String.valueOf(spinnerAcc.getItemAtPosition(spinnerAcc.getSelectedItemPosition()).toString())));
                accId = acc.getId();
                // Adds or subtracts the value of the transaction to the
                // selected account
                alterAccountValue(acc,Integer.parseInt(transactionValue.getText().toString()));
            }
            if (spinnerCat.getSelectedItemPosition() != -1) {
                Category cat = listCat.get(listCat.indexOf(String.valueOf(spinnerCat.getItemAtPosition(spinnerCat.getSelectedItemPosition()).toString())));
                catId = cat.getId();
            }

            Transaction newTransaction = new Transaction(
                    transactionDate.getText().toString(),
                    Double.parseDouble(transactionValue.getText().toString()),
                    transactionDesc.getText().toString(),
                    catId,
                    accId);

            new SaveTransactionTask().execute(newTransaction);

        }
    }

    public void alterAccountValue(Account acc, double value) {
        double balanceFinal = acc.getBalance() + value;
        acc.setBalance(balanceFinal);
        ds = new AccountDataSource(getView().getContext());
        ds.update(acc);
    }

    //Get the account list on the DB to fill the spinner
    private class SaveTransactionTask extends AsyncTask<Transaction, Void, Void> {

        @Override
        protected Void doInBackground(Transaction... params) {
            for(Transaction transactions: params){
                TransactionDataSource tds = new TransactionDataSource(getActivity());
                tds.add(transactions);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setCurrentDateOnView(getView());
            transactionValue.setText("");
            transactionDesc.setText("");
            spinnerAcc.setSelection(0);
            spinnerCat.setSelection(0);
            Toast.makeText(getActivity(), "Insert successful!", Toast.LENGTH_SHORT).show();
        }
    }

}
