package vn.edu.usth.soicondition;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Plant_Add_ListView_Adapter extends BaseAdapter {
    private List<PlantData> plantDataList;
    private Context context;
    private LayoutInflater inflater;
    private boolean isAllChecked = false;
    private List<PlantData> selectedPlants = new ArrayList<>();
    private OnCheckedChangeListener onCheckedChangeListener;

    public Plant_Add_ListView_Adapter(Context context, List<PlantData> plantDataList) {
        this.context = context;
        this.plantDataList = plantDataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return plantDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return plantDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.add_plant_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PlantData plantData = plantDataList.get(position);
        viewHolder.checkBox.setChecked(isAllChecked || plantData.isChecked());
        if (plantData != null) {
            viewHolder.Add_common_name.setText(plantData.getCommon_name());
            viewHolder.checkBox.setChecked(plantData.isChecked());

            default_Image defaultImage = plantData.getDefaultImage();
            if (defaultImage != null) {
                String thumbnailUrl = defaultImage.getThumbnail();
                if (!TextUtils.isEmpty(thumbnailUrl)) {
                    Picasso.get().load(thumbnailUrl).into(viewHolder.thumbnail);
                }
            } else {
                Log.d("Thumbnail", "Not found");
            }

            if (viewHolder.wateringIcon != null) {
                int wateringIcon = getWateringIcon(plantData.getWatering());
                viewHolder.wateringIcon.setBackgroundResource(getWateringIcon(plantData.getWatering()));
            }

            setSunlightIcons(plantData.getSunlight(), viewHolder.sunlightIconContainer);
            viewHolder.Add_cycle.setText(plantData.getCycle());

            viewHolder.checkBox.setOnCheckedChangeListener(null); // Remove previous listener to avoid conflicts
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    plantData.setChecked(isChecked);

                    // Update the selectedPlants list based on checkbox state
                    if (isChecked) {
                        selectedPlants.add(plantData);
                    } else {
                        selectedPlants.remove(plantData);
                    }

                    if (onCheckedChangeListener != null) {
                        onCheckedChangeListener.onCheckedChanged(isAtLeastOneChecked());
                    }
                }
            });
        }

        return view;
    }

    private static class ViewHolder {
        LinearLayout sunlightIconContainer;
        private final TextView Add_common_name;
        private final TextView Add_cycle;
        private final ImageView wateringIcon;
        private final ImageView thumbnail;
        CheckBox checkBox;

        public ViewHolder(View view) {
            Add_common_name = view.findViewById(R.id.Addcommon_name);
            thumbnail = view.findViewById(R.id.Addthumbnail);
            Add_cycle = view.findViewById(R.id.Addcycle);
            wateringIcon = view.findViewById(R.id.AddwateringIcon);
            sunlightIconContainer = view.findViewById(R.id.AddsunlightIconsContainer);
            checkBox = view.findViewById(R.id.checkbox_plant);
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
        sunlightContainer.removeAllViews();

        if (sunlightConditions.isEmpty()) {
            ImageView defaultIcon = createSunlightIcon(R.drawable.ic_thumbnail);
            sunlightContainer.addView(defaultIcon);
            return;
        }

        for (String sunlightCondition : sunlightConditions) {
            int iconResourceId = getSunlightIcon(sunlightCondition);
            ImageView icon = createSunlightIcon(iconResourceId);
            sunlightContainer.addView(icon);
        }
    }

    Set<Integer> getSelectedPlantIds() {
        Set<Integer> selectedPlantIds = new HashSet<>();
        for (PlantData selectedPlant : selectedPlants) {
            selectedPlantIds.add(Integer.valueOf(selectedPlant.getId()));
            Log.d("Selected Plant Details", "Add ID: " + selectedPlantIds);
        }
        return selectedPlantIds;
    }

    private ImageView createSunlightIcon(int iconResourceId) {
        ImageView icon = new ImageView(context);
        icon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageResource(iconResourceId);
        icon.setPadding(0, 0, 20, 0);
        return icon;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isAtLeastOneChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public boolean isAtLeastOneChecked() {
        for (PlantData plantData : plantDataList) {
            if (plantData.isChecked()) {
                return true;
            }
        }
        return false;
    }
    public void switchAllChecked() {
        isAllChecked = !isAllChecked;
        notifyDataSetChanged();
    }
}