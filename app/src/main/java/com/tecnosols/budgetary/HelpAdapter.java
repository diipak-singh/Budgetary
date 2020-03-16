package com.tecnosols.budgetary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.helpViewHolder> {
    ArrayList<HelpModel> helpList = new ArrayList<>();

    public HelpAdapter(ArrayList<HelpModel> helpList) {
        this.helpList = helpList;
    }

    @NonNull
    @Override
    public helpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_center_layout, parent, false);
        return new HelpAdapter.helpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull helpViewHolder holder, int position) {
        String heading = helpList.get(position).getHeading();
        String content = helpList.get(position).getContent();
        int img = helpList.get(position).getImage();

        holder.setData(heading, content, img);
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    public static class helpViewHolder extends RecyclerView.ViewHolder {
        private TextView heading, content;
        private ImageView helpImg;

        public helpViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.textView_helpHeading);
            content = itemView.findViewById(R.id.textView_helpHeading2);
            helpImg = itemView.findViewById(R.id.imageView_helpImg);
        }

        private void setData(String hd, String ct, int img) {
            heading.setText(hd);
            content.setText(ct);
            helpImg.setImageResource(img);
        }
    }
}
