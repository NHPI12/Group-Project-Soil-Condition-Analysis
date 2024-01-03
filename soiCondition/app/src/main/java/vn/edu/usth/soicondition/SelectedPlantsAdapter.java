package vn.edu.usth.soicondition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.usth.soicondition.network.model.PlantData;

public class SelectedPlantsAdapter extends RecyclerView.Adapter<SelectedPlantsAdapter.ViewHolder> {

    private List<PlantData> selectedPlants;

    // Constructor to initialize the adapter with a list of selected plants
    public SelectedPlantsAdapter(List<PlantData> selectedPlants) {
        this.selectedPlants = selectedPlants;
    }

    public void setPlantList(List<PlantData> plantList) {
        this.selectedPlants = plantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_plants_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantData plantData = selectedPlants.get(position);

        // Bind the plant data to the ViewHolder
        // Set the plant's common name and thumbnail (modify as per your data structure)
        holder.textViewCommonName.setText(plantData.getCommon_name());
        // Set the thumbnail using Picasso or any other image loading library
        Picasso.get().load(plantData.getDefaultImage().getThumbnail()).into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return selectedPlants.size();
    }

    // ViewHolder class to hold references to UI components
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCommonName;
        ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCommonName = itemView.findViewById(R.id.selectedPlantsTextView);
            imageViewThumbnail = itemView.findViewById(R.id.selectedPlantsImageView);
        }
    }
}