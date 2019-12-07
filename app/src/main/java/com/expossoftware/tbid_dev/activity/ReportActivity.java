package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.fragment.ReportFragment;

public class ReportActivity extends AppCompatActivity {

    String strUserPassw;
    String strUsername, strPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        strUsername = getIntent().getExtras().getString("username_menu");
        strPassw = getIntent().getExtras().getString("password_menu");

        loadFragment();
    }

    private boolean loadFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_report_container, new ReportFragment())
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
