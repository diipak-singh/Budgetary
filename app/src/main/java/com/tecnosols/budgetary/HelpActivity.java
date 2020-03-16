package com.tecnosols.budgetary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private ImageView imageBack;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<HelpModel> helpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        imageBack = findViewById(R.id.imageView_helpback);
        recyclerView = findViewById(R.id.recyclerView_Help);
        helpList.clear();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        helpList.add(new HelpModel("1. Adding an expense data.", getResources().getString(R.string.add_expense), R.drawable.add));
        helpList.add(new HelpModel("2. Deleting an expense data.", getResources().getString(R.string.delete_expense), R.drawable.home));
        helpList.add(new HelpModel("3. Getting previous expenses.", getResources().getString(R.string.previous_expenses), R.drawable.expns));
        helpList.add(new HelpModel("4. Removing ads.", getResources().getString(R.string.remove_ads), R.drawable.fin_sett));

        adapter = new HelpAdapter((ArrayList<HelpModel>) helpList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
