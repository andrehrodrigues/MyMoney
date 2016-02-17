package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import br.com.saturno.android.database.datasources.CardDataSource;
import br.com.saturno.android.database.entities.Card;
import br.com.saturno.android.mymoney.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    private EditText last4Digits;
    private EditText balance;
    private String type;
    private Card currentItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        void onFragmentInteraction(Uri uri);
    }

    public void setCard(Card card){
        currentItem = card;
    }

    public Card getCardData(){
        last4Digits = (EditText)getView().findViewById(R.id.card_lastDigitsInput);
        balance = (EditText) getView().findViewById(R.id.card_balanceInput);

        //TODO: Change the ACCOUNT_ID on the instantiation
        return new Card(Integer.parseInt(last4Digits.getText().toString()),
                        type,Double.parseDouble(balance.getText().toString()),0);
    }

    //Action to insert the card on the database. If no balance is defined
    //the balance will be set as ZERO.
    public void insertCard(View view) {
        last4Digits = (EditText)getView().findViewById(R.id.card_lastDigitsInput);
        balance = (EditText) getView().findViewById(R.id.card_balanceInput);

        if (balance.getText().length() == 0) {
            balance.setText("0");
        }
        Card newCard = new Card(Integer.parseInt(last4Digits.getText().toString()),
                type,Double.parseDouble(balance.getText().toString()),0);

        new AddCardTask().execute(newCard);

        updateFields("", "");

    }

    public void updateCard(View view) {
        last4Digits = (EditText)getView().findViewById(R.id.card_lastDigitsInput);
        balance = (EditText) getView().findViewById(R.id.card_balanceInput);

        if (balance.getText().length() == 0) {
            balance.setText("0");
        }
        Card newCard = new Card(currentItem.getId(),Integer.parseInt(last4Digits.getText().toString()),
                type,Double.parseDouble(balance.getText().toString()),0);

        new UpdateCardTask().execute(newCard);

        updateFields("", "");

    }

    public void deleteCard(View view) {
        last4Digits = (EditText)getView().findViewById(R.id.card_lastDigitsInput);
        balance = (EditText) getView().findViewById(R.id.card_balanceInput);

        Card newCard = new Card(currentItem.getId(),Integer.parseInt(last4Digits.getText().toString()),
                type,Double.parseDouble(balance.getText().toString()),0);

        new DeleteCardTask().execute(newCard);

        updateFields("", "");

    }

    public void onCardTypeSelected(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.debitRadioBtn:
                if (checked)
                    type = "Debit";
                    break;
            case R.id.creditRadioButton:
                if (checked)
                    type = "Credit";
                    break;
        }
    }

    private class AddCardTask extends AsyncTask<Card, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Card... params) {
            CardDataSource cds = new CardDataSource(getActivity());

            Boolean result = false;
            for (Card card:params){
                result = cds.add(card);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(getActivity(), "Insert successful =)",
                        Toast.LENGTH_SHORT).show();

                //mListener.onCardOperation();
            }else {
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateCardTask extends AsyncTask<Card, Void, Integer> {

        @Override
        protected Integer doInBackground(Card... params) {
            CardDataSource cds = new CardDataSource(getActivity());

            int result = 0;

            for (Card card:params){
                result = cds.update(card);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result > 0) {
                Toast.makeText(getActivity(), "Update successful =)",
                        Toast.LENGTH_SHORT).show();

                //mListener.onAccountOperation();
            } else{
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteCardTask extends AsyncTask<Card, Void, Integer> {

        @Override
        protected Integer doInBackground(Card... params) {
            CardDataSource cds = new CardDataSource(getActivity());
            int result = 0;

            for (Card card:params){
                result = cds.delete(card);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result > 0) {
                Toast.makeText(getActivity(), "Delete successful =)",
                        Toast.LENGTH_SHORT).show();

                //mListener.onAccountOperation();
            } else{
                Toast.makeText(getActivity(), "Something went wrong =(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateFields(String last4Digits, String balance){
        this.last4Digits = (EditText)getView().findViewById(R.id.card_lastDigitsInput);
        this.balance = (EditText) getView().findViewById(R.id.card_balanceInput);

        this.last4Digits.setText(last4Digits);
        this.balance.setText(balance);

    }
}
