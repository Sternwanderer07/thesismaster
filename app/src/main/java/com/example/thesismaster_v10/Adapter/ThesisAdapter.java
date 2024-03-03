package com.example.thesismaster_v10.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesismaster_v10.AddNewThesis;
import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.Model.ThesisModel;
import com.example.thesismaster_v10.R;
import com.example.thesismaster_v10.ThesisActivity;

import java.util.ArrayList;
import java.util.List;

public class ThesisAdapter extends RecyclerView.Adapter<ThesisAdapter.ViewHolder>{

    private List<ThesisModel> thesisList;
    private DatabaseHandler MyDB;

    private ThesisActivity activity;
    private static RecyclerViewClickListener recListener;




    public ThesisAdapter(DatabaseHandler MyDB, ThesisActivity activity, RecyclerViewClickListener recListener) {
        this.MyDB = MyDB;
        this.activity = activity;
        this.recListener = recListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thesis_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MyDB.openDatabase();

        final ThesisModel item = thesisList.get(position);
        holder.thesis.setText(item.getThesis());
        holder.duedate.setText(item.getDuedate());

        switch (item.getStars()){
            case 0:
                holder.stars.setImageResource(R.drawable.stars0);
                break;
            case 1:
                holder.stars.setImageResource(R.drawable.stars1);
                break;
            case 2:
                holder.stars.setImageResource(R.drawable.stars2);
                break;
            case 3:
                holder.stars.setImageResource(R.drawable.stars3);
                break;
            case 4:
                holder.stars.setImageResource(R.drawable.stars4);
                break;
            case 5:
                holder.stars.setImageResource(R.drawable.stars5);
                break;
        }

    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return thesisList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setThesis(List<ThesisModel> thesisList) {
        this.thesisList = thesisList;
        notifyDataSetChanged();
    }



    public void deleteItem(int position) {
        ThesisModel item = thesisList.get(position);
        MyDB.deleteThesis(item.getId());
        thesisList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ThesisModel item = thesisList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("thesis", item.getThesis());
        bundle.putString("duedate", item.getDuedate());
        AddNewThesis fragment = new AddNewThesis();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewThesis.TAG);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView thesis;
        TextView duedate;
        ImageView stars;


        ViewHolder(View view) {
            super(view);
            thesis = view.findViewById(R.id.thesisText1);
            duedate = view.findViewById(R.id.thesisText2);
            stars = view.findViewById(R.id.thesisImageStar);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recListener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

}

