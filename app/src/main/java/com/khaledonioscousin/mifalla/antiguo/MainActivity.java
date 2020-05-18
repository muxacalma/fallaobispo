package com.khaledonioscousin.mifalla.antiguo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.khaledonioscousin.mifalla.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.Activities.DashboardAdministrador;
import app.Adapters.AdapterEventos;
import app.Activities.Login;
import app.Objetos.Evento;

public class MainActivity extends AppCompatActivity {

    private static final String URL_EVENTOS = "http://muxacalma.com/obispo/getEventos.php";

    ArrayList<Evento> eventos;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtNombre;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Buzón de sugerencias
        if (id == R.id.nuevomensaje) {
            Intent intent = new Intent(MainActivity.this, FormularioSugerencias.class);
            startActivity(intent);
        }
        else if (id == R.id.cerrarsesion){
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.administradores){
            Intent intent = new Intent(MainActivity.this, DashboardAdministrador.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow (). setFlags ( WindowManager . LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffffffff));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");


        txtNombre = findViewById(R.id.txtNombre);
        txtNombre.setText(getIntent().getStringExtra("nombreUsuario") + "!");

        recyclerView = findViewById(R.id.listaEventos);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

         //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //getEventos();
    }
    /*
    public void getEventos(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EVENTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Eventos recuperados", response);
                        try{
                            eventos = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject actual = jsonArray.getJSONObject(i);
                                    int id = actual.getInt("id");
                                    String titulo = actual.getString("titulo");
                                    String ruta_imagen = actual.getString("ruta_foto");
                                    String descripcion = actual.getString("descripcion");
                                    String fecha = actual.getString("fecha");
                                    boolean hayInscripcion = Boolean.valueOf(actual.getString("hayInscripcion"));

                                    Evento evento = new Evento(id, titulo, ruta_imagen, descripcion, fecha, hayInscripcion);
                                    eventos.add(evento);
                                }
                                Log.d("Conteo eventos" , "Hay " + eventos.size() + " eventos.");
                                mAdapter = new AdapterEventos(MainActivity.this, eventos, MainActivity.this);
                                recyclerView.setAdapter(mAdapter);

                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(" ERROR Eventos:", error.toString());
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
                return params;
            }

        };
        queue.add(stringRequest);
    }
    */

}
