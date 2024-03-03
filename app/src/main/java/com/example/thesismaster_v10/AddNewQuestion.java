package com.example.thesismaster_v10;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddNewQuestion extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private TextInputEditText newQuestionTitle;
    private Button newQuestionButton;
    private TextView newQuestionViewTheme;
    private List<QuestionModel> questionListOpen;
    private List<QuestionModel> questionListAll;
    private List<QuestionModel> questionListDone;
    private int sizeAll;
    private int sizeDone;
    private DatabaseHandler MyDB;

    private DatabaseHelper database;

    public static AddNewQuestion newInstance() {

        return new AddNewQuestion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_question, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newQuestionTitle = getView().findViewById(R.id.newQuestionTitle_edit_text);
        newQuestionButton = getView().findViewById(R.id.newQuestionButton);
        newQuestionViewTheme = getView().findViewById(R.id.newQuestionViewTheme);

        database = new DatabaseHelper(getActivity());
        database.openDatabase();

        final Bundle bundle = getParentFragment().getArguments();
        if (bundle != null) {
            String theme = bundle.getString("theme");
            newQuestionViewTheme.setText(theme);

            assert theme != null;

        }
        newQuestionTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newQuestionButton.setEnabled(false);
                    newQuestionButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    newQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttondisable));
                } else {
                    newQuestionButton.setEnabled(true);
                    newQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.marine));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questiontext = newQuestionTitle.getText().toString();
                final Bundle bundle = getParentFragment().getArguments();
                assert bundle != null;
                String thesis = bundle.getString("thesis");
                String theme = bundle.getString("theme");

                    QuestionModel question = new QuestionModel();
                    question.setQuestion(questiontext);
                    question.setStatus(0);
                    question.setThesis(thesis);
                    question.setTheme(theme);
                    question.setQnote("");

                    database.insertQuestion(question);

                questionListAll = database.getAllThesisQuestions(thesis);
                sizeAll = questionListAll.size();
                questionListDone = database.getAllDoneThesisQuestions(thesis);
                sizeDone = questionListDone.size();
                int percent = (sizeDone * 100)/sizeAll;
                MyDB = new DatabaseHandler(getContext());
                MyDB.openDatabase();
                int checkedStars = MyDB.checkStars(thesis);

                if ((percent >= 80) && (percent <= 99))  {
                    if (checkedStars == 5) {
                        MyDB.updateStars(thesis, 4);
                    }
                } else if ((percent >= 60) && (percent <= 79)) {
                    switch (checkedStars) {
                        case (5):
                            MyDB.updateStars(thesis, 4);
                        case (4):
                            MyDB.updateStars(thesis, 3);
                    }
                } else if ((percent >= 40) && (percent <= 59)) {
                    switch (checkedStars) {
                        case (5):
                            MyDB.updateStars(thesis, 4);
                        case (4):
                            MyDB.updateStars(thesis, 3);
                        case (3):
                            MyDB.updateStars(thesis, 2);
                    }
                } else if ((percent >= 20) && (percent <= 39)) {
                    switch (checkedStars) {
                        case (5):
                            MyDB.updateStars(thesis, 4);
                        case (4):
                            MyDB.updateStars(thesis, 3);
                        case (3):
                            MyDB.updateStars(thesis, 2);
                        case (2):
                            MyDB.updateStars(thesis, 1);
                    }
                } else if ((percent > 0) && (percent <= 19)) {
                    switch (checkedStars) {
                        case (5):
                            MyDB.updateStars(thesis, 4);
                        case (4):
                            MyDB.updateStars(thesis, 3);
                        case (3):
                            MyDB.updateStars(thesis, 2);
                        case (2):
                            MyDB.updateStars(thesis, 1);
                        case (1):
                            MyDB.updateStars(thesis, 0);
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogCloseListener) {
            ((DialogCloseListener) parentFragment).handleDialogClose(dialog);
        }
    }


}