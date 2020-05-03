package com.madugada.fallaobispo.antiguo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.madugada.fallaobispo.R;

import java.util.HashMap;
import java.util.Map;

public class FormularioSugerencias extends AppCompatActivity {

    private static final String URL_ENVIAR = "http://muxacalma.com/obispo/enviar_sugerencia.php";

    RadioGroup radioGroup;
    RadioButton observacion, propuesta, peticion;
    EditText titulo, descripcion;

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
        setContentView(R.layout.old_activity_formulario_sugerencias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buzón de sugerencias");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffffffff));
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );


        radioGroup = findViewById(R.id.radioGroup);
        observacion = findViewById(R.id.observacion);
        propuesta = findViewById(R.id.propuesta);
        peticion = findViewById(R.id.peticion);

        titulo = findViewById(R.id.txtTitulo);
        descripcion = findViewById(R.id.txtDesc);
    }

    public void enviarSugerencia(View view){
        if(validar()){
        RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ENVIAR,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Envio sugerencia", response);
                            if(response.contains("OK")){
                                muestraAlert("Sugerencia enviada", "Tu sugerencia se ha enviado con éxito a la directiva de la falla.");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(" ERROR Envio SUG:", error.toString());
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
                    params.put("titulo", titulo.getText().toString());
                    RadioButton elegido = findViewById(radioGroup.getCheckedRadioButtonId());
                    params.put("tipo", elegido.getText().toString());
                    params.put("mensaje", descripcion.getText().toString());
                    return params;
                }

            };

            queue.add(stringRequest);
        }
    }

    public boolean validar(){
        if(titulo.getText().toString().length() == 0){
            titulo.setError("Debes indicar el título/asunto de la sugerencia.");
            return false;
        }
        else if(!observacion.isChecked() && !propuesta.isChecked() && !peticion.isChecked()){
            observacion.setError("Es necesario indicar qué tipo de sugerencia quieres hacer.");
            return false;
        }
        else if(descripcion.getText().toString().length() == 0){
            descripcion.setError("¿Qué quieres enviar?");
            return false;
        }
        return true;
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

    public void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
