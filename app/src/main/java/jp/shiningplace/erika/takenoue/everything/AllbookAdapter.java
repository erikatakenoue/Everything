package jp.shiningplace.erika.takenoue.everything;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AllbookAdapter extends BaseAdapter {
    private List<String> mAllList;

    public void setTaskList(List<String> taskList) {
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
        return null;
    }
}