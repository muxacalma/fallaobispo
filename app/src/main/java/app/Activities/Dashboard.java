package app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khaledonioscousin.mifalla.R;
import com.khaledonioscousin.mifalla.antiguo.FormularioSugerencias;

import app.Adapters.AdapterPage;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView txtNombre;
    ViewPager pager;
    AdapterPage adapterPage;
    LinearLayout linearNombre;

    //-----    Datos del Shared Preferences    -----\\
    SharedPreferences sharedPreferences;
    String email;
    String nombreSimple;
    String nombreComleto;
    int esAdmin = 0;
    //----------------------------------------------\\
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        if(esAdmin == 0){
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Buzón de sugerencias
        if (id == R.id.nuevomensaje) {
            Intent intent = new Intent(Dashboard.this, FormularioSugerencias.class);
            startActivity(intent);
        }
        else if (id == R.id.cerrarsesion){
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            Intent intent = new Intent(Dashboard.this, Login.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.administradores){
            Intent intent = new Intent(Dashboard.this, DashboardAdministrador.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow (). setFlags ( WindowManager. LayoutParams . FLAG_FULLSCREEN , WindowManager. LayoutParams . FLAG_FULLSCREEN );
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        linearNombre = findViewById(R.id.layoutNombre);

        sharedPreferences = getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "-");
        nombreSimple = sharedPreferences.getString("nombreSimple", "-");
        nombreComleto = sharedPreferences.getString("nombreCompleto", "-");
        esAdmin = sharedPreferences.getInt("esAdmin", 0);

        ponerNombre();

        pager = findViewById(R.id.pager);
        adapterPage = new AdapterPage(getSupportFragmentManager(), 4);
        pager.setAdapter(adapterPage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().toString().equals("Eventos")){
                    pager.setCurrentItem(0);
                }
                else if(item.getTitle().toString().equals("Nuestra falla")){
                    pager.setCurrentItem(1);
                }
                else if(item.getTitle().toString().equals("Noticias")){
                    pager.setCurrentItem(2);
                }
                else if(item.getTitle().toString().equals("Competiciones")){
                    pager.setCurrentItem(3);
                }
                return true;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    pager.setCurrentItem(0);
                    bottomNavigationView.setSelectedItemId(R.id.itemEventos);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.rojoSuave));
                    linearNombre.setBackgroundColor(getResources().getColor(R.color.rojoSuave));
                }
                else if(position == 1){
                    pager.setCurrentItem(1);
                    bottomNavigationView.setSelectedItemId(R.id.itemFalla);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.azulSuave));
                    linearNombre.setBackgroundColor(getResources().getColor(R.color.azulSuave));
                }
                else if(position == 2){
                    pager.setCurrentItem(2);
                    bottomNavigationView.setSelectedItemId(R.id.itemNoticias);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.amarilloSuave));
                    linearNombre.setBackgroundColor(getResources().getColor(R.color.amarilloSuave));
                }
                else if(position == 3){
                    pager.setCurrentItem(3);
                    bottomNavigationView.setSelectedItemId(R.id.itemCompeticiones);
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
                    linearNombre.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void ponerNombre(){
        txtNombre = findViewById(R.id.txtNombre);
        txtNombre.setText(getIntent().getStringExtra("nombreUsuario") + "!");
    }

}


