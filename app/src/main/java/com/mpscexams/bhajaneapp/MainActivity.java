package com.mpscexams.bhajaneapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "abcdef";
    public SQLiteDatabase db;
    DatabaseHelper dbManager;
    GridLayout mainGrid;
    float density;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.gg);
        setTitle(R.string.app_name_marathi);





        context = getApplicationContext();
        density = context.getResources()
                .getDisplayMetrics()
                .density;
        DatabaseConnectivity connect = new DatabaseConnectivity(this);

        mainGrid = findViewById(R.id.mainGrid);
        connecWithDb();

        String sql = "select * from Bhagwans";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        for(int i = 0; i<6; i++){
            for(int j = 0; j<3; j++){

                if(!cursor.isAfterLast()){
                    String imageName = cursor.getString(cursor.getColumnIndex("img"));
                    String bhagwanName = cursor.getString(cursor.getColumnIndex("title"));

                    try {
                        addCard(imageName, bhagwanName, i, j);//Add a new card created using data
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cursor.moveToNext();
                }else break;
            }
        }
        cursor.close();
    }

    public void openWhatsapp(){
        /*
        Method to Send specific method to specific mobile number in whatsapp
         */
        String smsNumber = "917756971964"; //without '+'
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "*आरत्यांचा संग्रह पाहण्या करीता खालील लिंक वर क्लिक करा*: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
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
                        "आरत्यांचा संग्रह पाहण्या करीता खालील लिंक वर क्लिक करा: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.whatsApp:
                openWhatsapp();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        /*
        back Pressed event on this activity to show confirm dialogue box before exit
         */

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit MPSCExams App ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
//        super.onBackPressed();
    }

    public void addButton(String text, int index, int row, int column){
        Button newBtn = new Button(getApplicationContext());
        newBtn.setText(text);

        mainGrid.addView(newBtn, index);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();

        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setGravity(Gravity.FILL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.columnSpec = GridLayout.spec(column,1f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.rowSpec = GridLayout.spec(row,1f);
        }
        newBtn.setLayoutParams(layoutParams);

    }

    public void addCard(final String imageName, final String bhagwanName, int row, int column) throws IOException {
        final Context context = getApplicationContext();
        CardView card = (CardView) LayoutInflater.from(this).inflate(R.layout.card_view,null );
        card.setClickable(true);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql ="";
                if(bhagwanName.equals("आवडते (Favourite)")){
                    sql = "select * from arati where fav > 0 ";
                }else{
                    sql = "select * from arati where img LIKE " + "\"" +imageName +"\"";
                }
                Cursor cursor = db.rawQuery(sql, null);

                ArrayList<String> dataset = new ArrayList<String>();
                ArrayList<String> titleset = new ArrayList<String>();
                ArrayList<Integer> textIdSet = new ArrayList<Integer>();
                if (cursor.moveToFirst()){
                    do{
                        String data = cursor.getString(cursor.getColumnIndex("title"));
                        String data1 = cursor.getString(cursor.getColumnIndex("text"));
                        int textId = cursor.getInt(cursor.getColumnIndex("id"));
                        dataset.add(data);
                        titleset.add(data1);
                        textIdSet.add(textId);
                    }while(cursor.moveToNext());
                }
                cursor.close();

                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("list", dataset);
                i.putExtra("listText", titleset);
                i.putExtra("listTextIds", textIdSet);

                startActivity(i);
            }
        });

        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.CENTER);
        llayout.setOrientation(LinearLayout.VERTICAL);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins((int) density * 10, (int) density * 15, (int) density * 5, (int) density * 15);
        layoutParams.setGravity(Gravity.FILL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.columnSpec = GridLayout.spec(column,1f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.rowSpec = GridLayout.spec(row,1f);
        }

//        card.setContentPadding((int) density * 5, (int) density * 5, (int) density * 5, (int) density * 5);
        card.setPadding((int) density * 5, (int) density * 5, (int) density * 5, (int) density * 5);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            card.setForeground( getDrawableFromAttrRes(R.attr.selectableItemBackgroundBorderless,this));
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            card.setForeground(getResources().getDrawable(R.drawable.ripple_drawable));
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            card.setBackground(getDrawable(R.color.appricot));
//        }
//        card.setRadius(9f);


        card.setLayoutParams(layoutParams);

        ImageView image = new ImageView(context);
        image.setTag(imageName);
        AssetManager assetManager = getAssets();
        InputStream is = assetManager.open("images/"+imageName+".jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        image.setImageBitmap(bitmap);
        image.setPadding((int) density * 5, (int) density * 5, (int) density * 5, (int) density * 0);

        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams((int)density * 140, (int)density * 140);

        cp.gravity = Gravity.CENTER;

        image.setLayoutParams(cp);//add image to cardview

        TextView textView = new TextView(this);
        textView.setText(bhagwanName);
        textView.setTextSize(15f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding((int) density * 0, (int) density * 5, (int) density * 0, (int) density * (int)1.5);
        cp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cp.gravity = Gravity.CENTER;
        textView.setLayoutParams(cp);

        llayout.addView(image);
        llayout.addView(textView);

        card.addView(llayout);

        mainGrid.addView(card);//add card view to grid





    }

    public Drawable getDrawableFromAttrRes(int attrRes, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] { attrRes });
        try
        {
            return a.getDrawable(0);
        }
        finally
        {
            a.recycle();
        }
    }

    public void connecWithDb(){

        //=======Code For copying Existing Database file to system folder for use====//
        // Copying Existing Database into system folder
        try {

            String destPath = "/data/data/" + getPackageName()
                    + "/databases/data.db";

            File f = new File(destPath);
            if(!f.exists()){
                Log.v(TAG,"File Not Exist");
                InputStream in = getAssets().open("bhajan.db");
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

        dbManager =  new DatabaseHelper(this);
        Log.v(TAG,"Database is there with version: "+dbManager.getReadableDatabase().getVersion());



        db = dbManager.getReadableDatabase();

    }
}
