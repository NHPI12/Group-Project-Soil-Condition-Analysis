package vn.edu.usth.soicondition;



import android.content.Context;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;


import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Plant_List_Recycle_Adapter extends RecyclerView.Adapter<Plant_List_Recycle_Adapter.MyViewHolder> {
    private List<PlantData> PlantData;
    private Context context;
    private List<PlantData> SearchList;
    public Plant_List_Recycle_Adapter(Context context, List<PlantData> plantData) {
        this.context = context;
        this.PlantData = plantData;
        this.SearchList = new ArrayList<>(plantData);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.plant_list_item,parent,false);
        return new MyViewHolder(view);
    }
    public void filterList(String text){
        PlantData.clear();
        if(text.isEmpty()){
            PlantData.addAll(SearchList);
        }else{
            text = text.toLowerCase();
            for(PlantData plantDataItem : SearchList){
                if(plantDataItem.getCommon_name().toLowerCase().contains(text)){
                    PlantData.add(plantDataItem);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlantData plantData = PlantData.get(position);
        if(plantData == null){
            return;
        }
        holder.common_name.setText((plantData.getCommon_name()));
        default_Image defaultImage = plantData.getDefaultImage();
        if (defaultImage != null) {
            String thumbnailUrl = defaultImage.getThumbnail(); // Assuming getThumbnail returns the URL
            Log.d("Thumbnail", "Thumbnail URL: " + thumbnailUrl);
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                Picasso.get().load(thumbnailUrl).into(holder.thumbnail);
            }
        } else{
            Log.d("Thumbnail", "Not found");
        }
        if (holder.wateringIcon != null) {
            int wateringIcon = getWateringIcon(plantData.getWatering());
            // Set the icon to the ImageView
            holder.wateringIcon.setBackgroundResource(getWateringIcon((plantData.getWatering())));
        }
        setSunlightIcons(plantData.getSunlight(), holder.sunlightIconsContainer);
        holder.cycle.setText(plantData.getCycle());
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
                onItemClickListener.onItemClick(adapterPosition);
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return PlantData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout sunlightIconsContainer;
        private final TextView common_name;
        private final TextView cycle;
        private final ImageView wateringIcon;
        private final ImageView thumbnail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            common_name =itemView.findViewById(R.id.common_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            cycle = itemView.findViewById(R.id.cycle);
            wateringIcon = itemView.findViewById(R.id.wateringIcon);
            sunlightIconsContainer = itemView.findViewById(R.id.sunlightIconsContainer);
        }
    }
    private int getWateringIcon(String wateringText) {
        switch (wateringText.toLowerCase()) {
            case "frequent":
                return R.drawable.frequent;
            case "average":
                return R.drawable.average;
            case "minimum":
                return R.drawable.minimum;
            case "none":
                return R.drawable.none;
            default:
                return R.drawable.ic_thumbnail;
        }
    }
    private int getSunlightIcon(String sunlightText) {
        switch (sunlightText.toLowerCase()) {
            case "full shade":
                return R.drawable.full_shade;
            case "part shade":
                return R.drawable.part_shade;
            case "filtered shade":
                return R.drawable.sunpart_shade;
            case "full sun":
                return R.drawable.full_sun;
            case "part sun/part shade":
                return R.drawable.partsun_partshade;
            default:
                return R.drawable.ic_thumbnail;
        }
    }
    private void setSunlightIcons(List<String> sunlightConditions, ViewGroup sunlightContainer) {
        // Clear previous icons
        sunlightContainer.removeAllViews();

        if (sunlightConditions.isEmpty()) {
            // Set default icon if the list is empty
            ImageView defaultIcon = createSunlightIcon(R.drawable.ic_thumbnail);
            sunlightContainer.addView(defaultIcon);
            return;
        }

        // Create and add icons for each sunlight condition
        for (String sunlightCondition : sunlightConditions) {
            int iconResourceId = getSunlightIcon(sunlightCondition);
            ImageView icon = createSunlightIcon(iconResourceId);
            sunlightContainer.addView(icon);
        }
    }
    private ImageView createSunlightIcon(int iconResourceId) {
        ImageView icon = new ImageView(context);
        icon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageResource(iconResourceId);
        icon.setPadding(0, 0, 20, 0); // Add padding between icons if needed
        return icon;
    }
}
