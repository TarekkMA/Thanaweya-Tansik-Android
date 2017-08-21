package com.tmaprojects.tansik.networking;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tmaprojects.tansik.io.TansikLocal;
import com.tmaprojects.tansik.model.Table;
import com.tmaprojects.tansik.model.TansikYear;
import com.tmaprojects.tansik.model.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import timber.log.Timber;

/**
 * Created by tarekkma on 8/19/17.
 */

public class FirebaseManager {

    private static final String TANSIK_YEARS_KEY = "tansik_years";
    private static final String TANSIK_YEAR_KEY = "year";
    private static final String TANSIK_LINK_KEY = "link";

    public interface TansikRequestListener {
        void onTansikRetrived(List<Integer> tansikYears);

        void onError(String massage);
    }

    public static void getTansikYears(final TansikRequestListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference yearsRef = database.getReference().child(TANSIK_YEARS_KEY);
        yearsRef.keepSynced(true);
        yearsRef.orderByChild(TANSIK_YEAR_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Integer> years = new ArrayList<Integer>();
                List<String> links = new ArrayList<String>();
                for (DataSnapshot yearSnap : dataSnapshot.getChildren()) {
                    int year = yearSnap.child(TANSIK_YEAR_KEY).getValue(Integer.class);
                    String link = yearSnap.child(TANSIK_LINK_KEY).getValue(String.class);
                    years.add(year);
                    links.add(link);
                }
                Collections.reverse(years);
                listener.onTansikRetrived(years);
                startDownloadingTansikData(links);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.toException(),"Failed to fetch TansikYears");
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public static void startDownloadingTnasikYear(final String id){
        DatabaseReference yearRef = FirebaseDatabase.getInstance().getReference().child(TANSIK_YEARS_KEY).child(id);
        yearRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int year = snapshot.child(TANSIK_YEAR_KEY).getValue(Integer.class);
                String link = snapshot.child(TANSIK_LINK_KEY).getValue(String.class);
                if(TextUtils.isEmpty(link)){
                    Timber.e("Link is empty :(");
                }else {
                    TansikLocal.getInstance().saveYear(link, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.toException(),"Error downloading year with id" + id);
            }
        });

    }

    private static void startDownloadingTansikData(List<String> links){
        for (String link : links) {
            TansikLocal.getInstance().saveYear(link,false);
        }
    }
}
