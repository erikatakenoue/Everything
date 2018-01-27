package jp.shiningplace.erika.takenoue.everything;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class DetailActivity extends AppCompatActivity {
    private Book mBook;
    int checkedItem=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookdetail_main);

        BookItem bookitem = getIntent().getParcelableExtra("item");

        TextView titleDetail = findViewById(R.id.titleDetail);
        titleDetail.setText(bookitem.title);

        TextView authorDetail = findViewById(R.id.authorDetail);
        authorDetail.setText(bookitem.author);

        TextView publisherNameDetail = findViewById(R.id.publisherDetailView);
        publisherNameDetail.setText(bookitem.publisherName);

        TextView salesDateDetail = findViewById(R.id.dateDetailView);
        salesDateDetail.setText(bookitem.salesDate);

        TextView sizeDetail = findViewById(R.id.sizeDetailView);
        sizeDetail.setText(bookitem.size);

        TextView itemCaptionDetail = findViewById(R.id.itemCaptionDetailView);
        itemCaptionDetail.setText(bookitem.itemCaption);

        ImageView imageDetail = findViewById(R.id.imageDetail);
        Picasso.with(this).load(bookitem.largeImageUrl).into(imageDetail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonDetail).setOnClickListener(mOnDoneClickListener);

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
                .setTitle("本棚に登録")
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

        mBook = new Book();

        RealmResults<Book> bookRealmResults = realm.where(Book.class).findAll();

        int identifier;
        if (bookRealmResults.max("id") != null) {
            identifier = bookRealmResults.max("id").intValue() + 1;
            mBook.setId(identifier);
        }

        mBook.setShelf(shelf);

        BookItem bookitem = getIntent().getParcelableExtra("item");


        mBook.setTitle(bookitem.title);
        mBook.setAuthor(bookitem.author);
        mBook.setPublisherName(bookitem.publisherName);
        mBook.setItemCaption(bookitem.itemCaption);
        mBook.setLargeImageUrl(bookitem.largeImageUrl);
        mBook.setSize(bookitem.size);
        mBook.setSalesDate(bookitem.salesDate);
        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}