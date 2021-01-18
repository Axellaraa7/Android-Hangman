package com.computomovil.ahorcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread hilo=new Thread(){
            public void run(){
                try{
                    sleep(getResources().getInteger(R.integer.secSplash));
                }catch(Exception e){

                }finally{
                    Intent main=new Intent(Splash.this,MainActivity.class);
                    startActivity(main);
                    finish();
                }
            }
        };

        hilo.start();
    }
}