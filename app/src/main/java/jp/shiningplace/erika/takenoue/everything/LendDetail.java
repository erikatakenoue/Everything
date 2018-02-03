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

public class LendDetail extends AppCompatActivity {
    private Lend mLend;
    public final static String EXTRA_LEND = " jp.shiningplace.erika.takenoue.everything.LEND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_detail);

        Intent intent = getIntent();
        int lendId = intent.getIntExtra(LendActivity.EXTRA_LEND, -1);
        Realm realm = Realm.getDefaultInstance();
        mLend = realm.where(Lend.class).equalTo("id", lendId).findFirst();
        realm.close();

        TextView titleDetail = findViewById(R.id.lendDetail2);
        titleDetail.setText(mLend.getLendtitle());

        TextView authorDetail = findViewById(R.id.lendDetail4);
        authorDetail.setText(mLend.getLendauthor());

        TextView whoDetail = findViewById(R.id.lendDetail6);
        whoDetail.setText(mLend.getLendwho());

        TextView whenDetail = findViewById(R.id.lendDetail8);
        if (mLend.getLenddate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE);
            Date date = mLend.getLenddate();
            whenDetail.setText(simpleDateFormat.format(date));
        }

        TextView memoDetail = findViewById(R.id.lendDetail10);
        memoDetail.setText(mLend.getLendmemo());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonlendDetail).setOnClickListener(mOnDoneClickListener);
    }

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int lendId = getIntent().getIntExtra(LendActivity.EXTRA_LEND, -1);
            Intent intent = new Intent(LendDetail.this, LendEditing.class);
            intent.putExtra(EXTRA_LEND, lendId);

            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LendDetail.this, LendActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
