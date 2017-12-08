package trash.jak.id.go.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import trash.jak.id.go.R;
import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.model.User;
import trash.jak.id.go.utils.ConnectionDetector;
import trash.jak.id.go.utils.ConnectionManager;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {


    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView, signup;
    private ProgressDialog mProgressDialog;
    private String email,name,formattedDate, imei, status, datetime, sessiond, key, nama_email,personPhotoUrl;
    private TelephonyManager mTelephonyManager;

    //Cek Internet
    Boolean isInternetPresent = false;
    ConnectionDetector connectionDetector;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;

    final Context context = this;
    private static final String TAG_SESSION = "session";
    private static final String KEY_SESSION = "session";
    private static final String TAG_STATUS_CODE = "status_code";
    private boolean isTaskRunning = false;
    WebView web;
    private JSONObject jsonObject, jsonData;
    private Button buttonsignup, button;
    private EditText namaform,emailform,hpform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (LoginActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                askPermissions();
            } else {
                getDeviceImei();
            }
        } else {
            getDeviceImei();
        }
//        token();
        connectionDetector = new ConnectionDetector(this);
        isInternetPresent = connectionDetector.isConnectingToInternet();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        setDateTimeField1();
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);


        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        button = (Button) findViewById(R.id.sign_in_button);
        namaform = (EditText) findViewById(R.id.editTextNama);
        emailform = (EditText) findViewById(R.id.editTextEmail);
        hpform = (EditText) findViewById(R.id.editTextHp);
        // [END customize_button]
        signup = (TextView) findViewById(R.id.sign_up);
        signup.setOnClickListener(dialogfromsignup);


    }


    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private View.OnClickListener dialogfromsignup = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.customform, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                   /* namaform.setText(userInput.getText());
                                    emailform.setText(userInput.getText());
                                    hpform.setText(userInput.getText());*/
                                    dialog.cancel();
                                    Toast.makeText(LoginActivity.this, "Terkirim", Toast.LENGTH_LONG).show();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();



        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        connectionDetector = new ConnectionDetector(LoginActivity.this);
        isInternetPresent = connectionDetector.isConnectingToInternet();
        if (isInternetPresent){
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                email = acct.getEmail();
                if (acct.getPhotoUrl()==null) {
                    personPhotoUrl = "";

                }else {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                }
                //pengkondisian jika 2 hal yang berbeda   || lambang ini untuk pengkondisian atau
                if (email.contains("@trashjak.com") || email.contains("@gmail.com")) {
                    //requestLogin(ConnectionManager.CM_URL_LOGIN);

                    //sementara sebelum menggi
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("session", sessiond);
                    i.putExtra("email", email);
                    setUserSesson();
                    startActivity(i);
                    LoginActivity.this.finish();
                } else {
                    showAlert();
                    signOut();
                    LoginActivity.this.finish();
                }

                //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getEmail()));
                //updateUI(true);
            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(getApplicationContext(), "Maaf, anda harus login kembali", Toast.LENGTH_SHORT).show();
                updateUI(false);
            }
        } else  {
            Toast.makeText(getApplicationContext(), "Maaf, Periksa Koneksi Internet anda.", Toast.LENGTH_SHORT).show();
            updateUI(false);
        }
    }
    //Request Data From Server
    private void requestLogin(String url) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("email", email);
            //Log.e(TAG, "params: " + params.toString());
            url = ConnectionManager.requestUrl(url, params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getString("status_code").equalsIgnoreCase("000")) {
                        jsonObject = new JSONObject(response);
                        status = jsonObject.getString("status");
                        nama_email = jsonObject.getString("name");
                        email = jsonObject.getString("email");
                        sessiond = jsonObject.getString("session");
                        if (nama_email.equalsIgnoreCase("")||nama_email.isEmpty()||nama_email.equalsIgnoreCase(" ")){
                            Toast.makeText(LoginActivity.this, "Sorry Forbiden Access - Email Not Found", Toast.LENGTH_SHORT).show();
                            signOut();
                        }else {
                            // userid = jsonObject.getString("userid");
                            setUserSesson();
                            SharedDate();
                            Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                            User user = new User(nama_email, email, "foto", "id", "phone");
                            //Cara baru untuk setting sharedpreferens
                            MyApplication.getInstance().getPrefManager().storeUser(user);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("session", sessiond);
                            i.putExtra("email", email);
                            startActivity(i);
                            LoginActivity.this.finish();
                        }
                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(LoginActivity.this, "Error Login, silahkan tunggu beberapa saat" , Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast toast = Toast.makeText(LoginActivity.this, "Maaf, " + e.getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(LoginActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginActivity.this, "Error Network, mohon periksa kembali jaringan internet anda.", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
/*                params.put("datetime", formattedDate);
                params.put("key", Utils.getSaltString());*/
                //Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    //Sesson login
    private void setUserSesson() {
        SharedPreferences shered = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor sheredEdit = shered.edit();
        sheredEdit.putString("session", sessiond);
        sheredEdit.putString("email", email);
        sheredEdit.putString("name", nama_email);
        sheredEdit.putString("photo", personPhotoUrl.toString());
        sheredEdit.commit();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Memeriksa...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
        mProgressDialog.dismiss();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pastikan email yang anda gunakan adalah @trashjak.com atau @gmail.com")
                .setCancelable(false)
                .setIcon(R.drawable.gmail)
                .setTitle("Email Belum Terdaftar")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText("Assalamu`alaikum");
            // findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(LoginActivity.this,
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            /*case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;*/
        }
    }
    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_PHONE_STATE",
                "android.permission.CAMERA"

        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    private void getDeviceImei() {
        mTelephonyManager = (TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = mTelephonyManager.getDeviceId();
        Log.d("msg", "DeviceImei " + imei);
    }

 /*   private void date(){
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        setDateTimeField1();
    }*/
    private void setDateTimeField1() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(LoginActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                //_textDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void SharedDate() {
        SharedPreferences shered = getSharedPreferences("isLoginDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor sheredEdit = shered.edit();
        sheredEdit.putString("datelogin", formattedDate);
        sheredEdit.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginActivity.this.finish();
    }
}