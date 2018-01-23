package jp.shiningplace.erika.takenoue.everything;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookdetail_main);

        BookItem bookitem = getIntent().getParcelableExtra("item");

        TextView titleDetail = findViewById(R.id.titleDetail);
        titleDetail.setText(bookitem.title);

        TextView authorDetail = findViewById(R.id.authorDetail);
        authorDetail.setText(bookitem.author);

        TextView publisherNameDetail = findViewById(R.id.publisherDetailView);
        publisherNameDetail.setText(bookitem.publisherName);

        TextView salesDateDetail = findViewById(R.id.dateDetailView);
        salesDateDetail.setText(bookitem.salesDate);

        TextView sizeDetail = findViewById(R.id.sizeDetailView);
        sizeDetail.setText(bookitem.size);

        TextView itemCaptionDetail = findViewById(R.id.itemCaptionDetailView);
        itemCaptionDetail.setText(bookitem.itemCaption);

        ImageView imageDetail = findViewById(R.id.imageDetail);
        Picasso.with(this).load(bookitem.largeImageUrl).into(imageDetail);
    }
}
