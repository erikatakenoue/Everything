package jp.shiningplace.erika.takenoue.everything;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SerchActivity";
    public static final String API_URL = "https://app.rakuten.co.jp/services/api/BooksBook/Search/";
    private EditText mTitleEdit, mAuthorEdit;

    private View.OnClickListener mOnSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(API_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        RakutenBooks rakuten = retrofit.create(RakutenBooks.class);
                        String title = mTitleEdit.getText().toString();
                        String author = mAuthorEdit.getText().toString();
                        Call<BookItems> call = rakuten.Search(title, author);

                        BookItems bookitems = null;
                        try {
                            bookitems = call.execute().body();
                            if (bookitems != null) {
                                Log.d(TAG, "book is not null");
                            } else {
                                Log.d(TAG, "book is null");
                            }
                        } catch (IOException e) {
                            Log.d(TAG, "book :" + e.getMessage());
                            e.printStackTrace();
                        }

                        final BookItems temp_book = bookitems;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (temp_book != null) {
                                    for(int i = 1; i < temp_book.items.size(); i += 1) {
                                        mTitleText.setText(String.valueOf(temp_book.items.get(i).title));
                                        mAuthorText.setText(String.valueOf(temp_book.items.get(i).author));
                                    }
                                }
                            }
                        });
                    }
                });

                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        TypefaceProvider.registerDefaultIconSets();

        mTitleEdit = (EditText) findViewById(R.id.titleSearchText);
        mAuthorEdit = (EditText) findViewById(R.id.authorSearchText);

        findViewById(R.id.button2).setOnClickListener(mOnSearchClickListener);


    }
}
