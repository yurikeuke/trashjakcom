package trash.jak.id.go.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import trash.jak.id.go.R;
import trash.jak.id.go.adapter.RecyclerAdapterBuilding;
import trash.jak.id.go.adapter.RecylerAdapterRatting;
import trash.jak.id.go.model.BuildingModel;
import trash.jak.id.go.model.RattingModel;
import trash.jak.id.go.utils.ConnectionDetector;
import trash.jak.id.go.utils.ConnectionManager;

/**
 * Created by itp on 08/12/17.
 */

public class Ratting extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swp;
    private List<RattingModel> buildingModelArrayList =new ArrayList<>();
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private ProgressDialog mProgressDialog;
    private RecyclerView recyclerView;
    private ConnectionDetector connectionDetector;
    private RecylerAdapterRatting mAdapter;
    private static String sessiond;
    private Toolbar _toolbar;
    private int current_page = 0, perpage = 0, status_code = -1,current_pageS;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    private EditText txt_nama, txt_usia, txt_alamat, txt_website, inputsearch;;
    TextView txt_hasil;
    //Web api url
    public static final String DATA_URL = "http://myshaf.com/alif/eventtrash.php";
    //Tag values to read from json
    public static final String TAG_NAMA = "nama_penyedia_jasa";
    public static final String TAG_Alamat = "alamat";
    public static final String TAG_Kelurahan = "kelurahan";
    public static final String TAG_TLP = "nomor_telepon";
    public static final String TAG_Wilayah = "wilayah";

    private  String nama, usia, alamat, website,cekInput, _inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_performance);
        buildingModelArrayList = new ArrayList<RattingModel>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutParams = new LinearLayoutManager(Ratting.this, LinearLayoutManager.VERTICAL, false);
        showProgressDialog();
        recyclerView.setLayoutManager(layoutParams);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        _toolbar    = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Performance");
        requestListBuilding();
        showData();
    }


    private void requestListBuilding() {
        final ProgressDialog loading = ProgressDialog.show(Ratting.this, "Please wait...","Fetching data...",false,false);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Ratting.this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        List<RattingModel> list = new ArrayList<RattingModel>();

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                RattingModel item = new RattingModel();
                item.setNama_penyedia(obj.getString(TAG_NAMA));
                item.setAlamat(obj.getString(TAG_Alamat));
                item.setKelurahan(obj.getString(TAG_Kelurahan));
                item.setWilayah(obj.getString(TAG_Wilayah));
                item.setRatting(obj.getString(TAG_TLP));

                list.add(item);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RecylerAdapterRatting fragmentFotoAdapter = new RecylerAdapterRatting(Ratting.this,list);

        //Adding adapter to gridview
        recyclerView.setAdapter(fragmentFotoAdapter);
    }

    private void showData() {
        mAdapter = new RecylerAdapterRatting(Ratting.this,  buildingModelArrayList);
        recyclerView.setAdapter(mAdapter);

    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(Ratting.this); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
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
                cd = new ConnectionDetector(Ratting.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    Toast.makeText(Ratting.this, "List Perusahaan", Toast.LENGTH_SHORT).show();
                } else{
                    showAlertDialog(Ratting.this, getResources().getString(R.string.no_internet1),
                            getResources().getString(R.string.no_internet2), false);
                };
            }
        }, 2000);

    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(Ratting.this,
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


}

