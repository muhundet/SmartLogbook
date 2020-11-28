package com.example.smartlogbook.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartlogbook.database.DatabaseContract;
import com.example.smartlogbook.database.OpenHelper;
import com.example.smartlogbook.ui.main.FragmentMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;
    private OpenHelper db;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new OpenHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo(); //TODO: add network permission

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced data
                Cursor cursor = db.getUnsynchronizedRegisterEntries();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced data to MySQL
                        saveRegisterEntry(
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry._ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_EMPLOYEE_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_DATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_TIME_IN)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.RegisterEntry.COLUMN_TIME_OUT))
                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }


    private void saveRegisterEntry(final String registerEntryId, final String employeeId, final String date, final String timeIn, final String timeOut) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FragmentMain.URL_SAVE_TO_SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateRegisterEntryStatus(registerEntryId, FragmentMain.ENTRY_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(FragmentMain.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("register_entry_id", registerEntryId);
                params.put("employee_id", employeeId);
                params.put("date", date);
                params.put("time_in", timeIn);
                params.put("time_out", timeOut);

                return params;
            }
        };

        VolleyUtil.getInstance(context).addToRequestQueue(stringRequest);
    }

}
