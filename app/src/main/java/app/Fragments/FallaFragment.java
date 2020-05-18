package app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.khaledonioscousin.mifalla.R;

import app.Activities.Cargos;

public class FallaFragment extends Fragment {

    MaterialCardView cardHistoria, cardMonumento, cardCargos;

    public FallaFragment() {
        // Required empty public constructor
    }

    public static FallaFragment newInstance(String param1, String param2) {
        FallaFragment fragment = new FallaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_falla, container, false);
        cardHistoria = view.findViewById(R.id.cardHistoria);
        cardMonumento = view.findViewById(R.id.cardMonumento);
        cardCargos = view.findViewById(R.id.cardCargos);
        cardCargos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCargos();
            }
        });
        cardHistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHistoria(cardHistoria);
            }
        });
        cardMonumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMonumento(cardMonumento);
            }
        });
        return view;
    }

    public void abrirCargos(){
        Intent intent = new Intent(getContext(), Cargos.class);
        startActivity(intent);
    }

    public void abrirHistoria(View view){
        Snackbar.make(view, "Esto aún no está listo", Snackbar.LENGTH_LONG)
                .setAction("Ok", null).show();
    }

    public void abrirMonumento(View view){
        Snackbar.make(view, "Esto aún no está listo", Snackbar.LENGTH_LONG)
                .setAction("Ok", null).show();
    }
}
