package app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.khaledonioscousin.mifalla.R;

import java.util.HashMap;
import java.util.Map;

public class GestionUsuario extends AppCompatActivity {

    private static final String URL_CREAR_USUARIO = "http://muxacalma.com/obispo/crearUsuario.php";

    SwitchMaterial esAdministrador;
    SwitchMaterial swCrearEventos, swEliminarEventos,
    swPublicarNoticas, swEliminarNoticias, swCrearTorneos,
    swEliminarTorneos, swGestionarUsuarios;
    LinearLayout layoutPermisos;
    TextInputEditText txtEmail;

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
        setContentView(R.layout.activity_gestion_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        toolbar.setTitle("Gestión de usuarios");
        setSupportActionBar(toolbar);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEmail = findViewById(R.id.txtEmail);
        layoutPermisos = findViewById(R.id.layoutPermisos);
        swCrearEventos = findViewById(R.id.swCrearEventos);
        swEliminarEventos = findViewById(R.id.swEliminarEventos);
        swPublicarNoticas = findViewById(R.id.swPublicarNoticias);
        swEliminarNoticias = findViewById(R.id.swEliminarNoticias);
        swCrearTorneos = findViewById(R.id.swCrearTorneos);
        swEliminarTorneos = findViewById(R.id.swEliminarTorneos);
        swGestionarUsuarios = findViewById(R.id.swGestionarUsuarios);

        esAdministrador = findViewById(R.id.esAdministrador);
        esAdministrador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    layoutPermisos.setVisibility(View.VISIBLE);
                }
                else {
                    layoutPermisos.setVisibility(View.GONE);
                    resetPermisos();
                }
            }
        });
    }

    public void resetPermisos(){
        swCrearEventos.setChecked(false);
        swEliminarEventos.setChecked(false);
        swPublicarNoticas.setChecked(false);
        swEliminarNoticias.setChecked(false);
        swCrearTorneos.setChecked(false);
        swEliminarTorneos.setChecked(false);
        swGestionarUsuarios.setChecked(false);
    }

    public void crearUsuario(View view){
        if(comprobarCampos()){
            Log.d("Pulsado", "Enviamos petición");
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREAR_USUARIO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Crear usuario", response);
                            if(response.equals("1")){
                                muestraAlert("Usuario creado", "El usuario se ha dado de alta correctamente en el sistema.\n\nYa puede iniciar sesión en la app con el email proporcionado.", true);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR Crear usuario", error.toString());
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
                    params.put("email", txtEmail.getText().toString());
                    params.put("esAdmin", esAdministrador.isChecked() + "");
                    params.put("crearEventos", swCrearEventos.isChecked() + "");
                    params.put("eliminarEventos", swEliminarEventos.isChecked() + "");
                    params.put("publicarNoticias", swPublicarNoticas.isChecked() + "");
                    params.put("eliminarNoticias", swEliminarNoticias.isChecked() + "");
                    params.put("crearTorneos", swCrearTorneos.isChecked() + "");
                    params.put("eliminarTorneos", swEliminarTorneos.isChecked() + "");
                    params.put("gestionarUsuarios", swGestionarUsuarios.isChecked() + "");
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }

    public boolean comprobarCampos(){
        if(txtEmail.getText().toString().length() < 1){
            txtEmail.setError("Campo necesario");
            txtEmail.requestFocus();
            return false;
        }
        else if(!txtEmail.getText().toString().contains("@gmail.com")){
            txtEmail.setError("Debe ser un correo de Gmail");
            txtEmail.requestFocus();
            return false;
        }
        else if(esAdministrador.isChecked() &&
                !(swCrearEventos.isChecked() || swEliminarEventos.isChecked()
                || swPublicarNoticas.isChecked() || swEliminarNoticias.isChecked()
                || swCrearTorneos.isChecked() || swEliminarTorneos.isChecked()
                || swGestionarUsuarios.isChecked())){
            muestraAlert("¿Administrador sin permisos?", "Concede al usuario algún permiso, o desmarca la casilla de administrador.", false);
            return false;
        }
        return true;
    }

    public void muestraAlert(String titulo, String mensaje, final boolean cerrarActivity){

        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(titulo);
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(cerrarActivity)
                    finish();
            }
        });
        materialAlertDialogBuilder.show();
    }

    public void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
