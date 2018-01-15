package jp.shiningplace.erika.takenoue.everything;

import android.content.ClipData;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Book> mAllList;

    public DetailAdapter(Context context) {
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
            convertView = mLayoutInflater.inflate(R.layout.bookdetail_main, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(R.id.titleDetail);
        TextView textView2 = (TextView) convertView.findViewById(R.id.authorDetail);

        textView1.setText(mAllList.get(position).getTitle());
        textView2.setText(mAllList.get(position).getAuthor());

        return convertView;
    }
}