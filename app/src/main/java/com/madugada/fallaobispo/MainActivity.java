package com.madugada.fallaobispo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        /*SharedPreferences sp = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String esProduccion = sp.getString("esProduccion", "1"); //1 = prod
        if(esProduccion.equals("1")){
            menu.getItem(0).setIcon(R.drawable.mas);

        }
        else if(esProduccion.equals("0")){
            menu.getItem(0).setIcon(R.drawable.ic_pruebason);
        }*/
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow (). setFlags ( WindowManager . LayoutParams . FLAG_FULLSCREEN ,
                WindowManager. LayoutParams . FLAG_FULLSCREEN );
    }
}
