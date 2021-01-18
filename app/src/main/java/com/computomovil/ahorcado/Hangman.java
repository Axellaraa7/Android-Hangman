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
    private String actualWord; //Guardamos la palabra para que no se repita
    protected int lifes=6; //Numero de vidas: 6
    private TextView[] palabra; //Arreglo de TextView para la palabra a adivina
    private Random rnd=new Random(); //Palabra aleatoria
    private LinearLayout llWord; //Referencia al linearLayout
    private Adaptador adaptador; //Referencia al adaptador
    private GridView gvLetras; //Referencia al GridView
    protected int sizeWord=0; // Tamaño de la palabra
    private ImageView[] toy=new ImageView[lifes]; //Referencia al muñeco
    protected int part=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        llWord=findViewById(R.id.llWord); //Conexion de linearLayout
        gvLetras=findViewById(R.id.gvLetras); //Conexion de gridview

        //Conexion de ImageView
        toy[0]=findViewById(R.id.ivHead);
        toy[1]=findViewById(R.id.ivCuerpo);
        toy[2]=findViewById(R.id.ivBizq);
        toy[3]=findViewById(R.id.ivBder);
        toy[4]=findViewById(R.id.ivPizq);
        toy[5]=findViewById(R.id.ivPder);

        //Método jugar
        jugar();
    }

    private void jugar(){
        //Asignamos una palabra aleatoria
        String word=words[rnd.nextInt(words.length)].toUpperCase() ;
        //Comprobamos que la palabra no sea la anterior
        while(word.equals(actualWord)) word=words[rnd.nextInt(words.length)].toUpperCase();
        //Asignamos la nueva palabra a la palabra actual
        actualWord=word;
        //Instanciamos el arreglo de TextViews
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
                setDisabled();
                AlertDialog.Builder ad=new AlertDialog.Builder(this);
                ad.setTitle(getResources().getString(R.string.winner));
                ad.setPositiveButton(getResources().getString(R.string.playAgain), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hangman.this.jugar();
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
            toy[part].setVisibility(View.VISIBLE);
            setDisabled();
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            //HARDCODE
            ad.setTitle(getResources().getString(R.string.loser));
            //HARDCODE
            ad.setPositiveButton(getResources().getString(R.string.playAgain), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Hangman.this.jugar();
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
}