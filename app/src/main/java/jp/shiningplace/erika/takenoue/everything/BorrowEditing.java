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

public class BorrowEditing extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private EditText mTitleEdit, mAuthorEdit, mWhoEdit, mMemoEdit, mDateEdit;
    private Borrow mBorrow;
    public final static String EXTRA_BORROW = " jp.shiningplace.erika.takenoue.everything.BORROW";

    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(BorrowEditing.this,
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
            addBorrow();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_main);

        // UI部品の設定
        mDateEdit = (EditText)findViewById(R.id.whenborrowText);
        mDateEdit.setOnClickListener(mOnDateClickListener);
        findViewById(R.id.borrowbutton).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText)findViewById(R.id.titleborrowText);
        mAuthorEdit = (EditText)findViewById(R.id.authorborrowText);
        mWhoEdit = (EditText)findViewById(R.id.whoborrowText);
        mMemoEdit = (EditText)findViewById(R.id.memoborrowText);

        Intent intent = getIntent();
        int borrowId = intent.getIntExtra(BorrowDetail.EXTRA_BORROW, -1);
        Realm realm = Realm.getDefaultInstance();
        mBorrow = realm.where(Borrow.class).equalTo("id", borrowId).findFirst();
        realm.close();

        mTitleEdit.setText(mBorrow.getBorrowtitle());
        mAuthorEdit.setText(mBorrow.getBorrowauthor());
        mWhoEdit.setText(mBorrow.getBorrowwho());
        mMemoEdit.setText(mBorrow.getBorrowmemo());

        Calendar calendar = Calendar.getInstance();
        if (mBorrow.getBorrowdate() != null) {
            calendar.setTime(mBorrow.getBorrowdate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
            mDateEdit.setText(dateString);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addBorrow() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        String title = mTitleEdit.getText().toString();
        String author = mAuthorEdit.getText().toString();
        String who = mWhoEdit.getText().toString();
        String memo = mMemoEdit.getText().toString();

        mBorrow.setBorrowtitle(title);
        mBorrow.setBorrowauthor(author);
        mBorrow.setBorrowwho(who);
        mBorrow.setBorrowmemo(memo);

        GregorianCalendar calendar = new GregorianCalendar(mYear,mMonth,mDay);
        if (mDateEdit.getText().toString().equals("") == false) {
            Date date = calendar.getTime();
            mBorrow.setBorrowdate(date);
        }

        realm.copyToRealmOrUpdate(mBorrow);
        realm.commitTransaction();

        realm.close();

        int borrowId = getIntent().getIntExtra(BorrowDetail.EXTRA_BORROW, -1);
        Intent intent = new Intent(
                BorrowEditing.this, BorrowActivity.class);
        intent.putExtra(EXTRA_BORROW, borrowId);

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
