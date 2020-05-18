package app.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class PublicarEvento extends AppCompatActivity {

    private static final String URL_PUBLICAR_EVENTO = "http://muxacalma.com/obispo/publicarEvento.php";

    FirebaseAuth mAuth;

    String mCurrentPhotoPath;
    String imageFileName = "";
    private StorageReference storageRef;
    private NestedScrollView scroll;
    private ImageButton imagenEvento;
    private TextView txtCambioImagen;
    private SwitchMaterial privacidadEvento, necesarioConfirmar;
    private TextInputEditText inputTitulo, inputDesc, inputPrecio;
    private boolean haPuestoFoto = false;
    private MaterialButton btnVistaPrevia, btnCrearEvento, btnFecha, btnHoraInicio, btnHoraFin;
    private RadioGroup radioGroup;
    private DatePickerDialog dialogFecha;
    private TimePickerDialog dialogHoraInicio, dialogHoraFin;
    private int idRadioElegido = R.id.inicioYFin;

    private String enviaHora;

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
        setContentView(R.layout.activity_publicar_evento);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        scroll = findViewById(R.id.scrollEvento);
        imagenEvento = findViewById(R.id.imagenEvento);
        txtCambioImagen = findViewById(R.id.txtCambioImagen);
        privacidadEvento = findViewById(R.id.privacidadEvento);
        necesarioConfirmar = findViewById(R.id.necesarioConfirmar);
        inputTitulo = findViewById(R.id.inputTitulo);
        inputDesc = findViewById(R.id.inputDesc);
        inputPrecio = findViewById(R.id.inputPrecio);
        btnFecha = findViewById(R.id.btnFecha);
        radioGroup = findViewById(R.id.radioGroup);
        btnHoraInicio = findViewById(R.id.btnHoraInicio);
        btnHoraFin = findViewById(R.id.btnHoraFin);
        btnVistaPrevia = findViewById(R.id.btnVistaPrevia);
        btnCrearEvento = findViewById(R.id.btnCrearEvento);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.inicioYFin){
                    btnHoraInicio.setEnabled(true);
                    btnHoraInicio.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnHoraInicio.setText("Hora inicio");
                    btnHoraFin.setEnabled(true);
                    btnHoraFin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnHoraFin.setText("Hora fin");
                    idRadioElegido = R.id.inicioYFin;
                }
                else if(checkedId == R.id.aPartir){
                    btnHoraInicio.setEnabled(true);
                    btnHoraInicio.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnHoraInicio.setText("Hora inicio");
                    btnHoraFin.setEnabled(false);
                    btnHoraFin.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    btnHoraFin.setText("No disponible");
                    idRadioElegido = R.id.aPartir;
                }
                else if(checkedId == R.id.todoElDia){
                    btnHoraInicio.setEnabled(false);
                    btnHoraInicio.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    btnHoraInicio.setText("No disponible");
                    btnHoraFin.setEnabled(false);
                    btnHoraFin.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    btnHoraFin.setText("No disponible");
                    idRadioElegido = R.id.todoElDia;
                }
            }
        });

        final Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        dialogFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dia = "" + dayOfMonth;
                if(dia.length() == 1){
                    dia = "0" + dayOfMonth;
                }
                String mes = "" + (month+1);
                if(mes.length() == 1){
                    mes = "0" + mes;
                }
                btnFecha.setText(dia + "/" + mes + "/" + year);
            }
        }, today.year, today.month, today.monthDay);
        dialogFecha.getDatePicker().setMinDate(System.currentTimeMillis());

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFecha.show();
            }
        });

        dialogHoraInicio = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hora = "" + hourOfDay;
                if(hora.length() == 1)
                    hora = "0" + hora;

                String minuto = "" + minute;
                if(minuto.length() == 1)
                    minuto = "0" + minuto;
                btnHoraInicio.setText(hora + ":" + minuto);
            }
        }, today.hour, today.minute, true);

        btnHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHoraInicio.show();
            }
        });

        dialogHoraFin = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hora = "" + hourOfDay;
                if(hora.length() == 1)
                    hora = "0" + hora;

                String minuto = "" + minute;
                if(minuto.length() == 1)
                    minuto = "0" + minuto;
                btnHoraFin.setText(hora + ":" + minuto);
            }
        }, today.hour, today.minute, true);

        btnHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHoraFin.show();
            }
        });

        privacidadEvento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    privacidadEvento.setText("Evento público (miembros de la falla e invitados externos)");
                    privacidadEvento.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else {
                    privacidadEvento.setText("Evento privado (sólo miembros de la falla)");
                    privacidadEvento.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        });

        necesarioConfirmar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    necesarioConfirmar.setText("Los asistentes SÍ deben confirmar asistencia");
                    necesarioConfirmar.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else {
                    necesarioConfirmar.setText("Los asistentes NO deben confirmar asistencia");
                    necesarioConfirmar.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        });

        btnVistaPrevia.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        btnCrearEvento.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btnCrearEvento.setEnabled(false);

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

            imagenEvento.setImageBitmap(nuevaFoto);
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
            btnCrearEvento.setEnabled(true);
            btnCrearEvento.setBackgroundColor(getResources().getColor(R.color.verdeOK));
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
        if(btnFecha.getText().toString().equals("Fecha")){
            Snackbar.make(scroll, "Es necesario indicar la fecha del evento", Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        if(idRadioElegido == R.id.aPartir && btnHoraInicio.getText().toString().equals("Hora inicio")){
            Snackbar.make(scroll, "Debes indicar la hora de inicio del evento", Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        if(idRadioElegido == R.id.inicioYFin && (btnHoraInicio.getText().toString().equals("Hora inicio") || btnHoraFin.getText().toString().equals("Hora fin"))){
            Snackbar.make(scroll, "Debes indicar las horas de inicio y fin del evento", Snackbar.LENGTH_SHORT)
                    .show();
            return false;
        }
        if(inputPrecio.getText().toString().length() < 1){
            inputPrecio.setError("Debes indicar un precio (aunque sean 0€).");
            inputPrecio.requestFocus();
            return false;
        }
        return true;
    }

    public void crearEvento(View view){
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
                        Log.d("Foto subida", "Seguimos a publicar");
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
            btnCrearEvento.setEnabled(false);
            btnCrearEvento.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PUBLICAR_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Evento creado", response);
                        if(response.contains("1")){
                            muestraAlert("Evento creado", "El evento se ha creado correctamente", true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR Crear Evento", error.toString());
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
                params.put("fecha", btnFecha.getText().toString());
                params.put("rutaImagen", imageFileName);
                params.put("hora", enviaHora);
                params.put("precio", inputPrecio.getText().toString());
                params.put("hayInscripcion", necesarioConfirmar.isChecked() + "");
                params.put("esPublico", privacidadEvento.isChecked() + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void ocultarTeclado(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void muestraAlert(String titulo, String mensaje, final boolean terminar){

        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle(titulo);
        materialAlertDialogBuilder.setMessage(mensaje);
        materialAlertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(terminar)
                    finish();
            }
        });
        materialAlertDialogBuilder.show();
    }

    public AlertDialog vistaPrevia(){

        final View vistaPrevia = getLayoutInflater().inflate(R.layout.item_evento, null);
        TextView titulo = vistaPrevia.findViewById(R.id.txtTitulo);
        TextView desc = vistaPrevia.findViewById(R.id.txtDesc);
        TextView fecha = vistaPrevia.findViewById(R.id.txtFecha);
        TextView hora = vistaPrevia.findViewById(R.id.txtHora);
        TextView precio = vistaPrevia.findViewById(R.id.txtPrecio);
        ImageView img = vistaPrevia.findViewById(R.id.imgEvento);
        MaterialCardView inscritos = vistaPrevia.findViewById(R.id.cardInscritos);

        titulo.setText(inputTitulo.getText().toString());
        desc.setText(inputDesc.getText().toString());
        fecha.setText(btnFecha.getText().toString());
        precio.setText("Precio: " + inputPrecio.getText().toString() + "€");
        img.setImageDrawable(imagenEvento.getDrawable());
        if(idRadioElegido == R.id.inicioYFin){
            hora.setText("De " + btnHoraInicio.getText().toString() + " a " + btnHoraFin.getText().toString());
            enviaHora = "De " + btnHoraInicio.getText().toString() + " a " + btnHoraFin.getText().toString();
        }
        else if(idRadioElegido == R.id.aPartir){
            hora.setText("A partir de las " + btnHoraInicio.getText().toString());
            enviaHora = "A partir de las " + btnHoraInicio.getText().toString();
        }
        else if(idRadioElegido == R.id.todoElDia){
            hora.setText("Todo el día");
            enviaHora = "Todo el día";
        }

        if(necesarioConfirmar.isChecked()){
            inscritos.setVisibility(View.VISIBLE);
        }
        else {
            inscritos.setVisibility(View.INVISIBLE);
        }

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

}
