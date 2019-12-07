package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.fragment.PresensiFragment;

public class PresensiActivity extends AppCompatActivity {

    String strUsernamePresensi, strPasswordPresensi, strURLPresensi;
    String strUserPasswPresensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi);

        strUsernamePresensi = getIntent().getExtras().getString("username_menu");
        strPasswordPresensi = getIntent().getExtras().getString("password_menu");
        strURLPresensi = getIntent().getExtras().getString("url_menu");

        loadFragment();
    }

    private boolean loadFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.presensi_frag_container, new PresensiFragment())
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
            return strUserPasswPresensi = strUsernamePresensi;
        }else if (strType.equals("passw")){
            return strUserPasswPresensi = strPasswordPresensi;
        }else if (strType.equals("url")){
            return strUserPasswPresensi = strURLPresensi;
        }
        return strUserPasswPresensi;
    }
}
