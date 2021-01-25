package com.computomovil.ahorcado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Hangman extends AppCompatActivity {

    private String category;
    private String actualWord; //Guardamos la palabra
    private boolean ban=true;

    private int lifes=6; //Numero de vidas: 6
    private TextView[] palabra; //Arreglo de TextView para la palabra a adivina
    private LinearLayout llWord; //Referencia al linearLayout
    private Adaptador adaptador; //Referencia al adaptador
    private GridView gvLetras; //Referencia al GridView
    private int sizeWord=0; // Tamaño de la palabra
    private ImageView[] toy=new ImageView[lifes]; //Referencia al muñeco
    private int part=0;//contador del cuerpo dl muñeco
    private TextView tvClave;

    private String url;
    private RequestQueue queue;
    private JsonObjectRequest jsonObject;
    private ImageButton ivSound;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        llWord=findViewById(R.id.llWord); //Conexion de linearLayout
        gvLetras=findViewById(R.id.gvLetras); //Conexion de gridview
        tvClave=findViewById(R.id.tvClave);
        ivSound=findViewById(R.id.ivSound);
        //Conexion de ImageView
        toy[0]=findViewById(R.id.ivHead);
        toy[1]=findViewById(R.id.ivCuerpo);
        toy[2]=findViewById(R.id.ivBizq);
        toy[3]=findViewById(R.id.ivBder);
        toy[4]=findViewById(R.id.ivPizq);
        toy[5]=findViewById(R.id.ivPder);

        //Método jugar
        Conexion();

    }

    private void jugar(){

        palabra=new TextView[actualWord.length()];
        //Limpiamos el LinearLayout
        llWord.removeAllViews();
        //Reestablecemos el ahorcado
        eraseToy();
        //Ciclo que llenara el LinearLayout con los textViews
        for(int i=0;i<actualWord.length();i++){
            //Instanciamos los elementos del arreglo
            palabra[i]=new TextView(this);
            //Agregamos la letra en su respectiva posicion
            palabra[i].setText(actualWord.charAt(i)+getResources().getString(R.string.vacio));
            //Asignamos el tamaño de la letra
            palabra[i].setTextSize(getResources().getInteger(R.integer.sizeLetter));
            palabra[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //Asignamos que el texto esté en el centro
            palabra[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //Asignamos el color del texto
            palabra[i].setTextColor(getResources().getColor(R.color.black));
            //Asignamos el background
            palabra[i].setBackgroundResource(R.drawable.guionbajo);

            //Añadimos al layout el textView
            llWord.addView(palabra[i]);
        }
        tvClave.setText(category);
        //Instanciamos el adaptador
        adaptador=new Adaptador(this);
        //Asignamos el adaptador al gridView
        gvLetras.setAdapter(adaptador);
    }

    public void letraSelected(View view){
        //La letra seleccionada la asignamos a la variable letra
        char letra=((TextView)view).getText().toString().charAt(0);
        //Deshabilitamos esa letra
        view.setEnabled(false);
        //Comprobamos si el usuario ha ganado
        boolean ban=false;

        //Ciclo que recorre la palabra y muestra las letras
        for(int i=0;i<actualWord.length();i++){
            if(actualWord.toUpperCase().charAt(i)==letra){
                ban=true;
                sizeWord++;
                palabra[i].setTextColor(getResources().getColor(R.color.white));
            }
        }

        if(ban){
            if(sizeWord==actualWord.length()){
                mp.stop();
                mp=MediaPlayer.create(this,R.raw.aleluyah);
                mp.start();
                setDisabled();
                AlertDialog.Builder ad=new AlertDialog.Builder(this);
                ad.setTitle(getResources().getString(R.string.winner));
                ad.setPositiveButton(getResources().getString(R.string.playAgain), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hangman.this.Conexion();
                    }
                });
                ad.setNegativeButton(getResources().getString(R.string.salir), new DialogInterface.OnClickListener() {
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
            mp.stop();
            mp=MediaPlayer.create(this,R.raw.risa_demoniaca);
            mp.start();
            toy[part].setVisibility(View.VISIBLE);
            setDisabled();
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            //HARDCODE
            ad.setTitle(getResources().getString(R.string.loser));
            ad.setMessage(getResources().getString(R.string.respuesta)+actualWord);
            //HARDCODE
            ad.setPositiveButton(getResources().getString(R.string.playAgain), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Hangman.this.Conexion();
                }
            });
            ad.setNegativeButton(getResources().getString(R.string.salir), new DialogInterface.OnClickListener() {
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
        part=0;
        sizeWord=0;
    }

    public void eraseToy(){
        for(int i=0;i<toy.length;i++){
            toy[i].setVisibility(View.INVISIBLE);
        }
    }

    public void Conexion(){
        if(ban){
            mp=MediaPlayer.create(this,R.raw.sonido_espera);
            mp.start();
        }else mp.stop();
        queue= Volley.newRequestQueue(this);
        url=getResources().getString(R.string.url);
        jsonObject=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    category=response.getString(getResources().getString(R.string.category)).toUpperCase();
                    actualWord=response.getString(getResources().getString(R.string.word)).toUpperCase();
                    jugar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObject);
    }

    public void disableSound(View view) {
        if(ban){
            ban=false;
            ivSound.setBackgroundResource(R.drawable.volume_up);
            mp.stop();
        }else{
            ban=true;
            ivSound.setBackgroundResource(R.drawable.volume_off);
            mp=MediaPlayer.create(this,R.raw.sonido_espera);
            mp.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
}