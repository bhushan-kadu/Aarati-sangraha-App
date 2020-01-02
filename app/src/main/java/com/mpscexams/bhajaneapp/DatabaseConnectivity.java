package com.mpscexams.bhajaneapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseConnectivity {

    Context context;
    public SQLiteDatabase db;
    DatabaseHelper dbManager;
    String DATABASE_NAME ="bhajan.db";
    public DatabaseConnectivity(Context context){
        this.context = context;
    }

    private static final String TAG ="abcdef" ;

    public SQLiteDatabase connecWithDb(){

        //=======Code For copying Existing Database file to system folder for use====//
        // Copying Existing Database into system folder
        try {

            String destPath = "/data/data/" + context.getPackageName()
                    + "/databases/data.db";

            File f = new File(destPath);
            if(!f.exists()){
                Log.v(TAG,"File Not Exist");
                InputStream in = context.getAssets().open("bhajan.db");
                OutputStream out = new FileOutputStream(destPath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.v("TAG","ioexeption");
            e.printStackTrace();
        }

        dbManager =  new DatabaseHelper(context);
        Log.v(TAG,"Database is there with version: "+dbManager.getReadableDatabase().getVersion());



         db = dbManager.getReadableDatabase();

        return db;
    }

    public void overWriteFile(){
        String destPath = "/data/data/" + context.getPackageName()
                + "/databases/data.db";

        File f = new File(destPath);
        if(f.exists()){
            f.delete();
            connecWithDb();
        }
    }

    public void createDatabase(){
        String parentPath = "/data/data/" + context.getPackageName()
                + "/databases/data.db";
        String path = context.getDatabasePath("data.db").getPath();

        File file = new File(parentPath);

        Log.v("TAG",String.valueOf(file.delete()));
        if (!file.exists()) {
            if (!file.mkdir()) {
                Log.w(TAG, "Unable to create database directory");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open(DATABASE_NAME);
            os = new FileOutputStream(path);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
