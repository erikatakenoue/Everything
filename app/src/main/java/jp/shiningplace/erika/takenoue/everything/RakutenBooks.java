package jp.shiningplace.erika.takenoue.everything;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RakutenBooks {
    @GET("20170404")
    Call<BookItems> Search(@Query("title") String title, @Query("author") String author);
    }

