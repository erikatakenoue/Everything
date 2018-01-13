package jp.shiningplace.erika.takenoue.everything;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        TypefaceProvider.registerDefaultIconSets();
    }
}

