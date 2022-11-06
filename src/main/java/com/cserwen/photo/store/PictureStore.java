package com.cserwen.photo.store;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cserwen.photo.utils.GsonUtil;
import com.cserwen.photo.utils.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PictureStore {
    private ConcurrentHashMap<Long, PictureItem> picTable = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(PictureStore.class);
    private AtomicLong counter = new AtomicLong();

    public boolean load() {
        File preDir = new File(PathUtil.getPicturePreviewDirPath());
        if (!preDir.exists()) {
            preDir.mkdirs();
        }

        File srcDir = new File(PathUtil.getPictureDirPath());
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }

        String fileName = PathUtil.getPictureInfoPath();
        try {
            File file = new File(fileName);
            String jsonStr = null;
            if (file.exists()) {
                byte[] data = new byte[(int) file.length()];
                boolean result;
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    int len = inputStream.read(data);
                    result = len == data.length;
                }
                if (result) {
                    jsonStr = new String(data);
                }
            }

            if (StringUtils.isNotEmpty(jsonStr)) {
                PictureStore pictureStore = GsonUtil.fromJson(jsonStr, PictureStore.class);
                if (pictureStore != null) {
                    this.picTable = pictureStore.picTable;
                    this.counter = pictureStore.counter;
                }

            }
            return true;
        } catch (Exception e) {
            log.error("load " + fileName + " failed", e);
        }

        return false;
    }

    public boolean start() {
        return load();
    }

    public void shutdown() {
        flush();
    }

    public long addPicture(PictureItem pictureItem) {
        long id = counter.incrementAndGet();
        this.picTable.putIfAbsent(id, pictureItem);
        return id;
    }

    public List<PictureItem> getItemsByPage(long page, long size) {
        if (size == 0) {
            size = 1;
        }
        int totalSize = getTotalSize();
        long start = totalSize - size * page + 1;
        long end = totalSize - size * (page - 1);
        List<PictureItem> result = new ArrayList<>();
        for (long i = end; i >= start; i--) {
            if (i <= 0) {
                break;
            }
            result.add(picTable.getOrDefault(i, new PictureItem()));
        }

        return result;
    }

    public int getTotalSize() {
        return picTable.size();
    }

    public void flush() {
        String jsonStr = GsonUtil.toJson(this);
        if (jsonStr != null) {
            String fileName = PathUtil.getPictureInfoPath();
            File file = new File(fileName);
            File fileParent = file.getParentFile();
            if (fileParent != null) {
                fileParent.mkdirs();
            }
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonStr);
            } catch (IOException e) {
                log.error("failed to flush, json result = {}", jsonStr, e);
            }
        }
    }
}
