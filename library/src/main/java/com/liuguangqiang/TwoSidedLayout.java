/*
 * Copyright 2015 Eric Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liuguangqiang;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * TwoSidedLayout is a view that can turn to show the back side.
 * <p/>
 * Created by Eric on 15/4/15.
 */
public class TwoSidedLayout extends RelativeLayout implements View.OnClickListener {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final int CAMERA_DISTANCE = 8100;
    private static final int ORIGINAL_ROTATION = 0;
    private static final int END_ROTATION = 180;
    private static final int ANCHOR = 90;

    private static final double DEFAULT_TENSION = 32;
    private static final double DEFAULT_FRICTION = 8;

    private double mTension = DEFAULT_TENSION;
    private double mFriction = DEFAULT_FRICTION;

    private int mOrientation = HORIZONTAL;

    private View front;
    private View back;

    private OnTurnChangedListener onTurnChangedListener;
    private boolean isFront = true;
    private SpringSystem springSystem;
    private Spring spring;

    private boolean turnable = true;

    public void setTurnable(boolean enable) {
        this.turnable = enable;
    }

    public TwoSidedLayout(Context context) {
        this(context, null);
    }

    public TwoSidedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setCameraDistance(CAMERA_DISTANCE);
        setOnClickListener(this);
    }

    public boolean isFront() {
        return isFront;
    }

    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public void setTensionAndFriction(double tension, double friction) {
        mTension = tension;
        mFriction = friction;
    }

    public void setOnTurnChangedListener(OnTurnChangedListener listener) {
        this.onTurnChangedListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getChildCount() != 2) {
            throw new IllegalStateException("TurnLayout must contains only two direct child.");
        }

        front = getChildAt(1);
        back = getChildAt(0);

        back.setRotationY(ORIGINAL_ROTATION);
        back.setRotationX(ORIGINAL_ROTATION);
        setMRotation(back, END_ROTATION);
    }

    @Override
    public void onClick(View v) {
        if (turnable && v instanceof TwoSidedLayout) {
            overturn();
        }
    }

    private void setMRotation(float rotation) {
        setMRotation(this, rotation);
    }

    private void setMRotation(View view, float rotation) {
        switch (mOrientation) {
            case HORIZONTAL:
                view.setRotationY(rotation);
                break;
            case VERTICAL:
                view.setRotationX(rotation);
                break;
            default:
                view.setRotationY(rotation);
                break;
        }
    }

    public void overturn() {
        overturn(isFront);
    }

    private void overturn(final boolean showBack) {
        if (springSystem == null) {
            springSystem = SpringSystem.create();
            spring = springSystem.createSpring();
            spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(mTension, mFriction));
            spring.addListener(new SimpleSpringListener() {

                @Override
                public void onSpringUpdate(Spring spring) {
                    float value = (float) spring.getCurrentValue();
                    setMRotation(value);

                    if (isFront && value > ANCHOR) {
                        front.setVisibility(View.GONE);
                        isFront = false;
                        if (onTurnChangedListener != null) onTurnChangedListener.onChanged(isFront);
                    } else if (!isFront && value < ANCHOR) {
                        front.setVisibility(View.VISIBLE);
                        isFront = true;
                        if (onTurnChangedListener != null) onTurnChangedListener.onChanged(isFront);
                    }
                }

                @Override
                public void onSpringEndStateChange(Spring spring) {
                    super.onSpringEndStateChange(spring);
                }
            });
        }

        int endValue = showBack ? END_ROTATION : ORIGINAL_ROTATION;
        spring.setEndValue(endValue);
    }

    public interface OnTurnChangedListener {
        void onChanged(boolean isFront);
    }

}
