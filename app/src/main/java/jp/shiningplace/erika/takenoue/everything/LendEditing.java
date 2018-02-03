package jp.shiningplace.erika.takenoue.everything;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class LendEditing extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private EditText mTitleEdit, mAuthorEdit, mWhoEdit, mMemoEdit, mDateEdit;
    private Lend mLend;
    public final static String EXTRA_LEND = " jp.shiningplace.erika.takenoue.everything.LEND";

    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(jp.shiningplace.erika.takenoue.everything.LendEditing.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            String dateString = mYear + "/" + String.format("%02d",(mMonth + 1)) + "/" + String.format("%02d", mDay);
                            mDateEdit.setText(dateString);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addLend();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lend_main);

            // UI部品の設定
        mDateEdit = (EditText)findViewById(R.id.whenlendText);
        mDateEdit.setOnClickListener(mOnDateClickListener);
        findViewById(R.id.lendbutton).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText)findViewById(R.id.titlelendText);
        mAuthorEdit = (EditText)findViewById(R.id.authorlendText);
        mWhoEdit = (EditText)findViewById(R.id.wholendText);
        mMemoEdit = (EditText)findViewById(R.id.memolendText);

        Intent intent = getIntent();
        int lendId = intent.getIntExtra(LendDetail.EXTRA_LEND, -1);
        Realm realm = Realm.getDefaultInstance();
        mLend = realm.where(Lend.class).equalTo("id", lendId).findFirst();
        realm.close();

        mTitleEdit.setText(mLend.getLendtitle());
        mAuthorEdit.setText(mLend.getLendauthor());
        mWhoEdit.setText(mLend.getLendwho());
        mMemoEdit.setText(mLend.getLendmemo());

        Calendar calendar = Calendar.getInstance();
        if (mLend.getLenddate() != null) {
            calendar.setTime(mLend.getLenddate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
            mDateEdit.setText(dateString);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addLend() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        String title = mTitleEdit.getText().toString();
        String author = mAuthorEdit.getText().toString();
        String who = mWhoEdit.getText().toString();
        String memo = mMemoEdit.getText().toString();

        mLend.setLendtitle(title);
        mLend.setLendauthor(author);
        mLend.setLendwho(who);
        mLend.setLendmemo(memo);

        GregorianCalendar calendar = new GregorianCalendar(mYear,mMonth,mDay);
        if (mDateEdit.getText().toString().equals("") == false) {
            Date date = calendar.getTime();
            mLend.setLenddate(date);
        }

        realm.copyToRealmOrUpdate(mLend);
        realm.commitTransaction();

        realm.close();

        int taskId = getIntent().getIntExtra(LendDetail.EXTRA_LEND, -1);
        Intent intent = new Intent(
                LendEditing.this, LendActivity.class);
        intent.putExtra(EXTRA_LEND, taskId);

        startActivity(intent);
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

