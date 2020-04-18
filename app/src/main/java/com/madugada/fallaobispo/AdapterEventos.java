package com.madugada.fallaobispo;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.MyViewHolder> {

    ArrayList<Evento> eventos;
    Context context;
    Activity activity;
    RequestQueue queue;

    public AdapterEventos(Context context, ArrayList<Evento> eventos) {
        this.eventos = eventos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterEventos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evento, null, false);
        return new AdapterEventos.MyViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEventos.MyViewHolder myViewHolder, int i) {
        myViewHolder.asignarValores(eventos.get(i));
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titulo;
        String valorTitulo, rutaImagen;
        MaterialCardView elemento;

        public MyViewHolder(@NonNull final View itemView, final Activity act) {
            super(itemView);
            elemento = itemView.findViewById(R.id.cardEvento);
            titulo = itemView.findViewById(R.id.txtTitulo);
            imageView = itemView.findViewById(R.id.imgEvento);

            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i = new Intent(activity, DetallesFirmas.class);
                    i.putExtra("tipoDoc", valorTipoDocumento);
                    i.putExtra("numDoc", valorCodigo);
                    i.putExtra("empresa", valorEmpresa);
                    i.putExtra("firmarComo", firmarComo);
                    activity.startActivityForResult(i, 0);*/
                }
            });
        }

        public void asignarValores(Evento evento) {
            valorTitulo = evento.getTitulo();
            titulo.setText(valorTitulo);

            rutaImagen = evento.getRuta_imagen();
            Picasso.get().load(rutaImagen).into(imageView);
        }


    }

}
