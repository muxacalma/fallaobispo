package app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.khaledonioscousin.mifalla.R;

import java.util.ArrayList;

import app.Objetos.Usuario;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.MyViewHolder> {

    ArrayList<Usuario> usuarios;
    Context context;
    Activity activity;
    RequestQueue queue;

    public AdapterUsuarios(Context context, ArrayList<Usuario> usuarios, Activity activity) {
        this.usuarios = usuarios;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterUsuarios.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuario, null, false);
        return new AdapterUsuarios.MyViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUsuarios.MyViewHolder myViewHolder, int i) {
        myViewHolder.asignarValores(usuarios.get(i));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, email;
        ImageView esAdmin;
        String valorNombre, valorEmail;
        int valorEsAdmin;
        LinearLayout elemento;


        public MyViewHolder(@NonNull final View itemView, final Activity act) {
            super(itemView);
            elemento = itemView.findViewById(R.id.elemento);
            nombre = itemView.findViewById(R.id.txtNombre);
            email = itemView.findViewById(R.id.txtEmail);
            esAdmin = itemView.findViewById(R.id.imgEsAdmin);

            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView,"imagenEvento");
                    Intent in = new Intent(act, DetalleEvento.class);
                    in.putExtra("rutaImagen", rutaImagen);
                    in.putExtra("titulo", valorTitulo);
                    in.putExtra("desc", valorDesc);
                    context.startActivity(in,activityOptionsCompat.toBundle());
                    */
                }
            });
        }

        public void asignarValores(Usuario usuario) {
            valorNombre = usuario.getNombreCompleto();
            nombre.setText(valorNombre);

            valorEmail = usuario.getEmail();
            email.setText(valorEmail);

            valorEsAdmin = usuario.getEsAdmin();
            if(valorEsAdmin == 1){
                esAdmin.setVisibility(View.VISIBLE);
            }
            else {
                esAdmin.setVisibility(View.INVISIBLE);
            }

        }
    }
}
