package br.com.saturno.android.mymoney.SMSHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Map;

import br.com.saturno.android.database.entities.Card;
import br.com.saturno.android.database.datasources.CardDataSource;
import br.com.saturno.android.database.entities.Transaction;
import br.com.saturno.android.database.datasources.TransactionDataSource;

/**
 * Created by andre on 31/10/2015.
 */
public class SMSHandlerBroadcastReceiver extends BroadcastReceiver {

    static final String SMS_ACCOUNT_DIGITS = "account";
    static final String SMS_VALUE = "value";
    static final String SMS_DATA = "data";
    static final String SMS_LOCAL = "local";

    Context context;
    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        context = this.context;
        new ProcessSMSTask().execute(intent);
    }

    private class ProcessSMSTask extends AsyncTask<Intent, Integer, Boolean> {

        protected Boolean doInBackground(Intent... intents) {

            boolean success = false;
            SMSData smsMsg = new SMSData();

            for (Intent intent : intents) {
                Bundle intentExtras = intent.getExtras();
                if (intentExtras != null) {
                    Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                    String smsMessageStr = "";
                    for (int i = 0; i < sms.length; ++i) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                        smsMsg.setNumber(smsMessage.getOriginatingAddress());
                        smsMsg.setBody(smsMessage.getMessageBody().toString());

                        Map<String, String> smsData = null;
                        if("25010".equals(smsMsg.getNumber())) {
                            smsData = SMSParser.SMSParserItau(smsMsg);
                        }

                        CardDataSource cDS = new CardDataSource(context);
                        Card card = new Card(Integer.parseInt(smsData.get(SMS_ACCOUNT_DIGITS)),null,0.0,0) ;
                                cDS.get(card);

                        TransactionDataSource tDS = new TransactionDataSource(context);
                        tDS.add(
                                new Transaction(smsData.get(SMS_DATA),
                                        Double.parseDouble(smsData.get(SMS_VALUE).replace(",", ".")),
                                        smsData.get(SMS_LOCAL),
                                        null,
                                        card.getAccountId()
                                )
                        );
                    success = true;
                    }
                }
            }
            return success;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(context,"Downloaded " + result + " bytes", Toast.LENGTH_LONG);
            }
        }
    }
}
