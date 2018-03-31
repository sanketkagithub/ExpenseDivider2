package com.busyprojects.roomies.helper;

import android.content.Context;
import android.widget.Toast;

import com.busyprojects.roomies.pojos.master.History;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by sanket on 12/23/2017.
 */

public class Helper {

    public static final String USER = "User";
    public static final String PAYMENT_LIST = "paymentList";
    public static final String MOBILE_LOGGED = "mobileLogged";
    public static final String MOBILE = "mobile";
    public static final String PAYMENT_NOTIFICATION = "PaymentNotification";
    public static final String ROOMY = "Roomy";
    public static final String SELECT_ROOMY = "Select Roomy";
    public static final String EACH_TOTAL_PAMENT = "EachTotalPayment";
    public static final String AMOUNT_VARIATION = "amountVariation";
    public static final String TOTAL_PAID = "amountTg";
    public static final String NAME = "name";
    public static int CURRENT_TAB = 0;
    public static final String APP_INFO = "AppInfo";
    public static final String INFO = "info";
    public static final String APPROX_CONF_TIME_BY_ADMIN = "approxConfirmedTimeByAdmin";

    public static final String AFTER_TRANSFER = "AfterTransfer";
    public static final String CURRENT_BID = "currentBookingId";
    public static final String DRIVER_ID = "driverId";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String ON_DUTY = "onDuty";
    public static final String PENDING = "pending";
    public static final String PAYMENT = "Payment";

    public static final String PAYMENT_HISTORY = "PaymentHistory";
    public static final String PAYMENT_PAYTG = "PaymentPayTg";
    public static final String HISTORY = "History";
    public static final String IS_TRANSFER = "isTransfer";

    public static final String SELECT_DATE = "Select Date";
    public static final String SELECT_TIME = "Select Time";
    public static final String CONFIRMED = "confirmed";
    public static final String COMPLETED = "completed";

    public static final String BOOOKIND_STATUS = "bookingStatus";
    public static final String CONFIRMATION_SHOWN = "confirmationShown";

    public static final String USER_ALLOWED = "userAllowed";
    public static final String PAYING_ITEMS = "PayingItems";


    public static final String SDF_FORMAT = "HH:mm:ss dd-MMM-yyyy";

    static String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    public  static void showCheckInternet(Context context)
    {
        Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
    }


    static String NOTIF = "0123456789";

    public static String randomNotifId(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(NOTIF.charAt(rnd.nextInt(NOTIF.length())));
        return sb.toString();
    }

    public static DatabaseReference getFirebaseDatabseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static final String EMPTY_FIELD = "empty";
    public static final String REGISTERD = "registered";

    public static String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Helper.SDF_FORMAT);
        String dateTimeCurrent = simpleDateFormat.format(new Date());
        return dateTimeCurrent;
    }

    Date date1, date2;

    public List<Payment> getSortedTransactionList(List<Payment> paymentList) {


        Collections.sort(paymentList, new Comparator<Payment>() {
            @Override
            public int compare(Payment o1, Payment o2) {

                SimpleDateFormat sdf = new SimpleDateFormat(Helper.SDF_FORMAT);
                try {
                    date1 = sdf.parse(o1.getPaymentDateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    date2 = sdf.parse(o2.getPaymentDateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                return date1.compareTo(date2);
            }
        });


        return paymentList;

    }


    public List<History> getSortedHistoryList(List<History> historyList) {


        Collections.sort(historyList, new Comparator<History>() {
            @Override
            public int compare(History o1, History o2) {

                SimpleDateFormat sdf = new SimpleDateFormat(Helper.SDF_FORMAT);
                try {
                    date1 = sdf.parse(o1.getDateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    date2 = sdf.parse(o2.getDateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                return date1.compareTo(date2);
            }
        });


        return historyList;

    }


    public static List<PayTg> getSortedPaymentTakeGiveList(List<PayTg> payTgList) {


        Collections.sort(payTgList, new Comparator<PayTg>() {
            @Override
            public int compare(PayTg o1, PayTg o2) {
                return o1.getRoomyName().compareTo(o2.getRoomyName());
            }
        });


        return payTgList;

    }


//    public static String getMaccAddress(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wInfo = wifiManager.getConnectionInfo();
//        String macAddress = wInfo.getMacAddress();
//
//        return  macAddress;
//    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


   public double  getRoundedOffValue(double input)
    {
        return Math.round(input * 100.0) / 100.0;

    }





    public static void setRemoteIstransfer(String mobileLogged,boolean isTransferValue)
    {
       DatabaseReference dbRef = getFirebaseDatabseRef();

       dbRef.child(SessionManager.IS_TRANSFER).child(mobileLogged).setValue(isTransferValue);

    }















}

