package assignment.moneytap.com.wikipedia.Util;

public class WikiConstants {

    public static final String[] COLUMN = {
            "_id",
            "icon",
            "title",
            "desc",
            "url"
    };

    public static enum ColumnType {
        PAGEID(0), ICON(1), TITLE(2), DESC(3), URL(4);

        private int index;

        ColumnType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return COLUMN[index];
        }
    };
}
