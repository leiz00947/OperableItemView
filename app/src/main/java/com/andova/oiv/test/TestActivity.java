package com.andova.oiv.test;

import android.graphics.Color;
import android.os.Bundle;

import com.wangjie.shadowviewhelper.ShadowProperty;
import com.wangjie.shadowviewhelper.ShadowViewDrawable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

/**
 * Created by Administrator on 2017-11-21.
 *
 * @author kzaxil
 * @since 1.1.0
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test);

        ShadowProperty sp = new ShadowProperty()
                .setShadowColor(0x59ff0000)
                .setShadowDy(10)
                .setShadowRadius(10)
                .setShadowSide(ShadowProperty.BOTTOM);
        ShadowViewDrawable sd = new ShadowViewDrawable(sp, Color.WHITE, 0, 0);
        ViewCompat.setBackground(findViewById(R.id.oiv_test), sd);
        ViewCompat.setLayerType(findViewById(R.id.oiv_test), ViewCompat.LAYER_TYPE_SOFTWARE, null);
    }
}
