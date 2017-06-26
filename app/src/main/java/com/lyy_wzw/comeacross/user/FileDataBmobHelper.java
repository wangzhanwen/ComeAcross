package com.lyy_wzw.comeacross.user;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by yidong9 on 17/6/21.
 */

public class FileDataBmobHelper {
    private static FileDataBmobHelper instance;
    private FileDataBmobHelper(){}
    public static synchronized FileDataBmobHelper getInstance(){
        if (instance == null) {
            instance = new FileDataBmobHelper();
        }
        return instance;
    }

    public interface UserUpLoadFileCallback{
        void onSuccess(BmobFile bmobFile);
        void onError(BmobException e);
        void onProgress(Integer value);
    }

    public interface UserUpLoadBatchFileCallback{
        void onSuccess(List<BmobFile> files, List<String> urls);
        void onError(int statuscode, String errormsg);
        void onProgress(int curIndex, int curPercent, int total,int totalPercent);
    }

    /**
     * 长传单一文件
     * @param path
     * @param userUpLoadFileCallback
     */
    public void upLoadFile(String path, final UserUpLoadFileCallback userUpLoadFileCallback){
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //toast("上传文件成功:" + bmobFile.getFileUrl());
                    userUpLoadFileCallback.onSuccess(bmobFile);
                }else{
                    //toast("上传文件失败：" + e.getMessage());
                    userUpLoadFileCallback.onError(e);
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                userUpLoadFileCallback.onProgress(value);
            }
        });
    }


    public void upLoadFileBatch(final String[] filePaths, final UserUpLoadBatchFileCallback userUpLoadBatchFileCallback){

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                    userUpLoadBatchFileCallback.onSuccess(files, urls);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                //ShowToast("错误码"+statuscode +",错误描述："+errormsg);
                userUpLoadBatchFileCallback.onError(statuscode, errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                userUpLoadBatchFileCallback.onProgress(curIndex,  curPercent,  total, totalPercent);

            }
        });
    }

}
