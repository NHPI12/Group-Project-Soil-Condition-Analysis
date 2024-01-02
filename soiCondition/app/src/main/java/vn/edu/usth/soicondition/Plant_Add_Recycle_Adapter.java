package vn.edu.usth.soicondition;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Plant_Add_Recycle_Adapter extends RecyclerView.Adapter<Plant_Add_Recycle_Adapter.MyViewHolder2> {
    private List<PlantData> PlantData;
    private Context context;
    private boolean isAllChecked = false;
    public  Plant_Add_Recycle_Adapter(Context context, List<PlantData> plantData){
        this.context = context;
        this.PlantData = plantData;
    }
    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.add_plant_item,parent,false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Plant_Add_Recycle_Adapter.MyViewHolder2 holder, int position) {
        PlantData plantData = PlantData.get(position);
        if(plantData == null){
            return;
        }
        holder.Add_common_name.setText((plantData.getCommon_name()));
        default_Image defaultImage = plantData.getDefaultImage();
        if(defaultImage != null){
            String thumbnailUrl = defaultImage.getThumbnail();
            if(!TextUtils.isEmpty(thumbnailUrl)){
                Picasso.get().load(thumbnailUrl).into(holder.thumbnail);
            }
        } else{
            Log.d("Thumbnail","Not found");
        }
        if (holder.wateringIcon != null){
            int wateringIcon = getWateringIcon(plantData.getWatering());
            // Set the icon to the ImageView
            holder.wateringIcon.setBackgroundResource(getWateringIcon((plantData.getWatering())));
        }
        setSunlightIcons(plantData.getSunlight(), holder.sunlightIconContainer);
        holder.Add_cycle.setText(plantData.getCycle());
        if(isAllChecked == false){
            holder.checkBox.setChecked(false);
        }else{
            holder.checkBox.setChecked(true);
        }

    }
    public void switchAllChecked(){
        this.isAllChecked = !this.isAllChecked;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return PlantData.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder{
        LinearLayout sunlightIconContainer;
        private final TextView Add_common_name;
        private final TextView Add_cycle;
        private final ImageView wateringIcon;
        private final ImageView thumbnail;
        CheckBox checkBox;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            Add_common_name = itemView.findViewById(R.id.Addcommon_name);
            thumbnail = itemView.findViewById(R.id.Addthumbnail);
            Add_cycle = itemView.findViewById(R.id.Addcycle);
            wateringIcon = itemView.findViewById(R.id.AddwateringIcon);
            sunlightIconContainer = itemView.findViewById(R.id.AddsunlightIconsContainer);
            checkBox = itemView.findViewById(R.id.checkbox_plant);

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
        icon.setLayoutParams(new ViewGroup.LayoutParams(75,75));
        icon.setImageResource(iconResourceId);
        icon.setPadding(0, 0, 8, 0); // Add padding between icons if needed
        return icon;
    }
}
