package jp.shiningplace.erika.takenoue.everything;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BorrowAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Borrow> mBorrowList;

    public BorrowAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setBorrowList(List<Borrow> borrowList) {
        mBorrowList = borrowList;
    }

    @Override
    public int getCount() {
        return mBorrowList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBorrowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mBorrowList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.borrow_listitem, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(R.id.borrow1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.borrow2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.borrow3);

        textView1.setText(mBorrowList.get(position).getBorrowtitle());
        textView2.setText(mBorrowList.get(position).getBorrowauthor());
        textView3.setText(mBorrowList.get(position).getBorrowwho());

        return convertView;
    }
}
