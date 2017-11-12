package com.kzax1l.oiv.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.florent37.expectanim.ExpectAnim;
import com.kzax1l.oiv.OperableItemView;

import static com.github.florent37.expectanim.core.Expectations.bottomOfParent;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.width;

/**
 * Created by Administrator on 2017-11-10.
 *
 * @author kzaxil
 * @since 1.0.0
 */
public class OivTestActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private ExpectAnim mExpectAnim;
    private OperableItemView oivToDoNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_oiv_test);
        oivToDoNum = (OperableItemView) findViewById(R.id.oiv_work_todo);
        oivToDoNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oivToDoNum.requestLayout();
            }
        });

        mExpectAnim = new ExpectAnim()
                .expect(oivToDoNum)
                .toBe(
                        bottomOfParent().withMarginDp(9),
                        leftOfParent().withMarginDp(15),
                        width(30).toDp().keepRatio()
                )
                .toAnimation();

        ((AppBarLayout) findViewById(R.id.abl_layout)).addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float percent = (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
        mExpectAnim.setPercent(percent);
        oivToDoNum.enableBodyText(false, percent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ((AppBarLayout) findViewById(R.id.abl_layout)).setExpanded(true, true);
        }
    }
}