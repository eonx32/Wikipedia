package assignment.moneytap.com.wikipedia.app;

import android.app.Application;

import assignment.moneytap.com.wikipedia.client.QueryManager;

public class WikiApplication extends Application{

    private static WikiApplication mInstance = null;

    private WikiApplication() { }

    public static WikiApplication getInstance() {
        if(mInstance == null)
            mInstance = new WikiApplication();
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        QueryManager.getInstance().delete();
    }
}
