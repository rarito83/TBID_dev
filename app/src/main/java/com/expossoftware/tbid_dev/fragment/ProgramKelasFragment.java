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
import com.expossoftware.tbid_dev.activity.ProgramKelasActivity;
import com.expossoftware.tbid_dev.adapter.adapter_programkelas;
import com.expossoftware.tbid_dev.model.ItemProgramKelas;

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
public class ProgramKelasFragment extends Fragment {

    private static final String URL = "http://103.215.177.234:3030";

    private JSONObject jsonObjClass = new JSONObject();
    private String responseClass = "";
    private String strUsernameClass, strPasswClass;
    private RecyclerView recyclerView;
    private adapter_programkelas adapterProgramKelas;

    private ArrayList<ItemProgramKelas> itemProgramKelasList = new ArrayList<ItemProgramKelas>();

    public ProgramKelasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button btnHomeProgramKelas;


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_kelas, container, false);

        ProgramKelasActivity programClass = (ProgramKelasActivity) getActivity();
        strUsernameClass = programClass.getDataUser("user");
        strPasswClass = programClass.getDataUser("passw");

        // add search nama kelas
        EditText editText = view.findViewById(R.id.edt_cari_kelas);
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
        recyclerView = view.findViewById(R.id.rvProgramKelas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        showNamaProgramKelas();

        new GetClassProgamData().execute(URL);

        btnHomeProgramKelas = view.findViewById(R.id.btnHomeprogramkelas);
        btnHomeProgramKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void filter(String text) {
        ArrayList<ItemProgramKelas> filteredList = new ArrayList<>();

        for (ItemProgramKelas item : itemProgramKelasList) {
            if (item.getNamaKelas().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterProgramKelas.filterList(filteredList);
    }

    private void showNamaProgramKelas() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_programkelas adapterProgramkelas = new adapter_programkelas(itemProgramKelasList);
        recyclerView.setAdapter(adapterProgramkelas);

    }

    public class GetClassProgamData extends AsyncTask<String, Void, String> {
        String strRespons;

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObjClass.put("ANDRODEVICE", "YES");
                jsonObjClass.put("REQTYPE", "TBIDANDROCLASSDATA");
                jsonObjClass.put("USERNAME", strUsernameClass);
                jsonObjClass.put("PASSWORD", strPasswClass);
                jsonObjClass.put("CMD1", "-");
                jsonObjClass.put("CMD2", "-");
                jsonObjClass.put("CMD3", "-");
                jsonObjClass.put("CMD4", "-");
                jsonObjClass.put("CMD5", "-");

                return httpPost(url[0], jsonObjClass.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String responseData) {
            String strRespons;
            boolean IsParseJson;
            if (responseData != null) {
                IsParseJson = true;
                strRespons = responseData;
                Log.d("PROGRAM KELAS", "responseData " + strRespons);
            } else {
                IsParseJson = false;
                strRespons = "No JSON received !";
                Log.d("PROGRAM KELAS", strRespons);
            }

            if (IsParseJson) {
                doParseJson(responseData);
            }
        }

        public String httpPost(String urlString, String jsonData) {
            java.net.URL url;

            HashMap<String, String> params;
            try {
                url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                //post request
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonData.toString());

                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                int responseCode = conn.getResponseCode();

                //1xx info, 2xx success, 3xx redirect, 4xx client error, 5xx server error
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        responseClass += line;
                    }
                } else {
                    responseClass = "";
                    //throw new HttpException(responseCode+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG) {
                Log.d("PROGRAM KELAS", "response = " + responseClass);
            }

            return responseClass;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse) {

            try {
                //RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

                JSONObject jsonObj = new JSONObject(strResponse);
                JSONArray arrPengajar = jsonObj.getJSONArray("msg_response");

                for (int i = 0; i < arrPengajar.length(); i++) {
                    JSONObject pengajarObject = arrPengajar.getJSONObject(i);

                    ItemProgramKelas itemProgramKelas = new ItemProgramKelas(pengajarObject.getString("NAMAKELAS"),
                            pengajarObject.getString("HARGAKELAS")
                    );
                    itemProgramKelasList.add(itemProgramKelas);
                }


                adapterProgramKelas = new adapter_programkelas(itemProgramKelasList);

                recyclerView.setAdapter(adapterProgramKelas);


            } catch (Throwable t) {
                Log.e("My app", "could't parse malformed JSON : \"" + strResponse + "\"");
            }
        }
    }
}
