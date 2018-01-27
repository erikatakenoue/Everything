package jp.shiningplace.erika.takenoue.everything;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AllbookAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Book> mAllList;
    private Context mContext;

    public AllbookAdapter(Context context) {
        mContext =context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setTaskList(List<Book> taskList) {
        mAllList = taskList;
    }


    @Override
    public int getCount() {
        return mAllList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_allbook, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView textView2 = (TextView) convertView.findViewById(R.id.authorTextView);
        ImageView imageview = (ImageView)convertView.findViewById(R.id.imageView2);

        Picasso.with(mContext).load(mAllList.get(position).getLargeImageUrl()).into(imageview);

        textView1.setText(mAllList.get(position).getTitle());
        textView2.setText(mAllList.get(position).getAuthor());

        return convertView;
    }
}