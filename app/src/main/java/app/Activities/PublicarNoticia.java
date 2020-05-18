package app.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khaledonioscousin.mifalla.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PublicarNoticia extends AppCompatActivity {

    private static final String URL_PUBLICAR_NOTICIA = "http://muxacalma.com/obispo/publicarNoticia.php";

    FirebaseAuth mAuth;

    String mCurrentPhotoPath;
    String imageFileName = "";
    private StorageReference storageRef;
    private NestedScrollView scroll;
    private ImageButton imagenNoticia;
    private TextView txtCambioImagen;
    private SwitchMaterial privacidadNoticia;
    private TextInputEditText inputTitulo, inputDesc;
    private boolean haPuestoFoto = false;
    private MaterialButton btnVistaPrevia, btnPublicarNoticia;
    //
    //      Método para usar flecha de atrás en Action Bar
    //
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_noticia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        scroll = findViewById(R.id.scrollNoticia);
        imagenNoticia = findViewById(R.id.imagenNoticia);
        txtCambioImagen = findViewById(R.id.txtCambioImagen);
        privacidadNoticia = findViewById(R.id.privacidadNoticia);
        inputTitulo = findViewById(R.id.inputTitulo);
        inputDesc = findViewById(R.id.inputDesc);
        btnVistaPrevia = findViewById(R.id.btnVistaPrevia);
        btnPublicarNoticia = findViewById(R.id.btnEnviarNoticia);

        privacidadNoticia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    privacidadNoticia.setText("Noticia Pública (miembros de la falla e invitados externos)");
                    privacidadNoticia.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else {
                    privacidadNoticia.setText("Noticia Privada (sólo miembros de la falla)");
                    privacidadNoticia.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        });

        btnVistaPrevia.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        btnPublicarNoticia.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btnPublicarNoticia.setEnabled(false);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("SIGN IN ANONYMOUSLY", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public void anadirFotoGaleria(View view) {
        try {
            createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, 0);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        this.imageFileName = imageFileName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPicFromGallery(Intent data) {
        try {
            final Uri imageUri = data.getData();
            Log.d("URI PATH", imageUri.getPath());
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap nuevaFoto;

            nuevaFoto = BitmapFactory.decodeStream(imageStream);

            imagenNoticia.setImageBitmap(nuevaFoto);
            txtCambioImagen.setVisibility(View.VISIBLE);
            int nh = (int) ( nuevaFoto.getHeight() * (512.0 / nuevaFoto.getWidth()) );
            haPuestoFoto = true;
            try (FileOutputStream out = new FileOutputStream(mCurrentPhotoPath)) {
                Bitmap scaled = Bitmap.createScaledBitmap(nuevaFoto, 512, nh, true);
                scaled.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                setPicFromGallery(data);
            }
            else {
                Toast.makeText(this, "No se ha seleccionado ninguna foto", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void vistaPrevia(View view){
        if(camposRellenos()){
            vistaPrevia();
            btnPublicarNoticia.setEnabled(true);
            btnPublicarNoticia.setBackgroundColor(getResources().getColor(R.color.verdeOK));
        }
    }

    public boolean camposRellenos(){
        if(inputTitulo.getText().toString().length() < 1){
            inputTitulo.setError("Campo necesario");
            inputTitulo.requestFocus();
            return false;
        }
        if(inputDesc.getText().toString().length() < 1){
            inputDesc.setError("Campo necesario");
            inputDesc.requestFocus();
            return false;
        }
        if(!haPuestoFoto){
            Snackbar.make(scroll, "Es necesario añadir la imagen de la noticia", Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    public void publicarNoticia(View view){
        subirFoto();
    }

    public void subirFoto(){
        final File foto = new File(mCurrentPhotoPath);
        Log.d("Tamaño foto", foto.length() + "");
        Uri file = Uri.fromFile(foto);
        final StorageReference riversRef = storageRef.child(imageFileName);
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        publicar(foto.length());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("Exception uploading", exception.toString());
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void publicar(final long tamañoFoto){
        if(!camposRellenos()){
            btnPublicarNoticia.setEnabled(false);
            btnPublicarNoticia.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PUBLICAR_NOTICIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Noticia publicada", response);
                        if(response.contains("1")){
                            muestraAlert("Noticia publicada", "La noticia se ha publicado correctamente");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Publicar Noticia", error.toString());
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
                params.put("titulo", inputTitulo.getText().toString());
                params.put("descripcion", inputDesc.getText().toString());
                params.put("fecha", fechaHora());
                params.put("rutaImagen", imageFileName);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public AlertDialog vistaPrevia(){

        final View vistaPrevia = getLayoutInflater().inflate(R.layout.item_noticia, null);
        TextView titulo = vistaPrevia.findViewById(R.id.txtTitulo);
        TextView desc = vistaPrevia.findViewById(R.id.txtDesc);
        TextView fecha = vistaPrevia.findViewById(R.id.txtFecha);
        ImageView img = vistaPrevia.findViewById(R.id.imgNoticia);

        titulo.setText(inputTitulo.getText().toString());
        desc.setText(inputDesc.getText().toString());
        fecha.setText(fechaHora());
        img.setImageDrawable(imagenNoticia.getDrawable());

        AlertDialog.Builder alertdialogobuilder = new AlertDialog.Builder(this);
        alertdialogobuilder.setView(vistaPrevia)
                .setCancelable(true)
                .create();
        alertdialogobuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertdialogobuilder.create();
        dialog.show();
        return dialog;
    }

    public String fechaHora(){
        //----------Fecha y hora del registro----------\\
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        String tempHora = "" + calendar.get(Calendar.HOUR_OF_DAY);
        if (tempHora.length() == 1) {
            tempHora = "0" + tempHora;
        }
        String tempMin = "" + calendar.get(Calendar.MINUTE);
        if (tempMin.length() == 1) {
            tempMin = "0" + tempMin;
        }
        String hora = tempHora + ":" + tempMin;
        return fecha + ", " + hora;
    }


}
