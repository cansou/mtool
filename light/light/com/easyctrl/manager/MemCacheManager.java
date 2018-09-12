package com.easyctrl.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import com.easyctrl.ldy.activity.MainApplication;

public class MemCacheManager {
    public static final int MAX_SIZE = 20;
    public static final String TAG = "MemCacheManager";
    public static MemCacheManager instance;
    private LruCache bitmapCache = new LruCache<String, Bitmap>(20) {
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if (oldValue != null && !oldValue.isRecycled()) {
                oldValue.recycle();
            }
        }
    };

    private class AsyncLoadImage extends AsyncTask<Integer, Integer, Bitmap> {
        private Bitmap bitmap;

        public AsyncLoadImage(int id) {
        }

        public Bitmap loadBitMap() {
            return this.bitmap;
        }

        protected Bitmap doInBackground(Integer... params) {
            this.bitmap = BitmapFactory.decodeResource(MainApplication.mContext.getResources(), params[0].intValue());
            return this.bitmap;
        }
    }

    private MemCacheManager() {
    }

    public static MemCacheManager getInstance() {
        if (instance == null) {
            instance = new MemCacheManager();
        }
        return instance;
    }

    public void set(String key, Bitmap bm) {
        synchronized (this.bitmapCache) {
            if (this.bitmapCache.get(key) == null) {
                this.bitmapCache.put(key, bm);
            }
        }
    }

    public void reset() {
        this.bitmapCache.evictAll();
    }

    public Bitmap get(String key) {
        return (Bitmap) this.bitmapCache.get(key);
    }

    public Bitmap loadBitmap(int drawable) {
        AsyncLoadImage asyncLoadImage = new AsyncLoadImage(drawable);
        asyncLoadImage.execute(new Integer[]{Integer.valueOf(drawable)});
        return asyncLoadImage.loadBitMap();
    }
}
