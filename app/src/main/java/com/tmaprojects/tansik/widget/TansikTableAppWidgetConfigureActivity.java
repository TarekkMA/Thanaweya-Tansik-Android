package com.tmaprojects.tansik.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tmaprojects.tansik.R;
import com.tmaprojects.tansik.networking.FirebaseManager;

import java.util.List;

/**
 * The configuration screen for the {@link TansikTableAppWidget TansikTableAppWidget} AppWidget.
 */
public class TansikTableAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.tmaprojects.tansik.widget.TansikTableAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;



    ListView listView;

    public TansikTableAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTansikPref(Context context, int appWidgetId, int year, String track) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, year+"_"+track);
        prefs.apply();
    }

    static void deleteTansikPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    static Pair<Integer,String> loadTansikPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String data = prefs.getString(PREF_PREFIX_KEY + appWidgetId,"");
        if(data.isEmpty())return null;

        int year = Integer.parseInt(data.split("_")[0]);
        String track = data.split("_")[1];

        return new Pair<>(year,track);
    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.tansik_table_app_widget_configure);
        listView = (ListView) findViewById(R.id.yearList);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        final Context context = TansikTableAppWidgetConfigureActivity.this;

        FirebaseManager.getTansikYears(new FirebaseManager.TansikRequestListener() {
            @Override
            public void onTansikRetrived(final List<Integer> tansikYears) {
                ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                        context,android.R.layout.simple_list_item_1,tansikYears);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long i){
                        new MaterialDialog.Builder(context)
                                .title(R.string.select_track)
                                .items(R.array.tracks)
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                        saveTansikPref(context, mAppWidgetId, tansikYears.get(position),text.toString());

                                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                                        TansikTableAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                                        Intent resultValue = new Intent();
                                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                                        setResult(RESULT_OK, resultValue);
                                        finish();

                                        return true;
                                    }
                                })
                                .show();
                    }
                });
                listView.setAdapter(adapter);
            }

            @Override
            public void onError(String massage) {
                new MaterialDialog.Builder(TansikTableAppWidgetConfigureActivity.this)
                        .title(R.string.err_years_list)
                        .content(massage)
                        .positiveText(R.string.ok)
                        .show();
            }
        });
    }
}

