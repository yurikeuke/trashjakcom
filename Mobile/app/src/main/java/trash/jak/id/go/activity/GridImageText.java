package trash.jak.id.go.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import trash.jak.id.go.R;
import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.utils.ConnectionManager;

/**
 * Created by itp on 07/12/17.
 */

public class GridImageText extends AppCompatActivity implements View.OnClickListener {

    ///////////////////////////   MENU Image  //////////////////////////
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGES= {"http://myshaf.com/images/trashjak3.jpg","http://myshaf.com/images/trashjak2.jpg","http://myshaf.com/images/trashjak1.jpg"};
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    ///////////////////////////////////
    private Toolbar _toolbar;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private String emaildb, username, sessiond, name, personName, personPhotoUrl, email, key,status;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    GridView androidGridView;
    private ImageView mIvMain;
    private JSONObject jsonObject, jsonData;
    String[] scan = { "Take Camera", "Input Barcode" };
    String[] gridViewString = {
            "Event",  "Tips","Perusahaan",
            "Jenis Sampah","Kuantitas Sampah"
    } ;
    int[] gridViewImageId = {
            R.drawable.ic_calender, R.drawable.tips,R.drawable.perusahaan,
            R.drawable.jenissampah, R.drawable.kuantitas

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_information);
        _toolbar    = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Infografis");

        SharedPreferences shered = GridImageText.this.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        sessiond = shered.getString("session",null);
        emaildb = shered.getString("email", null);
        name = shered.getString("name",null);
        token();
        firebase();
        ActionBar actionBar = getSupportActionBar();
//      mIvMain = (ImageView) findViewById(R.id.imageView);
        init();

        Gridview_Activity adapterViewAndroid = new Gridview_Activity(GridImageText.this, gridViewString, gridViewImageId);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        //   androidGridView.setLayoutParams(new GridView.LayoutParams(GridView.width, YOUR_HEIGHT))
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                if (gridViewString[+i]==("Event")) {
                    Intent intent = new Intent(GridImageText.this, EventFoto.class);
                    startActivity(intent);
                } else if (gridViewString[+i]==("Tips")) {

                    Intent intent = new Intent(GridImageText.this, TipsFoto.class);
                    startActivity(intent);
                } else if (gridViewString[+i]==("Perusahaan")) {
                   Intent intent = new Intent(GridImageText.this, Building.class);
                    startActivity(intent);
                } else if (gridViewString[+i]==("Jenis Sampah")) {
                    Intent intent = new Intent(GridImageText.this, ImJnsSampah.class);
                    startActivity(intent);
                } else if (gridViewString[+i]==("Kuantitas Sampah")) {
                    Intent intent = new Intent(GridImageText.this, ImJmlahSmpah.class);
                    startActivity(intent);
                }   else {
                    Toast.makeText(GridImageText.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(GridImageText.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent startMain = new Intent(GridImageText.this,MainActivity.class);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
    private void  token () {
        FirebaseInstanceId.getInstance().getToken();
        try {
            JSONObject obj = new JSONObject(FirebaseInstanceId.getInstance().getToken());
            key = obj.getString("session");
        } catch (Exception e) {
            key = FirebaseInstanceId.getInstance().getToken();
            e.printStackTrace();
        }
    }
    private void firebase() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_FIREBASE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getString("status_code").equalsIgnoreCase("000")) {
                        jsonObject = new JSONObject(response);
                        status = jsonObject.getString("status");


                    } else {
                        // error in fetching chat rooms
                        //Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("session", sessiond);
                params.put("firebase_key", key);


                Log.e(TAG, "paramsFirebase: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }
}

