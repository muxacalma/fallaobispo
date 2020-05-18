package app.Fragments;

import android.content.Context;
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
import com.khaledonioscousin.mifalla.R;
import app.Adapters.AdapterEventos;
import app.Objetos.Evento;

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
    public void onResume() {
        super.onResume();
        getEventos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = view.findViewById(R.id.listaEventos);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //getEventos();

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
                                    String hora = actual.getString("hora");
                                    String precio = actual.getString("precio");
                                    String hayInscripcion = actual.getString("hayInscripcion");
                                    boolean esPublico = Boolean.valueOf(actual.getString("esPublico"));
                                    String valoracionMedia = actual.getString("valoracionMedia");
                                    float valoracionM;
                                    if(valoracionMedia.equals("null"))
                                        valoracionM = 0;
                                    else
                                        valoracionM = Float.parseFloat(actual.getString("valoracionMedia"));
                                    String valoracionUsuario = actual.getString("valoracionUsuario");
                                    float valoracionU;
                                    if(valoracionUsuario.equals("null"))
                                        valoracionU = 0;
                                    else
                                        valoracionU = Float.parseFloat(actual.getString("valoracionUsuario"));

                                    Evento evento = new Evento(id, titulo, ruta_imagen, descripcion, fecha, hayInscripcion, hora, precio, esPublico, valoracionM, valoracionU);
                                    eventos.add(evento);
                                }
                                Log.d("Conteo eventos" , "Hay " + eventos.size() + " eventos.");
                                mAdapter = new AdapterEventos(getContext(), eventos, getActivity());
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
                params.put("email", getContext().getSharedPreferences("miFallaPreferences", Context.MODE_PRIVATE).getString("email", "-"));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
