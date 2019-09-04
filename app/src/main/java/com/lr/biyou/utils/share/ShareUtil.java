package com.lr.biyou.utils.share;

import android.content.Context;

/**
 *
 */
public class ShareUtil {

    public static void showShare(Context mContext,String title,String text,String url) {
//        OnekeyShare oks = new OnekeyShare();
//        /*oks.addHiddenPlatform(QQ.NAME);
//        oks.setImageData();
//        oks.setSilent(true);*/
//        oks.disableSSOWhenAuthorize();
//        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//            @Override
//            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
//                if ("SinaWeibo".equals(platform.getName())) {
//                    paramsToShare.setText("玩美夏日，护肤也要肆意玩酷！" + "www.mob.com");
//                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    /*paramsToShare.setFilePath(ResourcesManager.getInstace(MobSDK.getContext()).getFilePath());*/
//                    /*paramsToShare.setUrl("http://sharesdk.cn");*/
//                }
//                if ("Wechat".equals(platform.getName())) {
//                    paramsToShare.setTitle(title);
//                    paramsToShare.setText(text);
//                    /*paramsToShare.setWxUserName("");
//                    paramsToShare.setW*/
//                    /*Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//                    paramsToShare.setImageData(imageData);*/
//                    //paramsToShare.setImageUrl("http://scene3d.4dage.com/images/imagesZrbrfZzI/thumbSmallImg.jpg?m=7");
//                    paramsToShare.setUrl(url);
//                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
//                    Log.d("ShareSDK", paramsToShare.toMap().toString());
//                    //Toast.makeText(MainActivity.this, "点击微信分享啦", Toast.LENGTH_SHORT).show();
//                }
//                if ("WechatMoments".equals(platform.getName())) {
//                    paramsToShare.setTitle(title);
//                    paramsToShare.setText(text);
//                    //paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    paramsToShare.setUrl(url);
//                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
//                }
//                if ("WechatFavorite".equals(platform.getName())) {
//                    paramsToShare.setTitle(title);
//                    paramsToShare.setText(text);
//                    //paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    paramsToShare.setUrl(url);
//                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
//                }
//                if ("QQ".equals(platform.getName())) {
//                    paramsToShare.setTitle(title);
//                    paramsToShare.setTitleUrl(url);
//                    paramsToShare.setText(text);
//                    //paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                }
//                if ("Facebook".equals(platform.getName())) {
//                    paramsToShare.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
//                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                }
//                if("Twitter".equals(platform.getName())){
//                    paramsToShare.setText("我是共用的参数，这几个平台都有text参数要求，提取出来啦");
//                    paramsToShare.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
//                    /*paramsToShare.setUrl("http://sharesdk.cn");*/
//                }
//                if("ShortMessage".equals(platform.getName())){
//                    paramsToShare.setText(text+"\n"+url);
//                    paramsToShare.setTitle(title);
//                }
//                if("Email".equals(platform.getName())){
//                    paramsToShare.setText(text+"\n"+url);
//                    paramsToShare.setTitle(title);
//                }
//            }
//        });
//
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Log.d("ShareLogin", "onComplete ---->  分享成功");
//                platform.getName();
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace());
//                Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
//                Log.d("ShareLogin", "onError ---->  失败" + i);
//                Log.d("ShareLogin", "onError ---->  失败" + platform);
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                Log.d("ShareLogin", "onCancel ---->  分享取消");
//            }
//        });
//
//        // 启动分享GUI
//        oks.show(mContext);
    }





  //  public static void weXinShare(String text, String title, String url, Bitmap bitmap, String imgUrl, SettingActivity.MyPlatformActionListener platformActionListener){
       /* Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        shareParams.setText(text);
        shareParams.setTitle(title);
        shareParams.setUrl(url);
        shareParams.setImageData(bitmap);
        shareParams.setImageUrl(imgUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(platformActionListener);*/
		/*platform.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
				Log.d("ShareLogin", "onComplete ---->  分享成功");
				platform.getName();
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace());
				Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
				Log.d("ShareLogin", "onError ---->  失败" + i);
				Log.d("ShareLogin", "onError ---->  失败" + platform);
				throwable.printStackTrace();
			}

			@Override
			public void onCancel(Platform platform, int i) {
				Log.d("ShareLogin", "onCancel ---->  分享取消");
			}
		});*/
//        platform.share(shareParams);
//   }

   /* public static void qqShare(String text, String title, String url, String imgUrl, SettingActivity.MyPlatformActionListener platformActionListener){
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        shareParams.setText(text);
        shareParams.setTitle(title);
        shareParams.setTitleUrl(url);
        shareParams.setImageUrl(imgUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        platform.setPlatformActionListener(platformActionListener);
		*//*platform.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
				Log.d("ShareLogin", "onComplete ---->  分享成功");
				platform.getName();
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace());
				Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
				Log.d("ShareLogin", "onError ---->  失败" + i);
				throwable.printStackTrace();
			}

			@Override
			public void onCancel(Platform platform, int i) {
				Log.d("ShareLogin", "onCancel ---->  分享取消");
			}
		});*//*
        platform.share(shareParams);
    }
*/

	/*public static void shareEmail(){
		Platform platform = ShareSDK.getPlatform(Email.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public static void shareSMS(){
		Platform platform = ShareSDK.getPlatform(ShortMessage.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}*/


}

