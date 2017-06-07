package com.example.android.booklisting;


import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter{

    public BookAdapter(Activity context, ArrayList<Book> books){
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate
                    (R.layout.list_item, parent, false);
        }

        Book currentBook = (Book) getItem(position);

        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.author);
        authorsTextView.setText(currentBook.getAuthor());

        TextView titleTxtView = (TextView) listItemView.findViewById(R.id.title);
        titleTxtView.setText(currentBook.getTitle());

        return listItemView;
    }
}