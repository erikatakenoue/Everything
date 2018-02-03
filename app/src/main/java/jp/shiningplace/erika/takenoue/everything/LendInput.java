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

public class LendInput extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private EditText mTitleEdit, mAuthorEdit, mWhoEdit, mMemoEdit, mDateEdit;
    private Lend mLend;

    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(LendInput.this,
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
        int lendId = intent.getIntExtra(LendActivity.EXTRA_LEND, -1);
        Realm realm = Realm.getDefaultInstance();
        mLend = realm.where(Lend.class).equalTo("id", lendId).findFirst();
        realm.close();

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void addLend() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        mLend = new Lend();

        RealmResults<Lend> lendRealmResults = realm.where(Lend.class).findAll();
        int identifier;
        if (lendRealmResults.max("id") != null) {
            identifier = lendRealmResults.max("id").intValue() + 1;
        } else {
            identifier = 0;
        }
        mLend.setId(identifier);

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

        Intent intent = new Intent(LendInput.this, LendActivity.class);
        startActivity(intent);
    }
}
