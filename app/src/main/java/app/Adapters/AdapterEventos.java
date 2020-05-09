package app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.material.card.MaterialCardView;
import com.madugada.fallaobispo.R;

import app.Activities.DetalleEvento;
import app.Objetos.Evento;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.MyViewHolder> {

    ArrayList<Evento> eventos;
    Context context;
    Activity activity;
    RequestQueue queue;

    public AdapterEventos(Context context, ArrayList<Evento> eventos, Activity activity) {
        this.eventos = eventos;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterEventos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_evento, null, false);
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
        TextView titulo, desc;
        String valorTitulo, rutaImagen, valorDesc;
        MaterialCardView elemento;

        public MyViewHolder(@NonNull final View itemView, final Activity act) {
            super(itemView);
            elemento = itemView.findViewById(R.id.cardEvento);
            titulo = itemView.findViewById(R.id.txtTitulo);
            desc = itemView.findViewById(R.id.txtDesc);
            imageView = itemView.findViewById(R.id.imgEvento);

            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView,"imagenEvento");
                    Intent in = new Intent(act, DetalleEvento.class);
                    in.putExtra("rutaImagen", rutaImagen);
                    in.putExtra("titulo", valorTitulo);
                    in.putExtra("desc", valorDesc);
                    context.startActivity(in,activityOptionsCompat.toBundle());
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

            valorDesc = evento.getDescripcion();
            desc.setText(valorDesc);


            rutaImagen = evento.getRuta_imagen();
            Picasso.get().load(rutaImagen).into(imageView);
        }
    }
}
