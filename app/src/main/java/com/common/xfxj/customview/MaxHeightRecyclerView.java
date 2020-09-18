package com.common.xfxj.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.common.xfxj.R;

import static java.lang.Math.min;

public class MaxHeightRecyclerView extends RecyclerView {
    private int maxHeight;
    public MaxHeightRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaxHeightRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
        maxHeight = array.getDimensionPixelSize(R.styleable.MaxHeightRecyclerView_cus_max_height ,-1);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int height = MeasureSpec.getSize(heightSpec);
        int mode = MeasureSpec.getMode(heightSpec);
        int heightMeasureSpec ;
        if(height < 0){
            heightMeasureSpec = heightSpec;
        }else if(mode == MeasureSpec.AT_MOST){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(min(height, maxHeight), MeasureSpec.AT_MOST);
        }
        else if(mode == MeasureSpec.UNSPECIFIED ){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        else if(mode == MeasureSpec.EXACTLY ){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(min(height, maxHeight), MeasureSpec.EXACTLY);
        }
        else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightMeasureSpec);
    }
}
