package trash.jak.id.go.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import trash.jak.id.go.R;
import trash.jak.id.go.adapter.RecyclerAdapterBank;
import trash.jak.id.go.adapter.RecyclerAdapterBuilding;
import trash.jak.id.go.model.BankModel;
import trash.jak.id.go.model.BuildingModel;
import trash.jak.id.go.utils.ConnectionDetector;
import trash.jak.id.go.utils.ConnectionManager;

/**
 * Created by itp on 08/12/17.
 */

public class BankTrash extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , OnMapReadyCallback {
    private SwipeRefreshLayout swp;
    private List<BankModel> bankModelArrayList =new ArrayList<>();
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private ProgressDialog mProgressDialog;
    private RecyclerView recyclerView;
    private ConnectionDetector connectionDetector;
    private RecyclerAdapterBank mAdapter;
    private static String sessiond;
    private Toolbar _toolbar;
    Marker marker1,marker2,marker3,marker4,marker5,marker6,
            marker7,marker8,marker9,marker10;
    public GoogleMap mMap;
    private int current_page = 0, perpage = 0, status_code = -1,current_pageS;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private EditText txt_nama, txt_usia, txt_alamat, txt_website, inputsearch;;
    TextView txt_hasil;
    //Tag values to read from json
    public static final String TAG_NAMA = "nama_penyedia_jasa";
    public static final String TAG_Alamat = "alamat";
    public static final String TAG_Kelurahan = "kelurahan";
    public static final String TAG_TLP = "nomor_telepon";
    public static final String TAG_Wilayah = "wilayah";
    private ImageView mIvMain;

    private  String nama, usia, alamat, website,cekInput, _inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_bank);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapSebaran);
        mapFragment.getMapAsync(this);
        mIvMain = (ImageView) findViewById(R.id.iv_mainSebaran);
        loadingInternetPic();
        fitXY();
        bankModelArrayList = new ArrayList<BankModel>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutParams = new LinearLayoutManager(BankTrash.this, LinearLayoutManager.VERTICAL, false);
        showProgressDialog();
        recyclerView.setLayoutManager(layoutParams);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        _toolbar    = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Bank Sampah");
        requestListBuilding();
        showData();
    }


    private void fitXY(){
        Display display = BankTrash.this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        mIvMain.getLayoutParams().width = width;


    }
    private void loadingInternetPic() {
        DrawableRequestBuilder<Integer> thumbnailRequest = Glide
                .with(this)
                .load(R.drawable.jns_sampah);

        Glide.with(this)
                .load("http://myshaf.com/images/Sebaran_Bank_Sampah.png")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)//图片地址,还可以使用byte[],File,Integer,Uri参数
                .priority(Priority.HIGH)                       //优先级高
                .thumbnail(0.5f)                             //用原图的1/2作为缩略图
                //.thumbnail(thumbnailRequest)                   //使用自定义的缩略图
                .skipMemoryCache(true)                       //禁止内存缓存,使用Glide.get(context).clearMemory()清除缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)   //禁止磁盘缓存,使用Glide.get(applicationContext).clearDiskCache();清除缓存
                .bitmapTransform(new CenterCrop(this))       //圆形裁剪,使用前需要首先添加glide-transformations依赖
                .into(mIvMain);
    }
    private void requestListBuilding() {
        final ProgressDialog loading = ProgressDialog.show(BankTrash.this, "Please wait...","Fetching data...",false,false);
        loading.setCanceledOnTouchOutside(true);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ConnectionManager.CM_GET_LIST_Building,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        loading.dismiss();

                        //Displaying our grid
                        showGrid(response);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(BankTrash.this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        List<BankModel> list = new ArrayList<BankModel>();

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                BankModel item = new BankModel();
                item.setNama_penyedia(obj.getString(TAG_NAMA));
                item.setAlamat(obj.getString(TAG_Alamat));
                item.setKelurahan(obj.getString(TAG_Kelurahan));
                item.setWilayah(obj.getString(TAG_Wilayah));
                item.setNomor_telepon(obj.getString(TAG_TLP));

                list.add(item);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RecyclerAdapterBank fragmentFotoAdapter = new RecyclerAdapterBank(BankTrash.this,list);

        //Adding adapter to gridview
        recyclerView.setAdapter(fragmentFotoAdapter);
    }

    private void showData() {
        mAdapter = new RecyclerAdapterBank(BankTrash.this,  bankModelArrayList);
        recyclerView.setAdapter(mAdapter);

    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BankTrash.this); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
            mProgressDialog.setMessage("Retrieving Data...");
            mProgressDialog.setTitle("Status");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swp.setRefreshing(true);
                cd = new ConnectionDetector(BankTrash.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    Toast.makeText(BankTrash.this, "List Bank", Toast.LENGTH_SHORT).show();
                } else{
                    showAlertDialog(BankTrash.this, getResources().getString(R.string.no_internet1),
                            getResources().getString(R.string.no_internet2), false);
                };
            }
        }, 2000);

    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(BankTrash.this,
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icontruck = BitmapDescriptorFactory.fromResource(R.drawable.banksampahmarker);



        // Add a marker in Jakarta, and move the camera.
        LatLng jakarta = new LatLng(-6.21462, 106.84513);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(jakarta)      // Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(20)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder


        //Wisata
        marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.170171, 106.831421)).title("Bank Sampah Sample A")
                .draggable(true).icon(icontruck));
        marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.135192, 106.813291)).title("Bank Sampah Sample B")
                .draggable(true).icon(icontruck));
        marker3 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.175400, 106.827166)).title("Bank Sampah Sample C")
                .draggable(true).icon(icontruck));
        marker4 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.125778, 106.842843)).title("Bank Sampah Sample D")
                .draggable(true).icon(icontruck));
        marker5 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.2006518, 106.840763)).title("BBank Sampah Sample E")
                .draggable(true).icon(icontruck));
        marker6 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.302531, 106.895177)).title("BBank Sampah Sample F")
                .draggable(true).icon(icontruck));

        //location rakornas
        marker7 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.122100, 106.836316)).title("Bank Sampah Sample G")
                .draggable(true).icon(icontruck));
        //marker7.showInfoWindow();

        //Restauran
        marker8 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.200564, 106.852938)).title("Bank Sampah Sample H")
                .draggable(true).icon(icontruck));
        marker9 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.223703, 106.842091)).title("Bank Sampah Sample J")
                .draggable(true).icon(icontruck));
        marker10 = mMap.addMarker(new MarkerOptions().position(new LatLng(-6.248900, 106.798030)).title("Bank Sampah Sample I")
                .draggable(true).icon(icontruck));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 10.8f));
        enableMyLocation();
    }
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(BankTrash.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
}
