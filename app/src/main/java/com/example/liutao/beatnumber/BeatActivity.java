package com.example.liutao.beatnumber;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class BeatActivity extends AppCompatActivity {

    private static final String TAG = BeatActivity.class.getSimpleName();

    /***/
    private static final int START_PHONE_ICON_AINMATION = 1;

    private static final int START_PHONE_NUMBER_AINMATION = 2;

    private static final String TELEPHONE_NUMBER = "15295596999";

    /** 电话icon */
    private ImageView mPhoneIcon;
    /** 电话号码 添加的Layout */
    private LinearLayout mPhoneNumberLayout;

    private int mCurrentIndex = 0;//当前 运动的电话 号码哪个

    private Button mStartAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initNumber();
    }

    private void initView() {
        mPhoneIcon = (ImageView) findViewById(R.id.phone_icon);
        mPhoneNumberLayout = (LinearLayout) findViewById(R.id.phone_number_layout);
        mStartAnimation =(Button)findViewById(R.id.startAnim);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        phoneIconAnimation();
    }

    private void initNumber() {
        char[] num = TELEPHONE_NUMBER.toCharArray();
        for (int i = 0; i < num.length; i++) {
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_phone_number, null);

            textView.setText(num[i]+"");// 防止出现 查找资源文件
            mPhoneNumberLayout.addView(textView);

            ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationY", 0f, -10f, -20f, -10f, 0f);
            animator.setDuration(300);
            textView.setTag(animator);
        }
    }

    /**电话icon的 动画*/
    private void phoneIconAnimation(){
        //相对自身 旋转 30度
        RotateAnimation animation =new RotateAnimation(0,30, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        animation.setRepeatCount(1);//抖动 2次
        animation.setDuration(100);
        animation.setFillAfter(true);
        mPhoneIcon.startAnimation(animation);

        mHandler.sendEmptyMessageDelayed(START_PHONE_ICON_AINMATION,300);

    }

    /**电话号码的 动画*/
    private void phoneNumberAnimation(int i){

        View view =mPhoneNumberLayout.getChildAt(i);
        Animator animator =(Animator)view.getTag();
        animator.start();

        long dey = animator.getDuration() * 2 / mPhoneNumberLayout.getChildCount();
        mHandler.sendEmptyMessageDelayed(START_PHONE_NUMBER_AINMATION,dey);


    }

    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case START_PHONE_ICON_AINMATION:
                    phoneNumberAnimation(mCurrentIndex);
                    break;
                case START_PHONE_NUMBER_AINMATION:

                    mCurrentIndex++;
                    if(mCurrentIndex==mPhoneNumberLayout.getChildCount()){
                        mCurrentIndex =0;
                        phoneIconAnimation();
                    }else{
                        phoneNumberAnimation(mCurrentIndex);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeMessages(START_PHONE_NUMBER_AINMATION);
        }
    }
}
