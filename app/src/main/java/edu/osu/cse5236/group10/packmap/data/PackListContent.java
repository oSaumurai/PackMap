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
public class PackListContent {

    private static final String TAG = "PackListContent";

    private static int count = 0;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PackItem> ITEMS = new ArrayList<PackItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PackItem> ITEM_MAP = new HashMap<String, PackItem>();

    public static void addItem(PackItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static PackItem createPackItem(int position, String groupId, String group) {
        return new PackItem(String.valueOf(position), groupId, group);
    }

    public static void addItemWithIndex(String groupId, String group) {
        PackItem temp = new PackItem(String.valueOf(count++), groupId, group);
        ITEMS.add(temp);
        ITEM_MAP.put(temp.id, temp);
    }

    public static void clear() {
        ITEMS.clear();
        count = 0;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PackItem {
        public final String id;
        public final String content;
        public final String groupId;

        public PackItem(String id, String groupId, String content) {
            this.id = id;
            this.content = content;
            this.groupId = groupId;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
