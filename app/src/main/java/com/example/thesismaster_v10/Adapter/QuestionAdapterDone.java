package com.example.thesismaster_v10.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesismaster_v10.AddNewQuestion;
import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.DoneFragment;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.R;

import java.util.List;

public class QuestionAdapterDone extends RecyclerView.Adapter<QuestionAdapterDone.ViewHolder> {
    private List<QuestionModel> questionListDone;
    private List<QuestionModel> questionListD;
    private DoneFragment activity;
    private List<QuestionModel> questionListAll;
    private int sizeAll;
    private int sizeDone;
    private DatabaseHandler MyDB;
    private DatabaseHelper database;

    public QuestionAdapterDone(DatabaseHelper database, DoneFragment activity){
        this.database = database;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        database.openDatabase();
        QuestionModel item = questionListDone.get(position);
        holder.question.setText(item.getQuestion());
        holder.question.setChecked(toBoolean(item.getStatus()));

        holder.question.setOnCheckedChangeListener(null);
        holder.question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    database.updateStatus(item.getId(), 0);
                    questionListDone.remove(holder.getLayoutPosition());
                    notifyItemRemoved(holder.getLayoutPosition());

                    questionListAll = database.getAllThesisQuestions(item.getThesis());
                    sizeAll = questionListAll.size();
                    questionListD = database.getAllDoneThesisQuestions(item.getThesis());
                    sizeDone = questionListD.size();
                    int percent = (sizeDone * 100) / sizeAll;
                    MyDB = new DatabaseHandler(getContext());
                    MyDB.openDatabase();
                    int checkedStars = MyDB.checkStars(item.getThesis());

                    if ((percent >= 80) && (percent <= 99))  {
                            if (checkedStars == 5) {
                                MyDB.updateStars(item.getThesis(), 4);
                            }


                    } else if ((percent >= 60) && (percent <= 79)) {
                        switch (checkedStars) {
                            case (5):
                                MyDB.updateStars(item.getThesis(), 4);
                            case (4):
                                MyDB.updateStars(item.getThesis(), 3);

                        }

                    } else if ((percent >= 40) && (percent <= 59)) {
                        switch (checkedStars) {
                            case (5):
                                MyDB.updateStars(item.getThesis(), 4);
                            case (4):
                                MyDB.updateStars(item.getThesis(), 3);
                            case (3):
                                MyDB.updateStars(item.getThesis(), 2);
                        }
                    } else if ((percent >= 20) && (percent <= 39)) {
                        switch (checkedStars) {
                            case (5):
                                MyDB.updateStars(item.getThesis(), 4);
                            case (4):
                                MyDB.updateStars(item.getThesis(), 3);
                            case (3):
                                MyDB.updateStars(item.getThesis(), 2);
                            case (2):
                                MyDB.updateStars(item.getThesis(), 1);
                        }
                    } else if ((percent > 0) && (percent <= 19)) {
                        switch (checkedStars) {
                            case (5):
                                MyDB.updateStars(item.getThesis(), 4);
                            case (4):
                                MyDB.updateStars(item.getThesis(), 3);
                            case (3):
                                MyDB.updateStars(item.getThesis(), 2);
                            case (2):
                                MyDB.updateStars(item.getThesis(), 1);
                            case (1):
                                MyDB.updateStars(item.getThesis(), 0);
                        }
                    }
                }
            }
        });

    }

    public int getItemCount(){
        return questionListDone.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setQuestions(List<QuestionModel> questionListDone){
        this.questionListDone = questionListDone;
        notifyDataSetChanged();
    }

    public void editItem(int position){
        QuestionModel item = questionListDone.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("question", item.getQuestion());
        AddNewQuestion fragment = new AddNewQuestion();
        fragment.setArguments(bundle);
        fragment.show(fragment.getFragmentManager(), AddNewQuestion.TAG);
    }
    public void deleteItem(int position) {
        QuestionModel item = questionListDone.get(position);
        database.deleteTask(item.getId());
        questionListDone.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext() {
        return activity.getContext();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox question;

        ViewHolder(View view){
            super(view);
            question = view.findViewById(R.id.questionCheckBox);
        }
    }


}
