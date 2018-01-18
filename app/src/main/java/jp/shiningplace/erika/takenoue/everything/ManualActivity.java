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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class ManualActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private int mEndYear, mEndMonth, mEndDay;
    private EditText mTitleEdit, mAuthorEdit, mContentEdit, mDateEdit, mEndDateEdit;
    private ImageView mImageView;
    private Book mBook;
    private Uri mPictureUri;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int CHOOSER_REQUEST_CODE = 100;


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSER_REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                if (mPictureUri != null) {
                    getContentResolver().delete(mPictureUri, null, null);
                    mPictureUri = null;
                }
                return;
            }

            // 画像を取得
            Uri uri = (data == null || data.getData() == null) ? mPictureUri : data.getData();

            // URIからBitmapを取得する
            Bitmap image;
            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(uri);
                image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                return;
            }

            // 取得したBimapの長辺を500ピクセルにリサイズする
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            float scale = Math.min((float) 500 / imageWidth, (float) 500 / imageHeight); // (1)

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true);

            // BitmapをImageViewに設定する
            mImageView.setImageBitmap(resizedImage);

            mPictureUri = null;
        }
    }

    private void showChooser() {
        // ギャラリーから選択するIntent
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // ギャラリー選択のIntentを与えてcreateChooserメソッドを呼ぶ
        Intent chooserIntent = Intent.createChooser(galleryIntent, "画像を取得");

        startActivityForResult(chooserIntent, CHOOSER_REQUEST_CODE);
    }

    private View.OnClickListener mOnImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mImageView) {
                // パーミッションの許可状態を確認する
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // 許可されている
                        showChooser();
                    } else {
                        // 許可されていないので許可ダイアログを表示する
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

                        return;
                    }
                } else {
                    showChooser();
                }
            }
            BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();

            // 添付画像が設定されていれば画像を取り出してBASE64エンコードする
            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        TypefaceProvider.registerDefaultIconSets();
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnClickListener(mOnImageClickListener);

        mDateEdit = (EditText) findViewById(R.id.daysText);
        mDateEdit.setOnClickListener(mOnDateClickListener);
        mEndDateEdit = (EditText) findViewById(R.id.enddateText);
        mEndDateEdit.setOnClickListener(mOnEndDateClickListener);
        findViewById(R.id.button).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText) findViewById(R.id.titleText);
        mAuthorEdit = (EditText) findViewById(R.id.authorText);
        mContentEdit = (EditText) findViewById(R.id.contentText);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra(BookFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mBook = realm.where(Book.class).equalTo("id", taskId).findFirst();
        realm.close();

        if (mBook == null) {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mEndYear = calendar.get(Calendar.YEAR);
            mEndMonth = calendar.get(Calendar.MONTH);
            mEndDay = calendar.get(Calendar.DAY_OF_MONTH);

        } else {
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

            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
            mDateEdit.setText(dateString);
            String enddateString = mEndYear + "/" + String.format("%02d", (mEndMonth + 1)) + "/" + String.format("%02d", mEndDay);
            mEndDateEdit.setText(enddateString);

        }
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
        GregorianCalendar calendar2 = new GregorianCalendar(mEndYear, mEndMonth, mEndDay);
        Date date2 = calendar2.getTime();
        mBook.setDate(date2);
        GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay);
        Date date = calendar.getTime();
        mBook.setDate(date);

        realm.copyToRealmOrUpdate(mBook);
        realm.commitTransaction();

        realm.close();
    }
}