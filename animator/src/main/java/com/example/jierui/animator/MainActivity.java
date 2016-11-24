package com.example.jierui.animator;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private int[] res = {R.id.imageView_a, R.id.imageView_b, R.id.imageView_c, R.id.imageView_d, R.id.imageView_e, R.id.imageView_f, R.id.imageView_g, R.id.imageView_h};

    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < res.length; i++){
            ImageView imageView = (ImageView) findViewById(res[i]);
            imageView.setOnClickListener(this);
            imageViewList.add(imageView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_a:
                if (flag)
                startAnima();
                else
                closeAnima();
                break;
            default:
                break;
        }
    }
    private void startAnima(){
        for (int i = 0; i < res.length; i++){
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageViewList.get(i), "translationY", 0F, i * 150);
            animator.setDuration(500);
            animator.setInterpolator(new BounceInterpolator());
            animator.setStartDelay(i*200);
            animator.start();
            flag = false;
        }
    }
    private void closeAnima(){
        for (int i = 0; i < res.length; i++){
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageViewList.get(i), "translationY", i * 150, 0F);
            animator.setDuration(500);
            animator.setStartDelay(i*200);
            animator.start();
        }
        flag = true;
    }
}
