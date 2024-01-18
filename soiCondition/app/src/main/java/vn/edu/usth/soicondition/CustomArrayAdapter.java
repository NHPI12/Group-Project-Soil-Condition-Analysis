    package vn.edu.usth.soicondition;

    import android.content.Context;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.TextView;

    import androidx.annotation.NonNull;

    import java.util.List;

    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private boolean isDropDownShown = false;

        public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            // Set text color based on whether the dropdown is shown
            if (isDropDownShown) {
                ((TextView) view).setTextColor(getContext().getResources().getColor(android.R.color.black));
            } else {
                ((TextView) view).setTextColor(getContext().getResources().getColor(android.R.color.white));
            }

            return view;
        }

        public void setDropDownShown(boolean isDropDownShown) {
            this.isDropDownShown = isDropDownShown;
            notifyDataSetChanged(); // Notify the adapter that data set has changed
        }
    }
