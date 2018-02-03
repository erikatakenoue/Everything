package jp.shiningplace.erika.takenoue.everything;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class BorrowActivity extends AppCompatActivity {
    public final static String EXTRA_BORROW = "jp.shiningplace.erika.takenoue.everything.BORROW";
    private ListView mListView;
    private BorrowAdapter mBorrowAdapter;

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {

        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.borrowfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BorrowActivity.this, BorrowInput.class);
                startActivity(intent);
            }
        });

        // Realmの設定
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mBorrowAdapter = new BorrowAdapter(BorrowActivity.this);
        mListView = (ListView) findViewById(R.id.borrowlistView);

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Borrow borrow = (Borrow) parent.getAdapter().getItem(position);

                Intent intent = new Intent(BorrowActivity.this, BorrowDetail.class);
                intent.putExtra(EXTRA_BORROW, borrow.getId());

                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Borrow borrow = (Borrow) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(BorrowActivity.this);

                builder.setTitle("削除");
                builder.setMessage(borrow.getBorrowtitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Borrow> results = mRealm.where(Borrow.class).equalTo("id", borrow.getId()).findAll();

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
    }

    private void reloadListView() {
        RealmResults<Borrow> borrowRealmResults = mRealm.where(Borrow.class).findAllSorted("borrowdate", Sort.DESCENDING);
        mBorrowAdapter.setBorrowList(mRealm.copyFromRealm(borrowRealmResults));
        mListView.setAdapter(mBorrowAdapter);
        mBorrowAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}
