package app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khaledonioscousin.mifalla.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import app.Activities.DetalleNoticia;
import app.Activities.MyAppGlideModule;
import app.Objetos.Noticia;

import static android.content.Context.MODE_PRIVATE;

@com.bumptech.glide.annotation.GlideModule
public class AdapterNoticias extends RecyclerView.Adapter<AdapterNoticias.MyViewHolder> {

    ArrayList<Noticia> noticias;
    Context context;
    Activity activity;
    RequestQueue queue;
    FirebaseAuth mAuth;
    private StorageReference storageRef;
    StorageReference urlFichero;
    Uri uriArchivo;

    public AdapterNoticias(Context context, ArrayList<Noticia> noticias, Activity activity) {
        this.noticias = noticias;
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
        int valorId;
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
                    pasarFichero();
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView,"imagenNoticia");
                    //ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, titulo,"tituloNoticia");
                    Intent in = new Intent(act, DetalleNoticia.class);
                    in.putExtra("rutaImagen","foto.png");
                    in.putExtra("titulo", valorTitulo);
                    in.putExtra("desc", valorDesc);
                    in.putExtra("id", valorId);
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
                //otherwise this technique is useless
                fileOutStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        public void asignarValores(Noticia noticia) {
            valorId = noticia.getId();

            valorTitulo = noticia.getTitulo();
            titulo.setText(valorTitulo);

            valorDesc = noticia.getDescripcion();
            desc.setText(valorDesc);

            valorFecha = noticia.getFecha();
            fecha.setText(valorFecha);

            rutaImagen = noticia.getRutaImagen();
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
