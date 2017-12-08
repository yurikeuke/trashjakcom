package trash.jak.id.go.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import trash.jak.id.go.R;
import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.utils.ConnectionDetector;
import trash.jak.id.go.utils.ConnectionManager;

import static android.content.ContentValues.TAG;

/**
 * Created by itp on 17/10/17.
 */

public class  SplashScreen extends Activity {
    private ProgressBar mProgress;
    private String sessiond, sessionVersi, _nameVersi;
    final String appPackageName = "trash.jak.id.go";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    //Cek Internet
    Boolean isInternetPresent = false;
    ConnectionDetector connectionDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.activity_splash_screen);

        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        SharedPreferences shered = SplashScreen.this.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        sessiond = shered.getString("session", null);
        // Start lengthy operation in a background thread
        connectionDetector = new ConnectionDetector(SplashScreen.this);
        isInternetPresent = connectionDetector.isConnectingToInternet();
        if (isInternetPresent){
            version();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    doWork();
                    startApp();
                    finish();
                }
            }).start();
        }

    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=25) {
            try {
                Thread.sleep(900);
                mProgress.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
                //timber.e(e.getMessage());
            }
        }
    }

    private void startApp() {
        if (sessiond ==null || sessiond.equalsIgnoreCase("")){
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void AlertDilog1 () {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Aplikasi Versi Baru Tersedia")
                .setMessage("Maaf aplikasi versi ini sudah tidak didukung, silahkan melakukan update aplikasi TrashJak versi terbaru "+_nameVersi+".")
                .setCancelable(false)
                .setIcon(R.drawable.trashman)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(SplashScreen.this, "Open Playstore", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        new Thread(new Runnable() {
                            public void run() {
                                doWork();
                                startApp();
                                finish();
                            }
                        }).start();

                    }
                });
        builder.create().show();
    }
    private void AlertDilog2 () {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Aplikasi Versi Baru Tersedia")
                .setMessage("Maaf aplikasi versi ini sudah tidak didukung, untuk melanjutkan silahkan melakukan update aplikasi TrashJak versi terbaru "+_nameVersi+".")
                .setCancelable(false)
                .setIcon(R.drawable.trashman)
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(SplashScreen.this, "Open Playstore", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        finish();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashScreen.this.finish();

                    }
                });
        builder.create().show();
    }

    private void version() {
        StringRequest strReqa = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_GET_VERSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    //  JSONArray jsonArray = obj.getJSONArray("info");
                    JSONObject onlineDetail = (JSONObject) obj.getJSONObject("info");

                 /*  for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject chObj = (JSONObject) jsonArray.get(i);*/
                    String _sessionversionId = onlineDetail.getString("version");
                    _nameVersi = onlineDetail.getString("name");
                    if (_sessionversionId.equalsIgnoreCase("1")) {
                        AlertDilog1();
                    } else if (_sessionversionId.equalsIgnoreCase("2")){
                        AlertDilog2();
                    }else {
                        new Thread(new Runnable() {
                            public void run() {
                                doWork();
                                startApp();
                                finish();
                            }
                        }).start();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    //Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // params.put("username", MyApplication.getInstance().getPrefManager().getUser().getUsername());
                // params.put("session", sessiond);


                // Log.e(TAG, "paramsFirebase: " + params.toString());
                return params;
            }
        };
        //code jika volley timeout
        strReqa.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReqa);
    }

 }