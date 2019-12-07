package com.expossoftware.tbid_dev.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expossoftware.tbid_dev.BuildConfig;
import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemSubClassData;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopUpDialogFragment extends DialogFragment {

    private static final String TAG = "myPopUpDialog";
    private Spinner spnSubClass;
    private Button btnSearch;
    private GridLayout gridLayout;
    private Context myContext;

    private JSONObject jsonObjSubClass = new JSONObject();
    private String responseSubClass = "";
    private ArrayList<ItemSubClassData> itemSubClassDataList = new ArrayList<ItemSubClassData>();
    private ArrayList<String> subClassName = new ArrayList<String>();
    private Context context;


    //---- GRIDVIEW
    GridView gridView;
    List<String> ItemsList;
    TextView tbxSelectedItem;
    String strSelectedMonth, strSelectedClass;
    int intPrevSelectGridItem = -1;
    String[] itemsName = new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September",
            "Oktober", "November", "Desember"};

    public String strPassUsername, strPassPassword, strPassURL;
    private boolean isProceed = false;

    public PopUpDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pop_up_dialog, container, false);

        spnSubClass = (Spinner) view.findViewById(R.id.cmbSubClass);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

       /* gridLayout = (GridLayout) view.findViewById(R.id.grdLayout);
        //
        setSingleEvent(gridLayout);
*/
        //--- get data from presensi fragment
        Bundle mArgs = getArguments();
        strPassUsername = mArgs.getString("username");
        strPassPassword = mArgs.getString("password");
        strPassURL = mArgs.getString("url");

        spnSubClass.setPrompt("Pilih kelas");

        //----GRIDVIEW
        gridView = (GridView) view.findViewById(R.id.gridView1);
        tbxSelectedItem = (TextView) view.findViewById(R.id.tbxSelectedItem);
        ItemsList = new ArrayList<String>(Arrays.asList(itemsName));

        //--- request and parse json
        new getDataSubClass().execute(strPassURL);

        gridView.setAdapter(new TextAdapter(getActivity()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strSelectedMonth = adapterView.getItemAtPosition(i).toString();
                tbxSelectedItem.setText(strSelectedMonth + " is selected");

                try {
                    if (intPrevSelectGridItem != -1) {
                        viewPrev = (View) gridView.getChildAt(intPrevSelectGridItem);
                        viewPrev.setBackgroundColor(getResources().getColor(R.color.colorTBIDlight));
                    }

                    intPrevSelectGridItem = i;
                    if (intPrevSelectGridItem == i) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorTBIDark));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSelectedClass = spnSubClass.getSelectedItem().toString();

                if (inputCheck()) {
                    //--- send data to presensiFragment
                    Intent intent = new Intent();
                    intent.putExtra("class", strSelectedClass);
                    intent.putExtra("month", strSelectedMonth);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(),"Isian kelas dan bulan harus tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public boolean inputCheck(){
        if (strSelectedMonth == null){
            strSelectedMonth="";
        }

        if (strSelectedClass.equals(null)){
            strSelectedClass="";
        }

        if (strSelectedClass.equals("") || strSelectedMonth.equals("")){
            isProceed = false;
        }else {
            isProceed = true;
        }

        return  isProceed;
    }

    private class TextAdapter extends BaseAdapter {
        Context context;

        public TextAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemsName.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemsName[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            TextView text = new TextView(this.context);

            text.setText(itemsName[position]);
            text.setGravity(Gravity.CENTER);

            //text.setBackgroundColor(Color.parseColor("#fbdcbb"));
            text.setBackgroundColor(Color.parseColor("#d3b6e1"));
            text.setLayoutParams(new GridView.LayoutParams(200, 200));
            //text.setBackgroundResource(R.layout.grid_items_border);

            return text;
        }
    }


    public class getDataSubClass  extends AsyncTask<String, Void, String> {
        String accountId;
        String strRespons;

        Activity act;

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObjSubClass.put("ANDRODEVICE", "YES");
                jsonObjSubClass.put("REQTYPE", "TBIDANDROSUBCLASSSTUDENTS");
                jsonObjSubClass.put("USERNAME", strPassUsername);
                jsonObjSubClass.put("PASSWORD", strPassPassword);
                jsonObjSubClass.put("CMD1", strPassUsername);
                jsonObjSubClass.put("CMD2", "-");
                jsonObjSubClass.put("CMD3", "-");
                jsonObjSubClass.put("CMD4", "-");
                jsonObjSubClass.put("CMD5", "-");

                return httpPost(strPassURL, jsonObjSubClass.toString());

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

                //1xx info, 2xx success, 3xx redirect, 4xx client error, 5xx server error
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        responseSubClass+=line;
                    }
                }
                else {
                    responseSubClass="";
                    //throw new HttpException(responseCode+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG){
                Log.d("Sub Kelas", "response = " + responseSubClass);
            }

            return responseSubClass;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse){

            try{
                //RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

                JSONObject jsonObj = new JSONObject(strResponse);
                JSONArray arrSubClass = jsonObj.getJSONArray("msg_response");

                for (int i = 0; i<arrSubClass.length(); i++){
                    JSONObject subClassObject = arrSubClass.getJSONObject(i);

                    ItemSubClassData itemSubClassData = new ItemSubClassData(subClassObject.getString("NAMAKELAS"));
                    itemSubClassDataList.add(itemSubClassData);
                }

                //--- utk spinner
                for (int i=0; i<itemSubClassDataList.size();i++){
                    subClassName.add(itemSubClassDataList.get(i).getNamaSubKelas().toString());
                }

                spnSubClass.setPrompt("Pilih kelas");
                ArrayAdapter<String> spnSubClassArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,subClassName);
                spnSubClassArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSubClass.setAdapter(spnSubClassArrayAdapter);

            }catch (Throwable t) {
                Log.e("My app","could't parse malformed JSON : \""+strResponse+"\"");
            }
        }
    }
}
