package app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.khaledonioscousin.mifalla.R;

public class DashboardAdministrador extends AppCompatActivity {

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
        setContentView(R.layout.activity_dashboard_administrador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Menú de administradores");
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffffffff));
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
    }

    public void abrirListaEmails(View view){
        Intent i = new Intent(DashboardAdministrador.this, ListaEmails.class);
        startActivity(i);
    }

    public void abrirPublicarNoticia(View view){
        Intent i = new Intent(DashboardAdministrador.this, PublicarNoticia.class);
        startActivity(i);
    }

    public void abrirCrearEvento(View view){
        Intent i = new Intent(DashboardAdministrador.this, PublicarEvento.class);
        startActivity(i);
    }
}
