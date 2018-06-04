package me.bloodybadboy.popularmovies.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import me.bloodybadboy.popularmovies.R;

public class BottomArcLayout extends FrameLayout {

  private Path path;
  private boolean isArcInside;
  private float arcHeight;

  public BottomArcLayout(@NonNull Context context) {
    super(context);
    init(context, null);
  }

  public BottomArcLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private static float dpToPx(Context context, int dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics());
  }

  private void init(Context context, AttributeSet attrs) {
    path = new Path();

    TypedArray styledAttributes =
        context.obtainStyledAttributes(attrs, R.styleable.BottomArcLayout, 0, 0);
    arcHeight =
        styledAttributes.getDimension(R.styleable.BottomArcLayout_arc_height, dpToPx(context, 50));
    isArcInside = styledAttributes.getBoolean(R.styleable.BottomArcLayout_arc_inside, true);
    styledAttributes.recycle();

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (changed) {
      int height = getMeasuredHeight();
      int width = getMeasuredWidth();

      if (!isArcInside) {
        path.moveTo(0, 0);
        path.lineTo(0, height);
        path.quadTo(width / 2, height - 2 * arcHeight, width, height);
        path.lineTo(width, 0);
      } else {
        path.moveTo(0, 0);
        path.lineTo(0, height - arcHeight);
        path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
        path.lineTo(width, 0);
      }
      path.close();
    }
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    canvas.save();

    canvas.clipPath(path);
    super.dispatchDraw(canvas);

    canvas.restore();
  }
}
