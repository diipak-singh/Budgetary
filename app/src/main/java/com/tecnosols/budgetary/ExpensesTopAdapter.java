package com.tecnosols.budgetary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesTopAdapter extends RecyclerView.Adapter<ExpensesTopAdapter.expenseTopViewHolder> {

    ArrayList<ExpensesTopModel> expenseTopList = new ArrayList<>();
    private static Context ctx;

    public ExpensesTopAdapter(ArrayList<ExpensesTopModel> expenseTopList,Context ctx) {
        this.expenseTopList = expenseTopList;
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public expenseTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_top_layout, parent, false);
        return new ExpensesTopAdapter.expenseTopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull expenseTopViewHolder holder, int position) {
        int date=expenseTopList.get(position).getDate();
        String month=expenseTopList.get(position).getMonth();

        holder.setTop(Integer.toString(date),month);

    }

    @Override
    public int getItemCount() {
        return expenseTopList.size();
    }

    public static class expenseTopViewHolder extends RecyclerView.ViewHolder{
        private TextView topDate,topMonth;
        private ConstraintLayout clayout;

        public expenseTopViewHolder(@NonNull View itemView) {
            super(itemView);
            topDate=itemView.findViewById(R.id.textView_topDate);
            topMonth=itemView.findViewById(R.id.textView_topMonth);
            clayout=itemView.findViewById(R.id.constraintLayout_expTop);

        }
        private void setTop(String date,String month){
            topDate.setText(date);
            topMonth.setText(month);
        }


    }
}
