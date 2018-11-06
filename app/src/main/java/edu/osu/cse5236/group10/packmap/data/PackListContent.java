package edu.osu.cse5236.group10.packmap.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.Group;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
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

    public static void addItemWithIndex(Group group) {
        PackItem temp = new PackItem(String.valueOf(count++), group);
        addItem(temp);
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
        public final Group group;

        public PackItem(String id, Group group) {
            this.id = id;
            this.content = group.getName();
            this.group = group;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
