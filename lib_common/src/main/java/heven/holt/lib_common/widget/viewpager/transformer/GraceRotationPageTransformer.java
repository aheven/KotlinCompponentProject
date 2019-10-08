package heven.holt.lib_common.widget.viewpager.transformer;

import android.view.View;

import androidx.annotation.NonNull;

import heven.holt.lib_common.widget.viewpager.utils.GracePageTransformer;
import heven.holt.lib_common.widget.viewpager.utils.GracePagerAdapter;

public class GraceRotationPageTransformer extends GracePageTransformer {
    private static final float MIN_SCALE = 1f;

    public GraceRotationPageTransformer(@NonNull GracePagerAdapter pagerAdapter) {
        super(pagerAdapter);
    }

    @Override
    public void transformPageWithCorrectPosition(@NonNull View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float rotate = 10 * Math.abs(position);
        //position小于等于1的时候，代表page已经位于中心item的最左边，
        //此时设置为最小的缩放率以及最大的旋转度数
        if (position <= -1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setRotationY(rotate);
        }//position从0变化到-1，page逐渐向左滑动
        else if (position < 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        }//position从0变化到1，page逐渐向右滑动
        else if (position >= 0 && position < 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        }//position大于等于1的时候，代表page已经位于中心item的最右边
        else if (position >= 1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setRotationY(-rotate);
        }
    }
}
