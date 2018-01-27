package jp.shiningplace.erika.takenoue.everything;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class EditingActivity extends AppCompatActivity {
    private int mEndYear, mEndMonth, mEndDay;
    private EditText mTitleEdit, mAuthorEdit, mContentEdit, mDateEdit, mEndDateEdit, mMenoEdit;
    private Book mBook;

    private View.OnClickListener mOnEndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditingActivity.this,
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
        mEndDateEdit = (EditText) findViewById(R.id.enddateText);
        mEndDateEdit.setOnClickListener(mOnEndDateClickListener);
        findViewById(R.id.button).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText) findViewById(R.id.titleText);
        mAuthorEdit = (EditText) findViewById(R.id.authorText);
        mContentEdit = (EditText) findViewById(R.id.contentText);
        mMenoEdit = (EditText) findViewById(R.id.memoText);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        mTitleEdit.setText(mBook.getTitle());
        mAuthorEdit.setText(mBook.getAuthor());
        mContentEdit.setText(mBook.getItemCaption());
        mMenoEdit.setText(mBook.getMemo());
        mDateEdit.setText(mBook.getSalesDate());


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mBook.getDate());
        mEndYear = calendar.get(Calendar.YEAR);
        mEndMonth = calendar.get(Calendar.MONTH);
        mEndDay = calendar.get(Calendar.DAY_OF_MONTH);

        String enddateString = mEndYear + "/" + String.format("%02d", (mEndMonth + 1)) + "/" + String.format("%02d", mEndDay);
        mEndDateEdit.setText(enddateString);
    }

    private void addBook() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        String title = mTitleEdit.getText().toString();
        String author = mAuthorEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        String memo = mMenoEdit.getText().toString();
        String saledate = mDateEdit.getText().toString();

        mBook.setTitle(title);
        mBook.setAuthor(author);
        mBook.setItemCaption(content);
        mBook.setMemo(memo);
        mBook.setSalesDate(saledate);
        GregorianCalendar calendar = new GregorianCalendar(mEndYear, mEndMonth, mEndDay);
        Date date = calendar.getTime();
        mBook.setDate(date);

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();
    }
}

