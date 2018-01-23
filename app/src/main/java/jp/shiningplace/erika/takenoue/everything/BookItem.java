package jp.shiningplace.erika.takenoue.everything;

import android.os.Parcel;
import android.os.Parcelable;

public class BookItem implements Parcelable {
    String title;
    String author;
    String publisherName;
    String salesDate;
    String size;
    String itemCaption;
    String largeImageUrl;

    protected BookItem(Parcel in) {
        title = in.readString();
        author = in.readString();
        publisherName = in.readString();
        salesDate = in.readString();
        size = in.readString();
        itemCaption = in.readString();
        largeImageUrl = in.readString();
    }

    public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
        @Override
        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getItemCaption() {
        return itemCaption;
    }

    public void setItemCaption(String itemCaption) {
        this.itemCaption = itemCaption;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(author);
        out.writeString(publisherName);
        out.writeString(salesDate);
        out.writeString(size);
        out.writeString(itemCaption);
        out.writeString(largeImageUrl);
    }
}



