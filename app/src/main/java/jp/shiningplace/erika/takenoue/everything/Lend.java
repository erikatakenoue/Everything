package jp.shiningplace.erika.takenoue.everything;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lend extends RealmObject implements Serializable {
    private String lendtitle;
    private String lendauthor;
    private String lendwho;
    private String lendmemo;
    private Date lenddate;

    @PrimaryKey
    private int id;

    public String getLendtitle() {
        return lendtitle;
    }

    public void setLendtitle(String lendtitle) {
        this.lendtitle = lendtitle;
    }

    public String getLendauthor() {
        return lendauthor;
    }

    public void setLendauthor(String lendauthor) {
        this.lendauthor = lendauthor;
    }

    public Date getLenddate() {
        return lenddate;
    }

    public void setLenddate(Date lenddate) {
        this.lenddate = lenddate;
    }

    public String getLendwho() {
        return lendwho;
    }

    public void setLendwho(String lendwho) {
        this.lendwho = lendwho;
    }

    public String getLendmemo() {
        return lendmemo;
    }

    public void setLendmemo(String lendmemo) {
        this.lendmemo = lendmemo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

