package jp.shiningplace.erika.takenoue.everything;

import android.app.Application;

import io.realm.Realm;

public class BookApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

