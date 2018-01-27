package jp.shiningplace.erika.takenoue.everything;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity {
    private static final String[] texts = {"バックアップ ", "リストア（復元）"
    };
    private static final String[] texts2 = {"このアプリについて ", "アプリ評価", "友達に教える"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, R.layout.datalist_layout, texts);
        ListView listView = (ListView) findViewById(R.id.datalist);
        listView.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter2 =
                new ArrayAdapter<>(this, R.layout.datalist_layout, texts2);
        ListView listView2 = (ListView) findViewById(R.id.contentlist);
        listView2.setAdapter(arrayAdapter);

    }
}
