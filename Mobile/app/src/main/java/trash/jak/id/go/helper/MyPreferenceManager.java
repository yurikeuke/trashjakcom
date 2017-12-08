package trash.jak.id.go.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import trash.jak.id.go.model.User;

/**
 * Created by itp on 17/10/17.
 */

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref, prefNia;

    // Editor for Shared preferences
    SharedPreferences.Editor editor, editorNia;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Sharedpref file name
    private static final String PREF_NAME = "user_amil";

    // Sharedpref file nia
    private static final String PREF_Nia = "user_nia";

    // All Shared Preferences Keys
    private static final String KEY_USER_Nia = "nia";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "amil_email";
    private static final String KEY_USER_PROVINCES = "provinces_code";

    private static final String KEY_USER_CITIES = "cities_code";
    private static final String KEY_USER_INTITUTION_CODE = "institution_types_code";
    private static final String KEY_USER_INSTITUTION_NO = "institution_serial_no";
    private static final String KEY_USER_PHONE = "phone";


    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefNia = _context.getSharedPreferences(PREF_Nia, PRIVATE_MODE);

        editor = pref.edit();
        editorNia = prefNia.edit();
    }

    public void storeUser(User user) {
        editor.putString(KEY_USER_Nia, user.getNia());
        editor.putString(KEY_USER_NAME, user.getNama());
        editor.putString(KEY_USER_EMAIL, user.getAmilEmail());
        //editor.putString(KEY_USER_PROVINCES, user.getProvinces_code());

        /*editor.putString(KEY_USER_CITIES, user.getCities_code());
        editor.putString(KEY_USER_INTITUTION_CODE, user.getInstitution_types_code());
        editor.putString(KEY_USER_INSTITUTION_NO, user.getInstitution_serial_no());*/
        editor.putString(KEY_USER_PHONE, user.getPhone());

        editor.commit();
        editorNia.putString(KEY_USER_Nia, user.getNia());
        editorNia.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getNama() + ", " + user.getAmilEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_Nia, null) != null) {

            String nia,nama, amil_email, provinces_code, cities_code, institution_types_code, institution_serial_no, phone, photo;

            nia = pref.getString(KEY_USER_Nia, null);
            nama = pref.getString(KEY_USER_NAME, null);
            amil_email = pref.getString(KEY_USER_EMAIL, null);
            provinces_code = pref.getString(KEY_USER_PROVINCES, null);
            cities_code = pref.getString(KEY_USER_CITIES, null);
            institution_types_code = pref.getString(KEY_USER_INTITUTION_CODE, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            institution_serial_no = pref.getString(KEY_USER_INSTITUTION_NO, null);
            photo = pref.getString(KEY_USER_EMAIL, null);


            User user = new User(nama, amil_email, photo, nia, phone);
            return user;
        }
        return null;
    }
    public String getPREF_Nia () {
        if (prefNia.getString(KEY_USER_Nia, null) != null) {
            String nia = prefNia.getString(KEY_USER_Nia, null);
            return nia;
        }
        return null;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
