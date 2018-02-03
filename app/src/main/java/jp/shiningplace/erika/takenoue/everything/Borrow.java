package jp.shiningplace.erika.takenoue.everything;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Borrow extends RealmObject implements Serializable {
    private String borrowtitle;
    private String borrowauthor;
    private String borrowwho;
    private String borrowmemo;
    private Date borrowdate;

    @PrimaryKey
    private int id;

    public String getBorrowtitle() {
        return borrowtitle;
    }

    public void setBorrowtitle(String borrowtitle) {
        this.borrowtitle = borrowtitle;
    }

    public String getBorrowauthor() {
        return borrowauthor;
    }

    public void setBorrowauthor(String borrowauthor) {
        this.borrowauthor = borrowauthor;
    }

    public Date getBorrowdate() {
        return borrowdate;
    }

    public void setBorrowdate(Date borrowdate) {
        this.borrowdate = borrowdate;
    }

    public String getBorrowwho() {
        return borrowwho;
    }

    public void setBorrowwho(String borrowwho) {
        this.borrowwho = borrowwho;
    }

    public String getBorrowmemo() {
        return borrowmemo;
    }

    public void setBorrowmemo(String borrowmemo) {
        this.borrowmemo = borrowmemo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}