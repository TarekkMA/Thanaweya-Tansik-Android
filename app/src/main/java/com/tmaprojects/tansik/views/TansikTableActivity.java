package com.tmaprojects.tansik.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmaprojects.tansik.R;
import com.tmaprojects.tansik.utils.TansikFilter;
import com.tmaprojects.tansik.utils.Utils;
import com.tmaprojects.tansik.adapters.TansikTableAdapter;
import com.tmaprojects.tansik.io.TansikLocal;
import com.tmaprojects.tansik.model.TansikYear;
import com.tmaprojects.tansik.model.Track;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TansikTableActivity extends AppCompatActivity {

    @BindView(R.id.score_txt)
    TextView scoreText;
    @BindView(R.id.convert_btn)
    Button convertButton;
    @BindView(R.id.tansik_table_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.score_convert_layout)
    LinearLayout scoreConvertLayout;

    private static final String KEY_SCORE_DEGREES = "SCORE_DEGREES";
    private static final String KEY_YEAR = "YEAR";
    private static final String KEY_TRACK = "TRACK";
    private boolean viewAsPercent = false;


    private double scoreD;
    private double scoreP;
    private Track track;
    private int year;

    TansikYear tansikYear;
    TansikTableAdapter adapter;

    public static Bundle getBundle(int year, Track track, double scoreD){
        Bundle b = new Bundle();
        b.putDouble(KEY_SCORE_DEGREES,scoreD);
        b.putInt(KEY_YEAR,year);
        b.putSerializable(KEY_TRACK,track);
        return b;
    }

    private void extractBundle(){
        Bundle b = getIntent().getExtras();
        scoreD = b.getDouble(KEY_SCORE_DEGREES);
        scoreP = scoreD/410 * 100;
        track = (Track) b.get(KEY_TRACK);
        year = b.getInt(KEY_YEAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tansik_table);
        ButterKnife.bind(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extractBundle();

        tansikYear = TansikLocal.getInstance().getYear(year);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
        adapter = new TansikTableAdapter(tansikYear.getTable(track).getTable());
        recyclerView.setAdapter(adapter);

        if(scoreD>0) {
            scoreText.setText("Your Score : " + String.valueOf(Utils.round(scoreD, 2)));
            adapter.colorizeList(scoreD);
        }else{
            scoreConvertLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.convert_btn)
    void convertScore(Button btn){
        viewAsPercent = !viewAsPercent;
        btn.setText((viewAsPercent) ? "%" : "/");
        scoreText.setText("Your Score : " +
                ((viewAsPercent)?
                        String.valueOf(Utils.round(scoreD/410*100, 2)+"%") :
                        String.valueOf(Utils.round(scoreD, 2))));
        adapter.setPercent(viewAsPercent);
    }
}
