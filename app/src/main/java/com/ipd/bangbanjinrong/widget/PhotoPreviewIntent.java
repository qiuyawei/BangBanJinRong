package com.ipd.bangbanjinrong.widget;

import android.content.Context;
import android.content.Intent;

import com.ipd.bangbanjinrong.activity.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * 预览照片
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPreviewIntent extends Intent {

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
    }

    /**
     * 照片地址
     * @param paths
     */
    public void setPhotoPaths(ArrayList<String> paths){
        this.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, paths);
    }

    /**
     * 当前照片的下标
     * @param currentItem
     */
    public void setCurrentItem(int currentItem){
        this.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
    }

    /**
     * 来自于哪个页面
     * @param fromPage
     */
    public void setFromPage(String fromPage){
        this.putExtra(PhotoPreviewActivity.FROM_PAGE, PhotoPreviewActivity.FROM_PIC_PIC);
    }
}
