package trash.jak.id.go.utils;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by itp on 17/10/17.
 */

public class Utils {

    //Utils.Java kumpulan fungsi abstrak,  fungsi ini dapat dipanggil di-class java manapun.
    private Context _context;
    public Utils(Context context) {
        _context = context;
    }
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TimePickerDialog timePickerDialog;
    private String deviceID = "";

    /*
    |-----------------------------------------------------------------------------------------------
    | Popup Dialog for alert no internet connection
    |-----------------------------------------------------------------------------------------------
    */
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Dialog));
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.setIcon(android.R.drawable.stat_sys_warning);
        alert.show();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Dialog for alert request failed, dalam kondisi error
    |-----------------------------------------------------------------------------------------------
    */
    public void showErrorDlg(Handler handler, String message, Context context) {
        final String strMessage = message;
        final Context mContext = context;
        handler.post(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        new ContextThemeWrapper(mContext,
                                android.R.style.Theme_Dialog));
                builder.setMessage(strMessage)
                        .setCancelable(false)
                        .setNegativeButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setIcon(android.R.drawable.stat_sys_warning);
                alertDialog.show();
            }
        });
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to get current date
    |-----------------------------------------------------------------------------------------------
    */
    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to get current date and time
    |-----------------------------------------------------------------------------------------------
    */
    public String getCurrentDateandTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to get current One Day
    |-----------------------------------------------------------------------------------------------
    */
    public Calendar getAfterCurrentOneDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, +0);

        return null;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to convert format date from yyyy-MM-dd to dd MMM yyyy
    |-----------------------------------------------------------------------------------------------
    */
    public static String formatDateReverse(String date) throws ParseException {
        String initDateFormat;
        String endDateFormat;
        initDateFormat = "yyyy-MM-dd";
        endDateFormat = "dd MMM yyyy";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to convert format date from yyyy-MM-dd HH:mm to dd-MM-yyyy HH:mm
    |-----------------------------------------------------------------------------------------------
    */
    public static String formatDateTimeReverse(String date) throws ParseException {
        String initDateFormat;
        String endDateFormat;
        initDateFormat = "yyyy-MM-dd HH:mm";
        endDateFormat = "dd-MM-yyyy HH:mm";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method to convert format date from yyyy-MM-dd HH:mm to dd/MM/yyyy HH:mm:ss
    |-----------------------------------------------------------------------------------------------
    */
    public static String formatDate(String date) throws ParseException {
        String initDateFormat;
        String endDateFormat;
        initDateFormat = "yyyy-MM-dd HH:mm";
        endDateFormat = "dd/MM/yyyy HH:mm:ss";
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for set time field
    |-----------------------------------------------------------------------------------------------
    */
    public void setTimeField(Context context, TextView textView) {
        final TextView time = textView;
        Calendar newCalendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                time.setText(hourOfDay + ":" + minuteOfDay);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for set date field Only Context and Textview
    |-----------------------------------------------------------------------------------------------
    */
    public void setDateField(Context context, TextView textView) {
        final TextView date = textView;
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final SimpleDateFormat simpleDateFormat = dateFormatter;

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for set date field DatePickerDilog and SimpleDateFormat
    |-----------------------------------------------------------------------------------------------
    */
    public void setDateField(Context context, DatePickerDialog datePickerDialog, SimpleDateFormat dateFormatter,
                             TextView textView) {
        final TextView date = textView;
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final SimpleDateFormat simpleDateFormat = dateFormatter;

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Part of Floating Button library
    |-----------------------------------------------------------------------------------------------
    */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Boolean JellyBean
    |-----------------------------------------------------------------------------------------------
    */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Boolean LOLLIPOP
    |-----------------------------------------------------------------------------------------------
    */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Generator MD5
    |-----------------------------------------------------------------------------------------------
    */
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Email Validator
    |-----------------------------------------------------------------------------------------------
    */
    public class EmailValidator {
        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
    public boolean isMyServiceRunning(Class<?> serviceClass1) {
        ActivityManager manager = (ActivityManager) _context.getSystemService(_context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass1.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for scale bitmap
    |-----------------------------------------------------------------------------------------------
    */
    public static Bitmap decodeBitmapFromFile(String filePath,
                                              int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return bm;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for generate file name for image
    |-----------------------------------------------------------------------------------------------
    */
    public String setImageName(String formNumber, String type, String imageName) {
        String name = "";
        name = formNumber + "_" + type + "_" + imageName;
        return name;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for SetIMEI
    |-----------------------------------------------------------------------------------------------
    */
    public void setIMEI() {
        final TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
        SharedPreferences imeiPref = _context.getSharedPreferences("imei", Context.MODE_PRIVATE);
        SharedPreferences.Editor shareEditor = imeiPref.edit();
        shareEditor.putString("device_id", deviceID);
        shareEditor.commit();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for GetIMEI
    |-----------------------------------------------------------------------------------------------
    */
    public String getIMEI() {
        SharedPreferences sessionpref = _context.getSharedPreferences("imei", Context.MODE_PRIVATE);
        deviceID = sessionpref.getString("device_id", "");
        return deviceID;
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method for Random
    |-----------------------------------------------------------------------------------------------
    */
    public static  String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /*
    |-----------------------------------------------------------------------------------------------
    | Method isLocationEnabled
    |-----------------------------------------------------------------------------------------------
    */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    /*
    |-----------------------------------------------------------------------------------------------
    | Method findStringImage
    |-----------------------------------------------------------------------------------------------
    */
    public boolean findStringImage(String source, String keyword) {
        Boolean found = Arrays.asList(source.split("_")).contains(keyword);
        if (found) {
            return true;
        } else {
            return false;
        }
    }
    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.sample.sishin.maplocation";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";
    }



}
