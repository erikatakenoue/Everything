package jp.shiningplace.erika.takenoue.everything;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class BookFragment extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";
    private ListView mAllList;
    private AllbookAdapter mAllbookAdapter;
    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    public static BookFragment newInstance(@ColorRes int IdRes) {
        BookFragment frag = new BookFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, IdRes);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allbook_main, null);
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mAllList = (ListView) view.findViewById(R.id.listView1);
        mAllbookAdapter = new AllbookAdapter(getActivity());

        // ListViewをタップしたときの処理
        mAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
            }
        });

        // ListViewを長押ししたときの処理
        mAllList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する

                return true;
            }
        });
        addTaskForTest();

        reloadListView();
        return view;
    }

    private void reloadListView() {
        RealmResults<Book> bookRealmResults = mRealm.where(Book.class).findAllSorted("date", Sort.DESCENDING);
        mAllbookAdapter.setTaskList(mRealm.copyFromRealm(bookRealmResults));
        mAllList.setAdapter(mAllbookAdapter);
        mAllbookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }

    private void addTaskForTest() {
        Book book = new Book();
        book.setTitle("作業");
        book.setContents("プログラムを書いてPUSHする");
        book.setDate(new Date());
        book.setId(0);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
    }

}


