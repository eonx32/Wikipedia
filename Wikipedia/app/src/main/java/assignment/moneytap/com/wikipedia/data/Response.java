package assignment.moneytap.com.wikipedia.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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

    public class Query implements Serializable {

        @SerializedName("pages")
        private ArrayList<Page> pages;

        public ArrayList<Page> getPages() {
            return pages;
        }

        public void setPages(ArrayList<Page> pages) {
            this.pages = pages;
        }

        @Override
        public String toString() {
            return "{ pages = [" + pages + "]}";
        }
    }
}
