package com.tecnosols.budgetary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    BottomSheetDialog mBottomSheetDialog;

    private TextInputEditText eName, eDesc, eCurr, eAmount;
    private Button addExpense;
    private TextView topDate;
    FirebaseUser user;
    String month;
    String year;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        topDate = findViewById(R.id.textView_topDate);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Calendar c = Calendar.getInstance();
        day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        month = getMonthName();
        year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        topDate.setText(day + " " + month + ", " + year);

        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);

        eName = mBottomSheetDialog.findViewById(R.id.text_expenseName);
        eDesc = mBottomSheetDialog.findViewById(R.id.text_expenseDescription);
        eCurr = mBottomSheetDialog.findViewById(R.id.text_expenseCurrency);
        eAmount = mBottomSheetDialog.findViewById(R.id.text_expenseAmount);
        addExpense = mBottomSheetDialog.findViewById(R.id.button_addExpense);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.show();
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    private void saveData() {

        String expN = eName.getText().toString().trim();
        String expD = eDesc.getText().toString().trim();
        String expC = eCurr.getText().toString().trim();
        String expA = eAmount.getText().toString().trim();

        if (expN.isEmpty() || expN == null) {
            eName.setError("This field can't be empty.");
            eName.requestFocus();
            return;
        }
        if (expD.isEmpty() || expD == null) {
            eDesc.setError("This field can't be empty.");
            eDesc.requestFocus();
            return;
        }
        if (expC.isEmpty() || expC == null) {
            eCurr.setError("Currency..");
            eCurr.requestFocus();
            return;
        }
        if (expA.isEmpty() || expA == null) {
            eAmount.setError("Amount can't be empty.");
            eAmount.requestFocus();
            return;
        }
        String user_id = user.getUid();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("ExpensesData/" + user_id + "/" + year + "/" + month + "/" + day);
        String e_Id = dref.push().getKey();
        ExpenseDetail ed = new ExpenseDetail(expN, expD, expC, expA, e_Id);
        dref.child(e_Id).setValue(ed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    mBottomSheetDialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private String getMonthName() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }
}
