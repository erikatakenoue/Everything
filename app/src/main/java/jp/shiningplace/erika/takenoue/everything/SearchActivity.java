package jp.shiningplace.erika.takenoue.everything;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class SearchActivity extends AppCompatActivity {
    private EditText mTitleEdit, mAuthorEdit;

    private View.OnClickListener mOnSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = mTitleEdit.getText().toString();
            String author = mAuthorEdit.getText().toString();
            Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
            if(!TextUtils.isEmpty(title)){
                intent.putExtra("title", title);
            }
            if(!TextUtils.isEmpty(author)){
                intent.putExtra("author", author);
            }
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        TypefaceProvider.registerDefaultIconSets();

        findViewById(R.id.button2).setOnClickListener(mOnSearchClickListener);

        mTitleEdit = (EditText) findViewById(R.id.titleSearchText);
        mAuthorEdit = (EditText) findViewById(R.id.authorSearchText);
    }
}
