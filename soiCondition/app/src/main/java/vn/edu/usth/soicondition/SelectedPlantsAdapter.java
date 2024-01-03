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

        PlantData plant = selectedPlants.get(position);
        default_Image defaultImage = plant.getDefaultImage();
        if (defaultImage != null) {
            String thumbnailUrl = defaultImage.getThumbnail();
            Log.d("Selected Plant Details", "Thumbnail URL: " + thumbnailUrl);
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                Picasso.get().load(thumbnailUrl).into(holder.thumbnailImageView);
            }
        } else {
            Log.d("Selected Plant Details", "DefaultImage Not found");
        }
        holder.nameTextView.setText(plant.getCommon_name());

    }

    @Override
    public int getItemCount() {
        return selectedPlants.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnailImageView, arrowImageView;
        private TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.imageViewThumbnail);
            nameTextView = itemView.findViewById(R.id.textViewCommonName);
            arrowImageView = itemView.findViewById(R.id.ArrowSelectedPlant);
        }

    }

    public void toggleRecyclerViewVisibility(ImageView arrowImageView, RecyclerView selectedPlantsRecyclerView) {
        if (selectedPlantsRecyclerView != null) {
            expanded = !expanded;
            notifyDataSetChanged();
            Log.d("Selected Plant Details", "Recycle View Not null and Clicked" + expanded);
            // Rotate the arrow icon
            float newRotation = expanded ? 180f : 0f;
            arrowImageView.animate().rotation(newRotation).start();
            adjustRecyclerViewHeight(selectedPlantsRecyclerView);
        } else {
            Log.d("Selected Plant Details", "Recycle View null");
        }
    }

    public void setData(List<PlantData> newData) {
        selectedPlants.clear();
        selectedPlants.addAll(newData);
        expanded = false;
        notifyDataSetChanged();
    }

    private void adjustRecyclerViewHeight(RecyclerView recyclerView) {
        if (recyclerView != null && expanded) {
            int maxRecyclerViewHeight = 200; // Set your maximum height in pixels
            int recyclerViewHeight = recyclerView.getMeasuredHeight();

            if (recyclerViewHeight > maxRecyclerViewHeight) {
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                layoutParams.height = maxRecyclerViewHeight;
                recyclerView.setLayoutParams(layoutParams);
            }
        }

    }

}