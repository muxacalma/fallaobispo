package app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.madugada.fallaobispo.R;

import app.Adapters.AdapterNoticias;
import app.Objetos.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.Adapters.AdapterEventos;
import app.Objetos.Noticia;

public class NewsFragment extends Fragment {

    private static final String URL_NOTICIAS = "http://muxacalma.com/obispo/getNoticias.php";

    ArrayList<Noticia> noticias;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.listaNoticias);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        getNoticias();
        return view;
    }

    public void getNoticias(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_NOTICIAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Eventos recuperados", response);
                        try{
                            noticias = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject actual = jsonArray.getJSONObject(i);
                                    int id = actual.getInt("id");
                                    String titulo = actual.getString("titulo");
                                    String ruta_imagen = actual.getString("rutaImagen");
                                    String descripcion = actual.getString("descripcion");
                                    String fecha = actual.getString("fecha");

                                    Noticia noticia = new Noticia(id, titulo, descripcion, ruta_imagen, fecha);

                                    noticias.add(noticia);
                                }
                                Log.d("Conteo eventos" , "Hay " + noticias.size() + " noticias.");
                                mAdapter = new AdapterNoticias(getContext(), noticias, getActivity());
                                recyclerView.setAdapter(mAdapter);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(" ERROR Noticias:", error.toString());
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
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
