package com.liuguangqiang.turnlayout.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.liuguangqiang.turnlayout.widget.TurnLayout;


public class MainActivity extends ActionBarActivity {

    private TurnLayout turnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        turnLayout = (TurnLayout) findViewById(R.id.turnlayout);
        turnLayout.setOrientation(TurnLayout.HORIZONTAL);
        turnLayout.setTensionAndFriction(30, 15);
        turnLayout.setTurnable(true);
    }

}
