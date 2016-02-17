package br.com.saturno.android.mymoney.SMSHandler;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andre on 02/11/2015.
 */
public class SMSParser {

    public static Map<String,String> SMSParserItau(SMSData smsMessage){

        //String msg = "ITAU DEBITO: Cartao final 3258 COMPRA APROVADA 01/11 18:15:20 R$ 26,00 LOCAL: BOI BEER. Consulte tambem pelo celular.";

        Pattern pCardNun = Pattern.compile(".*(Cartao\\sfinal\\s)(\\d\\d\\d\\d).*");
        Pattern pData = Pattern.compile(".*(\\d{2}/\\d{2}).*");
        Pattern pValue = Pattern.compile(".*(R\\$ )(\\d+\\,\\d+).*");
        Pattern pLocal = Pattern.compile(".*(LOCAL: )(.[^.]+).*");
        Pattern pWithdraw = Pattern.compile(".*SAQUE.*");

        Matcher mCardNun = pCardNun.matcher(smsMessage.getBody());
        Matcher mData = pData.matcher(smsMessage.getBody());
        Matcher mValue = pValue.matcher(smsMessage.getBody());
        Matcher mLocal = pLocal.matcher(smsMessage.getBody());
        Matcher mWithdraw = pWithdraw.matcher(smsMessage.getBody());

        Map<String, String> smsData = new HashMap<>();

        try {
            if(mCardNun.matches() && mData.matches() && mLocal.matches() && mValue.matches()) {
                smsData.put(SMSHandlerBroadcastReceiver.SMS_ACCOUNT_DIGITS, mCardNun.group(2));
                smsData.put(SMSHandlerBroadcastReceiver.SMS_DATA, mData.group(1));
                smsData.put(SMSHandlerBroadcastReceiver.SMS_VALUE, mValue.group(2));
                //SE saque = 1 então local = "Saque" + local (o campo comentario vai ficar: Saque no local X)
                if(mWithdraw.find()){
                    smsData.put(SMSHandlerBroadcastReceiver.SMS_LOCAL, "Saque em: "+mLocal.group(2));
                }else{
                    smsData.put(SMSHandlerBroadcastReceiver.SMS_LOCAL,  mLocal.group(2));
                }
            }else{
                Log.e("SMSParser", "Could not find all matches");
            }
        }catch (Exception e){
            Log.e("SMSParser",e.toString());
        }

        System.out.println(mCardNun.matches() + " " + mData.matches() + " " + mLocal.matches() + " " + mValue.matches());
        System.out.println(mCardNun.groupCount() + " " + mData.groupCount() + " " + mLocal.groupCount() + " " + mValue.groupCount());

        return smsData;
    }

}
