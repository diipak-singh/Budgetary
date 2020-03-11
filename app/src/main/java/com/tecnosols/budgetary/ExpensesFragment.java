package com.tecnosols.budgetary;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ExpensesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<ExpensesTopModel> expenseTopList = new ArrayList<>();

    private Spinner spinnerYear, spinnerMonth;
    private List<String> spinnerListYear = new ArrayList<>();
    private List<String> spinnerListMonth = new ArrayList<>();

    int touch_position = 0;
    int iYear;
    int daysInMonth = 28;


    PieChart chart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;


    public ExpensesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);


        recyclerView = view.findViewById(R.id.recyclerView_expenseTop);
        spinnerYear = view.findViewById(R.id.spinner_year);
        spinnerMonth = view.findViewById(R.id.spinnerMonth);

        expenseTopList.clear();
        spinnerListYear.clear();
        spinnerListMonth.clear();
        getMonthList();
        getYearList();


        chart = view.findViewById(R.id.pieChart);
//        getChartEntries();
//        pieDataSet = new PieDataSet(pieEntries, "");
//        pieData = new PieData(pieDataSet);
//        chart.setData(pieData);
        setData();

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(Typeface.SANS_SERIF);
        chart.setCenterText(generateCenterSpannableText());

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);


        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerListYear);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinnerYear.setAdapter(dataAdapter);


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerListMonth);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinnerMonth.setAdapter(dataAdapter2);


        int selectionPosition = dataAdapter.getPosition("2020");
        spinnerYear.setSelection(selectionPosition);

        int selectionPosition2 = dataAdapter2.getPosition("Mar");
        spinnerMonth.setSelection(selectionPosition2);


        final int iMonth = Calendar.FEBRUARY; // 1 (months begin with 0)
        final int iDay = 1;

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iYear = Integer.parseInt(parent.getItemAtPosition(position).toString());
                //getting n.o of days in a particular month of a particular year

                Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
                daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expenseTopList.clear();
                for (int i = 1; i <= daysInMonth; i++) {
                    expenseTopList.add(new ExpensesTopModel(i, parent.getItemAtPosition(position).toString()));
                }
                adapter = new ExpensesTopAdapter((ArrayList<ExpensesTopModel>) expenseTopList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(21 - 3);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView date = view.findViewById(R.id.textView_topDate);
                        TextView month = view.findViewById(R.id.textView_topMonth);
                        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout_expTop);

                        date.setTextColor(getContext().getColor(R.color.colorBlack));
                        month.setTextColor(getContext().getColor(R.color.colorBlack));
                        constraintLayout.setBackgroundColor(getContext().getColor(R.color.colorAccent));

                        Toast.makeText(getContext(), Integer.toString(expenseTopList.get(position).getDate()) + expenseTopList.get(position).getMonth() + " Selected.", Toast.LENGTH_SHORT).show();

                        touch_position = position;
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {

                    }
                }));

        //((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(21,0);


        return view;
    }

    private void selectedDate(int position) {

    }

    private void getMonthList() {
        /*DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("AppData/Months");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String mon=ds.getValue(String.class);
                        spinnerListMonth.add(mon);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "There is some Error.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        spinnerListMonth.add("Jan");
        spinnerListMonth.add("Feb");
        spinnerListMonth.add("Mar");
        spinnerListMonth.add("Apr");
        spinnerListMonth.add("May");
        spinnerListMonth.add("Jun");
        spinnerListMonth.add("Jul");
        spinnerListMonth.add("Aug");
        spinnerListMonth.add("Sep");
        spinnerListMonth.add("Oct");
        spinnerListMonth.add("Nov");
        spinnerListMonth.add("Dec");
    }

    private void getYearList() {
        spinnerListYear.add("2020");
        spinnerListYear.add("2021");
        spinnerListYear.add("2022");
        spinnerListYear.add("2023");
        spinnerListYear.add("2024");
        spinnerListYear.add("2025");

    }

    private void showGraph(String day, String month, String year) {

    }

    private void getChartEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(30, 0));
        pieEntries.add(new PieEntry(100, 1));
        pieEntries.add(new PieEntry(80, 2));
        pieEntries.add(new PieEntry(150, 3));

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Your Expenditure\npresented by Budgetary in %");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 16, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 16, s.length() - 17, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 16, s.length() - 17, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 16, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 16, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }

    private void setData() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) (Math.random() * range) + range / 5, parties[i % parties.length]));
//        }
        entries.add(new PieEntry(80, "Breakfast"));
        entries.add(new PieEntry(150, "Lunch"));
        entries.add(new PieEntry(50, "Snacks"));
        entries.add(new PieEntry(200, "Dinner"));
        entries.add(new PieEntry(80, "Breakfast"));

        PieDataSet dataSet = new PieDataSet(entries, "Today's Expenditure");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.SANS_SERIF);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }


}
