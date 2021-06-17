package com.common.esimrfid.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.RadioButton;

public class RadioGroupX extends GridLayout {
    private boolean mProtectFromCheckedChange = false;
    private int mCheckedId = -1;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener = new CheckedStateTracker();
    private PassThroughHierarchyChangeListener mPassThroughListener = new PassThroughHierarchyChangeListener();
    private OnCheckedChangeListener mOnCheckedChangeListener;
    {
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }
    public RadioGroupX(Context context) {
        this(context, null);
    }

    public RadioGroupX(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RadioGroupX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(child instanceof RadioButton){
            if (((RadioButton)child).isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(child.getId());
            }
        }
        super.addView(child, index, params);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        mOnCheckedChangeListener = listener;
    }
    public interface OnCheckedChangeListener{
        void onCheckedChanged(RadioGroupX group,int checkedId);
    }
    class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener = null;
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioGroupX.this && child instanceof RadioButton) {
                int id = child.getId();
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
               ((RadioButton) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                if(mOnHierarchyChangeListener != null){
                    mOnHierarchyChangeListener.onChildViewAdded(parent,child);
                }
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioGroupX.this && child instanceof RadioButton) {
                ((RadioButton)child).setOnCheckedChangeListener(null);
            }
            if(mOnHierarchyChangeListener != null){
                mOnHierarchyChangeListener.onChildViewRemoved(parent,child);
            }
        }
    }

    class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

   private void check(int id){
       if (id != -1 && id == mCheckedId) {
           return;
       }
       if (mCheckedId != -1) {
           setCheckedStateForView(mCheckedId, false);
       }
       if (id != -1) {
           setCheckedStateForView(id, true);
       }
       setCheckedId(id);
   }

   private void setCheckedId(int id){
       mCheckedId = id;
       if(mOnCheckedChangeListener != null){
           mOnCheckedChangeListener.onCheckedChanged(this,mCheckedId);
       }
   }
   private void setCheckedStateForView(int viewId,boolean checked){
       View checkedView  = findViewById(viewId);
       if ( checkedView instanceof RadioButton) {
           ((RadioButton) checkedView).setChecked(checked);
       }
   }

    public void clearCheck() {
        check(-1);
    }

}
