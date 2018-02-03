package jp.shiningplace.erika.takenoue.everything;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class DetailViewPagerActivity extends AppCompatActivity {
    private Book mBook;
    int checkedItem=0;
    public final static String EXTRA_TASK = " jp.shiningplace.erika.takenoue.everything.TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailviewpager);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        TextView titleDetail = findViewById(R.id.titleDetail2);
        titleDetail.setText(mBook.getTitle());

        TextView authorDetail = findViewById(R.id.authorDetail2);
        authorDetail.setText(mBook.getAuthor());

        TextView publisherNameDetail = findViewById(R.id.publisherDetailView2);
        publisherNameDetail.setText(mBook.getPublisherName());

        TextView salesDateDetail = findViewById(R.id.dateDetailView2);
        salesDateDetail.setText(mBook.getSalesDate());

        TextView sizeDetail = findViewById(R.id.sizeDetailView2);
        sizeDetail.setText(mBook.getSize());

        TextView itemCaptionDetail = findViewById(R.id.itemCaptionDetailView2);
        itemCaptionDetail.setText(mBook.getItemCaption());

        TextView memoDetail = findViewById(R.id.memoView2);
        memoDetail.setText(mBook.getMemo());

        if (mBook.getDate() != null) {
            TextView endDateDetail = findViewById(R.id.enddateDetailView);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE);
            Date date = mBook.getDate();
            endDateDetail.setText(simpleDateFormat.format(date));
        }

        ImageView imageDetail = findViewById(R.id.imageDetail2);
        Picasso.with(this).load(mBook.getLargeImageUrl()).into(imageDetail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonDetail2).setOnClickListener(mOnDoneClickListener);
    }

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            choosePrefecture();
        }
    };

    public void choosePrefecture() {
        final String[] items = {"読了本", "読書中", "積読本", "気になる"};
        int defaultItem = 0; // デフォルトでチェックされているアイテム
        new AlertDialog.Builder((this))
                .setTitle("ステータスを変更")
                .setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem=which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item_which selected
                        if (checkedItem == 0) {
                            addBook(checkedItem);
                            finish();
                        } else if (checkedItem == 1) {
                            addBook(checkedItem);
                            finish();
                        } else if (checkedItem == 2) {
                            addBook(checkedItem);
                            finish();
                        } else if (checkedItem == 3) {
                            addBook(checkedItem);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addBook(int shelf) {

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        mBook.setShelf(shelf);

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.action_edit) {
                int taskId = getIntent().getIntExtra(BookFragment.EXTRA_TASK, -1);
                Intent intent = new Intent(DetailViewPagerActivity.this, EditingActivity.class);
                intent.putExtra(EXTRA_TASK, taskId);

                startActivity(intent);
            }
            switch(item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(DetailViewPagerActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
            }
        return super.onOptionsItemSelected(item);
    }
}
