package com.liuguangqiang.turnlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * Created by Eric on 15/4/15.
 */
public class TurnLayout extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "ReversalLayout";
    private static final int CAMERA_DISTANCE = 8100;

    private View front;
    private View back;

    private boolean isFront = true;

    private SpringSystem springSystem;
    private Spring spring;

    public TurnLayout(Context context) {
        this(context, null);
    }

    public TurnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setCameraDistance(CAMERA_DISTANCE);
        setOnClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getChildCount() != 2) {
            throw new IllegalStateException("ReversalLayout must contains only two direct child.");
        }

        front = getChildAt(1);
        back = getChildAt(0);
        back.setRotationY(180);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TurnLayout) {
            overturn();
        }
    }

    public void overturn() {
        overturn(isFront);
    }

    private void overturn(final boolean showBack) {
        if (springSystem == null) {
            springSystem = SpringSystem.create();
            spring = springSystem.createSpring();
            spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(32, 8));
        }

        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                setRotationY(value);
                if (isFront && value > 90) {
                    front.setVisibility(View.GONE);
                    isFront = false;
                } else if (!isFront && value < 90) {
                    front.setVisibility(View.VISIBLE);
                    isFront = true;
                }
            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
                super.onSpringEndStateChange(spring);
            }
        });

        int endValue = showBack ? 180 : 0;
        spring.setEndValue(endValue);
        isFront = !isFront;
    }

}
