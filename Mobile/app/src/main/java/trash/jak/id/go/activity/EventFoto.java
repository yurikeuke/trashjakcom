package trash.jak.id.go.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

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
import trash.jak.id.go.adapter.FragmentFotoAdapter;
import trash.jak.id.go.model.FotoModel;

/**
 * Created by itp on 07/12/17.
 */

public class EventFoto extends AppCompatActivity {

    //Web api url
    public static final String DATA_URL = "http://myshaf.com/alif/eventtrash.php";

    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESC = "desc";


    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;
    private boolean isTaskRunning = false;
    public View view;
    private FloatingActionButton fab;
    String user_status,email;
    private RecyclerView recyclerView;
    private Toolbar _toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_event);
        recyclerView = (RecyclerView) findViewById(R.id.list_recycle);

        LinearLayoutManager layoutParams = new LinearLayoutManager(EventFoto.this);
        recyclerView.setLayoutManager(layoutParams);

        _toolbar    = (Toolbar) findViewById(R.id.toolbarEvent);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Event");

        getData();
    }

    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(EventFoto.this, "Please wait...","Fetching data...",false,false);
        loading.setCanceledOnTouchOutside(true);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
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
        RequestQueue requestQueue = Volley.newRequestQueue(EventFoto.this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }
    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array
        List<FotoModel> list = new ArrayList<FotoModel>();

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                FotoModel item = new FotoModel();
                item.setGambar(obj.getString(TAG_IMAGE_URL));
                item.setNama(obj.getString(TAG_NAME));
                item.setDeskripsi(obj.getString(TAG_DESC));

                list.add(item);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        FragmentFotoAdapter fragmentFotoAdapter = new FragmentFotoAdapter(EventFoto.this,list);

        //Adding adapter to gridview
        recyclerView.setAdapter(fragmentFotoAdapter);
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
