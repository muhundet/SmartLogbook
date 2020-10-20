package com.example.smartlogbook.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartlogbook.MainActivity;
import com.example.smartlogbook.R;
import com.example.smartlogbook.database.OpenHelper;
import com.example.smartlogbook.models.RegisterEntryModel;
import com.example.smartlogbook.network.VolleyUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMain extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String URL_SAVE_TO_SERVER = "http://192.168.1.107/saveRegisterEntry.php";
    public static final int ENTRY_SYNCED_WITH_SERVER = 1;
    public static final int ENTRY_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "com.example.datasaved";
    private RegisterViewModel mRegisterViewModel;
    private IntentIntegrator qrScan;
    private OpenHelper db;
    public static FragmentMain newInstance(int index) {
        FragmentMain fragment = new FragmentMain();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        mRegisterViewModel.setIndex(index);

        db = new OpenHelper(getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        final Button btnScanQr = root.findViewById(R.id.btn_scan_qr);
        final Button btnManualEntry = root.findViewById(R.id.btn_manual_entry);
        final RecyclerView rv = root.findViewById(R.id.rv_current_register);
        mRegisterViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        mRegisterViewModel.getListRegisterEntry().observe(getViewLifecycleOwner(), new Observer<List<RegisterEntryModel>>() {
            @Override
            public void onChanged(List<RegisterEntryModel> registerEntryModels) {
                final CurrentRegisterRecyclerAdapter adapter = new CurrentRegisterRecyclerAdapter((ArrayList<RegisterEntryModel>) registerEntryModels);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(adapter);
            }
        });


        btnManualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterEmployeeIdDialog();
            }
        });
        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                qrScan = new IntentIntegrator(Activity.getClass());
                qrScan.initiateScan();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject employeeDetailJson = new JSONObject(result.getContents());
//                    TODO: read the json info on QR and save to db
                    String employeeId = employeeDetailJson.getString("employeeID");
                    if(!OpenHelper.getOpenHelperInstance().isLoggedInAlready(employeeId)){
                        saveRegisterEntryToServer(employeeId);
                    }else{
//                        TODO: update to server and local

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here that means the encoded format not matches in this case you can display whatever data is available on the qrcode to a toast
                    Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

     void saveRegisterEntryToServer(final String employeeId) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Clocking for Employee:" + employeeId);
        progressDialog.show();

         String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
         String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        final String registerEntryId = "movelast";
//        TODO: to get the string values of date and time
        final String mDate = currentDate;
        final String mTimeIn = currentTime;
        final String mTimeOut = "";
        final String mStatus = String.valueOf(ENTRY_SYNCED_WITH_SERVER);
        final String mNotStatus = String.valueOf(ENTRY_NOT_SYNCED_WITH_SERVER);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_TO_SERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveRegisterEntryToLocalStorage(registerEntryId, employeeId, mDate, mTimeIn, mTimeOut, mStatus);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveRegisterEntryToLocalStorage(registerEntryId, employeeId, mDate, mTimeIn, mTimeOut, mNotStatus);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveRegisterEntryToLocalStorage(registerEntryId, employeeId, mDate, mTimeIn, mTimeOut, mNotStatus);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("register_entry_id", registerEntryId);
                params.put("employee_id", employeeId);
                params.put("date", mDate);
                params.put("time_in", mTimeIn);
                params.put("time_out", mTimeOut);
                return params;
            }
        };

        VolleyUtil.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void saveRegisterEntryToLocalStorage(String registerEntryId, String employeeId, String date, String timeIn, String timeOut, String status) {

        db.saveRegisterEntry(registerEntryId, employeeId, date, timeIn, timeOut, status);
//        TODO: notify data changed to current and all registers
    }

    private void enterEmployeeIdDialog() {
        new AlertDialog.Builder(getContext()).setView(R.layout.dialog_manual_entry).setPositiveButton("Clock In", new DialogInterface.OnClickListener() {@Override
        public void onClick(DialogInterface dialogInterface, int i) {
            AlertDialog dialog = (AlertDialog) dialogInterface;
            TextInputEditText employeeID = dialog.findViewById(R.id.manual_entry_input);
            if (employeeID != null && employeeID.getText() != null ) {
                String employeeId = employeeID.getText().toString();
                if (employeeId.trim().length() > 0 ) {
                    //TODO: check if employeeis Logged in, if not save to database else update timeout
                    if(!OpenHelper.getOpenHelperInstance().isLoggedInAlready(employeeId)){
                        saveRegisterEntryToServer(employeeId);
                    }else{
//                        TODO: update to server and local

                    }
                }
            }
            dialog.dismiss();
        }
        }).show();
    }
}