package com.mpscexams.bhajaneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private RecyclerView myLyricList;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myRecyclerViewAdapter;
    SQLiteDatabase db;
    int itemCount;
    ArrayList<Integer> idArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle(R.string.app_name_marathi);

        getSupportActionBar().setLogo(R.mipmap.logo);




        Intent i = getIntent();
        this.idArray = i.getIntegerArrayListExtra("listTextIds");

        myLyricList = findViewById(R.id.myLyricList);
        myLyricList.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(this);
        myLyricList.setLayoutManager(myLayoutManager);

        myRecyclerViewAdapter = new MyLyricListAdapter( i.getStringArrayListExtra("list"),
                i.getStringArrayListExtra("listText"),
                i.getIntegerArrayListExtra("listTextIds") );
        itemCount = myRecyclerViewAdapter.getItemCount();

        myLyricList.setAdapter(myRecyclerViewAdapter);
    }

    public void openWhatsapp(){
        /*
        Method to Send specific method to specific mobile number in whatsapp
         */
        String smsNumber = "918411998115"; //without '+'
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
//            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch(Exception e) {
            Toast.makeText(this, "Whatsapp is not installed" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        Method to handle the click events of Menuitems in navbar
         */

        switch (item.getItemId()){

            case R.id.rateApp:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                break;

            case R.id.shareApp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.whatsApp:
                openWhatsapp();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLyrics(View view) {

        TextView v = view.findViewById(R.id.item);


        Log.v("abcdef", "position: "+view.getTag().toString());
        DatabaseConnectivity connect = new DatabaseConnectivity(getApplicationContext());
        db = connect.connecWithDb();
        String sql = "select * from arati where title LIKE " + "\"" + v.getText() +"\"";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        String data = cursor.getString(cursor.getColumnIndex("img"));

        String pos = view.getTag().toString().split(":")[0];
        String fav = view.getTag().toString().split(":")[1];
        Intent i = new Intent(ListActivity.this, DataDisplayActivity.class);
        i.putExtra("tag", data);
        i.putExtra("position", Integer.parseInt(pos));
        i.putExtra("favourite", Integer.parseInt(fav));//id
        i.putExtra("favIds", idArray);
        i.putExtra("count",itemCount);
        startActivity(i);


    }
}
