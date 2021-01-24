package com.computomovil.ahorcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mp==null){
            mp=MediaPlayer.create(this,R.raw.sonido_espera);
        }
        mp.start();
    }

    public void goGame(View view){
        Intent intent=new Intent(MainActivity.this,Hangman.class);
        startActivity(intent);
    }

    public void goInfo(View view){
        Toast.makeText(this,getResources().getString(R.string.MALA), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

}
