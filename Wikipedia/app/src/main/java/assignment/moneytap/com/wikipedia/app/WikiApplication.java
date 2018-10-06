package assignment.moneytap.com.wikipedia.app;

import android.app.Application;

import assignment.moneytap.com.wikipedia.client.QueryManager;

public class WikiApplication extends Application{

    private static WikiApplication sInstance = null;

    public WikiApplication() { }

    public static WikiApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        QueryManager.getInstance().delete();
    }
}
