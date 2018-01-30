package jp.shiningplace.erika.takenoue.everything;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class ManualActivity extends AppCompatActivity {

    private int mEndYear, mEndMonth, mEndDay;
    private EditText mTitleEdit, mAuthorEdit, mContentEdit, mDateEdit, mEndDateEdit, mMenoEdit, mPublisher;
    private Book mBook;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_manual);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                mBook.setSize(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        Calendar calendar = Calendar.getInstance();
        mEndYear = calendar.get(Calendar.YEAR);
        mEndMonth = calendar.get(Calendar.MONTH);
        mEndDay = calendar.get(Calendar.DAY_OF_MONTH);
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
        String author = mAuthorEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        String memo = mMenoEdit.getText().toString();
        String saledate = mDateEdit.getText().toString();
        String publisherName = mPublisher.getText().toString();

        mBook.setTitle(title);
        mBook.setAuthor(author);
        mBook.setPublisherName(publisherName);
        mBook.setItemCaption(content);
        mBook.setMemo(memo);
        mBook.setSalesDate(saledate);
        GregorianCalendar calendar = new GregorianCalendar(mEndYear, mEndMonth, mEndDay);
        Date date = calendar.getTime();
        mBook.setDate(date);

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();

        Intent intent = new Intent(ManualActivity.this, MainActivity.class);
        startActivity(intent);
    }
}