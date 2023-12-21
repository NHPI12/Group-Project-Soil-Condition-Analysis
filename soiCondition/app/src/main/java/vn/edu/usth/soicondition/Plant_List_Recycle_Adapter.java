package vn.edu.usth.soicondition;


import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Plant_List_Recycle_Adapter extends RecyclerView.Adapter<Plant_List_Recycle_Adapter.MyViewHolder> {
    private List<PlantData> PlantData;
    private Context context;


    public Plant_List_Recycle_Adapter(Context context, List<PlantData> plantData) {
        this.context = context;
        this.PlantData = plantData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.plant_list_item,parent,false);
        return new MyViewHolder(view);
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
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                Picasso.get().load(thumbnailUrl).into(holder.thumbnail);
            }
        }

        if (holder.wateringIcon != null) {
            int wateringIcon = getWateringIcon(plantData.getWatering());
            // Set the icon to the ImageView
            holder.wateringIcon.setBackgroundResource(getWateringIcon((plantData.getWatering())));
        }
        List<String> sunlight = plantData.getSunlight();
        String sunlighttext = TextUtils.join("// ",sunlight);
        holder.sunlight.setText(sunlighttext);
        holder.cycle.setText(plantData.getCycle());

    }

    @Override
    public int getItemCount() {
        return PlantData.size();
    }

       class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView common_name;
        private final TextView sunlight;
        private final TextView cycle;
        private final ImageView wateringIcon;
        private final ImageView thumbnail;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            common_name =itemView.findViewById(R.id.common_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            sunlight = itemView.findViewById(R.id.sunlight);
            cycle = itemView.findViewById(R.id.cycle);
            wateringIcon = itemView.findViewById(R.id.wateringIcon);

        }
    }
    private int getWateringIcon(String wateringText) {
        switch (wateringText) {
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
}
