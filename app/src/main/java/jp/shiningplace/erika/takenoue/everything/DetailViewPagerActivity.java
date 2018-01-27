package jp.shiningplace.erika.takenoue.everything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class DetailViewPagerActivity extends AppCompatActivity {
    private Book mBook;
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

        ImageView imageDetail = findViewById(R.id.imageDetail2);
        Picasso.with(this).load(mBook.getLargeImageUrl()).into(imageDetail);

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

            return true;
        }
        return true;
    }
}
