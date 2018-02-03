package jp.shiningplace.erika.takenoue.everything;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LendAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Lend> mLendList;

    public LendAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setLendList(List<Lend> lendList) {
        mLendList = lendList;
    }

    @Override
    public int getCount() {
        return mLendList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mLendList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.lend_listitem, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(R.id.lend1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.lend2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.lend3);

        textView1.setText(mLendList.get(position).getLendtitle());
        textView2.setText(mLendList.get(position).getLendauthor());
        textView3.setText(mLendList.get(position).getLendwho());

        return convertView;
    }
}
