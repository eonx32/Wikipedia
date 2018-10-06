package assignment.moneytap.com.wikipedia.client;

import java.util.Map;

import assignment.moneytap.com.wikipedia.data.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface QueryApiInterface {

    @GET("api.php")
    Call<Response> getResult(@QueryMap Map<String, String> query);
}
