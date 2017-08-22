package com.tmaprojects.tansik.io;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.io.Files;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.tmaprojects.tansik.model.TansikYear;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import timber.log.Timber;

/**
 * Created by tarekkma on 8/20/17.
 */

public class TansikLocal {


    private static TansikLocal INSTANCE;
    private Context context;

    private File path;
    private Charset charset = Charset.forName("UTF-8");

    public TansikLocal(Context context) {
        this.context = context;
        INSTANCE = this;
        path = new File(context.getFilesDir() , "TansikTables");
        if(!path.exists())path.mkdirs();
    }

    public static TansikLocal getInstance(){
        return INSTANCE;
    }

    public void saveYear(final String link,boolean force){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl(link);

        File file = new File(path,storageRef.getName());

        if(file.exists() && !force){
            Timber.d(storageRef.getName() + " already exists.");
            return;
        }

        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Timber.d("Saved '" + storageRef.getName() + "'");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e,"Can't save '"+ link + "' to the disk.");
            }
        });
    }


    public boolean checkYear(int year){
        File file = new File(path,String.valueOf(year) + ".json");
        return file.exists();
    }

    public @Nullable TansikYear getYear(int year){
        File file = new File(path,String.valueOf(year) + ".json");
        try {
            String json = Files.asCharSource(file,charset).read();
            return new Gson().fromJson(json,TansikYear.class);
        } catch (IOException e) {
            Timber.e(e,"Failed to read year " + year);
            return null;
        }
    }
}
