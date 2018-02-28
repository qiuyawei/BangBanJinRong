package com.ipd.bangbanjinrong.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.base.BaseActivity;
import com.ipd.bangbanjinrong.global.Constant;
import com.ipd.bangbanjinrong.utils.CommonUtil;
import com.ipd.bangbanjinrong.utils.ImageCompressUtil;
import com.ipd.bangbanjinrong.utils.SharedPreferencesUtils;
import com.ipd.bangbanjinrong.utils.ToastUtil;
import com.ipd.bangbanjinrong.widget.MySelfSheetDialog;
import com.jumpbox.jumpboxlibrary.bitmap.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import static com.ipd.bangbanjinrong.global.Constant.PHOTOTAKE;
import static com.ipd.bangbanjinrong.global.Constant.PHOTOZOOM;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/9
 * @Email 448739075@qq.com
 * 过桥垫资 第二步
 */

public class GuoQiaoDianZi2 extends BaseActivity {
    private Uri imageUri;
    private File file, file1, file2, file3,file4,file5;
    private String content;
    private String photoSaveName, photoSavePath = Constant.IMAGE_CACH_DIRECTORY ;
    private String path, path1, path2, path3,path4,path5;
    private int pos = 1;
    @ViewInject(R.id.bt_next)
    private Button bt_next;

    @ViewInject(R.id.iv_fczbhy)
    private ImageView iv1;
    @ViewInject(R.id.iv_fczzy)
    private ImageView iv2;
    @ViewInject(R.id.iv_fcfjy)
    private ImageView iv3;
    @ViewInject(R.id.iv_sfzzm)
    private ImageView iv4;
    @ViewInject(R.id.iv_sfzfm)
    private ImageView iv5;
    private ArrayList<String> pics = new ArrayList<>();

    @Override
    public int setLayout() {
        return R.layout.di_ya_bao_dan2;
    }

    @Override
    public void innitData() {
        setBack();
        setTopTitle(getResources().getString(R.string.guo_qiao_dian_zi));
        content = getIntent().getStringExtra("content");
    }

    @Override
    public void setListeners() {
        bt_next.setOnClickListener(this);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
//        iv4.setOnClickListener(this);
//        iv5.setOnClickListener(this);
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=4;
                showHead();

            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=5;
                showHead();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (TextUtils.isEmpty(path1) || TextUtils.isEmpty(path2) || TextUtils.isEmpty(path3) || TextUtils.isEmpty(path4) || TextUtils.isEmpty(path5)) {
                    ToastUtil.showShort(context, "请上传照片");
                    return;
                }
                dialog();
                new AsyncTask<Void, String, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        file1 = ImageCompressUtil.compressImage(path1);
                        file2 = ImageCompressUtil.compressImage(path2);
                        file3 = ImageCompressUtil.compressImage(path3);
                        file4 = ImageCompressUtil.compressImage(path4);
                        file5 = ImageCompressUtil.compressImage(path5);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        upLoadPhoto();
                    }
                }.execute();
                break;
            case R.id.iv_fczbhy:
                pos = 1;
                showHead();
                break;
            case R.id.iv_fczzy:
                pos = 2;
                showHead();
                break;
            case R.id.iv_fcfjy:
                Log.i("TAG", "=");
                pos = 3;
                showHead();
            /*case R.id.iv_sfzzm:
                pos = 4;
                showHead();
            case R.id.iv_sfzfm:
                pos = 5;
                showHead();
                break;*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent = new Intent();

        switch (requestCode) {
            case PHOTOZOOM:// 相册
                if (data == null)
                    return;
                path = BitmapUtils.getInstance().getPath(this, data.getData());
                if (pos == 1) {
                    path1 = path;
                    Glide.with(context).load(path1).into(iv1);
                } else if (pos == 2) {
                    path2 = path;
                    Glide.with(context).load(path2).into(iv2);
                } else if (pos == 3) {
                    path3 = path;
                    Glide.with(context).load(path3).into(iv3);
                } else if (pos == 4) {
                    path4 = path;
                    Glide.with(context).load(path4).into(iv4);
                } else if (pos == 5) {
                    path5 = path;
                    Glide.with(context).load(path5).into(iv5);
                }

                break;
            case PHOTOTAKE:// 拍照
                long length = file.length();
                if (length <= 0)
                    return;
                path = photoSavePath + photoSaveName;
                if (pos == 1) {
                    path1 = path;
                    Glide.with(context).load(path1).into(iv1);
                } else if (pos == 2) {
                    path2 = path;
                    Glide.with(context).load(path2).into(iv2);
                } else if (pos == 3) {
                    path3 = path;
                    Glide.with(context).load(path3).into(iv3);
                } else if (pos == 4) {
                    path4 = path;
                    Glide.with(context).load(path4).into(iv4);
                } else {
                    path5 = path;
                    Glide.with(context).load(path5).into(iv5);
                }

        }
    }

    /**
     * 头像选择
     */
    private void showHead() {
        new MySelfSheetDialog(context).builder().addSheetItem("拍照", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                if (CommonUtil.checkPersiomion(GuoQiaoDianZi2.this, Manifest.permission.CAMERA)) {
                    photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                    file = new File(photoSavePath + photoSaveName);
                    file.getParentFile().mkdirs();

                    //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //7.0
                        imageUri = FileProvider.getUriForFile(context, "com.ipd.bangbanjinrong.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                    } else {
                        imageUri = Uri.fromFile(file);
                    }

                    // 相机
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//不保存数据到Intent
                    startActivityForResult(openCameraIntent, PHOTOTAKE);
                }


            }
        }).addSheetItem("从相册选择", MySelfSheetDialog.SheetItemColor.Blue, new MySelfSheetDialog.OnSheetItemClickListener() {

            @Override
            public void onClick(int which) {
//                加上读取权限
                if (CommonUtil.checkPersiomion(GuoQiaoDianZi2.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, PHOTOZOOM);
                }

            }
        }).show();

    }


    /**
     * 上传房产照片
     */
    private void upLoadPhoto() {
        pics.clear();

        RequestParams params = new RequestParams(Constant.SEVER_API + "app_pic/upload.do");
        params.addBodyParameter("APPUSER_ID", SharedPreferencesUtils.getSharedPreferences(context, Constant.USER_ID));
        params.addBodyParameter("DIR", "order");
//        params.addBodyParameter("PIC", null, null);
        params.addBodyParameter("PICS", file1, null, file1.getName());
        params.addBodyParameter("PICS", file2, null, file2.getName());
        params.addBodyParameter("PICS", file3, null, file3.getName());
        params.addBodyParameter("PICS", file4, null, file4.getName());
        params.addBodyParameter("PICS", file5, null, file5.getName());

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("response").equals("0")) {
                        pics.add(path1);
                        pics.add(path2);
                        pics.add(path3);
                        pics.add(path4);
                        pics.add(path5);

                        content = content + json.getString("data2");
                        intent = new Intent(GuoQiaoDianZi2.this, GuoQiaoDianZi3.class);
                        intent.putStringArrayListExtra("pics", pics);
                        intent.putExtra("content", content);
                        startActivity(intent);
                    } else
                        ToastUtil.showShort(context, json.getString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "ex=" + ex.getMessage());

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
}
