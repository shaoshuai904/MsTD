package com.maple.mstd.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.InputStream;

/**
 * Created by Maple on 2017/2/16.
 */
public class BitmapUtils {
    private BitmapUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /** 读取资源文件的Bitmap图像 */
    public static Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /** 调整图片大小 */
    public static Bitmap resizeImage(Bitmap bitmap, float newWidth, float newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /** 读取资源文件的Bitmap图像并重置大小 */
    public static Bitmap readAndResizeBitmap(Context context, int resId, float newWidth, float newHeight) {
        return resizeImage(readBitmap(context, resId), newWidth, newHeight);
    }

    /** 分割图片 */
    public static Bitmap clipBitmap(Bitmap bitmap, int x, int y, int w, int h) {
        Bitmap tmp = Bitmap.createBitmap(bitmap, x, y, w, h);
        if (w > 64 && h > 64) {
            tmp = BitmapUtils.resizeImage(tmp, 64, 64);
        }
        return tmp;
    }
}
