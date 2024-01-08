package vn.edu.usth.soicondition;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Plant_Add_Recycle_Adapter extends RecyclerView.Adapter<Plant_Add_Recycle_Adapter.MyViewHolder2> {
    private List<PlantData> PlantData;
    private Context context;
    private CheckBox checkBoxAll;
    private boolean isAllChecked = false;
    private Set<Integer> addedPlantIds;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public Plant_Add_Recycle_Adapter(Context context, List<PlantData> plantData) {
        this.context = context;
        this.addedPlantIds = addedPlantIds;
        this.PlantData = filterAddedPlants(plantData, addedPlantIds);
        setHasStableIds(true);
        initializeSelectedItems();
    }

    private void initializeSelectedItems() {
        for (PlantData plantData : PlantData) {
            selectedItems.put(plantData.getId(), false);
        }
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.add_plant_item, parent, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Plant_Add_Recycle_Adapter.MyViewHolder2 holder, int position) {
        PlantData plantData = PlantData.get(position);
        if (plantData == null) {
            return;
        }
        holder.Add_common_name.setText((plantData.getCommon_name()));
        holder.checkBox.setChecked(selectedItems.get(plantData.getId()));
        default_Image defaultImage = plantData.getDefaultImage();
        if (defaultImage != null) {
            String thumbnailUrl = defaultImage.getThumbnail();
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                Picasso.get().load(thumbnailUrl).into(holder.thumbnail);
            }
        } else {
            Log.d("Thumbnail", "Not found");
        }
        if (holder.wateringIcon != null) {
            int wateringIcon = getWateringIcon(plantData.getWatering());
            holder.wateringIcon.setBackgroundResource(getWateringIcon((plantData.getWatering())));
        }
        setSunlightIcons(plantData.getSunlight(), holder.sunlightIconContainer);
        holder.Add_cycle.setText(plantData.getCycle());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(plantData.getId(), isChecked);
            onItemCheckedChanged(isAtLeastOneChecked());
            isAllChecked = isAllItemsChecked();
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(isAtLeastOneChecked());
            }
            if (!isChecked && holder.checkBoxAll != null) {
                holder.checkBoxAll.setChecked(false);
            }
        });
    }
    private boolean isAllItemsChecked() {
        for (PlantData plantData : PlantData) {
            if (!selectedItems.get(plantData.getId())) {
                return false;
            }
        }
        return true;
    }

    public void switchAllChecked() {
        this.isAllChecked = !this.isAllChecked;
        selectAllItems(isAllChecked);
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(isAtLeastOneChecked());
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder2 holder) {
        holder.checkBox.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return PlantData.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        LinearLayout sunlightIconContainer;
        private final TextView Add_common_name;
        private final TextView Add_cycle;
        private final ImageView wateringIcon;
        private final ImageView thumbnail;
        CheckBox checkBox, checkBoxAll;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            Add_common_name = itemView.findViewById(R.id.Addcommon_name);
            thumbnail = itemView.findViewById(R.id.Addthumbnail);
            Add_cycle = itemView.findViewById(R.id.Addcycle);
            wateringIcon = itemView.findViewById(R.id.AddwateringIcon);
            sunlightIconContainer = itemView.findViewById(R.id.AddsunlightIconsContainer);
            checkBox = itemView.findViewById(R.id.checkbox_plant);
            checkBoxAll = itemView.findViewById(R.id.checkAllCheckBox);
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

    public Set<Integer> getSelectedPlantIds() {
        Set<Integer> selectedPlantIds = new HashSet<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            if (selectedItems.get(key)) {
                selectedPlantIds.add(key);
            }
        }
        return selectedPlantIds;
    }

    private ImageView createSunlightIcon(int iconResourceId) {
        ImageView icon = new ImageView(context);
        icon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageResource(iconResourceId);
        icon.setPadding(0, 0, 20, 0); // Add padding between icons if needed
        return icon;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isAtLeastOneChecked);
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public boolean isAtLeastOneChecked() {
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                return true;
            }
        }
        return false;
    }

    private List<PlantData> filterAddedPlants(List<PlantData> plantDataList, Set<Integer> addedPlantIds) {
        List<PlantData> filteredList = new ArrayList<>();
        if (addedPlantIds == null) {
            addedPlantIds = new HashSet<>();
        }
        for (PlantData plantData : plantDataList) {
            if (!addedPlantIds.contains(plantData.getId())) {
                filteredList.add(plantData);
            }
        }
        return filteredList;
    }
    public void selectAllItems(boolean isSelected) {
        for (PlantData plantData : PlantData) {
            selectedItems.put(plantData.getId(), isSelected);
        }
        notifyDataSetChanged();
        isAllChecked = isSelected;
        if (checkBoxAll != null) {
            checkBoxAll.setChecked(isSelected);
        }
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(isAtLeastOneChecked());
        }
    }
    public void setCheckBoxAll(CheckBox checkBoxAll) {
        this.checkBoxAll = checkBoxAll;
    }
    private boolean areAllItemsSelected() {
        for (PlantData plantData : PlantData) {
            if (!selectedItems.get(plantData.getId())) {
                return false;
            }
        }
        return true;
    }
    private void onItemCheckedChanged(boolean isAtLeastOneChecked) {
        // Update the state of "Check All" checkbox based on all items' selection state
        if (checkBoxAll != null) {
            checkBoxAll.setChecked(areAllItemsSelected());
        }
    }
}