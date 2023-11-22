package vn.edu.usth.soicondition;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.soicondition.model.PlantListItem;

public class Plant_List_Recycle_Adapter extends RecyclerView.Adapter<Plant_List_Recycle_Adapter.MyViewHolder> {
    private List<PlantListItem> plantListData;


    public Plant_List_Recycle_Adapter(List<PlantListItem> plantListData) {
        this.plantListData = plantListData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_list_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlantListItem plantListItem = plantListData.get(position);
        if(plantListItem == null){
            return;
        }
        holder.common_name.setText(plantListItem.getCommon_name());
        holder.thumbnail.setImageResource(plantListItem.getThumbnail());
        holder.watering.setImageResource(plantListItem.getWatering());
        holder.sunlight.setImageResource(plantListItem.getSunlight());
        holder.cycle.setImageResource(plantListItem.getCycle());

    }

    @Override
    public int getItemCount() {
        if(plantListData != null){
            return plantListData.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView common_name;
        ImageView thumbnail, watering, sunlight,cycle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            common_name =itemView.findViewById(R.id.common_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            watering = itemView.findViewById(R.id.watering);
            sunlight = itemView.findViewById(R.id.sunlight);
            cycle = itemView.findViewById(R.id.cycle);
        }

    }
}
