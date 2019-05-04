package com.example.todayworld;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.example.todayworld.fragment.GIFFragment;
import com.example.todayworld.fragment.MyFragments;
import com.example.todayworld.fragment.NewsFragment;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 *
 */
/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 *
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 用于展示新闻的Fragment
     */
    private NewsFragment newFragment;

    /**
     * 用于展示GIF的Fragment
     */
    private GIFFragment gifFragment;

    /**
     * 用于展示我的的Fragment
     */
    private MyFragments myFragment;

    /**
     * 新闻界面布局
     */
    private View newLayout;

    /**
     *gif界面布局
     */
    private View gifLayout;

    /**
     * 我的界面布局
     */
    private View myLayout;

    /**
     * 在Tab布局上显示新闻图标的控件
     */
    private ImageView newImage;

    /**
     * 在Tab布局上显示gif图标的控件
     */
    private ImageView gifImage;

    /**
     * 在Tab布局上显示我的图标的控件
     */
    private ImageView myImage;

    /**
     * 在Tab布局上显示新闻标题的控件
     */
    private TextView newText;

    /**
     * 在Tab布局上显示GIF标题的控件
     */
    private TextView gifText;

    /**
     * 在Tab布局上显示我的标题的控件
     */
    private TextView myText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {

        newLayout = findViewById(R.id.news_layout);
        gifLayout = findViewById(R.id.gif_layout);
        myLayout = findViewById(R.id.my_layout);

        newImage = (ImageView) findViewById(R.id.news_image);
        gifImage = (ImageView) findViewById(R.id.gif_image);
        myImage = (ImageView) findViewById(R.id.my_image);

        newText = (TextView) findViewById(R.id.news_text);
        gifText = (TextView) findViewById(R.id.gif_text);
        myText = (TextView) findViewById(R.id.my_text);

        newLayout.setOnClickListener(this);
        gifLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_layout:
                // 当点击了newstab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.gif_layout:
                // 当点击了giftab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.my_layout:
                // 当点击了my tab时，选中第3个tab
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示新闻，1表示GIF，2表示我的
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                newImage.setImageResource(R.mipmap.tab_my_pressed);
                newText.setTextColor(Color.WHITE);
                if (newFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    newFragment = new NewsFragment();
                    transaction.add(R.id.framelayout,newFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(newFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                gifImage.setImageResource(R.mipmap.tab_better_pressed);
                gifText.setTextColor(Color.WHITE);
                if (gifFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    gifFragment = new GIFFragment();
                    transaction.add(R.id.framelayout, gifFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(gifFragment);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                myImage.setImageResource(R.mipmap.tab_my_pressed);
                myText.setTextColor(Color.WHITE);
                if (myFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    myFragment = new MyFragments();
                    transaction.add(R.id.framelayout, myFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        newImage.setImageResource(R.mipmap.tab_channel_normal);
        newText.setTextColor(Color.parseColor("#82858b"));
        gifImage.setImageResource(R.mipmap.tab_better_normal);
        gifText.setTextColor(Color.parseColor("#82858b"));
        myImage.setImageResource(R.mipmap.tab_my_normal);
        myText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。

     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(androidx.fragment.app.FragmentTransaction transaction) {
        if (newFragment != null) {
            transaction.hide(newFragment);
        }
        if (gifFragment != null) {
            transaction.hide(gifFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }
}