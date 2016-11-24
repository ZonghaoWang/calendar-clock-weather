package com.example.jierui.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View view){
        Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
    }
    public void move(View view){
//        TranslateAnimation animation = new TranslateAnimation(0, 200,0,0);
//        animation.setDuration(1000);
//        animation.setFillAfter(true);   // 图像的onclick不响应，缺点之一
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.startAnimation(animation);
//        ObjectAnimator.ofFloat(imageView, "translationY", 0F, 200F).setDuration(1000).start();
//        ObjectAnimator.ofFloat(imageView, "rotation", 0F, 360F).setDuration(1000).start();
        //使用propertyValuesHoloder实现
//        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation", 0, 360F);
//        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationX", 0, 360F);
//        PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("translationY", 0, 360F);
//        ObjectAnimator.ofPropertyValuesHolder(imageView, p1, p2, p3).setDuration(1000).start();

        //用Set
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "rotation", 0, 360F);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "translationX", 0, 360F);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "translationY", 0, 360F);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(imageView, "alpha", 1F, 0);
        AnimatorSet set = new AnimatorSet();
//        set.playTogether(animator1, animator2, animator3);
//        set.playSequentially(animator1, animator2, animator3);
        set.play(animator2).with(animator3);
        set.play(animator1).after(animator2);
        set.play(animator4).after(animator1);
        set.setDuration(1000);
        set.start();


    }
}
