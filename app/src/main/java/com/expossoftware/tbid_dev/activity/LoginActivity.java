package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expossoftware.tbid_dev.BuildConfig;
import com.expossoftware.tbid_dev.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    EditText tbxUsername, tbxPassword;
    String strLoginStatus, strUsername, strPassword;
    String strSesStudentCode, strSesStudentName;

    private static final String URL = "http://103.215.177.234:3030";

    private JSONObject jsonObject = new JSONObject();
    private String response = "";
    private boolean IsParseJson;
    private boolean isCheckLogin ;
    private String strInputLoginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tbxUsername = findViewById(R.id.tbxUsername);
        tbxPassword = findViewById(R.id.tbxPassword);

        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckLogin = false;
                strUsername = tbxUsername.getText().toString();
                strPassword = tbxPassword.getText().toString();
                Toast.makeText(getApplicationContext(),"BUTTON LOGIN", Toast.LENGTH_SHORT).show();

                if (isCheck()) {
                    new GetDataTask().execute(URL);
                }
                Toast.makeText(getApplicationContext(),strInputLoginState,Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isCheck() {
        if (strUsername.equals("") && strPassword.equals("")) {
            isCheckLogin = false;
            strInputLoginState = "Username dan password tidak boleh kosong.";
        } else if (strUsername.equals("")) {
            isCheckLogin = false;
            strInputLoginState = "Username tidak boleh kosong.";
        } else if (strPassword.equals("")) {
            isCheckLogin = false;
            strInputLoginState = "Password tidak boleh kosong.";
        } else if (strUsername.length() > 1 && strPassword.length() > 1) {
            isCheckLogin = true;
            strInputLoginState = "";
        }

        return isCheckLogin;
    }

    public class GetDataTask extends AsyncTask<String, Void, String> {
        //String accountId;
        //String strRespons;

        @Override
        protected String doInBackground(String... url) {

            try {
                jsonObject.put("ANDRODEVICE", "YES");
                jsonObject.put("REQTYPE", "TBIDANDROLOGIN");
                jsonObject.put("USERNAME", strUsername);
                jsonObject.put("PASSWORD", strPassword);
                jsonObject.put("CMD1", "-");
                jsonObject.put("CMD2", "-");
                jsonObject.put("CMD3", "-");
                jsonObject.put("CMD4", "-");
                jsonObject.put("CMD5", "-");

                return httpPost(url[0], jsonObject.toString());

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String responseData) {
            String strRespons="";

            try {
                if (responseData != null) {

                    JSONObject jsObj = new JSONObject(responseData);
                    if (jsObj.getString("error").equals("false")) {

                        //-- perlu dicek lagi
                        JSONArray tmpJsonArray = jsObj.getJSONArray("msg_response");
                        for (int i = 0; i < tmpJsonArray.length(); i++) {
                            JSONObject loopObj = tmpJsonArray.getJSONObject(i);
                            if(loopObj.getString("LOGIN_STATUS").equals("STUDENT_NOT_REGISTERED")){
                                IsParseJson = false;
                                strRespons = "Periksa kembali username dan password anda.";
                            }else if(loopObj.getString("LOGIN_STATUS").equals("PASSWORD_NOT_MATCH")){
                                IsParseJson = false;
                                strRespons = "Password anda belum diisi atau salah.";
                            }else if(loopObj.getString("LOGIN_STATUS").equals("LOGIN_SUCCESS")){
                                IsParseJson = true;
                                strRespons = "LOGIN SUCCESS";
                            }
                        }
                    }else{
                        IsParseJson = false;
                        strRespons = "No JSON received !";
                    }
                } else {
                    IsParseJson = false;
                    strRespons = "No JSON received !";
                }

                Toast.makeText(getApplicationContext(),strRespons, Toast.LENGTH_LONG).show();

                if (IsParseJson) {
                    doParseJson(responseData);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public String httpPost(String urlString, String jsonData) {
            java.net.URL url;
            //String response;

            HashMap<String, String> params;
            try {
                response="";
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
                        response+=line;
                    }
                }
                else {
                    response="";
                    //throw new HttpException(responseCode+"");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG){
                Log.d("LOGIN", "response = " + response);
            }

            return response;
        }

        @Override
        protected void onCancelled() {
        }

        public void doParseJson(String strResponse){

            try{
                JSONObject jsonObj = new JSONObject(strResponse);
                JSONArray arrLogin = jsonObj.getJSONArray("msg_response");

                for (int i = 0; i<arrLogin.length(); i++){
                    JSONObject loginObject = arrLogin.getJSONObject(i);

                    ItemLogin itemLogin = new ItemLogin(loginObject.getString("LOGIN_STATUS"),
                            loginObject.getString("KODE_MURID"), loginObject.getString("NAMA_MURID"));
                    strLoginStatus = (String) loginObject.getString("LOGIN_STATUS");
                    strSesStudentCode = (String) loginObject.getString("KODE_MURID");
                    strSesStudentName = (String) loginObject.getString("NAMA_MURID");
                }

                if(strLoginStatus.equals("LOGIN_SUCCESS")){
                    Toast.makeText(getApplicationContext(),"LOGIN SUCCESS", Toast.LENGTH_LONG).show();
                    Intent menu=new Intent(getApplicationContext(),MenuActivity.class);
                    menu.putExtra("username_login",strSesStudentCode);
                    menu.putExtra("password_login",strPassword);
                    menu.putExtra("studentName_login",strSesStudentName);
                    startActivity(menu);
                    finish();
                }else{
                    response = "";
                    Toast.makeText(getApplicationContext(),"Salah username atau password",Toast.LENGTH_SHORT).show();
                }

            }catch (Throwable t) {
                Log.e("My app","could't parse malformed JSON : \""+strResponse+"\"");
            }
        }

        public class ItemLogin implements Serializable {
            String loginStatus,studentCode,studentName;

            public ItemLogin(String tloginstatus, String tStudentCode, String tStudentName){
                this.loginStatus = tloginstatus;
                this.studentCode = tStudentCode;
                this.studentName = tStudentName;  }

            public String getLoginStatus(){return loginStatus;}
            public String getStudentCode(){return studentCode;}
            public String getStudentName(){return studentName;}
        }
    }
}
