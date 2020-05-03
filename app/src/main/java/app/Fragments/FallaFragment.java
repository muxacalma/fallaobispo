package app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madugada.fallaobispo.R;

public class FallaFragment extends Fragment {

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

        return view;
    }
}
