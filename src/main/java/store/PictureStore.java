package store;

import java.util.concurrent.ConcurrentHashMap;

public class PictureStore {
    private final ConcurrentHashMap<String, PictureItem> picTable = new ConcurrentHashMap<>();

    public boolean load() {
        return false;
    }

    public boolean addPicture() {
        return false;
    }

    public PictureItem getItem(String path) {
        return new PictureItem();
    }
}
