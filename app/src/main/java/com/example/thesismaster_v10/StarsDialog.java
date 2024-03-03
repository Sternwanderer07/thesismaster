package com.example.thesismaster_v10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.ThesisModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class StarsDialog extends BottomSheetDialogFragment {

    private TextView starsView;

    public static StarsDialog newInstance(){
        return new StarsDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_stars_dialog, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        starsView = getView().findViewById(R.id.starsView);

        final Bundle bundle = getArguments();
        if(bundle != null){
            int starsWin = bundle.getInt("starswin");
            if (starsWin == 5){
                starsView.setText("Du bist der Wahnsinn! \n 100% deiner Arbeit sind geprüft!");
            }else if (starsWin == 4){
                starsView.setText("Fast geschafft! \n 80% deiner Arbeit sind geprüft!");
            }else if (starsWin == 3){
                starsView.setText("Weiter so! \n 60% deiner Arbeit sind geprüft!");
            }else if (starsWin == 2){
                starsView.setText("Du bist auf einem guten Weg! \n 40% deiner Arbeit sind geprüft!");
            }else if (starsWin == 1){
                starsView.setText("Super! \n 20% deiner Arbeit sind geprüft!");
            }

        }

    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

}