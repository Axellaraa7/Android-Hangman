package com.computomovil.ahorcado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class Adaptador extends BaseAdapter {
    private String[] letras;
    private LayoutInflater inflater;

    public Adaptador(Context context){
        letras=new String[27];
        for(int i=0;i<letras.length;i++){
            if(i==context.getResources().getInteger(R.integer.catorce)){
                letras[i]=context.getResources().getString(R.string.enie);
            }else{
                if(i>context.getResources().getInteger(R.integer.catorce)) letras[i]=context.getResources().getString(R.string.vacio)+(char)(context.getResources().getString(R.string.letra_a).charAt(context.getResources().getInteger(R.integer.cero))+(i-context.getResources().getInteger(R.integer.uno)));
                else letras[i]=context.getResources().getString(R.string.vacio)+(char)(context.getResources().getString(R.string.letra_a).charAt(context.getResources().getInteger(R.integer.cero))+(i));
            }
        }
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return letras.length;
    }

    @Override
    public Object getItem(int position) {
        return letras[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btnLetra;
        if(convertView==null){
            btnLetra=(Button)inflater.inflate(R.layout.boton_letra,parent,false);
        }else{
            btnLetra=(Button)convertView;
        }

        btnLetra.setText(letras[position]);

        return btnLetra;
    }
}
