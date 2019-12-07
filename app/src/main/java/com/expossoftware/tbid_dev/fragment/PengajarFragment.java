package com.expossoftware.tbid_dev.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.expossoftware.tbid_dev.BuildConfig;
import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.activity.PengajarActivity;
import com.expossoftware.tbid_dev.adapter.adapter_pengajar;
import com.expossoftware.tbid_dev.model.ItemPengajar;

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
public class PengajarFragment extends Fragment {

    Button btnHomePengajar;
    String strUsrname, strPassw;

    private static final String URL = "http://103.215.177.234:3030";

    private JSONObject jsonObjPengajar = new JSONObject();
    private String response = "";
    private String[] separated = null;

    private String vANDRODEVICE, vREQTYPE, vUSERNAME, vPASSWOR, CMD1, CMD2, CMD3, CMD4, CMD5;
    TextView tANDRODEVICE, tREQTYPE, tUSERNAME, tPASSWORD, tCMD1, tCMD2, tCMD3, tCMD4, tCMD5;

    //--- RECYCLERVIEW
    private RecyclerView recyclerView;
    private adapter_pengajar adapter;

    private ArrayList<ItemPengajar> itemPengajarList = new ArrayList<ItemPengajar>();

    public PengajarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengajar, container, false);

        //--- getting data username & password
        //--- from menu
        PengajarActivity act = (PengajarActivity) getActivity();
        strUsrname = act.getDataUser("user");
        strPassw = act.getDataUser("passw");

        recyclerView = (RecyclerView) view.findViewById(R.id.rVPengajar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        new GetDataTaskPengajar().execute(URL);

        btnHomePengajar = (Button) view.findViewById(R.id.btnhomepengajar);
        btnHomePengajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public class GetDataTaskPengajar extends AsyncTask<String, Void, String> {
        /*String accountId;
        String strRespons;*/
        String strImageName;
        Context context;
        Resources resources = getActivity().getResources();

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObjPengajar.put("ANDRODEVICE", "YES");
                jsonObjPengajar.put("REQTYPE", "TBIDANDROTEACHERSDATA");
                jsonObjPengajar.put("USERNAME", strUsrname);
                jsonObjPengajar.put("PASSWORD", strPassw);
                jsonObjPengajar.put("CMD1", "-");
                jsonObjPengajar.put("CMD2", "-");
                jsonObjPengajar.put("CMD3", "-");
                jsonObjPengajar.put("CMD4", "-");
                jsonObjPengajar.put("CMD5", "-");

                return httpPost(url[0], jsonObjPengajar.toString());

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
                Log.d("PENGAJAR", "responseData " + strRespons);
            } else {
                IsParseJson = false;
                strRespons = "No JSON received !";
                Log.d("PENGAJAR", strRespons);
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
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG) {
                Log.d("PENGAJAR", "response = " + response);
            }

            return response;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse) {
            Boolean isSplitProcess = false;

            try {

                //---- adding image string
                ArrayList<HashMap<String, Object>> tmpList = new ArrayList<HashMap<String, Object>>();
                JSONObject addImgObj = new JSONObject(strResponse);
                JSONArray arrAddImgObj = addImgObj.getJSONArray("msg_response");
                for (int j = 0; j < arrAddImgObj.length(); j++) {
                    JSONObject addImg = arrAddImgObj.getJSONObject(j);
                    HashMap<String, Object> map = new HashMap<String, Object>();

                    String strNamaPengajar = addImg.getString("NAMA");
                    String strJabatan = addImg.getString("JABATAN");

                    if (strNamaPengajar.contains(".")) {
                        separated = strNamaPengajar.split("\\.");
                        isSplitProcess = true;
                    } else if (strNamaPengajar.contains(" ")) {
                        separated = strNamaPengajar.split(" ");
                        isSplitProcess = true;
                    }
                    if (isSplitProcess) {
                        strImageName = separated[1].trim();
                    }

                    strImageName = strImageName.toLowerCase();

                    //--------------- tampil foto pengajar sesuai id /imageResource

                    String uri = "@drawable/" + strImageName;  //@drawable/myresource ---> where myresource (without the extension) is the file

                    /*int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
                    ImageView imageview= (ImageView) getActivity().findViewById(R.id.imageView);
                    Drawable res = getResources().getDrawable(imageResource);
                    imageview.setImageDrawable(res);*/

                    //--------------- END OFF tampil foto pengajar sesuai id /imageResource

                    //get resources id by image name
                    //final int resImgId = resources.getIdentifier(strImageName, "drawable", getActivity().getPackageName());
                    final int resImgId = getResources().getIdentifier(uri, null, getActivity().getPackageName());

                    //-- adding string image
                    Log.e("My app", "could't parse malformed JSON : \"" + strResponse + "\"");
                    map.put("\"NAMA\"", "\"" + strNamaPengajar + "\"");
                    map.put("\"JABATAN\"", "\"" + strJabatan + "\"");
                    map.put("\"IMG\"", "\"" + Integer.toString(resImgId) + "\"");
                    tmpList.add(map);
                }

                strResponse = (tmpList.toString()).replaceAll("=", ":");

                /*JSONObject jsonObj = new JSONObject(strResponse);
                JSONArray arrPengajar = jsonObj.getJSONArray("msg_response");*/
                JSONArray jsonArrayPengajar = new JSONArray(strResponse);
                //JSONArray arrPengajar = jsonObj.getJSONArray("msg_response");
                for (int i = 0; i < jsonArrayPengajar.length(); i++) {
                    JSONObject pengajarObject = jsonArrayPengajar.getJSONObject(i);

                    ItemPengajar itemPengajar = new ItemPengajar(pengajarObject.getString("NAMA"),
                            pengajarObject.getString("JABATAN"),
                            pengajarObject.getInt("IMG")
                    );

                    itemPengajarList.add(itemPengajar);
                }

                adapter = new adapter_pengajar(itemPengajarList);
                recyclerView.setAdapter(adapter);


            } catch (Throwable t) {
                Log.e("My app", t.getMessage());
            }
        }
    }
}
