package com.example.thesismaster_v10;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thesismaster_v10.Adapter.QuestionAdapterDone;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DoneFragment extends Fragment {

    private RecyclerView questionRecyclerView;
    private QuestionAdapterDone questionsAdapterDone;

    private List<QuestionModel> questionListDone;
    private DatabaseHelper database;

    TextView questionText3, questionText4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionText3 = getView().findViewById(R.id.questionText3);
        questionText4 = getView().findViewById(R.id.questionText4);

        database = new DatabaseHelper(getActivity());
        database.openDatabase();

        questionListDone = new ArrayList<>();

        questionRecyclerView = view.findViewById(R.id.questionRecyclerView2);

        questionRecyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));

        questionsAdapterDone = new QuestionAdapterDone(database, this);
        questionRecyclerView.setAdapter(questionsAdapterDone);
        String thesis = getArguments().getString("thesis");
        String theme = getArguments().getString("theme");

        questionText3.setText(thesis);
        questionText4.setText(theme);

        questionListDone = database.getAllDoneQuestions(thesis, theme);
        Collections.reverse(questionListDone);
        questionsAdapterDone.setQuestions(questionListDone);



        questionRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                questionsAdapterDone.notifyDataSetChanged();

            }
        });
        /*QuestionModel question = new QuestionModel();
        question.setQuestion("Frage Nummer 1");
        question.setStatus(0);
        question.setId(1);

        questionList.add(question);
        questionList.add(question);
        questionList.add(question);
        questionList.add(question);
        questionList.add(question);
        */


    }

    @Override
    public void onResume() {
        super.onResume();
        String thesis = getArguments().getString("thesis");
        String theme = getArguments().getString("theme");
        questionListDone = database.getAllDoneQuestions(thesis, theme);
        Collections.reverse(questionListDone);
        questionsAdapterDone.setQuestions(questionListDone);
        questionsAdapterDone.notifyDataSetChanged();
    }
}


