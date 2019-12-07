package com.expossoftware.tbid_dev.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.expossoftware.tbid_dev.BuildConfig;
import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.activity.PresensiActivity;
import com.expossoftware.tbid_dev.adapter.adapter_presensi;
import com.expossoftware.tbid_dev.model.ItemPresensi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class PresensiFragment extends Fragment {

    public  static final int POPUPDIALOG = 1; //-- target fragment requestcode

    private JSONObject jsonObjPresensi = new JSONObject();
    private String responsePresensi = "";
    private String strUsername, strPassword, strURL,strUserPasswPresensiFrag;
    private String strClass, strMonth;

    private RecyclerView recyclerView;
    private adapter_presensi adapterPresensi;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ItemPresensi> itemPresensiList = new ArrayList<ItemPresensi>();
    private TextView tbxInformation;
    boolean IsParseJson;

    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    ViewGroup.LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;

    static Fragment MyFragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case POPUPDIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    //--- get value from dialog
                    Bundle bundle = data.getExtras();
                    strClass = bundle.getString("class");
                    strMonth = bundle.getString("month");
                    tbxInformation.setText("Kelas : " + strClass + "    Bulan : "+strMonth);

                    //---- clear recyclerview
                    recyclerView.setAdapter(null);
                    itemPresensiList.clear();

                    //---- get data
                    new getDataPresence().execute(strURL);

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btnHomePresensi, btnSearch;
        Spinner spnMonth, spnClass;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_presensi, container, false);

        PresensiActivity actPresensi = (PresensiActivity) getActivity();
        strUsername = actPresensi.getDataUser("user");
        strPassword = actPresensi.getDataUser("passw");
        strURL = actPresensi.getDataUser("url");

        tbxInformation = (TextView) view.findViewById(R.id.tbxInformation);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvPresensi);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //--- send data to pop up dialog & open popup dialog
                Bundle args = new Bundle();
                args.putString("username", strUsername);
                args.putString("password", strPassword);
                args.putString("url", strURL);
                PopUpDialogFragment dialog1 = new PopUpDialogFragment();
                dialog1.setTargetFragment(PresensiFragment.this, POPUPDIALOG);
                dialog1.setArguments(args);
                dialog1.show(getFragmentManager(),"myPopUpDialog");

            }
        });

        btnHomePresensi = view.findViewById(R.id.btnHomePresensi);
        btnHomePresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }



    public class getDataPresence  extends AsyncTask<String, Void, String> {
        String strRespons;

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObjPresensi.put("ANDRODEVICE", "YES");
                jsonObjPresensi.put("REQTYPE", "TBIDANDROSTUDENTSPRESENCE");
                jsonObjPresensi.put("USERNAME", strUsername);
                jsonObjPresensi.put("PASSWORD", strPassword);
                jsonObjPresensi.put("CMD1", strUsername);
                jsonObjPresensi.put("CMD2", strClass);
                jsonObjPresensi.put("CMD3", strMonth);
                jsonObjPresensi.put("CMD4", "-");
                jsonObjPresensi.put("CMD5", "-");

                return httpPost(strURL, jsonObjPresensi.toString());

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String responseData) {
            String strRespons;
            int intArrayLength;

            try {
                if (responseData != null) {

                    JSONObject jsObj = new JSONObject(responseData);
                    if (jsObj.getString("error").equals("false")) {

                        //-- perlu dicek lagi
                        //-- apakah ada data atau tidak, karena respon untuk ada data atau tidak, masuk dalam "error" = "false"
                        //-- untuk mempermudah, jika array = 1 baru dicek, bila array > 2 tentu ada data.
                        JSONArray tmpJsonArray = jsObj.getJSONArray("msg_response");
                        intArrayLength = tmpJsonArray.length();
                        if (intArrayLength==1){
                            //-- jumlah array hanya 1 perlu dicek apakah itu berisi data atau tidak ada data
                            //-- bila isi array = {"information","no_data"} maka tidak ada data
                            if (tmpJsonArray.toString().contains("infomation")){
                                IsParseJson = false;
                            }else{
                                IsParseJson = true;
                            }
                        }else {
                            //-- array > 1 maka dapat dipastikan sebagai data
                            IsParseJson = true;
                        }
                    }else{
                        IsParseJson = false;
                        strRespons = "No JSON received !";
                    }

                } else {
                    IsParseJson = false;
                    strRespons = "No JSON received !";
                    Log.d("Sub Class", strRespons);
                }

                if (IsParseJson) {
                    recyclerView.setLayoutManager(layoutManager);
                    doParseJson(responseData);
                }else{
                    recyclerView.setLayoutManager(null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public String httpPost(String urlString, String jsonData) {
            java.net.URL url;

            HashMap<String, String> params;
            try {
                url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json;charset=utf-8");

                //post request
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());

                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                int responseCode=conn.getResponseCode();

                responsePresensi = "";
                //1xx info, 2xx success, 3xx redirect, 4xx client error, 5xx server error
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        responsePresensi+=line;
                    }
                }
                else {
                    responsePresensi="";
                    //throw new HttpException(responseCode+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG){
                Log.d("Sub Kelas", "response = " + responsePresensi);
            }

            return responsePresensi;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse){

            try{
                JSONObject jsonObjPresence = new JSONObject(strResponse);
                JSONArray arrPresence = jsonObjPresence.getJSONArray("msg_response");

                for (int i = 0; i<arrPresence.length(); i++){
                    JSONObject presenceObject = arrPresence.getJSONObject(i);

                    ItemPresensi itemPresensi = new ItemPresensi(
                            presenceObject.getString("SUBKELASNAMA"),
                            presenceObject.getString("SUBKELASHARI"),
                            presenceObject.getString("WAKTUABSEN"),
                            presenceObject.getString("SUBKELASWAKTU")
                    );
                    itemPresensiList.add(itemPresensi);
                }

                adapterPresensi = new adapter_presensi(itemPresensiList);
                recyclerView.setAdapter(adapterPresensi);

            }catch (Throwable t) {
                Log.e("My app","could't parse malformed JSON : \""+strResponse+"\"");
            }
        }
    }
}
