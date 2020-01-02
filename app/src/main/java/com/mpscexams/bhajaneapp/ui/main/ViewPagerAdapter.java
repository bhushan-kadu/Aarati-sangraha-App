package com.mpscexams.bhajaneapp.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mpscexams.bhajaneapp.DatabaseConnectivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    String[] params;
    Context mContext;
    SQLiteDatabase db;
    ArrayList<String> dataset;
    ArrayList<String> titleSet;



    public ViewPagerAdapter( String selectedItem, Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        DatabaseConnectivity connect = new DatabaseConnectivity(mContext);
        db = connect.connecWithDb();
        String sql ="select * from arati where img LIKE " + "\"" + selectedItem +"\"";
//        context.get
        Cursor cursor = db.rawQuery(sql, null);

        dataset = new ArrayList<String>();
        titleSet = new ArrayList<String>();
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("text"));
                String data1 = cursor.getString(cursor.getColumnIndex("title"));
                dataset.add(data);
                titleSet.add(data1);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        return LyricsDisplayFragment.newInstance(dataset.get(position),titleSet.get(position));
    }

    @Override
    public int getCount() {
        return dataset.size();
    }
}
