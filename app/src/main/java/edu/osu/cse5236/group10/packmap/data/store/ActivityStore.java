package edu.osu.cse5236.group10.packmap.data.store;

public class ActivityStore extends AbstractStore {

    private static final String TAG = "ActivityStore";
    private static final String COLLECTION = "activities";

    private ActivityStore() {
        super();
    }

    private static ActivityStore instance;

    public static ActivityStore getInstance() {
        if (instance == null) {
            instance = new ActivityStore();
        }
        return instance;
    }

    @Override
    protected String getCollection() {
        return COLLECTION;
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
