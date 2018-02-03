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

public class LendActivity extends AppCompatActivity {
    public final static String EXTRA_LEND = "jp.shiningplace.erika.takenoue.everything.LEND";
    private ListView mListView;
    private LendAdapter mLendAdapter;

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
        setContentView(R.layout.lend_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.lendfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LendActivity.this, LendInput.class);
                startActivity(intent);
            }
        });

        // Realmの設定
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mLendAdapter = new LendAdapter(LendActivity.this);
        mListView = (ListView) findViewById(R.id.lendlistView);

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lend lend= (Lend) parent.getAdapter().getItem(position);

                Intent intent = new Intent(LendActivity.this, LendDetail.class);
                intent.putExtra(EXTRA_LEND, lend.getId());

                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Lend lend = (Lend) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(LendActivity.this);

                builder.setTitle("削除");
                builder.setMessage(lend.getLendtitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Lend> results = mRealm.where(Lend.class).equalTo("id", lend.getId()).findAll();

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
        RealmResults<Lend> lendRealmResults = mRealm.where(Lend.class).findAllSorted("lenddate", Sort.DESCENDING);
        mLendAdapter.setLendList(mRealm.copyFromRealm(lendRealmResults));
        mListView.setAdapter(mLendAdapter);
        mLendAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}

