package jp.shiningplace.erika.takenoue.everything;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class ManualActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private int mEndYear, mEndMonth, mEndDay;
    private EditText mTitleEdit, mAuthorEdit, mContentEdit, mDateEdit, mEndDateEdit;
    private Button mButton;
    private Book mBook;
    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ManualActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
                            mDateEdit.setText(dateString);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };
    private View.OnClickListener mOnEndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ManualActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mEndYear = year;
                            mEndMonth = monthOfYear;
                            mEndDay = dayOfMonth;
                            String dateString = mEndYear + "/" + String.format("%02d", (mEndMonth + 1)) + "/" + String.format("%02d", mEndDay);
                            mEndDateEdit.setText(dateString);
                        }
                    }, mEndYear, mEndMonth, mEndDay);
            datePickerDialog.show();
        }
    };

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addBook();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        TypefaceProvider.registerDefaultIconSets();

        mDateEdit = (EditText) findViewById(R.id.daysText);
        mDateEdit.setOnClickListener(mOnDateClickListener);
        mEndDateEdit = (EditText) findViewById(R.id.enddateText);
        mEndDateEdit.setOnClickListener(mOnEndDateClickListener);
        findViewById(R.id.button2).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText) findViewById(R.id.titleText);
        mAuthorEdit = (EditText) findViewById(R.id.authorText);
        mContentEdit = (EditText) findViewById(R.id.contentText);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        mTitleEdit.setText(mBook.getTitle());
        mAuthorEdit.setText(mBook.getAuthor());
        mContentEdit.setText(mBook.getContents());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mBook.getDate());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mEndYear = calendar.get(Calendar.YEAR);
        mEndMonth = calendar.get(Calendar.MONTH);
        mEndDay = calendar.get(Calendar.DAY_OF_MONTH);

        String dateString = mYear + "/" + String.format("%02d",(mMonth + 1)) + "/" + String.format("%02d", mDay);
        mDateEdit.setText(dateString);
        String enddateString = mEndYear + "/" + String.format("%02d",(mEndMonth + 1)) + "/" + String.format("%02d", mEndDay);
        mEndDateEdit.setText(enddateString);

    }

    private void addBook() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (mBook == null) {
            // 新規作成の場合
            mBook = new Book();

            RealmResults<Book> taskRealmResults = realm.where(Book.class).findAll();

            int identifier;
            if (taskRealmResults.max("id") != null) {
                identifier = taskRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mBook.setId(identifier);
        }
        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        String author = mAuthorEdit.getText().toString();

        mBook.setTitle(title);
        mBook.setContents(content);
        mBook.setAuthor(author);
        GregorianCalendar calendar2 = new GregorianCalendar(mEndYear,mEndMonth,mEndDay);
        Date date2 = calendar2.getTime();
        mBook.setDate(date2);
        GregorianCalendar calendar = new GregorianCalendar(mYear,mMonth,mDay);
        Date date = calendar.getTime();
        mBook.setDate(date);

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();
    }
}
