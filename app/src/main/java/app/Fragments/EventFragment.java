package app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.madugada.fallaobispo.R;
import app.Adapters.AdapterEventos;
import com.madugada.fallaobispo.antiguo.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventFragment extends Fragment {

    private static final String URL_EVENTOS = "http://muxacalma.com/obispo/getEventos.php";

    ArrayList<Evento> eventos;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = view.findViewById(R.id.listaEventos);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        getEventos();

        return view;
    }


    public void getEventos(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EVENTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Eventos recuperados", response);
                        try{
                            eventos = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject actual = jsonArray.getJSONObject(i);
                                    int id = actual.getInt("id");
                                    String titulo = actual.getString("titulo");
                                    String ruta_imagen = actual.getString("ruta_foto");
                                    String descripcion = actual.getString("descripcion");
                                    String fecha = actual.getString("fecha");
                                    boolean hayInscripcion = Boolean.valueOf(actual.getString("hayInscripcion"));

                                    Evento evento = new Evento(id, titulo, ruta_imagen, descripcion, fecha, hayInscripcion);
                                    eventos.add(evento);
                                }
                                Log.d("Conteo eventos" , "Hay " + eventos.size() + " eventos.");
                                mAdapter = new AdapterEventos(getContext(), eventos);
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
                Log.d(" ERROR Eventos:", error.toString());
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
