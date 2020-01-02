package com.mpscexams.bhajaneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpscexams.bhajaneapp.ui.main.ViewPagerAdapter;

import java.util.ArrayList;

public class DataDisplayActivity extends AppCompatActivity {
TextView displayData;
String selectedItem;
int position;
String titleString, textString;
int count, favoId, favoriteState;
public String tag;
SQLiteDatabase db;
MenuItem favMenuItem;
int counter = 0, counter1 =0;
ArrayList<Integer> favoIdArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.app_name_marathi);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Button next = findViewById(R.id.next);
        Button previous = findViewById(R.id.previous);
        RelativeLayout rl = findViewById(R.id.navigation);
        final ViewPager vp = findViewById(R.id.lyricViewPager);


supportInvalidateOptionsMenu();
invalidateOptionsMenu();
        Intent i = getIntent();
        selectedItem = i.getStringExtra("tag");
        position = i.getIntExtra("position", 1);
        count = i.getIntExtra( "count", 1);
        favoId = i.getIntExtra( "favourite", 1);
        favoIdArray = i.getIntegerArrayListExtra( "favIds");


        DatabaseConnectivity connect = new DatabaseConnectivity(this);
        db = connect.connecWithDb();
        this.favoriteState = getFavStateFromFavId(favoIdArray.get(position));

//        checkAndSetFav(favoId);

        if(count > 1){
            rl.setVisibility(View.VISIBLE);
        }

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {

//                position = pos;
                    if(counter1 > 0) {
                        checkAndSetFav(favoIdArray.get(pos));
                        position = pos;
                    }
                    counter1++;
            }

            @Override
            public void onPageSelected(int pos) {

//                position = pos;
//                if(counter > 0){
//                    checkAndSetFav(favoIdArray.get(pos));
//
//                }
//                counter++;

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(selectedItem,this, getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(position);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(vp.getCurrentItem() == (vp.getAdapter().getCount()-1)){
                 return;
                }else{
                    vp.setCurrentItem(position+1);
                    checkAndSetFav(favoIdArray.get(position+1));
                    position+=1;
//                    supportInvalidateOptionsMenu();

                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(vp.getCurrentItem() == 0){
                    return;
                }else{
                    vp.setCurrentItem(position-1);
                    checkAndSetFav(favoIdArray.get(position-1));
                    position-=1;
//                    supportInvalidateOptionsMenu();

                }
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        favMenuItem = menu.findItem(R.id.favouriteItem);
        if(this.favoriteState == 0) favMenuItem.setIcon(getResources().getDrawable(R.drawable.favnotselected));
        else if(this.favoriteState > 0)favMenuItem.setIcon(getResources().getDrawable(R.drawable.favselected));
        return super.onPrepareOptionsMenu(menu);

    }

    public void checkAndSetFav(int favId){

        this.favoriteState = getFavStateFromFavId(favId);

        if(this.favoriteState == 0){
            favMenuItem.setIcon(getResources().getDrawable(R.drawable.favnotselected));
        } else if(this.favoriteState > 0){
            favMenuItem.setIcon(getResources().getDrawable(R.drawable.favselected));
        }
    }

    public int getFavStateFromFavId(int favoId){
        String sql = "select * from arati where id=" + favoId;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex("fav"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_display_activity_menu, menu);

        this.favMenuItem = menu.findItem(R.id.favouriteItem);
        if(this.favoriteState == 0) favMenuItem.setIcon(getResources().getDrawable(R.drawable.favnotselected));
        else if(this.favoriteState > 0)favMenuItem.setIcon(getResources().getDrawable(R.drawable.favselected));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
        Method to handle the click events of Menuitems in navbar
         */

        switch (item.getItemId()){

            case R.id.favouriteItem:
                if(this.favoriteState == 0){
                    this.favoriteState = 1;
                    item.setIcon(getResources().getDrawable(R.drawable.favselected));
                }
                else if(this.favoriteState > 0){
                    this.favoriteState = 0;
                    item.setIcon(getResources().getDrawable(R.drawable.favnotselected));
                }

                ContentValues cv = new ContentValues();
                cv.put("fav", favoriteState);
                db.update("arati", cv, "id="+favoIdArray.get(position), null);
                break;

            case R.id.shareContent:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "*"+getTitleString()+"*\n"+getTextString()+" \n\n *यासारख्या अजून आरत्यांचा संग्रह पाहण्या करीता खालील लिंक वर क्लिक करा*\n: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case android.R.id.home:
                this.finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);

    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String str) {
         this.titleString = str;
    }

    public void setTextString(String str) {
        this.textString = str;
    }
    public String getTextString() {
        return textString;
    }
}
