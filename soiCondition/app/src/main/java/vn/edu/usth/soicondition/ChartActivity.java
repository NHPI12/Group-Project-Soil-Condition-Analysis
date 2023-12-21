package vn.edu.usth.soicondition;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        //LineChart

        LineChart lineChart1 = findViewById(R.id.chart1);
        ArrayList<Entry> Averages = dataValues1();

        float sum = 0;

        for(Entry entry : Averages){
            sum+= entry.getY();
        }
        float average = sum / Averages.size();


        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Dataset1");
        if(average>20){
            lineDataSet1.setColor(Color.rgb(34, 139, 34));
            lineDataSet1.setCircleColor(Color.rgb(34, 139, 34));
        }else{
            lineDataSet1.setColor(Color.RED);
            lineDataSet1.setCircleColor(Color.RED);
        }


        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setCircleRadius(4f);
        lineDataSet1.setValueTextSize(10f);
        int[] gradientColors = (average > 20) ?
                new int[]{Color.rgb(34, 139, 34), Color.parseColor("#FFFFFF")} :  // Green gradient
                new int[]{Color.parseColor("#FF0000"), Color.parseColor("#FFFFFF")} ; // Red gradient

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
        lineDataSet1.setFillDrawable(gradientDrawable);
        lineDataSet1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        lineChart1.setData(data);
        lineChart1.invalidate();


    }

    // Set values placeholder
    private ArrayList<Entry> dataValues1() {
        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0, 20));
        dataVals.add(new Entry(1, 21));
        dataVals.add(new Entry(2, 23));
        dataVals.add(new Entry(3, 26));
        dataVals.add(new Entry(4, 28));
        return dataVals;
    }

}