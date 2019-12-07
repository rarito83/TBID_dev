package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.fragment.PengajarFragment;

public class PengajarActivity extends AppCompatActivity {

    String strUsername, strPassw;
    String strUserPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajar);

        //--- get data username and password from login
        //--- sent by intent
        strUsername= getIntent().getExtras().getString("username_menu");
        strPassw=getIntent().getExtras().getString("password_menu");

        loadFragment();
    }

    private boolean loadFragment() {

        //---- 1
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.pengajar_frag_container, new PengajarFragment())
                .addToBackStack(null)
                .commit();
        return true;
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public String getDataUser(String strType){

        if(strType.equals("user")){
            return strUserPassw = strUsername;
        }else if (strType.equals("passw")){
            return strUserPassw = strPassw;
        }

        return strUserPassw;
    }
}
