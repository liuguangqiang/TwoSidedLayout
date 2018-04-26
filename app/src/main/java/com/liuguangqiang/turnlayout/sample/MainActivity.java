package com.liuguangqiang.turnlayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.liuguangqiang.TwoSidedLayout;

public class MainActivity extends AppCompatActivity {

  private TwoSidedLayout twoSidedLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  private void initViews() {
    twoSidedLayout = findViewById(R.id.turnlayout);
    twoSidedLayout.setOrientation(TwoSidedLayout.HORIZONTAL);
    twoSidedLayout.setTensionAndFriction(30, 15);
    twoSidedLayout.setTurnable(true);
  }
}
