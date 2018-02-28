package com.ipd.bangbanjinrong.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.entity.LoginEntity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CommonUtil;
import com.ipd.bangbanjinrong.utils.ImageCompressUtil;
import com.ipd.bangbanjinrong.utils.JsonUtil;
import com.ipd.bangbanjinrong.utils.LogUtils;
import com.ipd.bangbanjinrong.utils.ShareUtils;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.MySelfSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import static com.ipd.bangbanjinrong.global.Constant.IMAGE_COMPLETE;
import static com.ipd.bangbanjinrong.global.Constant.PHOTOTAKE;
import static com.ipd.bangbanjinrong.global.Constant.PHOTOZOOM;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/10
 * @Email 448739075@qq.com
 * 个人资料
 */

public class PersonInforMation extends BaseActivity {
    private LoginEntity loginEntity = null;
    private String photoSaveName, photoSavePath = Constant.IMAGE_CACH_DIRECTORY;
    private String path, newLogo, tempPaht;
    private File file, mFlie;

    @ViewInject(R.id.iv_photo)
    private ImageView iv_photo;
    @ViewInject(R.id.ll_wdjl)
    private RelativeLayout rl_head;
    @ViewInject(R.id.rl_cityChoic)
    private RelativeLayout rl_cityChoic;
    @ViewInject(R.id.rl_name)
    private RelativeLayout rl_name;
    @ViewInject(R.id.rl_restPwd)
    private RelativeLayout rl_restPwd;
    @ViewInject(R.id.bt_loginOut)
    private Button bt_loginOut;

    @ViewInject(R.id.tv_trueName)
    private TextView tv_trueName;
    @ViewInject(R.id.tv_city)
    private TextView tv_city;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_reCode)
    private TextView tv_reCode;

    @ViewInject(R.id.iv_ewm)
    private ImageView iv_ewm;
    private Uri imageUri, newUri;

    @Override
    public int setLayout() {
        return R.layout.person_information;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.grzl));
    }

    @Override
    public void setListeners() {
        rl_head.setOnClickListener(this);
        rl_cityChoic.setOnClickListener(this);
        rl_restPwd.setOnClickListener(this);
        bt_loginOut.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        iv_ewm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wdjl:
                showHead();
                break;
            case R.id.rl_cityChoic:
                intent = new Intent(context, CityChoice.class);
                startActivity(intent);
                break;
            case R.id.rl_restPwd:
                intent = new Intent(context, ResetPwd.class);
                intent.putExtra("phone", loginEntity.PHONE);
                startActivity(intent);
                break;
            case R.id.bt_loginOut:
                SharedPreferencesUtils.putSharedPreferences(context, Constant.USER_ID, "");
                SharedPreferencesUtils.putbooleanSharedPreferences(context, Constant.USER_ALISA, false);

                intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_name:
                intent = new Intent(context, ResetName.class);
                startActivity(intent);
                break;
            case R.id.iv_ewm:
                if (loginEntity != null)
                    ShareUtils.showShare(PersonInforMation.this, Constant.IMAGE_API + loginEntity.TWO_CODE+SharedPreferencesUtils.getSharedPreferences(context,Constant.EWM_KEY));
                break;
        }
    }


    /**
     * 头像选择
     */
    private void showHead() {
        new MySelfSheetDialog(PersonInforMation.this).builder().addSheetItem("拍照", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                if (CommonUtil.checkPersiomion(PersonInforMation.this, Manifest.permission.CAMERA)) {
                    photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                    file = new File(photoSavePath + photoSaveName);
                    file.getParentFile().mkdirs();

                    //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //7.0
                        imageUri = FileProvider.getUriForFile(context, "com.ipd.bangbanjinrong.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                    } else{
                        imageUri = Uri.fromFile(file);
                    }

                    // 相机
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //添加权限

                    openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//不保存数据到Intent
                    startActivityForResult(openCameraIntent, PHOTOTAKE);
                }


            }
        }).addSheetItem("从相册选择", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {

            @Override
            public void onClick(int which) {
//                加上读取权限
                if (CommonUtil.checkPersiomion(PersonInforMation.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, PHOTOZOOM);
                }

            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent = new Intent();

        switch (requestCode) {

            case PHOTOZOOM:// 相册
                if (data == null)
                    return;
                cropRawPhoto(data.getData());
                break;
            case PHOTOTAKE:// 拍照
                LogUtils.print("file.length", file.length() + "");
                long length = file.length();
                if (file.length() <= 0)
                    return;
                cropRawPhoto(imageUri);
                break;
            case IMAGE_COMPLETE:// 完成
//                if (data == null)
//                    return;
                mFlie = ImageCompressUtil.compressImage(newUri.getPath());
                upLoadPhoto(mFlie);

                break;


        }
    }


    /**
     * 上传头像
     */
    private void upLoadPhoto(File inputStream) {
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_pic/upload.do");
        params.addBodyParameter("APPUSER_ID", SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID));
        params.addBodyParameter("DIR", "user");
        params.addBodyParameter("Cut", "0");

        params.addBodyParameter("PIC", inputStream, null, inputStream.getName());
//        params.addBodyParameter("PICS", null, null);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        newLogo = json.getString("data");
                        updateInformation();
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.print("exLo", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    /**
     * 获取个人信息资料
     */
    private void getInfor() {
        dialog();
        String[] keys = {"APPUSER_ID"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)};
        String json = JsonUtil.getJsonString(keys, values);
        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/get.do");
        params.setBodyContent(json);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("infor", result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        Gson gson = new Gson();
                        loginEntity = gson.fromJson(json.getString("data"), LoginEntity.class);
                        setData();
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    /**
     * 更新个人信息
     */
    private void updateInformation() {
        dialog();
        String[] keys = {"APPUSER_ID", "LOGO"};
        String[] values = {SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID)
                , newLogo};
        String json = JsonUtil.getJsonString(keys, values);

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_user/update.do");
        params.setBodyContent(json);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.print("upDa", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("response").equals("0")) {
                        ToastUtil.showShort(context, "更新成功！");

                        RequestOptions requestOptions = RequestOptions.circleCropTransform();
                        Glide.with(context).load(Constant.IMAGE_API + newLogo).apply(requestOptions).into(iv_photo);

                        RequestOptions requestOptions1 = RequestOptions.centerCropTransform();
                        String erwKey = "?" + String.valueOf(SystemClock.currentThreadTimeMillis());
                        SharedPreferencesUtils.putSharedPreferences(context, Constant.EWM_KEY, erwKey);
                        Glide.with(context).load(Constant.IMAGE_API + loginEntity.TWO_CODE + erwKey).apply(requestOptions1).into(iv_ewm);
                    } else
                        ToastUtil.showShort(context, jsonObject.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.print("updateInformationex", ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void setData() {
        if (loginEntity == null)
            return;

        if (TextUtils.isEmpty(newLogo)) {
            RequestOptions requestOptions = RequestOptions.circleCropTransform().placeholder(R.mipmap.defalt_photo).error(R.mipmap.defalt_photo);
            Glide.with(context).load(Constant.IMAGE_API + loginEntity.LOGO).apply(requestOptions).into(iv_photo);
        }
        RequestOptions requestOptions1 = RequestOptions.centerCropTransform();
        String key = SharedPreferencesUtils.getSharedPreferences(context, Constant.EWM_KEY);
        Glide.with(context).load(Constant.IMAGE_API + loginEntity.TWO_CODE + key).apply(requestOptions1).into(iv_ewm);


//        RequestOptions requestOptions1 = RequestOptions.centerCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE);
//        ImageOptions imageOptions=new ImageOptions.Builder().setUseMemCache(false).setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
//        x.image().bind(iv_ewm,Constant.IMAGE_API + loginEntity.TWO_CODE,imageOptions);
        tv_trueName.setText(loginEntity.TRUENAME);
        tv_city.setText(loginEntity.REGION);
        tv_phone.setText(loginEntity.PHONE);
        tv_reCode.setText(loginEntity.CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfor();

    }


    private void cropRawPhoto(Uri uri) {
        File file = new File(Constant.IMAGE_CACH_DIRECTORY + SystemClock.currentThreadTimeMillis() + ".png");
        file.getParentFile().mkdirs();
        newUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0后需要次权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", "200");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("noFaceDetection", true);//取消人脸识别功能
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, newUri);//不保存数据到Intent
        startActivityForResult(intent, IMAGE_COMPLETE);
    }
}
