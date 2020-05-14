package app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khaledonioscousin.mifalla.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_USUARIO_AUTORIZADO = "http://muxacalma.com/obispo/usuarioAutorizado.php";

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
    }

    public void updateUI(GoogleSignInAccount account){
        if(account == null){
            Log.d("UpdateUI", "No hay cuenta");
        }
        else {
            Log.d("UpdateUI", "Sí hay cuenta");
            Log.d("UpdateUI", account.getGivenName() );
            Log.d("UpdateUI", account.getId());

            usuarioAutorizado(account);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 0) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error HANDLE RESULT", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void usuarioAutorizado(final GoogleSignInAccount account){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_USUARIO_AUTORIZADO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Usuario Autorizado", response);
                        if(response.equals("NO")){
                            GoogleSignIn.getClient(Login.this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
                            muestraAlert("Cuenta no autorizada", "Esta cuenta no ha sido dada de alta para poder acceder a la aplicación.\n\n" +
                                    "Por favor, ponte en contacto con los administradores de la app para solucionarlo.");
                        }
                        else{
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String email = jsonObject.getString("email");
                                String nombreSimple = jsonObject.getString("nombre_simple");
                                String nombreCompleto = jsonObject.getString("nombre_completo");
                                int esAdmin = jsonObject.getInt("esAdmin");
                                SharedPreferences.Editor editor = getSharedPreferences("miFallaPreferences", MODE_PRIVATE).edit();
                                editor.putString("email", email);
                                editor.putString("nombreSimple", nombreSimple);
                                editor.putString("nombreCompleto", nombreCompleto);
                                editor.putInt("esAdmin", esAdmin);
                                editor.apply();

                                Intent intent = new Intent(Login.this, Dashboard.class);
                                intent.putExtra("nombreUsuario", account.getGivenName());
                                startActivity(intent);
                                finish();
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(" ERROR Usuario autoriz", error.toString());
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
                params.put("email", account.getEmail());
                params.put("nombreSimple", account.getGivenName());
                params.put("nombreCompleto", account.getDisplayName());
                return params;
            }

        };
        queue.add(stringRequest);
    }

    public void muestraAlert(String titulo, String mensaje){

        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(titulo);
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        materialAlertDialogBuilder.show();
    }
}
