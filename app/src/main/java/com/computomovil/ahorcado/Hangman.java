package com.computomovil.ahorcado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class Hangman extends AppCompatActivity {

    //HARDCODE
    private String[] words={"kamila","axel","tchaikovsky","beethoven","mozart"};
    //HARDCODE
    private String clave="clasico";
    private String actual;
    private int lifes=6;
    private TextView[] palabra;
    private Random rnd=new Random();
    private LinearLayout llWord;
    private Adaptador adaptador;
    private GridView gvLetras;
    private int sizeWord;
    private ImageView[] toy=new ImageView[lifes];
    private int part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        llWord=findViewById(R.id.llWord);
        gvLetras=findViewById(R.id.gvLetras);

        toy[0]=findViewById(R.id.ivHead);
        toy[1]=findViewById(R.id.ivCuerpo);
        toy[2]=findViewById(R.id.ivBizq);
        toy[3]=findViewById(R.id.ivBder);
        toy[4]=findViewById(R.id.ivPizq);
        toy[5]=findViewById(R.id.ivPder);

        jugar();
    }

    private void jugar(){
        String word=words[rnd.nextInt(words.length)];

        while(word.equals(actual)) word=words[rnd.nextInt(words.length)];

        actual=word;
        palabra=new TextView[actual.length()];

        llWord.removeAllViews();
        for(int i=0;i<actual.length();i++){
            palabra[i]=new TextView(this);
            //HARDCODE
            palabra[i].setText(actual.toUpperCase().charAt(i)+"  ");
            palabra[i].setTextSize(24);
            palabra[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            palabra[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            palabra[i].setTextColor(getResources().getColor(R.color.black));
            palabra[i].setBackgroundColor(getResources().getColor(R.color.black));
            llWord.addView(palabra[i]);
        }

        adaptador=new Adaptador(this);
        gvLetras.setAdapter(adaptador);
        sizeWord=0;
        part=0;
        eraseToy();
    }

    public void letraSelected(View view){
        String caracter=((TextView)view).getText().toString();
        char letra=caracter.charAt(0);
        view.setEnabled(false);

        boolean ban=false;

        for(int i=0;i<actual.length();i++){
            if(actual.toUpperCase().charAt(i)==letra){
                ban=true;
                sizeWord++;
                palabra[i].setTextColor(getResources().getColor(R.color.white));
            }
        }

        if(ban){
            if(sizeWord==actual.length()){
                setDisabled();
                AlertDialog.Builder ad=new AlertDialog.Builder(this);
                //HARDCODE
                ad.setTitle("Ganaste");
                //HARDCODE
                ad.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hangman.this.jugar();
                    }
                });
                ad.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Hangman.this,MainActivity.class);
                        startActivity(intent);
                        Hangman.this.finish();
                    }
                });
                ad.show();
            }
        }else if(part<lifes-1){
            toy[part].setVisibility(View.VISIBLE);
            part++;
        }else{
            toy[part].setVisibility(View.VISIBLE);
            setDisabled();
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            //HARDCODE
            ad.setTitle("Perdiste");
            //HARDCODE
            ad.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Hangman.this.jugar();
                }
            });
            ad.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(Hangman.this,MainActivity.class);
                    startActivity(intent);
                    Hangman.this.finish();
                }
            });
            ad.show();
        }
    }

    public void setDisabled(){
        for(int i=0;i<gvLetras.getChildCount();i++){
            gvLetras.getChildAt(i).setEnabled(false);
        }
    }

    public void eraseToy(){
        for(int i=0;i<toy.length;i++){
            toy[i].setVisibility(View.INVISIBLE);
        }
    }
}