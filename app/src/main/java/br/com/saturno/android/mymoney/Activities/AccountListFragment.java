package br.com.saturno.android.mymoney.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.saturno.android.database.datasources.AccountDataSource;
import br.com.saturno.android.database.entities.Account;
import br.com.saturno.android.mymoney.R;
import br.com.saturno.android.util.AccountAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Account> accountsList;
    ListView accLV;
    AccountAdapter accountArrayAdapter;

    private OnFragmentInteractionListener mListener;

    public AccountListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountListFragment newInstance(String param1, String param2) {
        AccountListFragment fragment = new AccountListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View rootView = inflater.inflate(R.layout.fragment_account_list, container, false);

        accLV = (ListView) rootView.findViewById(R.id.listvAccount);
        accLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Account selectedAcc = (Account) parent.getItemAtPosition(position);
                mListener.onItemSelected(selectedAcc);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Account account) {
        if (mListener != null) {
            mListener.onItemSelected(account);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        new GetAccountsTask().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onItemSelected(Account account);
    }

    public void updateList(){
        new GetAccountsTask().execute();
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
            accountsList = result;
            accLV = (ListView) getView().findViewById(R.id.listvAccount);
            accountArrayAdapter = new AccountAdapter(getActivity(), result);
            accLV.setAdapter(accountArrayAdapter);
        }
    }
}
