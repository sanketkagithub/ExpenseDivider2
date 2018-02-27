package com.busyprojects.roomies.helper;

import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by sanket on 12/23/2017.
 */

public class SessionManager {

    public static final String FILE_WTC = "file";
    public static final String UID = "uid";
    public static final String HID = "hid";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "adddress";
    public static final String MOBILE = "mobile";
    public static final String TOTAL_ROOMMATES = "totalRoomates";
    public static final String IS_ALLOWED = "isallowed";
    public static final String NOTIF_ID = "notifId";
    public static final String BID = "bookingId";
    public static final String APPOINTMENT_ADDRESS = "appointmentAddress";
    public static final String APPOINTMENT_TIME = "appointmentTime";
    public static final String PROFILE_PIC = "profilePic";
    public static final String APP_COLOR = "appColor";
    public static final String DEFAULT_APP_COLOR = "#89003f";


    public static final String SKY_BLUISH = "#03abff";
    public static final String GREENISH = "#48bc00";
    public static final String YELLOWISH = "#deda00";
    public static final String BLACKISH = "#2c2c2c";
    public static final String REDISH = "#e30008";
    public static final String PINKISH = "#e7009a";
    public static final String VOILETISH = "#5102c0";


    public static final String IS_TRANSFER = "isTransfer";


    public static final String USER_TYPE = "userType";
    public static final String ADMIN = "admin";
    public static final String IV_ITEM = "iv_item";


    // TODO: 2/26/2018 images in session 
    public static final String IV_ROOMY = "iv_roomy";
    public static final String IV_MOBILE = "iv_mobile";
    public static final String CUSTOM_CURSOR = "customCursior";


    public static final String IV_RUPEE = "iv_rupee";
    public static final String IV_PAYING_ITEM = "iv_paying_item";


    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SELECTED_USER_NAME = "selectedUserName";
    public static final String SELECTED_USER_MOBILE = "selectedUserMobile";

    public static void setCursorColor(EditText editText, int cursorBackDrawable) throws NoSuchFieldException, IllegalAccessException {
        Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
        f.setAccessible(true);
        f.set(editText, cursorBackDrawable);
    }


}
