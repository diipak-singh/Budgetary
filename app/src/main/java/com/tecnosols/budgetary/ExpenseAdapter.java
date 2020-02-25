package com.tecnosols.budgetary;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Random;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.expenseViewHolder> {
    ArrayList<ExpenseDetail> expenseList = new ArrayList<>();

    public ExpenseAdapter(ArrayList<ExpenseDetail> expenseList) {
        this.expenseList = expenseList;
    }

    int[] colors = {Color.parseColor("#3271DC"),
            Color.parseColor("#c0c0c0"),
            Color.parseColor("#2d2d2d"),
            Color.parseColor("#FFB666"),
            Color.parseColor("#6200EE"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#CDDC39"),
            Color.parseColor("#FF5252"),
            Color.parseColor("#69F0AE"),
            Color.parseColor("#87DC32"),
            Color.parseColor("#6200EE"),};

    @NonNull
    @Override
    public expenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_layout, parent, false);
        return new ExpenseAdapter.expenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull expenseViewHolder holder, int position) {
        Random random = new Random();
        int randomInt = random.nextInt(10);

        String expN = expenseList.get(position).getExpName();
        String expD = expenseList.get(position).getExpDesc();
        String expA = expenseList.get(position).getExpAmount();
        String expC = expenseList.get(position).getExpCurr();

        holder.setName(expN, colors[randomInt]);
        holder.setDesc(expD);
        holder.setAmount(expA, expC);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class expenseViewHolder extends RecyclerView.ViewHolder {
        private TextView expName, expDesc, expAmount;
        private MaterialCardView expCard;

        public expenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expName = itemView.findViewById(R.id.textView_expName);
            expDesc = itemView.findViewById(R.id.textView_expDesc);
            expAmount = itemView.findViewById(R.id.textView_expAmount);
            expCard = itemView.findViewById(R.id.expCardView);

        }

        private void setName(String name, int color) {
            expName.setText(name);
            expName.setTextColor(color);
            expCard.setStrokeColor(color);
        }

        private void setDesc(String desc) {
            expDesc.setText(desc);
        }

        private void setAmount(String amt, String curr) {
            expAmount.setText(curr + amt);
        }
    }
}
