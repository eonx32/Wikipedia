package assignment.moneytap.com.wikipedia.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {

    @SerializedName("batchcomplete")
    private boolean batchComplete;

    @SerializedName("query")
    private Query query;

    public boolean isBatchComplete() {
        return batchComplete;
    }

    public void setBatchComplete(boolean batchComplete) {
        this.batchComplete = batchComplete;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "{ batchcomplete = '" + batchComplete + "'"
                + "query = '" + query + "'"
                + " }";
    }
}
