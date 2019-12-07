package com.expossoftware.tbid_dev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.expossoftware.tbid_dev.activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation fromBottom, fromTop;

        Button btnWelcome = findViewById(R.id.btnWelcome);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombotttom);
        btnWelcome.setAnimation(fromBottom);

        ImageView imgBg = findViewById(R.id.imgBg);
        fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        imgBg.setAnimation(fromTop);

        btnWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}
