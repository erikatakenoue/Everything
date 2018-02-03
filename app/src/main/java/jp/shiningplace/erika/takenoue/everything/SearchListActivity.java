package jp.shiningplace.erika.takenoue.everything;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchListActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private ListView mListView;
    public static final String API_URL = "https://app.rakuten.co.jp/services/api/BooksBook/Search/";
    public static final String TAG = "SearchListActivity";
    List<BookItem> booklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search);
        TypefaceProvider.registerDefaultIconSets();
        mListView = (ListView) findViewById(R.id.listView2);

        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RakutenBooks rakuten = retrofit.create(RakutenBooks.class);
                    String title = getIntent().getStringExtra("title");
                    String author = getIntent().getStringExtra("author");
                    String isbn = getIntent().getStringExtra("isbn");
                    Call<BookItems> call = rakuten.Search(title, author, "1061804608980707594",2, isbn);
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
                    booklist=bookitems.Items;
                    handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         if (temp_book != null) {
                                             BookAdapter bookAdapater = new BookAdapter(getApplicationContext(), 0, temp_book.Items);
                                             mListView.setAdapter(bookAdapater);
                                         }
                                     }
                    });
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchListActivity.this, DetailActivity.class);
                intent.putExtra("item", booklist.get(position));
                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }
}