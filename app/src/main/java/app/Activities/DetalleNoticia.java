package app.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khaledonioscousin.mifalla.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DetalleNoticia extends AppCompatActivity {

    private static final String URL_ELIMINAR= "http://muxacalma.com/obispo/borrarNoticia.php";

    private int idNoticia;

    MaterialButton btnEliminar;

    //
    //      Método para usar flecha de atrás en Action Bar
    //
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView imageView = findViewById(R.id.imagen);
        String rutaImagen = getIntent().getStringExtra("rutaImagen");
        File filePath = getFileStreamPath(rutaImagen);
        Drawable d = Drawable.createFromPath(filePath.toString());

        imageView.setImageDrawable(d);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView titulo = findViewById(R.id.txtTitulo);
        titulo.setText(getIntent().getStringExtra("titulo"));
        TextView desc = findViewById(R.id.txtDesc);
        desc.setText(getIntent().getStringExtra("desc"));

        idNoticia = getIntent().getIntExtra("id", -1);

        //Controlar botón de eliminar
        btnEliminar = findViewById(R.id.btnEliminarNoticia);
        habilitarBotonEliminar();
    }

    public void habilitarBotonEliminar(){
        int esAdmin = getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE).getInt("esAdmin", 0);
        if(esAdmin == 1){
            btnEliminar.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            btnEliminar.setVisibility(View.VISIBLE);
        }
        else {
            btnEliminar.setVisibility(View.GONE);
        }
    }

    public void eliminar(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ELIMINAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Noticia eliminada", response);
                        if(response.equals("1")){
                            muestraAlert("Noticia eliminada", "Esta noticia se ha borrado correctamente.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Borrar Noticia", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idNoticia + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void eliminarNoticia(View view){
        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("¿Seguro que quieres eliminar esta noticia?");
        materialAlertDialogBuilder.setMessage("Esta acción no se podrá deshacer.");
        materialAlertDialogBuilder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                eliminar();
            }
        });
        materialAlertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        materialAlertDialogBuilder.show();
    }


    public void muestraAlert(String titulo, String mensaje){

        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(titulo);
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        materialAlertDialogBuilder.show();
    }

}
