package com.example.android.booklisting;


import android.app.Activity;
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

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description);
        descriptionTextView.setText(currentBook.getDescription());

        TextView moreInfo = (TextView) listItemView.findViewById(R.id.more_info);
        moreInfo.setText(R.string.more_info);

        return listItemView;
    }
}