package vn.edu.usth.soicondition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class Plant_Details_Image_Recycle_Adapter extends RecyclerView.Adapter<Plant_Details_Image_Recycle_Adapter.ImageViewHolder> {
    private List<Integer> imageViewList;
    private Context context;
    public Plant_Details_Image_Recycle_Adapter(Context context, List<Integer> imageViewList){
        this.context = context;
        this.imageViewList = imageViewList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_plant_details,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        holder.imageView.setImageResource(imageViewList.get(position));
    }

    @Override
    public int getItemCount() {

        return imageViewList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(View itemView){
            super(itemView);
            imageView =itemView.findViewById(R.id.image_details_plant);
        }
    }
}

