package assignment.moneytap.com.wikipedia.client;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import assignment.moneytap.com.wikipedia.Util.WikiAppUtil;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.data.Response;
import retrofit2.Call;

public class QueryManager extends Handler{

    private static final String TAG = "QueryManager";
    private static final String QUERY = "query";
    private static final int SEND_QUERY = 101;

    private static QueryManager mInstance = null;
    private OnResponseListener mResponseListener;

    private QueryManager() {
    }

    public static QueryManager getInstance() {
        if(mInstance == null)
            mInstance = new QueryManager();
        return mInstance;
    }

    public void delete() {
        mInstance = null;
    }

    public void registerListener(OnResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
        WikiLog.d(TAG, "Listener registered : "+mResponseListener.getClass().getSimpleName());
    }

    public void unregisterListener(OnResponseListener mResponseListener) {
        this.mResponseListener = null;
        WikiLog.d(TAG, "Listener deregistered : "+mResponseListener.getClass().getSimpleName());
    }

    public void scheduleQuery(String query) {
        if(mResponseListener!=null) {
            removeMessages(SEND_QUERY);
            Message msg = obtainMessage(SEND_QUERY);
            Bundle data = new Bundle();
            data.putString(QUERY, query);
            msg.setData(data);
            handleMessage(msg);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        queryServer(msg.what, msg.getData());
    }

    private void queryServer(int what, Bundle data) {
        switch (what) {
            case SEND_QUERY: {
                String queryString = data.getString(QUERY);
                Map<String, String> queries = makeQueryMap(queryString);

                QueryApiInterface queryApiInterface = ApiClient.getApiClient().create(QueryApiInterface.class);
                Call<Response> call = queryApiInterface.getResult(queries);
                call.enqueue(new retrofit2.Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if(response.code() == 200) {
                            if(mResponseListener != null)
                                mResponseListener.onResponseReceived(response.body());
                        } else {
                            try {
                                WikiLog.e(TAG, "Error : "+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        WikiAppUtil.showToast("Server Error : "+t.getMessage());
                    }
                });
            }
            break;
        }
    }

    private Map<String, String> makeQueryMap(String queryString) {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("action","query");
        queries.put("format","json");
        queries.put("prop","pageimages|pageterms");
        queries.put("generator","prefixsearch");
        queries.put("redirects","1");
        queries.put("formatversion","2");
        queries.put("piprop","thumbnail");
        queries.put("pithumbsize","50");
        queries.put("pilimit","10");
        queries.put("wbptterms","description");
        queries.put("gpslimit","10");
        queries.put("gpssearch",queryString.replace(" ","+"));
        return queries;
    }

    public interface OnResponseListener {
        void onResponseReceived(Response response);
    }
}
