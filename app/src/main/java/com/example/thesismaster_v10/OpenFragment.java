package com.example.thesismaster_v10;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thesismaster_v10.Adapter.QuestionAdapter;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OpenFragment extends Fragment implements DialogCloseListener {
    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionsAdapter;

    private List<QuestionModel> questionListOpen;
    private DatabaseHelper database;
    private ImageView infoImage;

    private FloatingActionButton questionfab;

    TextView questionText1, questionText2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionText1 = getView().findViewById(R.id.questionText1);
        questionText2 = getView().findViewById(R.id.questionText2);

        database = new DatabaseHelper(getActivity());
        database.openDatabase();

        questionListOpen = new ArrayList<>();

        questionRecyclerView = view.findViewById(R.id.questionRecyclerView1);

        questionRecyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));

        questionsAdapter = new QuestionAdapter(database, this);
        questionRecyclerView.setAdapter(questionsAdapter);
        String thesis = getArguments().getString("thesis");
        String theme = getArguments().getString("theme");

        questionText1.setText(thesis);
        questionText2.setText(theme);

        questionListOpen = database.getAllOpenQuestions(thesis, theme);
        Collections.reverse(questionListOpen);
        questionsAdapter.setQuestions(questionListOpen);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new QRecyclerItemTouchHelper(questionsAdapter));
        itemTouchHelper.attachToRecyclerView(questionRecyclerView);

        infoImage = getView().findViewById(R.id.infoImageOpen);

        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Anleitung zur Nutzung");
                builder.setMessage("- Notiz hinzufügen: Frage nach rechts wischen \n- Löschen: Frage nach links wischen \n- Als Erledigt markieren: Anklicken der Frage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Schließen des Dialogs
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        questionfab = view.findViewById(R.id.questionfab1);
        questionfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewQuestion addNewQuestion = new AddNewQuestion();
                Bundle bundleputnew = new Bundle();
                bundleputnew.putString("thesis", thesis);
                bundleputnew.putString("theme", theme);
                addNewQuestion.setArguments(bundleputnew);

                addNewQuestion.newInstance().show(getChildFragmentManager(), AddNewQuestion.TAG);
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        String thesis = getArguments().getString("thesis");
        String theme = getArguments().getString("theme");
        questionListOpen = database.getAllOpenQuestions(thesis, theme);
        Collections.reverse(questionListOpen);
        questionsAdapter.setQuestions(questionListOpen);
        questionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        String thesis = getArguments().getString("thesis");
        String theme = getArguments().getString("theme");
        questionListOpen = database.getAllOpenQuestions(thesis, theme);
        Collections.reverse(questionListOpen);
        questionsAdapter.setQuestions(questionListOpen);
        questionsAdapter.notifyDataSetChanged();

    }


}