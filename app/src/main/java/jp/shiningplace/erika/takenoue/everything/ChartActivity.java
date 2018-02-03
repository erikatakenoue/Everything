package jp.shiningplace.erika.takenoue.everything;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class ChartActivity extends AppCompatActivity {

    protected BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chartlayout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        chart = (BarChart) findViewById(R.id.bar_chart);

        //Y軸(左)
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setLabelCount(5);
        left.setDrawTopYLabelEntry(true);
        //整数表示に
        left.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + (int) value;
            }
        });

        //Y軸(右)
        YAxis right = chart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawGridLines(false);
        right.setDrawZeroLine(true);
        right.setDrawTopYLabelEntry(true);

        //X軸
        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        //グラフ上の表示
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setClickable(false);

        //凡例
        chart.getLegend().setEnabled(false);

        chart.setScaleEnabled(false);
        //アニメーション
        chart.animateY(1200, Easing.EasingOption.Linear);

        setupMonthly();
    }

    private void setupMonthly() {

        XAxis xAxis = chart.getXAxis();
        final String[] labels = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月",};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(labels.length);

        Realm realm = Realm.getDefaultInstance();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {//12ヶ月分繰り返す
            // 月毎の１日と、最終日のDateを作成する（時分秒は０にしておく）,

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, i);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date monthStart = cal.getTime();
            cal.add(Calendar.MONTH, 1);//月の最終日にするため、一旦翌月の１日にして、そこから１ミリ秒減らす事で厳密な最終日の２３時５９分５９秒９９９ミリ秒にする
            cal.add(Calendar.MILLISECOND, -1);
            Date monthEnd = cal.getTime();
            //生成した１日～最終日のデータをRealmから検索する
            RealmResults<Book> books = realm.where(Book.class).between("date", monthStart, monthEnd).findAll();
            //取得したデータの件数をグラフに適用する
            entries.add(new BarEntry(i, books.size()));
        }
        realm.close();

        List<IBarDataSet> bars = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "bar");

        //整数で表示
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        //ハイライトさせない
        dataSet.setHighlightEnabled(false);

        //Barの色をセット
        dataSet.setColors(new int[]{R.color.grad2, R.color.grad2, R.color.grad2}, this);
        bars.add(dataSet);

        BarData data = new BarData(bars);
        chart.setData(data);
    }

    private void setupYearly() {
        Realm realm = Realm.getDefaultInstance();

        //グラフに表示するため、最小と最大の日付を取得する
        Date minDate = realm.where(Book.class).minimumDate("date");
        Date maxDate = realm.where(Book.class).maximumDate("date");
        List<String> labelList = new ArrayList<String>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (minDate != null && maxDate != null) {
            Calendar minCalendar = Calendar.getInstance();
            Calendar maxCalendar = Calendar.getInstance();
            minCalendar.setTime(minDate);
            maxCalendar.setTime(maxDate);

            //minからmaxまで１年分ずつデータをセットしていく
            int i = 0;
            while (minCalendar.getTimeInMillis() <= maxCalendar.getTimeInMillis()) {
                labelList.add(Integer.toString(minCalendar.get(Calendar.YEAR)));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date monthStart = cal.getTime();
                cal.add(Calendar.YEAR, 1);
                cal.add(Calendar.MILLISECOND, -1);
                Date monthEnd = cal.getTime();
                RealmResults<Book> books = realm.where(Book.class).between("date", monthStart, monthEnd).findAll();

                entries.add(new BarEntry(i++, books.size()));

                minCalendar.add(Calendar.YEAR, 1);
            }
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelList.toArray(new String[]{})));
        xAxis.setLabelCount(labelList.size());
        realm.close();

        List<IBarDataSet> bars = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "bar");

        //整数で表示
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        //ハイライトさせない
        dataSet.setHighlightEnabled(false);

        //Barの色をセット
        dataSet.setColors(new int[]{R.color.grad2, R.color.grad2, R.color.grad2}, this);
        bars.add(dataSet);

        BarData data = new BarData(bars);
        chart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_chart) {
        final String[] items = {"月", "年"};
        new AlertDialog.Builder((this))
                    .setTitle("グラフ表示")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                setupMonthly();
                                chart.invalidate();
                            } else if (which == 1) {
                                setupYearly();
                                chart.invalidate();
                            }
                        }
                    })
            .show();
        }

        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ChartActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

