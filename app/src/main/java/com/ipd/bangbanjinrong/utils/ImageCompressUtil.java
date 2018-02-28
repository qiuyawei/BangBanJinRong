package com.ipd.bangbanjinrong.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.ipd.bangbanjinrong.global.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2017/6/8.
 * 图片压缩
 */

public class ImageCompressUtil {
    /**
     * @param filePath
     * @return
     * 压缩图片 角度不对调整角度
     * file.length() 获取的是字节 B
     * 1M=1024KB    1KB=1024B
     */
    public static File compressImage(String filePath) {
        Bitmap bm;
        File orignFile=new File(filePath);
        long length=orignFile.length();
        if(length<1024*1024){
            //不超过1M不再压缩
            Log.i("TAG","length="+length);
            return orignFile;
        }
        String pictureName = Constant.IMAGE_CACH_DIRECTORY + System.currentTimeMillis() + ".jpg";
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        options.inSampleSize=2;
        options.inJustDecodeBounds=false;
         bm= BitmapFactory.decodeFile(filePath,options);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(pictureName);
        try {
            if (!outputFile.exists()) {
                //创建目录
                outputFile.getParentFile().mkdirs();
            } else {
                //文件存在  则删除
                outputFile.delete();
            }
            outputFile.createNewFile();

            FileOutputStream out = new FileOutputStream(outputFile);
            //压缩 小一些  现在图片都较大
             bm.compress(Bitmap.CompressFormat.JPEG, 70, out);
            if(bm!=null)
                bm.recycle();
        } catch (Exception e) {
            //出现异常直接返回源文件'

            return orignFile;
        }
        return outputFile;
    }






    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
