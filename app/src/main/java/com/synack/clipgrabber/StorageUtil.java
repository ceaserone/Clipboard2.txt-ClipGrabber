package com.synack.clipgrabber;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.*;

public final class StorageUtil {
    private StorageUtil() {}

    public static void appendToDownloads(Context ctx, String fileName, String text) {
        try {
            Uri fileUri = findOrCreateInDownloads(ctx, fileName);
            try (OutputStream os = ctx.getContentResolver().openOutputStream(fileUri, "wa")) {
                os.write(text.getBytes());
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Uri findOrCreateInDownloads(Context ctx, String fileName) throws Exception {
        Uri collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String[] proj = new String[]{ MediaStore.Downloads._ID, MediaStore.Downloads.DISPLAY_NAME };
        String sel = MediaStore.Downloads.DISPLAY_NAME + "=?";
        try (Cursor c = ctx.getContentResolver().query(collection, proj, sel, new String[]{fileName}, null)) {
            if (c != null && c.moveToFirst()) {
                long id = c.getLong(0);
                return Uri.withAppendedPath(collection, String.valueOf(id));
            }
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        return ctx.getContentResolver().insert(collection, values);
    }
}
