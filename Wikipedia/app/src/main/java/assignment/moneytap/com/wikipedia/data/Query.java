package assignment.moneytap.com.wikipedia.data;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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
        return "{ pages = [" + pages +"]}";
    }

    public class Page implements Serializable {

        @SerializedName("pageid")
        private int pageId;

        @SerializedName("ns")
        private int ns;

        @SerializedName("title")
        private String title;

        @SerializedName("index")
        private int index;

        @SerializedName("thumbnail")
        private Thumbnail thumbnail;

        @SerializedName("terms")
        private Term terms;

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public int getNs() {
            return ns;
        }

        public void setNs(int ns) {
            this.ns = ns;
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

        @Override
        public String toString() {
            return "{ pageid = '" + pageId + "'\n"
                    + "ns = '" + ns + "'\n"
                    + " title = '" + title + "'\n"
                    + " index = '" + index + "'\n"
                    + " thumbnail = '" + thumbnail + "'\n"
                    + "terms = '" + terms + "'\n"
                    + " }";
        }
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
