package com.mpscexams.bhajaneapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyLyricListAdapter extends RecyclerView.Adapter<MyLyricListAdapter.MyViewHolder> {

    public ArrayList<String> myDataset;
    public ArrayList<String> myTitleSet;
    public ArrayList<Integer> myidSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public TextView textViewText;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.item);
            textViewText = v.findViewById(R.id.itemText);
        }
    }

    public MyLyricListAdapter(ArrayList<String> dataset, ArrayList<String> titleSet, ArrayList<Integer> idSet ){

        myDataset = dataset;
        myTitleSet = titleSet;
        myidSet = idSet;

    }


    @Override
    public MyLyricListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a new view
        LayoutInflater li =  LayoutInflater.from(parent.getContext());
        View listItem = li.inflate(R.layout.my_text_view,parent,false);
        MyViewHolder vh = new MyViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyLyricListAdapter.MyViewHolder holder, int position) {

        holder.itemView.setTag(String.valueOf(position) + ":" + String.valueOf(myidSet.get(position)));
        String data = myDataset.get(position);
        String data1 = myTitleSet.get(position);
        holder.textView.setText(data);
        holder.textViewText.setText(data1.substring(0,50)+" .....");
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
