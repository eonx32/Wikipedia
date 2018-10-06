package assignment.moneytap.com.wikipedia.data;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable {

    @SerializedName("pageid")
    private int pageId;

    @SerializedName("title")
    private String title;

    @SerializedName("index")
    private int index;

    @SerializedName("thumbnail")
    private Thumbnail thumbnail;

    @SerializedName("terms")
    private Term terms;

    @SerializedName("fullurl")
    private String url;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Term getTerms() {
        return terms;
    }

    public void setTerms(Term terms) {
        this.terms = terms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{ pageid = '" + pageId + "'\n"
                + " title = '" + title + "'\n"
                + " index = '" + index + "'\n"
                + " thumbnail = '" + thumbnail + "'\n"
                + "terms = '" + terms + "'\n"
                + "fullurl = '" + url + "'\n"
                + " }";
    }

    public class Thumbnail implements Serializable{

        @SerializedName("source")
        private String source;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "{ source = '" + source + "'\n"
                    + "width = '" + width + "'\n"
                    + "height = '" + height + "'\n"
                    +"}";
        }
    }

    public class Term implements Serializable {

        @SerializedName("description")
        private ArrayList<String> description;

        public ArrayList<String> getDescription() {
            return description;
        }

        public void setDescription(ArrayList<String> description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "{ description = [" +
                    description.toString() +
                    " ]}";
        }
    }
}
