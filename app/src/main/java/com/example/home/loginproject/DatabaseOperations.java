package com.example.home.loginproject;

import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 7/18/2016.
 */
public class DatabaseOperations {
    // Declare controls
    ProgressDialog pDialog;
    public static final String KEY_NAME = "name_param";
    public static final String KEY_PASSWORD = "password_param";
    public static final String KEY_EMAIL = "email_param";
    /**
     * Method to make post a string request to the server
     */
    public void postSearch(String url, final String nameParam, final String emailParam,
                           final String passwordParam, final VolleyCallback callback) {


        if (pDialog == null) {
            pDialog = Util.createProgressDialog(Util.getActivity());
        }
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                            // pass jsonOutput to callback interface
                            callback.onSuccessResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Util.getContext(), "Data not available at the moment. " +
                                "Please check your internet connection", Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                }) {
            // build parameters for post request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (nameParam != null)
                params.put(KEY_NAME, nameParam);
                params.put(KEY_EMAIL, emailParam);
                params.put(KEY_PASSWORD, passwordParam);
                return params;
            }

        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Util.getContext());
        requestQueue.add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccessResponse(final String result);

    }
}
