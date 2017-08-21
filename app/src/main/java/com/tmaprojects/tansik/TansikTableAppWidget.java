package com.tmaprojects.tansik;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.tmaprojects.tansik.io.TansikLocal;
import com.tmaprojects.tansik.model.TansikYear;
import com.tmaprojects.tansik.model.Track;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TansikTableAppWidgetConfigureActivity TansikTableAppWidgetConfigureActivity}
 */
public class TansikTableAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Pair<Integer,String> widgetData = TansikTableAppWidgetConfigureActivity.loadTansikPref(context, appWidgetId);
        if(widgetData==null)return;
        TansikYear year = TansikLocal.getInstance().getYear(widgetData.first);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tansik_table_app_widget);
        views.setTextViewText(R.id.appwidget_text,widgetData.first + " - " + widgetData.second);

        Intent intent = new Intent(context, ListWidgetService.class);

        if(widgetData.second.equalsIgnoreCase(Track.LITERATURE.name())){
            intent.putExtra(ListWidgetService.KEY_LIST_TABLE, new Gson().toJson(year.getLiteratureTable().getTable()));
        }else if(widgetData.second.equalsIgnoreCase(Track.SCIENCE.name())){
            intent.putExtra(ListWidgetService.KEY_LIST_TABLE, new Gson().toJson(year.getScienceTable().getTable()));
        }

        views.setRemoteAdapter(R.id.appwidget_list, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TansikTableAppWidgetConfigureActivity.deleteTansikPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

