package jp.shiningplace.erika.takenoue.everything;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.obsez.android.lib.filechooser.ChooserDialog;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;


import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.IOException;


public class SettingActivity extends AppCompatActivity {
    private ListView listView,listView2;
    String path;
    String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        listView = (ListView) findViewById(R.id.datalist);
        listView2 = (ListView) findViewById(R.id.contentlist);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.add("バックアップ");
        arrayAdapter.add("リストア（復元）");

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) listView.getItemAtPosition(position);
                if (item.equals("バックアップ")) {
                    new ChooserDialog().with(SettingActivity.this)
                            .withFilter(true, false)
                            .withStartFile(Environment.getExternalStorageDirectory().getPath())
                            .withResources(R.string.text_choose_backup_folder, R.string.title_choose, R.string.dialog_cancel)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(final String path, File pathFile) {final EditText editView = new EditText(SettingActivity.this);
                                    AlertDialog show = new AlertDialog.Builder(SettingActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setTitle("ファイル名を記入してください。")
                                            //setViewにてビューを設定します。
                                            .setView(editView)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    final String title = editView.getText().toString();
                                                    Thread thread = new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Realm realm = Realm.getDefaultInstance();
                                                            RealmResults<Book> taskRealmResults = realm.where(Book.class).findAll();
                                                            String filePath = path + "/" + title;
                                                            File file = new File(filePath);
                                                            file.getParentFile().mkdir();

                                                            FileOutputStream fos;
                                                            try {
                                                                fos = new FileOutputStream(file, true);
                                                                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                                                                BufferedWriter bw = new BufferedWriter(osw);
                                                                for (Book book : taskRealmResults) {
                                                                    bw.write(book.getId() + ",");
                                                                    bw.write(book.getTitle() + ",");
                                                                    bw.write(book.getAuthor() + ",");
                                                                    bw.write(book.getPublisherName() + ",");
                                                                    bw.write(( book.getDate()!=null ? book.getDate().getTime() : "" )+ ",");
                                                                    bw.write(book.getSalesDate() + ",");
                                                                    bw.write(book.getSize() + ",");
                                                                    bw.write(book.getItemCaption() + ",");
                                                                    bw.write(book.getLargeImageUrl() + ",");
                                                                    bw.write(book.getShelf() + ",");
                                                                    bw.write(book.getMemo() + "\n");//最後のアイテムだけ,を付けない
                                                                }

                                                                bw.flush();
                                                                bw.close();

                                                            } catch (Exception e) {
                                                            }
                                                            realm.close();
                                                        }
                                                    });
                                                    thread.start();
                                                }
                                            })
                                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                }
                                            })
                                            .show();
                                }

                            })
                            .build()
                            .show();

                } else if (item.equals("リストア（復元）")) {
                    new ChooserDialog().with(SettingActivity.this)
                            .withStartFile(path)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    try {
                                        FileInputStream fis = new FileInputStream(path);
                                        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

                                        BufferedReader br = new BufferedReader(isr);
                                        Realm realm = Realm.getDefaultInstance();
                                        String s;
                                        realm.beginTransaction();
                                        while ((s = br.readLine()) != null) {
                                            Book book = new Book();
                                            String[] items = s.split(",", -1);
                                            book.setId(Integer.parseInt(items[0]));
                                            book.setTitle(items[1]);
                                            book.setAuthor(items[2]);
                                            book.setPublisherName(items[3]);
                                            if (!TextUtils.isEmpty(items[4]))
                                                book.setDate(new Date(Long.parseLong(items[4])));
                                            book.setSalesDate(items[5]);
                                            book.setSize(items[6]);
                                            book.setItemCaption(items[7]);
                                            book.setLargeImageUrl(items[8]);
                                            book.setShelf(Integer.parseInt(items[9]));
                                            book.setMemo(items[10]);

                                            realm.copyToRealmOrUpdate(book);
                                        }
                                        realm.commitTransaction();
                                        br.close();
                                        realm.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .build()
                            .show();
                }
            }
        });


        ArrayAdapter<String> arrayAdapter2 =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter2.add("このアプリについて");
        arrayAdapter2.add("アプリ評価");
        arrayAdapter2.add("友達に教える");

        listView2.setAdapter(arrayAdapter2);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setTitle("権限が必要です")
                            .setMessage("バックアップ＆リストア機能の為、ファイル入出力の権限が必要です。")
                            .setPositiveButton("許可する", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
                                }
                            })
                            .create()
                            .show();
                    return;
                }
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999) {
            boolean isAllGrant = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllGrant = false;
                    break;
                }
            }
            if (isAllGrant) {
            } else {
                finish();
            }
        }
    }

    private void startBackup(String folderPath){

    }
}