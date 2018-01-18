package jp.shiningplace.erika.takenoue.everything;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class SearchListActivity extends AppCompatActivity {

    private TextView mTitleText, mAuthorText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search);
        TypefaceProvider.registerDefaultIconSets();

        mTitleText = (TextView) findViewById(R.id.titleSearch);
        mAuthorText = (TextView) findViewById(R.id.authorSearch);


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
