package vn.edu.usth.soicondition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class temperatureFragment extends Fragment {
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_temperature, container, false);
        textView = view.findViewById(R.id.temperature_view);
        return view;
    }
    public void updateTextView(String newText){
        if(textView!=null){
            textView.setText(newText);
        }
    }
}