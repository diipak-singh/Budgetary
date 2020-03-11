package com.tecnosols.budgetary;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment extends Fragment {

    private FloatingActionButton fab;
    BottomSheetDialog mBottomSheetDialog;
    Context ctx;
    ProgressDialog pd;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<ExpenseDetail> expenseList = new ArrayList<>();

    private TextInputEditText eName, eDesc, eCurr, eAmount;
    private Button addExpense;
    private TextView topDate, totalExpenditure;
    FirebaseUser user;
    String month;
    String year;
    String day;
    int total = 0;


    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fab = view.findViewById(R.id.floatingActionButton);
        topDate = view.findViewById(R.id.textView_topDate);
        totalExpenditure = view.findViewById(R.id.textView_total);
        user = FirebaseAuth.getInstance().getCurrentUser();

        ctx = getContext();

        pd = new ProgressDialog(container.getContext());
        pd.setMessage("Fetching Data...");
        pd.setCancelable(false);
        pd.show();

        Calendar c = Calendar.getInstance();
        day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        month = getMonthName();
        year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        topDate.setText(day + " " + month + ", " + year);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mBottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.DialogStyle);
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet, null);
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

        getExpenseData();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {

                        new AlertDialog.Builder(getContext())
                                .setIcon(R.drawable.ic_delete_forever_black_24dp)
                                .setTitle("Deleting, " + expenseList.get(position).getExpName())
                                .setMessage("Are you sure, you want to delete this?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteExpenseData(expenseList.get(position).getExpId());
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();

                    }
                }));




        return view;
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
                if (task.isComplete()) {
                    mBottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
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

    private void deleteExpenseData(String id) {
        String user_id = user.getUid();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("ExpensesData/" + user_id + "/" + year + "/" + month + "/" + day);
        dref.child(id).removeValue();
        adapter.notifyDataSetChanged();
        Toast.makeText(ctx, "Deleted Successfully", Toast.LENGTH_SHORT).show();
    }

    private void getExpenseData() {
        String user_id = user.getUid();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("ExpensesData/" + user_id + "/" + year + "/" + month + "/" + day);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pd.cancel();
                    expenseList.clear();
                    total = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ExpenseDetail ed = ds.getValue(ExpenseDetail.class);
                        total = total + Integer.parseInt(ed.expAmount);
                        expenseList.add(new ExpenseDetail(ed.expName, ed.expDesc, ed.expCurr, ed.expAmount, ed.expId));

                    }
                    totalExpenditure.setText("Today's Expenditure, Rs." + Integer.toString(total));
                    adapter = new ExpenseAdapter((ArrayList<ExpenseDetail>) expenseList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    pd.cancel();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
