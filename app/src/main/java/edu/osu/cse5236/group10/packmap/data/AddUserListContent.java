package edu.osu.cse5236.group10.packmap.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AddUserListContent {

    private static final String TAG = "AddUserListContent";

    /**
     * An array of sample (dummy) items.
     */
    public static final List<AddUserItem> ITEMS = new ArrayList<AddUserItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, AddUserItem> ITEM_MAP = new HashMap<String, AddUserItem>();

    public static void addItem(AddUserItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static AddUserItem createAddUserItem(int position, String group) {
        return new AddUserItem(String.valueOf(position), group);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class AddUserItem {
        public final String id;
        public final String content;

        public AddUserItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
