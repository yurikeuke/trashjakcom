package trash.jak.id.go.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import trash.jak.id.go.R;
import trash.jak.id.go.adapter.SlidingImage_Adapter;
import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.model.MapsMainModel;
import trash.jak.id.go.utils.ConnectionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayout llProfileLayout;
    private String key, status, sessiond, emaildb, foto, _cekdate, formattedDate;
    private JSONObject jsonObject;
    private TextView txtEmail, txtnama, txtDate, txtPusat, txtProv, txtKabkot, txtLaz, txtjudul, txtNews;
    public static String personPhotoUrl;
    private ProgressDialog mProgressDialog;
    private ImageView imgProfilePic;
    private DrawerLayout mDrawerLayout;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGES = {"http://myshaf.com/images/trashjak1.jpg", "http://myshaf.com/images/trashjak2.jpg", "http://myshaf.com/images/trashjak3.jpg"};
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private Marker locationMarker;
    private CardView cinformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      cekSession();
        cinformation = (CardView) findViewById(R.id.information);
        cinformation.setVisibility(View.GONE);
        news();
        mapsOnline();
        date();
        useronline();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        slidingImage();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                useronline();

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);

        final Handler handlerMap = new Handler();
        final Runnable UpdateMap = new Runnable() {
            public void run() {
                mapsOnline();
                news();
            }
        };
        Timer swipeTimerMap = new Timer();
        swipeTimerMap.schedule(new TimerTask() {
            @Override
            public void run() {
                handlerMap.post(UpdateMap);
            }
        }, 30000, 30000);


        FloatingActionButton fabMain = (FloatingActionButton) findViewById(R.id.menuDrawer);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sheredDate = MainActivity.this.getSharedPreferences("isLoginDate", Context.MODE_PRIVATE);
        _cekdate = sheredDate.getString("datelogin", null);

        SharedPreferences shered = MainActivity.this.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        sessiond = shered.getString("session", null);
        emaildb = shered.getString("email", null);
        foto = shered.getString("photo", null);
        token();
        firebase();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate = (TextView) findViewById(R.id.mainDate);
        txtDate.setText(formattedDate);


        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header_main, null, false);
        View hView = navigationView.getHeaderView(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                askPermissions();
            }
        }
        imgProfilePic = (ImageView) hView.findViewById(R.id.profile_image);
        txtnama = (TextView) hView.findViewById(R.id.usernameheader);

        txtEmail = (TextView) hView.findViewById(R.id.emailheader);
        txtEmail.setText(emaildb);

        foto();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PROFILE))
                //               .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        //Pengkondisian Harian Jika Hari berganti, maka ke logout//
        //cekDate();
        ////////
        //cekSession();
       /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backButtonHandler();
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.report) {
            Intent intent = new Intent(MainActivity.this, SayaPeduli.class);
            startActivity(intent);
        } else if (id == R.id.banksampah) {
            Intent intent = new Intent(MainActivity.this, BankTrash.class);
            startActivity(intent);
        } else if (id == R.id.home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.information) {
            Intent intent = new Intent(MainActivity.this, GridImageText.class);
            startActivity(intent);
        } else if (id == R.id.trucktrash) {
            Intent intent = new Intent(MainActivity.this, MapsTruck.class);
            startActivity(intent);
        }else if (id == R.id.performance) {
            Intent intent = new Intent(MainActivity.this, Ratting.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            signOut();
            MainActivity.this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        SharedPreferences preferences3 = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = preferences3.edit();
                        editor2.clear();
                        editor2.commit();
                        //untuk membersihkan sharedpreferens MyApplication.getInstance().getPrefManager().clear();
                        MyApplication.getInstance().getPrefManager().clear();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG).show();
                        MainActivity.this.finish();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onDestroy();
    }


    private void token() {
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
                //Log.e(TAG, "response: " + response);

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


                // Log.e(TAG, "paramsFirebase: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
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


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        }

    }

    private void foto() {
        if (foto == null || foto == "") {
            Glide.with(getApplicationContext()).load(R.drawable.unknow)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(imgProfilePic);

        } else {
            personPhotoUrl = foto;
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
        }
    }


    /**
     * Method for set fragment view
     */
    private void initView(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.content_framemain, fragment).commit();
        setTitle(title);
        //mDrawerLayout.closeDrawer(_leftLayout);
    }


    public void backButtonHandler() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.ask_exit_app));
        builder.setTitle(getResources().getString(R.string.exit_app));
        builder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        alert.show();
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

    private void date() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        setDateTimeField1();
    }

    private void setDateTimeField1() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                //_textDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void cekDate() {
        if (formattedDate.equals(_cekdate)) {
            //Toast.makeText(MainActivity.this,"Same date", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences preferences3 = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferences3.edit();
            editor2.clear();
            editor2.commit();
            logout();
            MainActivity.this.finish();
            Toast.makeText(MainActivity.this, "Session Day Expired", Toast.LENGTH_LONG).show();
        }
    }

    public void logout() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                MyApplication.getInstance().getPrefManager().clear();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d(TAG, "User Logged out");
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
            }
        });
    }

    private void cekSession() {
        if (sessiond == null || sessiond.equalsIgnoreCase("")) {
            SharedPreferences preferences3 = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferences3.edit();
            editor2.clear();
            editor2.commit();
            logout();
            MainActivity.this.finish();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Jakarta, and move the camera.
        LatLng indonesia = new LatLng(-2.0552254, 118.9101838);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(indonesia)      // Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //enableMyLocation();

    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    private void slidingImage() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this, ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

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


    private void useronline() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_GET_USER_ONLINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // JSONArray jsonArray = obj.getJSONArray("users");
                    JSONObject onlineDetail = (JSONObject) obj.getJSONObject("users");

                  /*  for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject chObj = (JSONObject) jsonArray.get(i);*/
                    String _pusat = onlineDetail.getString("pusat");
                    String _prov = onlineDetail.getString("prov");
                    String _kabkota = onlineDetail.getString("kabkota");
                    String _laz = onlineDetail.getString("laz");
                    txtPusat = (TextView) findViewById(R.id.jakbar);
                    txtPusat.setText(_pusat);
                    txtProv = (TextView) findViewById(R.id.jakpus);
                    txtProv.setText(_prov);
                    txtKabkot = (TextView) findViewById(R.id.jaksel);
                    txtKabkot.setText(_kabkota);
                    txtLaz = (TextView) findViewById(R.id.jaktim);
                    txtLaz.setText(_laz);

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

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    private void mapsOnline() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_GET_USER_ONLINE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // JSONArray jsonArray = obj.getJSONArray("users");
                    JSONObject mapsDetail = (JSONObject) obj.getJSONObject("opzonline");

                  /*  for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject chObj = (JSONObject) jsonArray.get(i);*/
                    String _pusat = mapsDetail.getString("pusat");
                    String _prov = mapsDetail.getString("prov");
                    String _kabkota = mapsDetail.getString("kabkota");
                    String _laz = mapsDetail.getString("laz");
                    JSONArray jsonArray = mapsDetail.getJSONArray("daftar");
                    List<MapsMainModel> list = new ArrayList<MapsMainModel>();
                    list.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject chObj = (JSONObject) jsonArray.get(i);
                        MapsMainModel item = new MapsMainModel();
                        String _nama = chObj.getString("nama");
                        String _lng = chObj.getString("lng");
                        String _lat = chObj.getString("lat");

                        MapsMainModel model = new MapsMainModel(_nama, _lat, _lng);
                        list.add(model);


                    }

                    for (int a = 0; a < list.size(); a++) {
                        if (a == 0) ////FOR ANIMATING THE CAMERA FOCUS FIRST TIME ON THE GOOGLE MAP
                        {
                            LatLng indonesia = new LatLng(-2.0552254, 118.9101838);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(indonesia)      // Sets the center of the map to Mountain View
                                    .zoom(4)                   // Sets the zoom
                                    // .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }

                        locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(list.get(a).getLat()), Double.parseDouble(list.get(a).getLang()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.map)).title(list.get(a).getNama()));


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

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    private void news() {
        StringRequest strReqa = new StringRequest(Request.Method.POST,
                ConnectionManager.CM_GET_NEWS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                  //  JSONArray jsonArray = obj.getJSONArray("info");
                    JSONObject onlineDetail = (JSONObject) obj.getJSONObject("info");

                 /*  for (int i = 0; i < jsonArray.length(); i++) {
                       JSONObject chObj = (JSONObject) jsonArray.get(i);*/
                       String _judul = onlineDetail.getString("judul");
                       String _news = onlineDetail.getString("news");
                    if (_judul == null || _judul.equalsIgnoreCase("")) {
                        cinformation = (CardView) findViewById(R.id.information);
                        cinformation.setVisibility(View.GONE);
                    } else if (_judul!=null){
                        cinformation = (CardView) findViewById(R.id.information);
                        cinformation.setVisibility(View.VISIBLE);
                    }
                       txtjudul = (TextView) findViewById(R.id.judul);
                       txtjudul.setText(_judul);
                       txtNews = (TextView) findViewById(R.id.isinews);
                       txtNews.setText(_news);


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
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReqa);
    }
}