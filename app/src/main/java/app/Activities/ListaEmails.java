package app.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.khaledonioscousin.mifalla.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.Adapters.AdapterUsuarios;
import app.Objetos.Usuario;

public class ListaEmails extends AppCompatActivity {

    private static final String URL_GET_EMAILS = "http://muxacalma.com/obispo/emailsAutorizados.php";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Usuario> usuarios;

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
        setContentView(R.layout.activity_lista_emails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Emails autorizados");
        setSupportActionBar(toolbar);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.listaEmails);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        getEmails();
    }

    public void nuevoUsuario(View view){
        Intent intent = new Intent(ListaEmails.this, GestionUsuario.class);
        startActivity(intent);
    }

    public void getEmails(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_EMAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Listado emails", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            usuarios = new ArrayList<>();
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject actual = jsonArray.getJSONObject(i);
                                String email = actual.getString("email");
                                String nombreSimple = actual.getString("nombre_simple");
                                String nombreCompleto = actual.getString("nombre_completo");
                                int esAdmin = actual.getInt("esAdmin");
                                int crearEventos = actual.getInt("crearEventos");
                                int eliminarEventos = actual.getInt("eliminarEventos");
                                int publicarNoticias = actual.getInt("publicarNoticias");
                                int eliminarNoticias = actual.getInt("eliminarNoticias");
                                int crearTorneos = actual.getInt("crearTorneos");
                                int eliminarTorneos = actual.getInt("eliminarTorneos");
                                int gestionarUsuarios = actual.getInt("gestionarUsuarios");

                                Usuario usuario = new Usuario(email, nombreSimple, nombreCompleto, esAdmin, crearEventos, eliminarEventos, publicarNoticias, eliminarNoticias,
                                        crearTorneos, eliminarTorneos, gestionarUsuarios);
                                usuarios.add(usuario);
                            }
                            Log.d("Nº usuarios", usuarios.size() + "");
                            mAdapter = new AdapterUsuarios(getApplicationContext(), usuarios, ListaEmails.this);
                            recyclerView.setMinimumHeight(500);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.requestLayout();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Listado emails", error.toString());
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
}
