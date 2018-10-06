package assignment.moneytap.com.wikipedia.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit apiClient;
    private static final String BASE_URL = "https://en.wikipedia.org/w/";

    public static Retrofit getApiClient() {
        if(apiClient == null) {
            apiClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return apiClient;
    }
}
