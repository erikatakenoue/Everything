package jp.shiningplace.erika.takenoue.everything;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class BorrowInput extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private EditText mTitleEdit, mAuthorEdit, mWhoEdit, mMemoEdit, mDateEdit;
    private Borrow mBorrow;

    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(BorrowInput.this,
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
        int borrowId = intent.getIntExtra(BorrowActivity.EXTRA_BORROW, -1);
        Realm realm = Realm.getDefaultInstance();
        mBorrow = realm.where(Borrow.class).equalTo("id", borrowId).findFirst();
        realm.close();

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void addBorrow() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        mBorrow = new Borrow();

        RealmResults<Borrow> lendRealmResults = realm.where(Borrow.class).findAll();
        int identifier;
        if (lendRealmResults.max("id") != null) {
            identifier = lendRealmResults.max("id").intValue() + 1;
        } else {
            identifier = 0;
        }
        mBorrow.setId(identifier);

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

        Intent intent = new Intent(BorrowInput.this, BorrowActivity.class);
        startActivity(intent);
    }
}