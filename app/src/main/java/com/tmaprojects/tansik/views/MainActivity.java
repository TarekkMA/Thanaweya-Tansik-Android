package com.tmaprojects.tansik.views;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tmaprojects.tansik.R;
import com.tmaprojects.tansik.icepick_bundlers.ArrayListBundler;
import com.tmaprojects.tansik.utils.Utils;
import com.tmaprojects.tansik.io.TansikLocal;
import com.tmaprojects.tansik.model.Track;
import com.tmaprojects.tansik.networking.FirebaseManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import icepick.Icepick;
import icepick.State;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private enum InputMethod{
        DEGREES,PERCENTAGE
    }

    final int MAX_SCORE = 410;
    double score;
    double scoreD = 0;

    @State
    int selectedPos = -1;
    @State(ArrayListBundler.class)
    List<Integer> yearList = new ArrayList<>();

    @BindView(R.id.converted_txt)
    TextView convertedScoreTextView;
    @BindView(R.id.year_spinner)
    Spinner yearSpinner;
    @BindView(R.id.score_edittxt)
    EditText socreEditText;
    @BindView(R.id.score_editlayout)
    TextInputLayout scoreInputLayout;

    InputMethod inputMethod = InputMethod.DEGREES;

    MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);



        if(savedInstanceState!=null){
            fillSpinnerData(yearList,selectedPos);
        }else{
            loadingDialog = new MaterialDialog.Builder(this)
                    .title(R.string.loading)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
            getTablesFromFirebase();
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


    private void getTablesFromFirebase(){
        FirebaseManager.getTansikYears(new FirebaseManager.TansikRequestListener() {
            @Override
            public void onTansikRetrived(List<Integer> tansikYears) {
                yearList = tansikYears;
                fillSpinnerData(yearList,0);
                loadingDialog.dismiss();
            }

            @Override
            public void onError(String massage) {
                Toast.makeText(MainActivity.this, massage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSpinnerData(List<Integer> yearList,int selectedPos){
        ArrayAdapter<Integer> adapter;
        adapter = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_text_layout, yearList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setSelection(selectedPos);
    }


    @OnCheckedChanged(R.id.degrees_radiobtn)
    void inputMethodChangedD(boolean c){
        if(c && inputMethod != InputMethod.DEGREES){
            inputMethod = InputMethod.DEGREES;
            scoreInputLayout.setHint(getString(R.string.degrees_hint));
            scoreChanged(socreEditText.getText());
        }
    }

    @OnCheckedChanged(R.id.percent_radiobtn)
    void inputMethodChangedP(boolean c){
        if(c && inputMethod != InputMethod.PERCENTAGE){
            inputMethod = InputMethod.PERCENTAGE;
            scoreInputLayout.setHint(getString(R.string.percentage_hint));
            scoreChanged(socreEditText.getText());
        }
    }

    @OnItemSelected(R.id.year_spinner)
    void yearSpinnerChanged(int pos){
        selectedPos = pos;
    }

    @OnTextChanged(R.id.score_edittxt)
    void scoreChanged(CharSequence s){
        scoreInputLayout.setErrorEnabled(false);


        //Fix formmat issues
        if(s.toString().isEmpty()) s="0";
        if(s.charAt(s.length()-1)=='.')s = s.toString() + '0';  // ex. 15. -> 15.0 , . -> .0

        try {
            score = Double.parseDouble(s.toString());
        } catch (Exception e) {
            Timber.e(e,"Error parsing score");
            scoreInputLayout.setError(getString(R.string.incorrect_format));
            scoreInputLayout.setErrorEnabled(true);
            return;
        }


        if(inputMethod == InputMethod.PERCENTAGE){
            if(score > 100){
                scoreInputLayout.setError(getString(R.string.err_exceed_100p));
                scoreInputLayout.setErrorEnabled(true);
                return;
            }
            scoreD = Utils.round(MAX_SCORE * score / 100.0,2);
            convertedScoreTextView.setText(String.valueOf(scoreD));

        }else if(inputMethod == InputMethod.DEGREES){
            if(score > MAX_SCORE){
                scoreInputLayout.setError(getString(R.string.err_exceed_410));
                scoreInputLayout.setErrorEnabled(true);
                return;
            }

            scoreD = score;
            convertedScoreTextView.setText(String.valueOf(Utils.round(score / MAX_SCORE * 100,3)) + " %");
        }
    }

    @OnClick({R.id.science_btn,R.id.literature_btn})
    void tansikButtonClicked(Button btn){
        TansikLocal localStore = TansikLocal.getInstance();
        if(selectedPos == -1){
            Toast.makeText(this, R.string.select_year_first, Toast.LENGTH_SHORT).show();
        }else if(!localStore.checkYear(yearList.get(selectedPos))){
            Toast.makeText(this, R.string.err_busy_downloding_tables, Toast.LENGTH_SHORT).show();
        }else{
            int year = yearList.get(selectedPos);
            Track track = (btn.getId()==R.id.science_btn) ? Track.SCIENCE : Track.LITERATURE;
            startActivity(new Intent(this,TansikTableActivity.class).putExtras(TansikTableActivity.getBundle(year,track,scoreD)));
        }
    }



}
