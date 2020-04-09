package com.madugada.fallaobispo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FormularioSugerencias extends AppCompatActivity {

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
        setContentView(R.layout.activity_formulario_sugerencias);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buzón de sugerencias");
    }
}
