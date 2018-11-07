package edu.osu.cse5236.group10.packmap.data;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.osu.cse5236.group10.packmap.data.model.User;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AddUserListContent {

    private static final String TAG = "AddUserListContent";

    private static int count = 0;

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

    public static void clear() {
        ITEMS.clear();
        count = 0;
    }

    public static void createAddUserItem(User user) {
        addItem(new AddUserItem(String.valueOf(count++), user));
    }

    public static List<AddUserItem> getSelectedItems() {
        return Stream.of(ITEMS).distinct().filter(item -> item.selected).toList();
    }

    public static List<String> getSelectedsUserId() {
        return Stream.of(ITEMS)
                .distinct()
                .filter(item -> item.selected)
                .map(item -> item.userId)
                .toList();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class AddUserItem {
        public final String id;
        public final String content;
        public final String userId;
        public boolean selected;

        public AddUserItem(String id, User user) {
            this.id = id;
            this.content = user.getFirstName() + " " + user.getLastName();
            this.userId = user.getDocumentId();
            this.selected = true;
        }

        @Override
        public boolean equals(Object obj) {
            AddUserItem item;
            if (obj instanceof AddUserItem) item = (AddUserItem) obj;
            else return false;
            return this.userId.equals(item.userId);
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
