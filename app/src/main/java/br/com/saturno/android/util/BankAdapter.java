package br.com.saturno.android.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.saturno.android.mymoney.R;

/**
 * Created by andre on 15/02/2016.
 */
public class BankAdapter extends ArrayAdapter<Bank> {

    Context context;
    List<Bank> bankList;

    public BankAdapter(Context context, List<Bank> bankList){
        super(context,R.layout.bank_list_layout,R.id.bankImage,bankList);
        this.context = context;
        this.bankList = bankList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bank_list_layout, parent, false);
        }

        TextView bankName = (TextView) convertView.findViewById(R.id.bankName);
        ImageView bankLogo = (ImageView) convertView.findViewById(R.id.bankImage);

        Bank bank = this.bankList.get(position);

        bankName.setText(bank.getName());
        //bankLogo.setImageResource(R.drawable.);

        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.bankList.size();
    }

    @Override
    public Bank getItem(int arg0) {
        // TODO Auto-generated method stub
        return bankList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
}
