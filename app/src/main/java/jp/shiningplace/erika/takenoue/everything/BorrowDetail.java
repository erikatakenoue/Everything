package jp.shiningplace.erika.takenoue.everything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class BorrowDetail extends AppCompatActivity {
    private Borrow mBorrow;
    public final static String EXTRA_BORROW = " jp.shiningplace.erika.takenoue.everything.BORROW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_detail);

        Intent intent = getIntent();
        int borrowId = intent.getIntExtra(BorrowActivity.EXTRA_BORROW, -1);
        Realm realm = Realm.getDefaultInstance();
        mBorrow = realm.where(Borrow.class).equalTo("id", borrowId).findFirst();
        realm.close();

        TextView titleDetail = findViewById(R.id.borrowDetail2);
        titleDetail.setText(mBorrow.getBorrowtitle());

        TextView authorDetail = findViewById(R.id.borrowDetail4);
        authorDetail.setText(mBorrow.getBorrowauthor());

        TextView whoDetail = findViewById(R.id.borrowDetail6);
        whoDetail.setText(mBorrow.getBorrowwho());

        TextView whenDetail = findViewById(R.id.borrowDetail8);
        if (mBorrow.getBorrowdate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE);
            Date date = mBorrow.getBorrowdate();
            whenDetail.setText(simpleDateFormat.format(date));
        }

        TextView memoDetail = findViewById(R.id.borrowDetail10);
        memoDetail.setText(mBorrow.getBorrowmemo());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonborrowDetail).setOnClickListener(mOnDoneClickListener);
    }

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int borrowId = getIntent().getIntExtra(BorrowActivity.EXTRA_BORROW, -1);
            Intent intent = new Intent(BorrowDetail.this, BorrowEditing.class);
            intent.putExtra(EXTRA_BORROW, borrowId);

            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(BorrowDetail.this, BorrowActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

