package com.busyprojects.roomies.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sanket on 12/23/2017.
 */

public class Helper {

    public static final String USER = "User";
    public static final String NAME = "name";
    public static final String ROOMY = "Roomy";
    public static final String BOOK = "Book";
    public static final String DRIVER = "driver";
    public static final String DRIVER_IMAGE = "driverImage";
    public static final String ALLOWED = "Allowed";
    public static final String BLOCKED = "Blocked";
    public static  int CURRENT_TAB = 0;
    public static final String ADMIN_ACCESS = "AdminAccess";
    public static final String APPROX_CONF_TIME_BY_ADMIN = "approxConfirmedTimeByAdmin";

    public static final String PROFILE_PIC = "profilePic";
    public static final String CURRENT_BID = "currentBookingId";
    public static final String DRIVER_ID = "driverId";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String ON_DUTY = "onDuty";
    public static final String PENDING = "pending";
    public static final String PAYMENT = "Payment";
    public static final String SELECT_DATE = "Select Date";
    public static final String SELECT_TIME = "Select Time";
    public static final String CONFIRMED = "confirmed";
    public static final String COMPLETED = "completed";

    public static final String BOOOKIND_STATUS = "bookingStatus";
    public static final String CONFIRMATION_SHOWN = "confirmationShown";

    public static final String USER_ALLOWED = "userAllowed";
    public static final String USER_INSIDE = "userInside";
    public static final long ADMIN_NOTIFICATION_INTERVAL = 10000;
    public static final long MY_STATUS_INTERVAL = 10000;
    public static final long USER_ENABLED_INTERVAL = 10000;
    public static final long LATLONG_INTERVAL = 10000;
    public static final long CHECK_BOOKING_DATA_INSERTION_INTERVAL = 10000;
    public static final String NOTIFICATION_ID = "notificationId";
public static final String CLEANING_COMPLETION_CONFIRMATION_BY_USER = "cleaningDoneConfirmedByUser";
    public static final String ADMIN1_MOBILE = "11";
    public static final String ADMIN2_MOBILE = "22";
    public static final String ADMIN3_MOBILE = "33";

  public static final String ADMIN_MOBILE_LIST = "aDMINmOBILES";


    public static final String SDF_FORMAT = "HH:mm:ss dd-MMM-yyyy";

    static String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
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

    public static String getCurrentDateTime()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Helper.SDF_FORMAT);
        String dateTimeCurrent = simpleDateFormat.format(new Date());
return dateTimeCurrent;
    }

}

