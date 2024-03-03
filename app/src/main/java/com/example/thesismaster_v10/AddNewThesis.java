package com.example.thesismaster_v10;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.Model.ThesisModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewThesis extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private TextInputEditText newThesisTitle, newThesisDate;
    private TextView newThesisView;
    private Button newThesisBtn;
    private final Calendar myCalendar= Calendar.getInstance();

    private DatabaseHandler MyDB;
    private DatabaseHelper Database;

    public static AddNewThesis newInstance(){
        return new AddNewThesis();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_thesis, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newThesisTitle = getView().findViewById(R.id.newThesisTitle_edit_text);
        newThesisDate = getView().findViewById(R.id.newThesisDate_edit_text);
        newThesisBtn = getView().findViewById(R.id.newThesisButton);
        newThesisView = getView().findViewById(R.id.newThesisView);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String thesis = bundle.getString("thesis");
            String duedate = bundle.getString("duedate");
            newThesisView.setText("Vorhandene Arbeit bearbeiten");
            newThesisTitle.setText(thesis);
            newThesisDate.setText(duedate);

            assert thesis != null;

        }
        MyDB = new DatabaseHandler(getActivity());
        MyDB.openDatabase();
        Database = new DatabaseHelper(getActivity());
        Database.openDatabase();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        newThesisDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        newThesisTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newThesisBtn.setEnabled(false);
                    newThesisBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    newThesisBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.buttondisable));
                }
                else{
                    newThesisBtn.setEnabled(true);
                    newThesisBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.marine));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newThesisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String thtitle = newThesisTitle.getText().toString();
                String ddate = newThesisDate.getText().toString();
                ThesisActivity thesisActivity = new ThesisActivity();
                String user = thesisActivity.getShareduser();
                if(finalIsUpdate){
                    MyDB.updateThesis(bundle.getInt("id"), thtitle);
                    MyDB.updateDueDate(bundle.getInt("id"), ddate);
                }
                else {
                    Boolean checkthesis = MyDB.checkthesis(thtitle);
                    if (!checkthesis){

                        ThesisModel thesis = new ThesisModel();
                        thesis.setThesis(thtitle);
                        thesis.setDuedate(ddate);
                        thesis.setUser(user);
                        thesis.setStars(0);
                        MyDB.insertThesis(thesis);

                        setGlobalQuestionsTheme1(thtitle, "Struktur der Arbeit");
                        setGlobalQuestionsTheme2(thtitle, "Methodisches Vorgehen der Arbeit");
                        setGlobalQuestionsTheme3(thtitle, "Schlussfolgerung und Mehrwert");
                    }
                    else {
                        Toast.makeText(getActivity(), "Abschlussarbeit bereits hinterlegt", Toast.LENGTH_SHORT).show();
                    }

                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
    private void updateLabel(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.GERMAN);
        newThesisDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void setGlobalQuestionsTheme1 (String thesis, String theme) {
        setOneQuestion ("Ist die Einleitung klar und präzise formuliert, um den Leser über den Inhalt und die Ziele der Arbeit zu informieren?", thesis, theme);
        setOneQuestion ("Wird in der Einleitung ausreichend Kontext für das Thema gegeben, um die Relevanz der Arbeit zu verdeutlichen?", thesis, theme);
        setOneQuestion ("Werden die Forschungsfragen oder Hauptziele der Arbeit deutlich dargelegt?", thesis, theme);
        setOneQuestion ("Wird der Leser ausreichend über den gesellschaftlichen, wissenschaftlichen oder praktischen Kontext des Themas informiert?", thesis, theme);
        setOneQuestion ("Wird klargestellt, warum das Thema aktuell und relevant ist und warum es wert ist, erforscht zu werden?", thesis, theme);
        setOneQuestion ("Wird deutlich gemacht, warum du persönlich an diesem Thema interessiert bist und was dich dazu motiviert hat, dieses Forschungsprojekt anzugehen?", thesis, theme);
        setOneQuestion ("Werden die konkreten Forschungsfragen oder Hypothesen aufgeführt, die du in deiner Arbeit beantworten oder testen wirst?", thesis, theme);
        setOneQuestion ("Werden die Hauptziele deiner Abschlussarbeit klar formuliert?", thesis, theme);
        setOneQuestion ("Wird ein Überblick über die Struktur der Arbeit gegeben, um dem Leser zu zeigen, was in den einzelnen Kapiteln erwartet wird?", thesis, theme);
        setOneQuestion ("Wird erklärt, wie die verschiedenen Abschnitte miteinander verbunden sind und wie sie zur Beantwortung der Forschungsfragen beitragen?", thesis, theme);
        setOneQuestion ("Wird verdeutlicht, welche Art von Leser du mit deiner Einleitung ansprichst?", thesis, theme);
        setOneQuestion ("Wird auf mögliche ethische Überlegungen oder potenzielle Auswirkungen deiner Forschung hingewiesen und wie du beabsichtigst, damit umzugehen?", thesis, theme);
        setOneQuestion ("Wird betont, wie du sicherstellst, dass deine Forschung ethischen Standards und Richtlinien entspricht?", thesis, theme);

        //setOneQuestion ("", thesis, theme);


    }

    public void setGlobalQuestionsTheme2 (String thesis, String theme) {
        setOneQuestion ("Wird kurz erläutert, warum du dich für die gewählte Forschungsmethodik entschieden hast und wie sie zur Beantwortung der Forschungsfragen beitragen wird?", thesis, theme);
        setOneQuestion ("Wird auf mögliche Alternativen zur gewählten Methodik hingewiesen und ihre Vor- und Nachteile diskutiert?", thesis, theme);
        setOneQuestion ("Werden die erwarteten Beiträge deiner Arbeit zur bestehenden Literatur und/oder zur Praxis in deinem Forschungsfeld verdeutlicht?", thesis, theme);
        setOneQuestion ("Welches Forschungsdesign wurde gewählt, um die Forschungsfragen zu beantworten? Ist es angemessen, um die gesteckten Ziele zu erreichen?", thesis, theme);
        setOneQuestion ("Gibt es mögliche Alternativen zum gewählten Forschungsdesign, und warum wurde dieses spezielle Design bevorzugt?", thesis, theme);
        setOneQuestion ("Wie wurde die Stichprobe für deine Forschung ausgewählt? Sind die ausgewählten Teilnehmer repräsentativ für die zu untersuchende Population?", thesis, theme);
        setOneQuestion ("Sind potenzielle Verzerrungen oder Einschränkungen bei der Stichprobenauswahl vorhanden, die die Validität der Ergebnisse beeinflussen könnten?", thesis, theme);
        setOneQuestion ("Welche Methoden wurden zur Datenerhebung verwendet (z. B. Umfragen, Interviews, Experimente)? Sind diese Methoden geeignet, um die erforderlichen Daten zu sammeln?", thesis, theme);
        setOneQuestion ("Wie wurden potenzielle Quellen von Verzerrungen oder Fehlern bei der Datenerhebung minimiert?", thesis, theme);
        setOneQuestion ("Wie könnten zukünftige Forschende von deinen Erfahrungen und Erkenntnissen bei der Anwendung dieser Methodik profitieren?", thesis, theme);
        setOneQuestion ("Werden die methodischen Entscheidungen und deren Einfluss auf die Ergebnisse kritisch reflektiert? Gibt es potenzielle Limitationen oder Schwächen in der Methodik?", thesis, theme);
        setOneQuestion ("Wie wurden die Ergebnisse der Datenanalyse interpretiert? Wurden mögliche Erklärungen für unerwartete oder widersprüchliche Ergebnisse angeboten?", thesis, theme);
        setOneQuestion ("Welche potenziellen Quellen von internen und externen Validitätsbedrohungen wurden berücksichtigt?", thesis, theme);
        setOneQuestion ("Welche Maßnahmen wurden ergriffen, um die Validität (Genauigkeit) und Reliabilität (Zuverlässigkeit) der verwendeten Forschungsinstrumente und Daten sicherzustellen?", thesis, theme);
        setOneQuestion ("Wurden alternative Analysemethoden in Erwägung gezogen und diskutiert?", thesis, theme);
        setOneQuestion ("Welche statistischen oder analytischen Methoden wurden angewendet, um die gesammelten Daten zu analysieren? Sind diese Methoden angemessen für die Art der Daten und die Forschungsfragen?", thesis, theme);

        //setOneQuestion ("", thesis, theme);
    }

    public void setGlobalQuestionsTheme3 (String thesis, String theme) {
        setOneQuestion ("Wird erklärt, welche Lücke in der Literatur oder im Wissen du mit deiner Arbeit zu schließen beabsichtigst?", thesis, theme);
        setOneQuestion ("Wird angedeutet, welche Art von Ergebnissen oder Schlussfolgerungen du in den späteren Abschnitten deiner Arbeit erwartest?", thesis, theme);
        setOneQuestion ("Wird erläutert, wer von deiner Arbeit profitieren könnte, sei es in der akademischen Community, in der Praxis oder in anderen relevanten Bereichen?", thesis, theme);
        setOneQuestion ("Wird eine ausgewogene Interpretation vorgenommen, die sowohl positive als auch negative Aspekte der Ergebnisse berücksichtigt?", thesis, theme);

        //setOneQuestion ("", thesis, theme);
    }

    public void setOneQuestion (String question, String thesis, String theme){
        QuestionModel questionModel = new QuestionModel();
        questionModel.setQuestion(question);
        questionModel.setStatus(0);
        questionModel.setThesis(thesis);
        questionModel.setTheme(theme);
        questionModel.setQnote("");
        Database.insertQuestion(questionModel);
    }
}
