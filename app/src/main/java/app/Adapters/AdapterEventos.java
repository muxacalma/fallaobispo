package app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khaledonioscousin.mifalla.R;

import app.Activities.DetalleEvento;
import app.Objetos.Evento;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.MyViewHolder> {

    ArrayList<Evento> eventos;
    Context context;
    Activity activity;
    FirebaseAuth mAuth;
    private StorageReference storageRef;
    StorageReference urlFichero;
    Uri uriArchivo;

    public AdapterEventos(Context context, ArrayList<Evento> eventos, Activity activity) {
        this.eventos = eventos;
        this.context = context;
        this.activity = activity;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        storageRef = FirebaseStorage.getInstance().getReference();
        urlFichero = FirebaseStorage.getInstance().getReferenceFromUrl("gs://falla-obispo.appspot.com/");
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(activity, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("SIGN IN ANONYMOUSLY", "signInAnonymously:FAILURE", exception);
                    }
                });
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
        TextView titulo, desc, fecha, hora, precio;
        String valorTitulo, rutaImagen, valorDesc, valorFecha, valorHora, valorPrecio;
        float valoracionMedia, valoracionUsuario;
        String valorHayInscripcion;
        MaterialCardView elemento, cardInscritos;
        RatingBar ratingBar;
        int valorId;

        public MyViewHolder(@NonNull final View itemView, final Activity act) {
            super(itemView);
            elemento = itemView.findViewById(R.id.cardEvento);
            titulo = itemView.findViewById(R.id.txtTitulo);
            desc = itemView.findViewById(R.id.txtDesc);
            fecha = itemView.findViewById(R.id.txtFecha);
            hora = itemView.findViewById(R.id.txtHora);
            precio = itemView.findViewById(R.id.txtPrecio);
            cardInscritos = itemView.findViewById(R.id.cardInscritos);
            imageView = itemView.findViewById(R.id.imgEvento);
            ratingBar = itemView.findViewById(R.id.listitemrating);

            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pasarFichero();
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView,"imagenEvento");
                    Intent in = new Intent(act, DetalleEvento.class);
                    in.putExtra("id", valorId);
                    in.putExtra("rutaImagen", "foto.png");
                    in.putExtra("titulo", valorTitulo);
                    in.putExtra("desc", valorDesc);
                    in.putExtra("fecha", valorFecha);
                    in.putExtra("hora", valorHora);
                    in.putExtra("precio", valorPrecio);
                    in.putExtra("hayInscripcion", valorHayInscripcion);
                    in.putExtra("valoracionUsuario", valoracionUsuario);
                    context.startActivity(in,activityOptionsCompat.toBundle());
                }
            });
        }

        public void pasarFichero(){
            try {
                Drawable d; // the drawable (Captain Obvious, to the rescue!!!)
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                String fileName = "foto.png";

                FileOutputStream fileOutStream = context.openFileOutput(fileName, MODE_PRIVATE);
                fileOutStream.write(bitmapdata);  //b is byte array
                //(used if you have your picture downloaded
                // from the *Web* or got it from the *devices camera*)
                fileOutStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        public void asignarValores(Evento evento) {
            valorId = evento.getId();

            valorTitulo = evento.getTitulo();
            titulo.setText(valorTitulo);

            valorDesc = evento.getDescripcion();
            desc.setText(valorDesc);

            valorFecha = evento.getFecha();
            fecha.setText(valorFecha);

            valorHora = evento.getHora();
            hora.setText(valorHora);

            valorPrecio = evento.getPrecio();
            if(valorPrecio.equals("0"))
                precio.setText("Precio: Gratis");
            else
                precio.setText("Precio: " + valorPrecio + "€");

            valoracionMedia = evento.getValoracionEvento();
            if(valoracionMedia != 0){
                ratingBar.setRating(valoracionMedia);
            }

            valoracionUsuario = evento.getValoracionUsuario();

            valorHayInscripcion = evento.getHayInscripcion();
            if(valorHayInscripcion.equals("1"))
                cardInscritos.setVisibility(View.VISIBLE);
            else cardInscritos.setVisibility(View.INVISIBLE);

            rutaImagen = evento.getRuta_imagen();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(rutaImagen);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.d("URI", uri + "");
                    Picasso.get().load(uri).into(imageView);
                    uriArchivo = uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.d("MEC", exception.toString());
                }
            });
        }
    }
}
