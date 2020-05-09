package app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.Activities.DetalleNoticia;
import app.Objetos.Noticia;

public class AdapterNoticias extends RecyclerView.Adapter<AdapterNoticias.MyViewHolder> {

    ArrayList<Noticia> noticias;
    Context context;
    Activity activity;
    RequestQueue queue;

    public AdapterNoticias(Context context, ArrayList<Noticia> noticias, Activity activity) {
        this.noticias = noticias;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterNoticias.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_noticia, null, false);
        return new AdapterNoticias.MyViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNoticias.MyViewHolder myViewHolder, int i) {
        myViewHolder.asignarValores(noticias.get(i));
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titulo, desc, fecha;
        String valorTitulo, valorDesc, valorFecha, rutaImagen;
        MaterialCardView elemento;

        public MyViewHolder(@NonNull final View itemView, final Activity act) {
            super(itemView);
            elemento = itemView.findViewById(R.id.cardNoticia);
            titulo = itemView.findViewById(R.id.txtTitulo);
            imageView = itemView.findViewById(R.id.imgNoticia);
            desc = itemView.findViewById(R.id.txtDesc);
            fecha = itemView.findViewById(R.id.txtFecha);
            Log.d("ACTIVITY ADAPTER", act.getLocalClassName());
            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView,"imagenNoticia");
                    Intent in = new Intent(act, DetalleNoticia.class);
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

        public void asignarValores(Noticia noticia) {
            valorTitulo = noticia.getTitulo();
            titulo.setText(valorTitulo);

            valorDesc = noticia.getDescripcion();
            desc.setText(valorDesc);

            valorFecha = noticia.getFecha();
            fecha.setText(valorFecha);

            rutaImagen = noticia.getRutaImagen();
            Picasso.get().load(rutaImagen).into(imageView);
        }
    }
}
