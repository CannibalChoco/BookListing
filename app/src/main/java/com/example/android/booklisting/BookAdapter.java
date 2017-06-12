package com.example.android.booklisting;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.list_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book currentBook = (Book) getItem(position);
        viewHolder.authorsTextView.setText(currentBook.getAuthor());
        viewHolder.descriptionTextView.setText(currentBook.getDescription());
        viewHolder.titleTextView.setText(currentBook.getTitle());
        // TODO: shouldn't set it every time, because it never changes
        viewHolder.moreInfoTextView.setText(R.string.more_info);

        return convertView;
    }

    class ViewHolder {
        private TextView authorsTextView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView moreInfoTextView;

        public ViewHolder( View view){
            this.authorsTextView = (TextView) view.findViewById(R.id.author);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
            this.descriptionTextView = (TextView) view.findViewById(R.id.description);
            this.moreInfoTextView = (TextView) view.findViewById(R.id.more_info);

        }

    }
}