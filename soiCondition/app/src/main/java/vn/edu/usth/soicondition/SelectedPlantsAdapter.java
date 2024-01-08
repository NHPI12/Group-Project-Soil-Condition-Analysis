package vn.edu.usth.soicondition;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
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
import vn.edu.usth.soicondition.network.model.default_Image;

public class SelectedPlantsAdapter extends RecyclerView.Adapter<SelectedPlantsAdapter.ViewHolder> {

    private final List<PlantData> selectedPlants;
    private OnItemClickListener listener;
    private boolean expanded;

    public SelectedPlantsAdapter(List<PlantData> selectedPlantsList, OnItemClickListener listener) {
        this.selectedPlants = selectedPlantsList;
        this.expanded = false;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_plants_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (expanded) {
            return selectedPlants.size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnailImageView;
        private TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.imageViewThumbnail);
            nameTextView = itemView.findViewById(R.id.textViewCommonName);
            ImageView arrowImageView = itemView.findViewById(R.id.ArrowSelectedPlant);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position);
                }
            });
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
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public PlantData getPlantDataAtPosition(int position) {
        if (position >= 0 && position < selectedPlants.size()) {
            return selectedPlants.get(position);
        }
        return null;
    }
    public PlantData getTopItem() {
        if (selectedPlants.size() > 0) {
            return selectedPlants.get(0);
        } else {
            return null;
        }
    }
}