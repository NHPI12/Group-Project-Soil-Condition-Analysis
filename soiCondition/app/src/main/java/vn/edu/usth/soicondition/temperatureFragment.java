package vn.edu.usth.soicondition;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class temperatureFragment extends Fragment {
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_temperature, container, false);
        textView = view.findViewById(R.id.temperature_view);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://smart-pot-1d7b5-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("sensor_data");
        DatabaseReference databaseReference2 = databaseReference1.child("temperature");

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Float value = snapshot.getValue(Float.class);
                    if (value != null) {
                        String stringValue = String.valueOf(value);
                        // Call a method to update the UI with the string value
                        updateUIWithStringValue(stringValue);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
    private void updateUIWithStringValue(String stringValue) {
        if (getActivity() != null) {
            // Check if the fragment is attached to the activity
            // Update a TextView or any other UI element with the string value
            TextView textView = getView().findViewById(R.id.temperature_view);
            if (textView != null) {
                textView.setText(stringValue);
            }
        }
    }
}