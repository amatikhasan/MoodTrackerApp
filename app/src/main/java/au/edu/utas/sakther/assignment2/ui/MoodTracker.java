package au.edu.utas.sakther.assignment2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.db.DBHelper;
import au.edu.utas.sakther.assignment2.model.MoodModel;
import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

public class MoodTracker extends AppCompatActivity {
    Toolbar toolbar;
    private ArrayList<MoodModel> moodData;

    DBHelper dbHelper;

    private LineChartView chart;
    private LineChartData data;

    TextView tvMonth;
    TextView tvEmptyPie;
    TextView tvEmptyLine;

    ArrayList<String> dateList;
    ArrayList<Integer> valueList;

    String mMonth;
    int daysInMonth;
    ArrayList<Integer> days;

    int dailySells = 0;

    LineView lineView;

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);

        moodData=new ArrayList<>();

//        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        days = new ArrayList<>();
        dateList = new ArrayList<>();
        valueList = new ArrayList<>();


        tvEmptyPie=findViewById(R.id.emptyPie);
        tvEmptyLine=findViewById(R.id.emptyLine);
        tvMonth =findViewById(R.id.month);
        lineView = findViewById(R.id.lineView);
        pieChart = findViewById(R.id.pieChart);
//        chart = findViewById(R.id.chart);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mood Tracker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        Date now = calendar.getTime();
        mMonth = sdf.format(now);

        getData();

    }

    public void getData(){


        dbHelper=new DBHelper(getApplicationContext());
        moodData=dbHelper.getMoodInMonth(mMonth);
        tvMonth.setText(mMonth);


        if (moodData.size()>0) {

            tvEmptyPie.setVisibility(View.GONE);
            tvEmptyLine.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

            setPieChart();
            prepareChart();
        }
        else {
            tvEmptyPie.setVisibility(View.VISIBLE);
            tvEmptyLine.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);

        }
    }

    public void getMonth(View view){

        int yearSelected,monthSelected;
        final Calendar calendar=Calendar.getInstance();
        yearSelected=calendar.get(Calendar.YEAR);
        monthSelected=calendar.get(Calendar.MONTH);


        MonthYearPickerDialogFragment dialogFragment=MonthYearPickerDialogFragment.getInstance(monthSelected,yearSelected);
        dialogFragment.show(getSupportFragmentManager(),null);
        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int month) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);

                SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
                Date date = calendar.getTime();
                mMonth=sdf.format(date);

                Log.d("check", "onDateSet: "+month+" "+year+" "+mMonth);

                getData();
            }
        });

    }

    public void setPieChart(){

        pieChart.setUsePercentValues(true);

        ArrayList<Entry> entries=new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String> labels=new ArrayList<>();
        ArrayList<MoodModel> values = new ArrayList<>();

        values.add(new MoodModel("Very Happy",dbHelper.getMoodCount("Very Happy",mMonth)));
        values.add(new MoodModel("Happy",dbHelper.getMoodCount("Happy",mMonth)));
        values.add(new MoodModel("OK",dbHelper.getMoodCount("OK",mMonth)));
        values.add(new MoodModel("Boring",dbHelper.getMoodCount("Boring",mMonth)));
        values.add(new MoodModel("Sad",dbHelper.getMoodCount("Sad",mMonth)));

        int j=0;
        for (int i=0;i<values.size();i++){
            if (values.get(i).getMoodValue()>0){
                entries.add(new Entry(values.get(i).getMoodValue(),j));
                labels.add(values.get(i).getMood());

                if (values.get(i).getMood().equals("Very Happy"))
                    colors.add(getResources().getColor(R.color.very_happy));
                if (values.get(i).getMood().equals("Happy"))
                    colors.add(getResources().getColor(R.color.happy));
                if (values.get(i).getMood().equals("Neutral"))
                    colors.add(getResources().getColor(R.color.neutral));
                if (values.get(i).getMood().equals("Sad"))
                    colors.add(getResources().getColor(R.color.sad));
                if (values.get(i).getMood().equals("Depressed"))
                    colors.add(getResources().getColor(R.color.depressed));


                j++;
            }
        }

//        colors.add(getResources().getColor(R.color.very_happy));
//        colors.add(getResources().getColor(R.color.happy));
//        colors.add(getResources().getColor(R.color.ok));
//        colors.add(getResources().getColor(R.color.boring));
//        colors.add(getResources().getColor(R.color.sad));

//        labels.add("Very Happy");
//        labels.add("Happy");
//        labels.add("OK");
//        labels.add("Boring");
//        labels.add("Sad");

        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setColors(colors);

        PieData pieData=new PieData(labels,dataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.setDescription("");

        pieChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);


    }

    public void prepareChart() {
        Log.d("check", "prepare chart: " + moodData.size());


        ArrayList<String> labels = new ArrayList<String>();

        for (int i = 0; i < moodData.size(); i++) {
            String[] parts=moodData.get(i).getDate().split(" ");
            labels.add(String.valueOf(parts[0]));
            Log.d("check", "prepare chart: " + moodData.get(i).getDate());
        }

        valueList.clear();
        for (int i = 0; i < moodData.size(); i++) {

            valueList.add(moodData.get(i).getMoodValue());
        }


        /*
        BarData data = new BarData( labels, bardataset);
        barChart.setData(data); // set the data and list of lables into chart
        barChart.setDescription("Sells in "+monthYear);  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(1000);
        */


        ArrayList<ArrayList<Integer>> data = new ArrayList<>();

        data.add(valueList);

        lineView.setDrawDotLine(true);
//        lineView.setShowPopup(LineView.SHOW_POPUPS_All);
        lineView.setColorArray(new int[]{
                Color.parseColor("#FF1E90FF")
        });
        lineView.setBottomTextList(labels);
        lineView.setDataList(data);
    }


//    private void generateData() {
//        Log.d("check", "prepare chart: " + moodData.size());
//
//        List<Line> lines = new ArrayList<Line>();
//        int[] value=new int[moodData.size()];
//
//        for (int j = 0; j < moodData.size(); ++j) {
//            value[j] = moodData.get(j).getMoodValue();
//        }
//
//            List<PointValue> values = new ArrayList<PointValue>();
//            for (int j = 0; j < moodData.size(); ++j) {
//                values.add(new PointValue(j, value[j]));
//            }
//
//            Line line = new Line(values);
//            line.setColor(ChartUtils.COLORS[0]);
//            line.setShape(shape);
//            line.setCubic(isCubic);
//            line.setFilled(isFilled);
//            line.setHasLabels(hasLabels);
//            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
//            line.setHasLines(hasLines);
//            line.setHasPoints(hasPoints);
//            lines.add(line);
//
//
//        data = new LineChartData(lines);
//
//
//        if (hasAxes) {
//            Axis axisX = new Axis();
//            Axis axisY = new Axis().setHasLines(true);
//            if (hasAxesNames) {
//                axisX.setName("Axis X");
//                axisY.setName("Axis Y");
//            }
//            data.setAxisXBottom(axisX);
//            data.setAxisYLeft(axisY);
//        } else {
//            data.setAxisXBottom(null);
//            data.setAxisYLeft(null);
//        }
//
//
//        data.setBaseValue(Float.NEGATIVE_INFINITY);
//        chart.setLineChartData(data);
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
//                startActivity(new Intent(getApplicationContext(), MoodList.class));
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {


        startActivity(new Intent(getApplicationContext(), MoodList.class));
        finish();
    }
}