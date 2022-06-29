package com.relax.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.relax.R;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.util.ArrayList;
import java.util.List;

public class dashBoard extends Fragment {
    View root = null;
    boolean IsSurvey = globalVariables.IsSurvey;
    int userID = globalVariables.userID;
    String txt;
    ArrayAdapter<String> adapter;
    ArrayList<String> weakList,strengthList, gratitudeList, resentmentList;
    TextView surveyDate, session_nm, spent_times, last_session_date, last_session_duration, journaling_nm, notificationTimes, userRate, userFeedback;
    ListView listWeak, listStrength, resentment_items, gratitude_items;
    dbHelper dbHelper;
    BarChart barChart;
    BarData barData;
    ArrayList<BarEntry> barEntries;
    BarDataSet barDataSet;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new dbHelper(getActivity());
        if (IsSurvey) {
            dbHelper.getUserFlags(globalVariables.userID);
        }
        dbHelper.close();
    }

    private ArrayList<String> fillXAxis() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("JAN");
        labels.add("FEB");
        labels.add("MAR");
        labels.add("APR");
        labels.add("MAY");
        labels.add("JUN");
        labels.add("JUL");
        labels.add("AUG");
        labels.add("SEP");
        labels.add("OCT");
        labels.add("NOV");
        labels.add("DEC");
        return labels;
    }

    private ArrayList<String> fillYAxis() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("0");
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        return labels;
    }

    private void customizeBarDataSet() {
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.RED);
        barDataSet.setValueTextSize(10);
        barDataSet.setValueTextColor(Color.BLUE);
    }

    private void customizeBarChart() {
        barChart.getDescription().setText("");
        barChart.setDrawMarkers(true);
        barChart.getLegend().getEntries();
        barChart.animateY(1000);
        barChart.getLegend().setEnabled(false);

        final XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(11);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(fillXAxis()));
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(barDataSet.getEntryCount());
        xAxis.setLabelRotationAngle(315);

        final YAxis axisRight = barChart.getAxisRight();
        axisRight.setAxisMinimum(0);
        axisRight.setAxisMaximum(12);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight.setValueFormatter(new IndexAxisValueFormatter(fillYAxis()));
        axisRight.setGranularityEnabled(true);
        axisRight.setGranularity(1.0f);
        axisRight.setLabelCount(6, true);
        axisRight.setGridColor(Color.argb(102, 255, 255, 255));
        axisRight.setAxisLineColor(Color.GRAY);
        axisRight.setDrawLabels(false);

        final YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMinimum(0);
    }

    private ArrayList<BarEntry> fillBarEntries() {
        //from DB ==> Y parameter will hold session count for each month
        //X parameter is for months
        List<Integer> sessions_counts = dbHelper.getSessionCountPerMonth(userID);
        ArrayList<BarEntry> entries = new ArrayList<>();
        int month = 1;
        for (Integer i : sessions_counts) {
            entries.add(new BarEntry(month, i));
            month++;
        }
        return entries;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_myjourney, container, false);

        surveyDate = root.findViewById(R.id.surveyDate);
        session_nm=  root.findViewById(R.id.session_nm);
        txt = session_nm.getText()+" "+ dbHelper.getSessionCount(userID);
        session_nm.setText(txt);
        spent_times=  root.findViewById(R.id.spent_times);
        txt = spent_times.getText()+" "+ dbHelper.getUserTotalTime(userID);
        spent_times.setText(txt);
        last_session_date=  root.findViewById(R.id.last_session_date);
        txt = last_session_date.getText()+" "+ dbHelper.getLastSessionDate(userID);
        last_session_date.setText(txt);
        last_session_duration = root.findViewById(R.id.last_session_duration);
        txt = last_session_duration.getText()+" "+ dbHelper.getLastSessionDuration(userID);
        last_session_duration.setText(txt);
        journaling_nm=  root.findViewById(R.id.journaling_nm);
        txt = journaling_nm.getText()+" "+ dbHelper.getTotalOfJournaling(userID);
        journaling_nm.setText(txt);
        notificationTimes=  root.findViewById(R.id.notificationTimes);
        txt = notificationTimes.getText()+" "+ dbHelper.getUserNotificationTime(userID);
        notificationTimes.setText(txt);
        userRate=  root.findViewById(R.id.userRate);
        txt = userRate.getText()+" "+ dbHelper.getUserRate(userID);
        userRate.setText(txt);
        userFeedback=  root.findViewById(R.id.userFeedback);
        txt = userFeedback.getText()+" "+ dbHelper.getUserFeedback(userID);
        userFeedback.setText(txt);

        listWeak = root.findViewById(R.id.stressFactors);
        listStrength = root.findViewById(R.id.StrengthFactors);
        resentment_items = root.findViewById(R.id.resentment_items);
        gratitude_items = root.findViewById(R.id.gratitude_items);

        barEntries = fillBarEntries();
        barDataSet = new BarDataSet(barEntries, "Months");
        customizeBarDataSet();
        barData = new BarData(barDataSet);
        barChart = root.findViewById(R.id.chart);
        barChart.setData(barData);
        customizeBarChart();

        if (globalVariables.IsSurvey) {
            String date = "Survey was taken on: " + dbHelper.getSurveyDate(userID);
            surveyDate.setText(date);

            weakList = new ArrayList<>();
            strengthList = new ArrayList<>();
            gratitudeList = new ArrayList<>();
            resentmentList = new ArrayList<>();

            weakList = getWeaknesses();
            strengthList = getStrengths();
            gratitudeList = dbHelper. getGratitudeNotes(userID);
            resentmentList = dbHelper.getResentmentNotes(userID);

            if (!weakList.isEmpty()) {
                adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, weakList);
                adapter.notifyDataSetChanged();
                listWeak.setAdapter(adapter);
            }

            if (!strengthList.isEmpty()) {
                adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, strengthList);
                adapter.notifyDataSetChanged();
                listStrength.setAdapter(adapter);
            }

            if (!gratitudeList.isEmpty()) {
                adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, gratitudeList);
                adapter.notifyDataSetChanged();
                gratitude_items.setAdapter(adapter);
            }

            if (!resentmentList.isEmpty()) {
                adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, resentmentList);
                adapter.notifyDataSetChanged();
                resentment_items.setAdapter(adapter);
            }

        }

        return root;
    }


    private ArrayList<String> getWeaknesses() {
        ArrayList<String> arrayList = new ArrayList<>();
        String text;

        if (!globalVariables.physicalFlag.equals("L")) {
            text = " • Physical Issues    ";
            if (globalVariables.physicalHandled != 0) {
                text += "Status: relieved";
            } else {
                text += "Status: unresolved";
            }
            arrayList.add(text);
        }

        if (!globalVariables.sleepFlag.equals("L")) {
            text = " • Sleep Issues    ";
            if (globalVariables.sleepHandled != 0) {
                text += "Status: relieved";
            } else {
                text += "Status: unresolved";
            }
            arrayList.add(text);
        }

        if (!globalVariables.behaviorFlag.equals("L")) {
            text = " • Behavioral Issues    ";
            if (globalVariables.behaviorHandled != 0) {
                text += "Status: relieved";
            } else {
                text += "Status: unresolved";
            }
            arrayList.add(text);
        }

        if (!globalVariables.emotionalFlag.equals("L")) {
            text = " • Emotional Issues    ";
            if (globalVariables.emotionalHandled != 0) {
                text += "Status: relieved";
            } else {
                text += "Status: unresolved";
            }
            arrayList.add(text);
        }

        return arrayList;
    }

    private ArrayList<String> getStrengths() {
        ArrayList<String> arrayList = new ArrayList<>();
        if (globalVariables.physicalFlag.equals("L")) arrayList.add(" • Physical aspect");
        if (globalVariables.sleepFlag.equals("L")) arrayList.add(" • Sleep aspect");
        if (globalVariables.behaviorFlag.equals("L")) arrayList.add(" • Behavioral aspect");
        if (globalVariables.emotionalFlag.equals("L")) arrayList.add(" • Emotional aspect");
        return arrayList;
    }
}
