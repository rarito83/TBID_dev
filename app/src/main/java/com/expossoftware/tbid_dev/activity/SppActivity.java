package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.fragment.PembayaranSPPFragment;

public class SppActivity extends AppCompatActivity {

    String strUsername, strPassw;
    String strUserPassw, strURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spp);

        strUsername = getIntent().getExtras().getString("username_menu");
        strPassw = getIntent().getExtras().getString("password_menu");
        strURL = getIntent().getExtras().getString("url_menu");

        loadFragment();
    }

    private boolean loadFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.spp_frag_container, new PembayaranSPPFragment())
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
        }else if (strType.equals("url")){
            return strUserPassw = strURL;
        }

        return strUserPassw;
    }
}
