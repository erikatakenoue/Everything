package jp.shiningplace.erika.takenoue.everything;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<BookItem> {
    private LayoutInflater layoutInflater_;

    public BookAdapter(Context context, int textViewResourceId, List<BookItem> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookItem item = (BookItem) getItem(position);

        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.search_list, null);
        }

        TextView textView;
        textView = (TextView)convertView.findViewById(R.id.titleTextView2);
        textView.setText(item.getTitle());

        TextView textView2;
        textView2 = (TextView)convertView.findViewById(R.id.authorTextView2);
        textView2.setText(item.getAuthor());

        ImageView imageDetail = (ImageView)convertView.findViewById(R.id.imageView3);
        Picasso.with(getContext()).load(item.getLargeImageUrl()).into(imageDetail);

        return convertView;
    }
}
