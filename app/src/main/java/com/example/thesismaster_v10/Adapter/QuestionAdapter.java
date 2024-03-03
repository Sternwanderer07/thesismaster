package com.example.thesismaster_v10.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesismaster_v10.AddNewQuestion;
import com.example.thesismaster_v10.AddNewThesis;
import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.DialogCloseListener;
import com.example.thesismaster_v10.MainActivity;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.OpenFragment;
import com.example.thesismaster_v10.QEditActivity;
import com.example.thesismaster_v10.R;
import com.example.thesismaster_v10.StarsDialog;
import com.example.thesismaster_v10.ThesisActivity;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<QuestionModel> questionListOpen;
    private List<QuestionModel> questionListAll;
    private List<QuestionModel> questionListDone;
    private int sizeAll;
    private int sizeDone;
    private DatabaseHandler MyDB;

    private OpenFragment activity;

    private DatabaseHelper database;

    public QuestionAdapter(DatabaseHelper database, OpenFragment activity){
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
        QuestionModel item = questionListOpen.get(position);
        holder.question.setText(item.getQuestion());
        holder.question.setChecked(toBoolean(item.getStatus()));

        holder.question.setOnCheckedChangeListener(null);
        holder.question.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    database.updateStatus(item.getId(),1);
                    questionListOpen.remove(holder.getLayoutPosition());
                    notifyItemRemoved(holder.getLayoutPosition());

                    questionListAll = database.getAllThesisQuestions(item.getThesis());
                    sizeAll = questionListAll.size();
                    questionListDone = database.getAllDoneThesisQuestions(item.getThesis());
                    sizeDone = questionListDone.size();
                    int percent = (sizeDone * 100)/sizeAll;
                    MyDB = new DatabaseHandler(getContext());
                    MyDB.openDatabase();
                    int checkedStars = MyDB.checkStars(item.getThesis());

                    if ((percent >= 20) && (percent <= 39)) {
                        if (checkedStars == 0) {
                            MyDB.updateStars(item.getThesis(), 1);
                            showStarsDialog(1);

                        }
                    } else if ((percent >= 40) && (percent <= 59)) {
                        switch (checkedStars) {
                            case (0):
                                MyDB.updateStars(item.getThesis(), 1);
                                showStarsDialog(1);
                            case (1):
                                MyDB.updateStars(item.getThesis(), 2);
                                showStarsDialog(2);
                        }

                    } else if ((percent >= 60) && (percent <= 79)) {
                        switch (checkedStars) {
                            case (0):
                                MyDB.updateStars(item.getThesis(), 1);
                                showStarsDialog(1);
                            case (1):
                                MyDB.updateStars(item.getThesis(), 2);
                                showStarsDialog(2);
                            case (2):
                                MyDB.updateStars(item.getThesis(), 3);
                                showStarsDialog(3);

                        }

                    } else if ((percent >= 80) && (percent <= 99)) {
                        switch (checkedStars) {
                            case (0):
                                MyDB.updateStars(item.getThesis(), 1);
                                showStarsDialog(1);
                            case (1):
                                MyDB.updateStars(item.getThesis(), 2);
                                showStarsDialog(2);
                            case (2):
                                MyDB.updateStars(item.getThesis(), 3);
                                showStarsDialog(3);
                            case (3):
                                MyDB.updateStars(item.getThesis(), 4);
                                showStarsDialog(4);


                        }
                    } else if (percent == 100) {
                        switch (checkedStars) {
                            case (0):
                                MyDB.updateStars(item.getThesis(), 1);
                                showStarsDialog(1);
                            case (1):
                                MyDB.updateStars(item.getThesis(), 2);
                                showStarsDialog(2);
                            case (2):
                                MyDB.updateStars(item.getThesis(), 3);
                                showStarsDialog(3);
                            case (3):
                                MyDB.updateStars(item.getThesis(), 4);
                                showStarsDialog(4);
                            case (4):
                                MyDB.updateStars(item.getThesis(), 5);
                                showStarsDialog(5);


                        }
                    }

                }
            }

        });

    }
public void showStarsDialog(int stars){

        Bundle bundle = new Bundle();
        bundle.putInt("starswin", stars);

        StarsDialog starsDialog = new StarsDialog();
        starsDialog.setArguments(bundle);
        starsDialog.show(activity.getFragmentManager(), AddNewThesis.TAG);
}



    public int getItemCount(){
        return questionListOpen.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }
    public Context getContext() {
        return activity.getContext();
    }

    public void setQuestions(List<QuestionModel> questionListOpen){
        this.questionListOpen = questionListOpen;
        notifyDataSetChanged();
    }

    public void editItem(int position){
        QuestionModel item = questionListOpen.get(position);
        Bundle bundleputnew = new Bundle();
        bundleputnew.putInt("id", item.getId());
        bundleputnew.putString("question", item.getQuestion());
        bundleputnew.putString("theme", item.getTheme());
        bundleputnew.putString("thesis", item.getThesis());
        bundleputnew.putString("qnote", item.getQnote());

        QEditActivity qEditActivity = new QEditActivity();
        qEditActivity.setArguments(bundleputnew);
        qEditActivity.show(activity.getChildFragmentManager(), QEditActivity.TAG2);
    }

    public void deleteItem(int position) {
        QuestionModel item = questionListOpen.get(position);
        database.deleteTask(item.getId());
        questionListOpen.remove(position);
        notifyItemRemoved(position);

        questionListAll = database.getAllThesisQuestions(item.getThesis());
        sizeAll = questionListAll.size();
        questionListDone = database.getAllDoneThesisQuestions(item.getThesis());
        sizeDone = questionListDone.size();
        int percent = (sizeDone * 100)/sizeAll;
        MyDB = new DatabaseHandler(getContext());
        MyDB.openDatabase();
        int checkedStars = MyDB.checkStars(item.getThesis());

        if ((percent >= 20) && (percent <= 39)) {
            if (checkedStars == 0) {
                MyDB.updateStars(item.getThesis(), 1);
                showStarsDialog(1);

            }
        } else if ((percent >= 40) && (percent <= 59)) {
            switch (checkedStars) {
                case (0):
                    MyDB.updateStars(item.getThesis(), 1);
                    showStarsDialog(1);
                case (1):
                    MyDB.updateStars(item.getThesis(), 2);
                    showStarsDialog(2);
            }

        } else if ((percent >= 60) && (percent <= 79)) {
            switch (checkedStars) {
                case (0):
                    MyDB.updateStars(item.getThesis(), 1);
                    showStarsDialog(1);
                case (1):
                    MyDB.updateStars(item.getThesis(), 2);
                    showStarsDialog(2);
                case (2):
                    MyDB.updateStars(item.getThesis(), 3);
                    showStarsDialog(3);

            }

        } else if ((percent >= 80) && (percent <= 99)) {
            switch (checkedStars) {
                case (0):
                    MyDB.updateStars(item.getThesis(), 1);
                    showStarsDialog(1);
                case (1):
                    MyDB.updateStars(item.getThesis(), 2);
                    showStarsDialog(2);
                case (2):
                    MyDB.updateStars(item.getThesis(), 3);
                    showStarsDialog(3);
                case (3):
                    MyDB.updateStars(item.getThesis(), 4);
                    showStarsDialog(4);


            }
        } else if (percent == 100) {
            switch (checkedStars) {
                case (0):
                    MyDB.updateStars(item.getThesis(), 1);
                    showStarsDialog(1);
                case (1):
                    MyDB.updateStars(item.getThesis(), 2);
                    showStarsDialog(2);
                case (2):
                    MyDB.updateStars(item.getThesis(), 3);
                    showStarsDialog(3);
                case (3):
                    MyDB.updateStars(item.getThesis(), 4);
                    showStarsDialog(4);
                case (4):
                    MyDB.updateStars(item.getThesis(), 5);
                    showStarsDialog(5);


            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox question;

        ViewHolder(View view){
            super(view);
            question = view.findViewById(R.id.questionCheckBox);
        }
    }


}
