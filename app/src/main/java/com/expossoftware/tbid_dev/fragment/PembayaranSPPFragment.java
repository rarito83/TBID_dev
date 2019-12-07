package com.expossoftware.tbid_dev.fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.expossoftware.tbid_dev.BuildConfig;
import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.activity.SppActivity;
import com.expossoftware.tbid_dev.adapter.adapter_programkelas;
import com.expossoftware.tbid_dev.adapter.adapter_spp;
import com.expossoftware.tbid_dev.model.ItemProgramKelas;
import com.expossoftware.tbid_dev.model.ItemSpp;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class PembayaranSPPFragment extends Fragment {

    //private static final String URL = "http://103.215.177.234:3030";

    private JSONObject jsonObjSPP = new JSONObject();
    private String responseSpp = "";

    //--- RECYCLERVIEW
    private RecyclerView recyclerViewSpp;
    private adapter_spp adapterSpp;
    private ArrayList<ItemSpp> itemSppList = new ArrayList<>();

    String strUsername, strPassword, strUrl;

    public PembayaranSPPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btnHomeSpp;

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pembayaran_spp, container, false);

        //--- get username, password & url from sppActivity
        SppActivity sppActivity = (SppActivity) getActivity();
        strUsername = sppActivity.getDataUser("user");
        strPassword = sppActivity.getDataUser("passw");
        strUrl = sppActivity.getDataUser("url");

        // add search bulan pembayaran SPP
        EditText editText = view.findViewById(R.id.edt_cari_pembayaranSPP);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        recyclerViewSpp = view.findViewById(R.id.rvSpp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewSpp.setLayoutManager(layoutManager);
        recyclerViewSpp.setHasFixedSize(true);
        showPembayaranSPP();

        new getDataPresence().execute(strUrl);

        btnHomeSpp = (Button) view.findViewById(R.id.btnHomeSpp);
        btnHomeSpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void filter(String text) {
        ArrayList<ItemSpp> filteredList = new ArrayList<>();

        for (ItemSpp item : itemSppList) {
            if (item.getSppStrMonth().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterSpp.filterList(filteredList);
    }

    private void showPembayaranSPP() {
        recyclerViewSpp.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_spp adapterSpp = new adapter_spp(itemSppList);
        recyclerViewSpp.setAdapter(adapterSpp);

    }

    public class getDataPresence  extends AsyncTask<String, Void, String> {
        String strRespons;

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObjSPP.put("ANDRODEVICE", "YES");
                jsonObjSPP.put("REQTYPE", "TBIDANDROSTUDENTSPAYMENT");
                jsonObjSPP.put("USERNAME", strUsername);
                jsonObjSPP.put("PASSWORD", strPassword);
                jsonObjSPP.put("CMD1", strUsername);
                jsonObjSPP.put("CMD2", "-");
                jsonObjSPP.put("CMD3", "-");
                jsonObjSPP.put("CMD4", "-");
                jsonObjSPP.put("CMD5", "-");

                return httpPost(strUrl, jsonObjSPP.toString());

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String responseData) {
            String strRespons;
            boolean IsParseJson;

            try {
                if (responseData != null) {

                    JSONObject jsObj = new JSONObject(responseData);
                    if (jsObj.getString("error").equals("false")) {

                        IsParseJson = true;
                        strRespons = responseData;
                        Log.d("Sub Class", "responseData " + strRespons);
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
                    doParseJson(responseData);
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

                responseSpp = "";
                //1xx info, 2xx success, 3xx redirect, 4xx client error, 5xx server error
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        responseSpp+=line;
                    }
                }
                else {
                    responseSpp="";
                    //throw new HttpException(responseCode+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG){
                Log.d("SPP", "response = " + responseSpp);
            }
            return responseSpp;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse){

            try{
                JSONObject jsonObjSpp = new JSONObject(strResponse);
                JSONArray arrSpp = jsonObjSpp.getJSONArray("msg_response");

                for (int i = 0; i<arrSpp.length(); i++){
                    JSONObject sppObject = arrSpp.getJSONObject(i);

                    ItemSpp itemSpp = new ItemSpp(
                            sppObject.getString("TANGGALINPUT"),
                            sppObject.getString("BULANBAYAR"),
                            sppObject.getString("KELASNAMA"),
                            sppObject.getString("SUBKELASNAMA"),
                            sppObject.getString("BESARBAYAR"),
                            sppObject.getString("REFID"),
                            sppObject.getString("TIPEBAYAR"),
                            sppObject.getString("NAMABAYAR")

                    );
                    itemSppList.add(itemSpp);
                }

                adapterSpp = new adapter_spp(itemSppList);
                recyclerViewSpp.setAdapter(adapterSpp);

            }catch (Throwable t) {
                Log.e("My app","could't parse malformed JSON : \""+strResponse+"\"");
            }
        }
    }
}
