package com.tecnosols.budgetary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog mBottomSheetDialog;

    private Dialog testDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);

        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
       // mBottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.setContentView(sheetView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.show();
            }
        });

    }
}
