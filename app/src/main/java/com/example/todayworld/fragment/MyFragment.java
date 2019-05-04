package com.example.todayworld.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.todayworld.Main2Activity;
import com.example.todayworld.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONObject;


public class MyFragment extends Fragment {

    ImageView login;
    boolean isServerSideLogin = false;
    public static Tencent mTencent;
    private UserInfo userInfo; //qq用户信息
    private IUiListener userInfoListener; //获取用户信息监听器

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_my, container, false);

        login=messageLayout.findViewById(R.id.userImage);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTencent = Tencent.createInstance("101568619", getActivity());
                if (mTencent!=null&&!mTencent.isSessionValid()) {
                    mTencent.login(getActivity(), "all", loginListener);
                    isServerSideLogin = false;
                    Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                } else {
                    if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                        mTencent.logout(getActivity());
                        mTencent.login(getActivity(), "all", loginListener);
                        isServerSideLogin = false;
                        Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                        return;
                    }
                    if (mTencent!=null)
                        mTencent.logout(getActivity());
                }
            }
        });
        return messageLayout;
    }

    //点击按钮之后QQ登录
    public void loginQQ(){
        //初始化，得到APPID
        mTencent = Tencent.createInstance("101568619", getActivity());
        if (!mTencent.isSessionValid()) {
            mTencent.login(getActivity(), "all", loginListener);
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(getActivity());
                mTencent.login(getActivity(), "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(getActivity());
        }
    }



    //初始化OPENID和TOKEN值（为了得了用户信息）
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        public void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
        }
    };


    //实现回调
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();
                return;
            }
//            Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
            Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();

            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);
        }


        public void doComplete(JSONObject values) {
            System.out.println((JSONObject) values);
            Toast.makeText(getActivity(), "Your Nickname is，" + values, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getActivity(), Main2Activity.class);
            //intent.putExtra("nickname", (String) values);
            //intent.putExtra("logo", icon);
            startActivity(intent);
        }

        @Override
        public void onError(UiError e) {
//            Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
//            Util.dismissDialog();
            Toast.makeText(getActivity(),e.errorDetail,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
//            Util.toastMessage(MainActivity.this, "onCancel: ");
//            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }


    //QQ登录后的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
            //MainActivity.this.finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
