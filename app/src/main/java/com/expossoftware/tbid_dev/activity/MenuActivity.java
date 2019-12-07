package com.expossoftware.tbid_dev.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.adapter.SliderImageAdapter;
import com.smarteist.autoimageslider.SliderView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPengajar, btnProgramKelas, btnPresensi, btnPembayaranSPP, btnReport;
    TextView tbxMenuItem, tbxStudentCode, tbxStudentName;
    String strUsrName, strPassw, strStudentName;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();
        setStudent();
        int[] mImagesId = new int[] {R.drawable.images1, R.drawable.images2, R.drawable.images3, R.drawable.images4, R.drawable.images5};

        viewFlipper = findViewById(R.id.view_flipper);
        for (int image : mImagesId) {
            flipperImages(image);
        }
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000); // 3 sec
        viewFlipper.setAutoStart(true);

        //animation
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    @SuppressLint("WrongViewCast")
    public void initView(){
        //-- getting username & password value that sent from login
        strUsrName = getIntent().getExtras().getString("username_login");
        strPassw = getIntent().getExtras().getString("password_login");
        strStudentName = getIntent().getExtras().getString("studentName_login");

        //-- initialize buttons and textview
        btnPengajar = findViewById(R.id.btn_pengajar);
        btnPresensi = findViewById(R.id.btn_programkelas);
        btnProgramKelas = findViewById(R.id.btn_presensi);
        btnPembayaranSPP = findViewById(R.id.btn_pembayaran);
        btnReport = findViewById(R.id.btn_raport);
//        tbxMenuItem = findViewById(R.id.tbxMenuItem);
        tbxStudentCode = findViewById(R.id.tbxStudentCode);
        tbxStudentName = findViewById(R.id.tbxStudentName);

        btnPengajar.setOnClickListener(this);
        btnPresensi.setOnClickListener(this);
        btnProgramKelas.setOnClickListener(this);
        btnPembayaranSPP.setOnClickListener(this);
    }

    public void setStudent(){
        tbxStudentName.setText(strStudentName);
        tbxStudentCode.setText(strUsrName);
    }

    @Override
    public void onClick(View v) {
        String strValue=null;
        Intent intentItemMenu=null;

        switch(v.getId()){
            case R.id.btn_pengajar:
                intentItemMenu = new Intent(this,PengajarActivity.class);
                break;

            case R.id.btn_programkelas:
                intentItemMenu = new Intent(this,ProgramKelasActivity.class);
                break;

            case R.id.btn_presensi:
                intentItemMenu = new Intent(this,PresensiActivity.class);
                break;

            case R.id.btn_pembayaran:
                intentItemMenu = new Intent(this,SppActivity.class);
                break;

            case R.id.btn_raport:
                intentItemMenu = new Intent(this, ReportActivity.class);
                break;
        }
        intentItemMenu.putExtra("username_menu",strUsrName);
        intentItemMenu.putExtra("password_menu",strPassw);
        intentItemMenu.putExtra("url_menu","http://103.215.177.234:3030");
        startActivity(intentItemMenu);
    }

    public void onBackPressed(){
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
