package trash.jak.id.go.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import trash.jak.id.go.app.MyApplication;
import trash.jak.id.go.interfaces.Sendsuccess;

/**
 * Created by itp on 06/12/16.
 */

public class RequestHelper {
    public void sendDataToServer(Context context, final CoordinatorLayout mainWrapper, JSONObject json, String url, String message, final Sendsuccess sendSuccess){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if ( progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    if ( ! response.has("error")){
                        sendSuccess.success(response);
                    }else{
                        Snackbar.make(mainWrapper, "Terjadi kesalahan, silahkan coba lagi", Snackbar.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                    Snackbar.make(mainWrapper, "Terjadi kesalahan, silahkan coba lagi", Snackbar.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ( progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Snackbar.make(mainWrapper, "Terjadi kesalahan, silahkan coba lagi", Snackbar.LENGTH_LONG).show();

                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getDataFromServer(Context context, String url, String message, final Sendsuccess sendSuccess){

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if ( ! response.has("error")){
                        sendSuccess.success(response);
                    }else{
                        Log.i("status" , response.toString());
                    }

                } catch (JSONException e) {
                    Log.i("status" , e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void requestDataFromServer(Context context, String url, String message, final String tag, final Sendsuccess sendSuccess) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ( progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    if ( ! response.has("error")){
                        sendSuccess.success(response);
                    }else{
                        Log.i("status" , response.toString());
                    }

                } catch (JSONException e) {
                    Log.i("status" , e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ( progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(tag, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    }
                });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}

