package com.ipd.bangbanjinrong.activity;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.adapter.PhotoPagerAdapter;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.widget.ViewPagerFixed;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPreviewActivity extends BaseActivity implements PhotoPagerAdapter.PhotoViewClickListener {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";
    public static final String FROM_PAGE = "fromPage";
    public static final String FROM_PIC_PIC = "fromPicPic";

    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";

    /**
     * 预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<String> paths=new ArrayList<>();

    @ViewInject(R.id.vp_photos)
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;
    private String fromPage = "";


    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {

    }

    @Override
    public int setLayout() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void innitData() {
        setBack();

        paths = new ArrayList<String>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        if (pathArr != null) {
            paths.addAll(pathArr);
        }

        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        fromPage = getIntent().getStringExtra(FROM_PAGE);

        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);
    }

    @Override
    public void setListeners() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateActionBarTitle();
    }

    @Override
    public void onClick(View v) {

    }

    public void updateActionBarTitle() {
        setTopTitle(getString(R.string.image_index, mViewPager.getCurrentItem() + 1, paths.size()));
    }
}
