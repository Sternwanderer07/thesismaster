package com.example.thesismaster_v10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.Model.ThesisModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class QEditActivity extends BottomSheetDialogFragment {

    public static final String TAG2 = "ActionBottomDialog";
    private TextView editQuestionView, editQuestionViewTheme;
    private TextInputEditText editQNote_edit_text;
    private Button editQuestionButton, shareQuestionButton;
    private String questiontitle, thesis, theme, qnote;

    private DatabaseHelper Database;

    public static QEditActivity newInstance() {
        return new QEditActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qedit, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editQuestionView = getView().findViewById(R.id.editQuestionView);
        editQuestionViewTheme = getView().findViewById(R.id.editQuestionViewTheme);
        editQNote_edit_text = getView().findViewById(R.id.editQNote_edit_text);
        editQuestionButton = getView().findViewById(R.id.editQuestionButton);
        shareQuestionButton = getView().findViewById(R.id.shareQuestionButton);

        Database = new DatabaseHelper(getActivity());
        Database.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String questiontitle = bundle.getString("question");
            String theme = bundle.getString("theme");
            String qnote = bundle.getString("qnote");

            editQuestionView.setText(questiontitle);
            editQuestionViewTheme.setText(theme);
            editQNote_edit_text.setText(qnote);
            assert qnote != null;

        }

        editQNote_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    editQuestionButton.setEnabled(false);
                    editQuestionButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    editQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttondisable));
                    shareQuestionButton.setEnabled(false);
                    shareQuestionButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    shareQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttondisable));
                } else {
                    editQuestionButton.setEnabled(true);
                    editQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.marine));
                    shareQuestionButton.setEnabled(true);
                    shareQuestionButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.marine));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        editQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editQNote_edit_text.getText().toString();
                String thesis = bundle.getString("thesis");

                if(finalIsUpdate){
                    Database.updateQnote(bundle.getInt("id"), text);
                }
                else {

                    QuestionModel question = new QuestionModel();
                    question.setQuestion(questiontitle);
                    question.setStatus(0);
                    question.setThesis(thesis);
                    question.setTheme(theme);
                    question.setQnote(text);
                    Database.insertQuestion(question);
                }

                dismiss();
            }

        });
        shareQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharethesis = bundle.getString("thesis");
                String shareqnote = editQNote_edit_text.getText().toString();
                String sharequestion = editQuestionView.getText().toString();

                if(finalIsUpdate){
                    Database.updateQnote(bundle.getInt("id"), shareqnote);
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = sharequestion + "\n" + shareqnote;
                String shareSub = "Frage zu " + sharethesis;
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "ShareVia"));


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