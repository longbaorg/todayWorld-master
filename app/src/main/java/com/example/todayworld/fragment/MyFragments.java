package com.example.todayworld.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todayworld.R;
import com.example.todayworld.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;


public class MyFragments extends Fragment {
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "101568619";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    public static String openidString;
    private UserInfo mUserInfo;
    private TextView nicknameTextView;
    private ImageView userlogo;
    private TextView openidTextView;
    private String nicknameString;
    Bitmap bitmap = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_my_fragments, container, false);
        //用来显示OpenID的textView
        openidTextView = messageLayout.findViewById(R.id.user_id);
        //用来显示昵称的textview
        nicknameTextView =  messageLayout.findViewById(R.id.name);
        //用来显示头像的Imageview
        userlogo =  messageLayout.findViewById(R.id.image);
        userlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mIUiListener = new MyFragment.BaseUiListener();
                mIUiListener = new MyFragments.BaseUiListener();
                mTencent.login(getActivity(),"all", mIUiListener);
            }
        });
        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID, MyFragments.this.getContext());
        return messageLayout;
    }

//    public void buttonLogin(View v){
//        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
//         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
//         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
//        mIUiListener = new BaseUiListener();
//        //all表示获取所有权限
//        mTencent.login(getActivity(),"all", mIUiListener);
//    }



    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    public class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(final Object response) {
                        Log.e(TAG,"登录成功"+response.toString());
                        try {
                            //获得的数据是JSON格式的，获得你想获得的内容
                            //如果你不知道你能获得什么，看一下下面的LOG
                            Log.e(TAG, "-------------"+response.toString());
                            openidString = ((JSONObject) response).getString("openid");
                            openidTextView.setText(openidString);
                            Log.e(TAG, "-------------"+openidString);
                            //access_token= ((JSONObject) response).getString("access_token");              //expires_in = ((JSONObject) response).getString("expires_in");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        QQToken qqToken = mTencent.getQQToken();
                        UserInfo info = new UserInfo(getContext(), qqToken);
                        info.getUserInfo(new IUiListener() {
                            @Override
                            public void onComplete(Object o) {
                                Log.e(TAG, "---------------111111");
                                Message msg = new Message();
                                msg.obj = response;
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                                Log.e(TAG, "-----111---"+response.toString());
                                /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                                 * 在mHandler里进行操作
                                 *
                                 */
                                new Thread(){

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        JSONObject json = (JSONObject)response;
                                        try {
                                            bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        Message msg = new Message();
                                        msg.obj = bitmap;
                                        msg.what = 1;
                                        mHandler.sendMessage(msg);
                                    }
                                }.start();
                            }

                            @Override
                            public void onError(UiError uiError) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();

        }

    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        nicknameString = response.getString("nickname");

                        nicknameTextView.setText(nicknameString);
                        Log.e(TAG, "--"+ nicknameString);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                userlogo.setImageBitmap(bitmap);

            }
        }

    };

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
