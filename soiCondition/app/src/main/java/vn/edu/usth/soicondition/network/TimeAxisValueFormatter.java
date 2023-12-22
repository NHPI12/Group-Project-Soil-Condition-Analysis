package vn.edu.usth.soicondition.network;

import android.annotation.SuppressLint;
import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeAxisValueFormatter implements IAxisValueFormatter {
    public TimeAxisValueFormatter(List<String> timestamps) {
        this.timestamps = timestamps;
    }

    public static float convertTimestampToFloat(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timestamp);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }

    public static List<Entry> createEntryList(List<String> timestamps, List<Float> values) {
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < timestamps.size(); i++) {
            float value = values.get(i);
            entries.add(new Entry(i, value));
        }

        return entries;
    }

    // New method to create x-axis labels from timestamps
    public static String[] createXAxisLabels(List<String> timestamps) {
        String[] labels = new String[timestamps.size()];

        for (int i = 0; i < timestamps.size(); i++) {
            long timestampMillis = convertTimestampToLong(timestamps.get(i));
            labels[i] = convertTimestampToHHmm(timestampMillis);
            Log.d("Milis", "timestampMillis: "+ timestampMillis);
        }

        return labels;
    }

    private static long convertTimestampToLong(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timestamp);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    private static String convertTimestampToHHmm(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    @SuppressLint("SimpleDateFormat")
    private String getFormattedTimestamp(long timestamp) {
        // Create a SimpleDateFormat instance for your desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
    private final List<String> timestamps;
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // Ensure the index is within the bounds of the timestamps list
        int index = (int) value;
        if (index >= 0 && index < timestamps.size()) {
            // Parse the timestamp and format it as "HH:mm"
            String originalTimestamp = timestamps.get(index);
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date date = originalFormat.parse(originalTimestamp);
                return targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
