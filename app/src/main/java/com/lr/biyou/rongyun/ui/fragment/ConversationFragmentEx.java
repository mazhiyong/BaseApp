package com.lr.biyou.rongyun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.im.provider.ForwardClickActions;
import com.lr.biyou.rongyun.ui.activity.ConversationActivity;
import com.lr.biyou.rongyun.ui.activity.GroupReadReceiptDetailActivity;
import com.lr.biyou.rongyun.ui.dialog.EvaluateBottomDialog;
import com.lr.biyou.ui.moudle2.activity.RedMoneyActivity;
import com.lr.biyou.ui.moudle2.activity.SelectContractListActivity;
import com.lr.biyou.ui.moudle2.activity.TransferMoneyActivity;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.rong.common.RLog;
import io.rong.contactcard.activities.ContactDetailActivity;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.actions.IClickActions;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.message.TextMessage;

import static io.rong.contactcard.ContactCardPlugin.IS_FROM_CARD;

/**
 * 会话 Fragment 继承自ConversationFragment
 * onResendItemClick: 重发按钮点击事件. 如果返回 false,走默认流程,如果返回 true,走自定义流程
 * onReadReceiptStateClick: 已读回执详情的点击事件.
 * 如果不需要重写 onResendItemClick 和 onReadReceiptStateClick ,可以不必定义此类,直接集成 ConversationFragment 就可以了
 */
public class ConversationFragmentEx extends ConversationFragment {
    private OnShowAnnounceListener onShowAnnounceListener;
    private RongExtension rongExtension;
    private ListView listView;
    private static final int REQUEST_CONTACT = 55;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        rongExtension = (RongExtension) v.findViewById(io.rong.imkit.R.id.rc_extension);
        View messageListView = findViewById(v, io.rong.imkit.R.id.rc_layout_msg_list);
        listView = findViewById(messageListView, io.rong.imkit.R.id.rc_list);
        return v;
    }


    /**
     * 回执消息的点击监听，
     *
     * @param message
     */
    @Override
    public void onReadReceiptStateClick(io.rong.imlib.model.Message message) {
        if (message.getConversationType() == Conversation.ConversationType.GROUP) { //目前只适配了群组会话
            // 群组显示未读消息的人的信息
            Intent intent = new Intent(getActivity(), GroupReadReceiptDetailActivity.class);
            intent.putExtra(IntentExtra.PARCEL_MESSAGE, message);
            getActivity().startActivity(intent);
        }
    }

    // 警告 dialog
    @Override
    public void onWarningDialog(String msg) {
        String typeStr = getUri().getLastPathSegment();
        if (!typeStr.equals("chatroom")) {
            super.onWarningDialog(msg);
        }
    }

    @Override
    public void onShowAnnounceView(String announceMsg, String announceUrl) {
        // 此处为接收到通知消息， 然后回调到 activity 显示。
        if (onShowAnnounceListener != null) {
            onShowAnnounceListener.onShowAnnounceView(announceMsg, announceUrl);
        }
    }


    @Override
    public void onShowStarAndTabletDialog(String dialogId) {
        // 评星的dialog 或者自定义评价 dialog 可在此自定义显示
        showEvaluateDialog(dialogId);
    }

    @Override
    public List<IClickActions> getMoreClickActions() {
        List<IClickActions> actions = new ArrayList();
        actions.addAll(super.getMoreClickActions());
        actions.add(0, new ForwardClickActions());
        return actions;
    }

    /**
     * 输入区Plugin 按钮点击监听。
     *
     * @param v
     * @param extensionBoard
     */
    @Override
    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
        // 当点击输入去 Plugin （+）的切换按钮后， 则是消息列表显示最后一条。
        setMessageListLast();
    }


    /**
     * 输入区表情切换按钮的监听
     *
     * @param v
     * @param extensionBoard
     */
    @Override
    public void onEmoticonToggleClick(View v, ViewGroup extensionBoard) {
        // 当点击输入去表情的切换按钮后， 则是消息列表显示最后一条。
        setMessageListLast();
    }

    @Override
    public void onPluginClicked(IPluginModule pluginModule, int position) {
        Intent intent;
        switch (position){
            case 3: //名片
                intent = new Intent(getActivity(), SelectContractListActivity.class);
                intent.putExtra("TYPE","5");
                intent.putExtra(IS_FROM_CARD,true);
                startActivityForResult(intent, REQUEST_CONTACT);

                break;
            case 4: //红包
                intent = new Intent(getActivity(), RedMoneyActivity.class);
                ConversationActivity activity = (ConversationActivity) getActivity();
                if (!UtilTools.empty(activity)){
                    intent.putExtra("tarid",activity.targetId);
                    if (activity.conversationType == Conversation.ConversationType.PRIVATE){
                        intent.putExtra("type","1");
                        intent.putExtra("id",activity.Id);
                    }else {
                        intent.putExtra("type","2");
                    }
                    startActivity(intent);
                }
                break;
            case 5: //转账
                intent = new Intent(getContext(), TransferMoneyActivity.class);
                ConversationActivity activity1 = (ConversationActivity) getActivity();
                if (!UtilTools.empty(activity1)){
                    intent.putExtra("tarid",activity1.targetId);
                    if (activity1.conversationType == Conversation.ConversationType.PRIVATE){
                        intent.putExtra("type","1");
                        intent.putExtra("id",activity1.Id);
                        startActivity(intent);
                    }else {
                        intent.putExtra("type","2");
                        Toast.makeText(getActivity(),"群聊不支持转账功能",Toast.LENGTH_LONG).show();
                    }
                }

               /* final String[] stringItems = {"拍照", "从手机相册选择"};
                final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, null);
                dialog.isTitleShow(false).show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        switch (position) {
                            case 0:
                                PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                                    @Override
                                    public void requestSuccess() {
                                        //toast(R.string.successfully);
                                        photoPic();
                                    }

                                    @Override
                                    public void requestFailer() {
                                       // toast(R.string.failure);
                                    }
                                }, Permission.Group.STORAGE, Permission.Group.CAMERA);


                                break;
                            case 1:
                                PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                                    @Override
                                    public void requestSuccess() {
                                        //toast(R.string.successfully);
                                        localPic();
                                    }

                                    @Override
                                    public void requestFailer() {
                                        //toast(R.string.failure);
                                    }
                                }, Permission.Group.STORAGE);


                                break;
                        }

                    }
                });
*/

                break;
            default:
                super.onPluginClicked(pluginModule, position);
                break;
        }

    }

    //发送消息
    @Override
    public void onSendToggleClick(View v, String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            RLog.e(TAG, "text content must not be null");
            return;
        }


        ConversationActivity activity = (ConversationActivity) getActivity();
        if (!UtilTools.empty(activity)){
            //发送消息(文本)
            if (getConversationType() == Conversation.ConversationType.PRIVATE) {
                //私聊发送消息
                activity.sendMessageAction(text);
                TextMessage textMessage = TextMessage.obtain(text);
                MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
                if (mentionedInfo != null) {
                    if (mentionedInfo.getMentionedUserIdList().contains("-1")) {
                        mentionedInfo.setType(MentionedInfo.MentionedType.ALL);
                    } else {
                        mentionedInfo.setType(MentionedInfo.MentionedType.PART);
                    }
                    textMessage.setMentionedInfo(mentionedInfo);
                }
                io.rong.imlib.model.Message message = io.rong.imlib.model.Message.obtain(getTargetId(), getConversationType(), textMessage);
                RongIM.getInstance().sendMessage(message, null, null, (IRongCallback.ISendMessageCallback) null);

            } else if (getConversationType()== Conversation.ConversationType.GROUP) {
                //群聊设置发送消息(文本)
                activity.sendGroupMessageAction(text,getTargetId(),getConversationType());

            }

        }

    }




    @Override
    public boolean showMoreClickItem() {
        return true;
    }


    /**
     * 会话界面设置最后一条
     */
    private void setMessageListLast() {
        if (!rongExtension.isExtensionExpanded()) {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.requestFocusFromTouch();
                    listView.setSelection(listView.getCount());
                }
            }, 100);
        }
    }


    /**
     * 显示客服评价的dialog。
     *
     * @param dialogId
     */
    private void showEvaluateDialog(final String dialogId) {
        EvaluateBottomDialog.Builder builder = new EvaluateBottomDialog.Builder();
        builder.setTargetId(getTargetId());
        builder.setDialogId(dialogId);
        EvaluateBottomDialog dialog = builder.build();
        dialog.setOnEvaluateListener(new EvaluateBottomDialog.OnEvaluateListener() {
            @Override
            public void onCancel() {
                FragmentActivity activity = getActivity();
                if(activity != null){
                    activity.finish();
                }
            }

            @Override
            public void onSubmitted() {
                FragmentActivity activity = getActivity();
                if(activity != null){
                    activity.finish();
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), dialogId);
    }

    /**
     * 设置通知信息回调。
     *
     * @param listener
     */
    public void setOnShowAnnounceBarListener(OnShowAnnounceListener listener) {
        onShowAnnounceListener = listener;
    }

    /**
     * 显示通告栏的监听器
     */
    public interface OnShowAnnounceListener {

        /**
         * 展示通告栏的回调
         *
         * @param announceMsg 通告栏展示内容
         * @param annouceUrl  通告栏点击链接地址，若此参数为空，则表示不需要点击链接，否则点击进入链接页面
         * @return
         */
        void onShowAnnounceView(String announceMsg, String annouceUrl);
    }



    @Override
    public void onImageResult(LinkedHashMap<String, Integer> selectedMedias, boolean origin) {
        super.onImageResult(selectedMedias, origin);
        Log.i("show","图片&&&&:"+selectedMedias.toString());
    }

  /*  private void photoPic() {
        *//**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的，这个地方也错了，必须改正
         *//*
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getActivity(), AppUtil.getAppProcessName(getActivity()) + ".FileProvider", new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        } else {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    private void localPic() {

        *//**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         *//*

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        *//**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         *//*
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

*/
    private Intent dataIntent = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
            intent.putExtra("DATA", data.getSerializableExtra("DATA"));
            ConversationActivity activity1 = (ConversationActivity) getActivity();
            if (!UtilTools.empty(activity1)){
                intent.putExtra("conversationType", activity1.conversationType);
                intent.putExtra("targetId", activity1.targetId);
            }
            startActivity(intent);
        }


      /*  Uri uri = null;
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                    Log.i("show","Uri:"+data.getData());
                    Toast.makeText(getActivity(),"发送图片",Toast.LENGTH_SHORT).show();
                    Uri mUri = Uri.fromFile(new File("file:///storage/emulated/0/Pictures/1568937085588.jpg=1"));
                    Log.i("show","mUri:"+mUri);

                    ImageMessage imageMessage = ImageMessage.obtain(mUri,mUri);
                    ConversationActivity activity1 = (ConversationActivity) getActivity();
                    RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, activity1.targetId,imageMessage , null, null, new RongIMClient.SendImageMessageCallback() {

                        @Override
                        public void onAttached(Message message) {
                            //保存数据库成功
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode code) {
                            //发送失败
                        }

                        @Override
                        public void onSuccess(Message message) {
                            //发送成功
                        }

                        @Override
                        public void onProgress(Message message, int progress) {
                            //发送进度
                        }
                    });
                }
                break;
            // 如果是调用相机拍照时
            case 2:

                File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
                if (temp.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(getActivity(), AppUtil.getAppProcessName(getActivity()) + ".FileProvider", temp);
                    } else {
                        uri = Uri.fromFile(temp);
                    }
                    startPhotoZoom(uri);
                }

                //			path=photoSavePath+photoSaveName;
                //			uri = Uri.fromFile(new File(path));
                //			Intent intent2=new Intent(getActivity(), ClipActivity.class);
                //			intent2.putExtra("path", path);
                //			startActivityForResult(intent2, 4);
                break;
            // 取得裁剪后的图片
            case 3:
                *//**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只
                 * 在这个地方加下，大家可以根据不同情况在合适的
                 * 地方做判断处理类似情况
                 *
                 *//*
            *//*    Toast.makeText(BusinessInfoActivity.this,"EEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",Toast.LENGTH_LONG).show();
                if(data != null){
                    Toast.makeText(BusinessInfoActivity.this,"QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ",Toast.LENGTH_LONG).show();
                    dataIntent = data;
                    setPicToView(data);
                }else {
                    Toast.makeText(BusinessInfoActivity.this,"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",Toast.LENGTH_LONG).show();

                }
*//*

                // 将Uri图片转换为Bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uritempFile));
                    // TODO，将裁剪的bitmap显示在imageview控件上
                    Drawable dr = new BitmapDrawable(getResources(), bitmap);
                    saveCroppedImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                //ImageLoader.getmContext().displayImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage);
                // UtilTools.showImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage, R.drawable.no_def);

                break;
            default:
                break;

        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     *//*
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            uploadPic();
        }
    }

    private void uploadPic() {
        if (dataIntent != null) {
            Bundle extras = dataIntent.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo == null) {
                    return;
                }
                saveCroppedImage(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);

            }
        } else {
            Toast.makeText(getActivity(), "图片不存在", Toast.LENGTH_SHORT).show();
        }
    }


    private String mHeadImgPath = "";

    private void saveCroppedImage(Bitmap bmp) {

        try {
            File saveFile = new File(MbsConstans.HEAD_IMAGE_PATH);

            mHeadImgPath = MbsConstans.HEAD_IMAGE_PATH + new Date().getTime() + ".png";
            File file = new File(mHeadImgPath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            File saveFile2 = new File(mHeadImgPath);

            FileOutputStream fos = new FileOutputStream(saveFile2);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // uploadAliPic(new Date().getTime()+".png",filepath);

            //上传头像
            //uploadPicAction();



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String imgName = "";
    private Uri uritempFile;

    *//**
     * 裁剪图片方法实现
     *
     * @param uri
     *//*
    public void startPhotoZoom(Uri uri) {
        *//*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         *//*
    *//*    Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//**//*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);*//*
        File file = new File(MbsConstans.BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        // intent.putExtra("noFaceDetection", true);
        // 宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);

        // intent.putExtra("scale", true);
        // intent.putExtra("return-data", true);
        // this.startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);
        *//**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         *//*
        imgName = System.currentTimeMillis() + ".jpg";
        uritempFile = Uri.parse("file:///" + MbsConstans.BASE_PATH + "/" + imgName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);

    }

*/
}
