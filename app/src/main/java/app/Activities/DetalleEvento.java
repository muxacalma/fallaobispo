package app.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khaledonioscousin.mifalla.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DetalleEvento extends AppCompatActivity {

    private static final String URL_BORRAR_EVENTO = "http://muxacalma.com/obispo/borrarEvento.php";
    private static final String URL_VALORAR_EVENTO = "http://muxacalma.com/obispo/valorarEvento.php";
    private static final String URL_ACTUALIZAR_VALORACION_EVENTO = "http://muxacalma.com/obispo/actualizarValoracionEvento.php";

    ImageView imageView;
    TextView titulo, desc, fecha, hora, precio;
    boolean hayInscripcion;
    MaterialButton btnInscripcion, btnBorrarEvento, btnValorarEvento;
    RatingBar rating;
    float valoracionUsuario;
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
         setContentView(R.layout.activity_detalle_evento);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imagen);
        String rutaImagen = getIntent().getStringExtra("rutaImagen");
        File filePath = getFileStreamPath(rutaImagen);
        Drawable d = Drawable.createFromPath(filePath.toString());
        imageView.setImageDrawable(d);

        titulo = findViewById(R.id.txtTitulo);
        titulo.setText(getIntent().getStringExtra("titulo"));

        desc = findViewById(R.id.txtDesc);
        desc.setText(getIntent().getStringExtra("desc"));

        fecha = findViewById(R.id.txtFecha);
        fecha.setText(getIntent().getStringExtra("fecha"));

        hora = findViewById(R.id.txtHora);
        hora.setText(getIntent().getStringExtra("hora"));

        precio = findViewById(R.id.txtPrecio);
        precio.setText(getIntent().getStringExtra("precio") + "€");

        btnInscripcion = findViewById(R.id.btnConfirmarAsistencia);
        String hayInscripcion = getIntent().getStringExtra("hayInscripcion");
        if(hayInscripcion.equals("1"))
            btnInscripcion.setVisibility(View.VISIBLE);
        else
            btnInscripcion.setVisibility(View.GONE);

        btnValorarEvento = findViewById(R.id.btnValorarEvento);
        btnValorarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnValorarEvento.getText().toString().equals("Enviar valoración"))
                    valorarEvento();
                else if(btnValorarEvento.getText().toString().equals("Enviar nueva valoración"))
                    actualizarValoracionEvento();
            }
        });
        rating = findViewById(R.id.rating);
        valoracionUsuario = getIntent().getFloatExtra("valoracionUsuario", 0);
        if(valoracionUsuario != 0) {
            rating.setRating(valoracionUsuario);
            btnValorarEvento.setText("Enviar nueva valoración");
        }


        btnBorrarEvento = findViewById(R.id.btnEliminarEvento);
        if(getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE).getInt("esAdmin", 0) == 1){
            btnBorrarEvento.setVisibility(View.VISIBLE);
        }
        else {
            btnBorrarEvento.setVisibility(View.INVISIBLE);
        }
    }


    public void eliminar(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BORRAR_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Evento eliminado", response);
                        if(response.equals("1")){
                            muestraAlert("Evento eliminado", "Este evento se ha borrado correctamente.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Borrar Evento", error.toString());
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
                params.put("id", getIntent().getIntExtra("id", -1) + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void eliminarEvento(View view){
        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("¿Seguro que quieres eliminar este evento?");
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

    public void valorarEvento(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VALORAR_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Enviar valoración", response);
                        if(response.equals("1")){
                            muestraAlert("Valoración enviada", "Tu valoración de este evento se ha enviado correctamente.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Valorar Evento", error.toString());
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
                params.put("email", getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE).getString("email", "-"));
                params.put("valoracion", rating.getRating() + "");
                params.put("idEvento", getIntent().getIntExtra("id", -1) + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void actualizarValoracionEvento(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ACTUALIZAR_VALORACION_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Actualizar valoración", response);
                        if(response.equals("1")){
                            muestraAlert("Valoración actualizada", "Tu nueva valoración se ha guardado.");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Act Valoración", error.toString());
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
                params.put("email", getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE).getString("email", "-"));
                params.put("valoracion", rating.getRating() + "");
                params.put("idEvento", getIntent().getIntExtra("id", -1) + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
