package com.tmaprojects.tansik.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmaprojects.tansik.R;
import com.tmaprojects.tansik.model.TableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarekkma on 8/21/17.
 */

public class ListWidgetService extends RemoteViewsService {
    public static final String KEY_LIST_TABLE = "list_table";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewFactory(getApplicationContext(), intent);
    }

    class RemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {


        private final Context context;
        private final Intent intent;
        private List<TableItem> items = new ArrayList<>();

        public RemoteViewFactory(Context context, Intent intent) {

            this.context = context;
            this.intent = intent;
        }

        @Override
        public void onCreate() {
            items = new Gson().fromJson(intent.getStringExtra(KEY_LIST_TABLE), new TypeToken<List<TableItem>>() {}.getType());
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            TableItem item = items.get(position);

            RemoteViews views =
                    new RemoteViews(context.getPackageName(), R.layout.tansik_table_item_widget);

            views.setTextViewText(R.id.collage_name, String.valueOf(item.getCollage()));
            views.setTextViewText(R.id.collage_score, String.valueOf(item.getScore()));

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}