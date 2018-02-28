package com.jumpbox.jumpboxlibrary.bitmap;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;

public class BitmapUtils {

	private static BitmapUtils instance;

	private BitmapUtils() {
	}

	public static BitmapUtils getInstance() {
		if (instance == null) {
			instance = new BitmapUtils();
		}
		return instance;
	}

	public Bitmap reSizeBitmap(Activity a, File file) {
		Display display = a.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
		options.inJustDecodeBounds = false;
		int bitmapHeight = options.outHeight;
		int bitmapWidth = options.outWidth;

		if (bitmapHeight > height || bitmapWidth > width) {
			int scaleX = bitmapWidth / width;
			int scaleY = bitmapHeight / height;
			if (scaleX > scaleY) {// 按照水平方向的比例缩放
				options.inSampleSize = scaleX;
			} else {// 按照竖直方向的比例缩放
				options.inSampleSize = scaleY;
			}
		} else {// 如果图片比手机屏幕小 不去缩放了.
			options.inSampleSize = 1;
		}
		return bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
	}
	public Bitmap reSizeBitmap(Activity a,int width, File file) {
		Display display = a.getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
		options.inJustDecodeBounds = false;
		int bitmapHeight = options.outHeight;
		int bitmapWidth = options.outWidth;
		
		if (bitmapHeight > height || bitmapWidth > width) {
			int scaleX = bitmapWidth / width;
			int scaleY = bitmapHeight / height;
			if (scaleX > scaleY) {// 按照水平方向的比例缩放
				options.inSampleSize = scaleX;
			} else {// 按照竖直方向的比例缩放
				options.inSampleSize = scaleY;
			}
		} else {// 如果图片比手机屏幕小 不去缩放了.
			options.inSampleSize = 1;
		}
		return bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
	}

	private boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	private boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	private boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	private boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public Bitmap reSizeBitmap(Activity a, Uri uri) {
		Display display = a.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		String path = getPath(a, uri);
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);

		options.inJustDecodeBounds = false;
		int bitmapHeight = options.outHeight;
		int bitmapWidth = options.outWidth;

		if (bitmapHeight > height || bitmapWidth > width) {
			int scaleX = bitmapWidth / width;
			int scaleY = bitmapHeight / height;
			if (scaleX > scaleY) {// 按照水平方向的比例缩放
				options.inSampleSize = scaleX;
			} else {// 按照竖直方向的比例缩放
				options.inSampleSize = scaleY;
			}
		} else {// 如果图片比手机屏幕小 不去缩放了.
			options.inSampleSize = 1;
		}
		return bitmap = BitmapFactory.decodeFile(path, options);
	}

	private String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

}
