package app.Activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.madugada.fallaobispo.R;
import com.squareup.picasso.Picasso;

public class DetalleNoticia extends AppCompatActivity {

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

        ImageView imageView = findViewById(R.id.imagen);
        String rutaImagen = getIntent().getStringExtra("rutaImagen");
        Picasso.get().load(rutaImagen).into(imageView);

        TextView titulo = findViewById(R.id.txtTitulo);
        titulo.setText(getIntent().getStringExtra("titulo"));
        TextView desc = findViewById(R.id.txtDesc);
        desc.setText(getIntent().getStringExtra("desc"));
    }
}
