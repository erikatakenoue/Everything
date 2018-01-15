package jp.shiningplace.erika.takenoue.everything;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class BookFragment extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";
    private ListView mAllList;
    private AllbookAdapter mAllbookAdapter;
    public final static String EXTRA_TASK = " jp.shiningplace.erika.takenoue.everything.TASK";

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
        // 編集画面に移行
        mAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), ManualActivity.class);
                intent.putExtra(EXTRA_TASK, book.getId());

                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        // 削除処理
        mAllList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Book book = (Book) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("削除");
                builder.setMessage(book.getTitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Book> results = mRealm.where(Book.class).equalTo("id", book.getId()).findAll();

                        mRealm.beginTransaction();
                        results.deleteAllFromRealm();
                        mRealm.commitTransaction();

                        reloadListView();
                    }
                });
                builder.setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        reloadListView();
        return view;
    }

    private void reloadListView() {
        RealmResults<Book> taskRealmResults = mRealm.where(Book.class).findAllSorted("date", Sort.DESCENDING);
        // 上記の結果を、TaskList としてセットする
        mAllbookAdapter.setTaskList(mRealm.copyFromRealm(taskRealmResults));
        // TaskのListView用のアダプタに渡す
        mAllList.setAdapter(mAllbookAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mAllbookAdapter.notifyDataSetChanged();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}




