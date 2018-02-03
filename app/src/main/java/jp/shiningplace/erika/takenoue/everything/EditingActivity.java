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

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class EditingActivity extends AppCompatActivity {
    private int mEndYear, mEndMonth, mEndDay;
    private EditText mTitleEdit, mAuthorEdit, mContentEdit, mDateEdit, mEndDateEdit, mMenoEdit, mPublisher, mSizeEdit;
    private Book mBook;
    public final static String EXTRA_TASK = " jp.shiningplace.erika.takenoue.everything.TASK";

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

    private View.OnClickListener mOnDoneClickListener =new View.OnClickListener() {
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
        mPublisher =(EditText) findViewById(R.id.publisherText);
        mSizeEdit =(EditText)findViewById(R.id.sizeText);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        Calendar calendar = Calendar.getInstance();
        mEndYear = calendar.get(Calendar.YEAR);
        mEndMonth = calendar.get(Calendar.MONTH);
        mEndDay = calendar.get(Calendar.DAY_OF_MONTH);

        mTitleEdit.setText(mBook.getTitle());
        mAuthorEdit.setText(mBook.getAuthor());
        mContentEdit.setText(mBook.getItemCaption());
        mMenoEdit.setText(mBook.getMemo());
        mDateEdit.setText(mBook.getSalesDate());
        mPublisher.setText(mBook.getPublisherName());
        mSizeEdit.setText(mBook.getSize());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addBook() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        String title = mTitleEdit.getText().toString();
        String author = mAuthorEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        String memo = mMenoEdit.getText().toString();
        String saledate = mDateEdit.getText().toString();
        String publisherName = mPublisher.getText().toString();
        String size = mSizeEdit.getText().toString();

        mBook.setTitle(title);
        mBook.setAuthor(author);
        mBook.setItemCaption(content);
        mBook.setMemo(memo);
        mBook.setPublisherName(publisherName);
        mBook.setSalesDate(saledate);
        mBook.setSize(size);

        GregorianCalendar calendar = new GregorianCalendar(mEndYear, mEndMonth, mEndDay);
        if (mEndDateEdit.getText().toString().equals("") == false) {
            Date date = calendar.getTime();
            mBook.setDate(date);
        } else {
            mBook.setDate(null);
        }

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();

        int taskId = getIntent().getIntExtra(BookFragment.EXTRA_TASK, -1);
        Intent intent = new Intent(
                EditingActivity.this, DetailViewPagerActivity.class);
        intent.putExtra(EXTRA_TASK, taskId);

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

