package com.iteast.myapplication;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity {

    private Space mSpace;
    private AppBarLayout mAppBar;
    private ImageView mImageView;
    private float mCollapsedSize;
    private float mExpandSize;
    private float mColloapsedTextSize;
    private float mExpandTextSize;
    private AppBarStateChangeListener mAppBarStateChangeListener;

    private float[] mAvatarPoint = new float[2], mSpacePoint = new float[2], mToolbarTextPoint =
            new float[2], mTitleTextViewPoint = new float[2];
    private TextView mTvTitle;
    private TextView mTvTitleSpace;
    private int mTitleWidth;
    private int mTitleHeight;
    private int mToolBarTitleWidth;
    private int mToolBarTitleHeight;
    private float mTitleTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        mExpandSize = getResources().getDimension(R.dimen.size_avatart_expand);
        mCollapsedSize = getResources().getDimension(R.dimen.size_avatart_collapsed);
        mColloapsedTextSize = getResources().getDimension(R.dimen.dimen_title_collapsed);
        mExpandTextSize = getResources().getDimension(R.dimen.dimen_title_expand);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mSpace = findViewById(R.id.space);
        mImageView = findViewById(R.id.imageview);
        mTvTitle = findViewById(R.id.tv_title);
        mAppBar = findViewById(R.id.app_bar);
        mTvTitleSpace = findViewById(R.id.tv_title_space);
        mAppBarStateChangeListener = new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout,
                                       AppBarStateChangeListener.State state) {
            }

            @Override
            public void onOffsetChanged(AppBarStateChangeListener.State state, float offset) {
                translationView(offset);
            }
        };
        mAppBar.addOnOffsetChangedListener(mAppBarStateChangeListener);
    }

    public void translationView(float offset){
        float newAvatarSize = mExpandSize - (mExpandSize - mCollapsedSize)*offset;
        mImageView.getLayoutParams().width = Math.round(newAvatarSize);
        mImageView.getLayoutParams().height = Math.round(newAvatarSize);


        float appBarParallax = mAppBarStateChangeListener.getCurrentOffset()*mAppBar.getTotalScrollRange()*0.5f*offset;
        mImageView.setTranslationX((mSpacePoint[0]-mAvatarPoint[0])*offset);
        mImageView.setTranslationY((mSpacePoint[1]-mAvatarPoint[1]-(mExpandSize - newAvatarSize))*offset+appBarParallax);
        mImageView.requestLayout();


        float newTextSizeX = mTvTitle.getWidth();
        float newTextSizeY =  mTvTitle.getHeight();
//        mTvTitle.getLayoutParams().width = Math.round(newTextSizeX);
//        mTvTitle.getLayoutParams().height = Math.round(newTextSizeY);
//        mTvTitle.requestLayout();
        float x = mTitleWidth - (mTitleWidth - mToolBarTitleWidth)* offset;
        float y = mTitleHeight - (mTitleHeight - mToolBarTitleHeight)* offset;

        float newTextSize= mExpandTextSize-(mExpandTextSize-mColloapsedTextSize)*offset;
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,newTextSize);

        mTvTitle.setTranslationX((mToolbarTextPoint[0]-mTitleTextViewPoint[0]-(mTitleWidth - x))*offset);
        mTvTitle.setTranslationY((mToolbarTextPoint[1]-mTitleTextViewPoint[1]-(mTitleHeight - y)/2)*offset+appBarParallax);
        mTvTitle.requestLayout();

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        resetPoints(false);
        mTitleWidth = mTvTitle.getWidth();
        mTitleHeight = mTvTitle.getHeight();
        mToolBarTitleWidth = mTvTitleSpace.getWidth();
        mToolBarTitleHeight = mTvTitleSpace.getHeight();
        mTitleTextSize = mTvTitle.getTextSize();
    }

    private void resetPoints(boolean b) {
        final float offset = mAppBarStateChangeListener.getCurrentOffset();
        float newAvatarSize = mExpandSize - (mExpandSize - mCollapsedSize)*offset;
        float expandAvatarSize = mExpandSize;


        int[] spacePoint = new int[2];
        mSpace.getLocationOnScreen(spacePoint);
        mSpacePoint[0] = spacePoint[0];
        mSpacePoint[1] = spacePoint[1];

        int[] avatarPoint = new int[2];
        mImageView.getLocationOnScreen(avatarPoint);
        mAvatarPoint[0] = avatarPoint[0];
        mAvatarPoint[1] = avatarPoint[1];

        int[] toolBarTitle = new int[2];
        mTvTitleSpace.getLocationOnScreen(toolBarTitle);
        mToolbarTextPoint[0] = toolBarTitle[0];
        mToolbarTextPoint[1] = toolBarTitle[1];

//        Paint paint = new Paint(mTvTitle.getPaint());
//        float newTextWidth = getTextWidth(paint,mTvTitle.getText().toString());
//        paint.setTextSize(mTitleTextSize);
//        float originTextWidth = getTextWidth(paint,mTvTitle.getText().toString());
        int[] titleTextViewPoint = new int[2];
        mTvTitle.getLocationOnScreen(titleTextViewPoint);

        mTitleTextViewPoint[0]=titleTextViewPoint[0] ;
        mTitleTextViewPoint[1]=titleTextViewPoint[1] ;
    }

    public  float getTextWidth(Paint paint,String text){
        return paint.measureText(text);
    }
}
