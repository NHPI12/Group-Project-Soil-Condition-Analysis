package vn.edu.usth.soicondition;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class soilMoistureFragment extends Fragment {

    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soil_moisture, container, false);

        textView = view.findViewById(R.id.moisture_view);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://smart-pot-1d7b5-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 =firebaseDatabase.getReference("sensor_data");
        DatabaseReference databaseReference2 = databaseReference1.child("soil moisture");

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Float value = snapshot.getValue(Float.class);
                    if(value != null){
                        String stringValue = String.valueOf(value);
                        updateUIWithStringValue(stringValue);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                Intent intent = new Intent(getActivity(),ChartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
            }
        });
        return view;
    }
    private void updateUIWithStringValue(String stringValue){
        if (getActivity() !=null){
            TextView textView = getView().findViewById(R.id.moisture_view);
            if(textView != null){
                textView.setText(stringValue);
            }
        }
    }
}