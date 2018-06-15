package me.bloodybadboy.popularmovies.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import me.bloodybadboy.popularmovies.R;
import me.bloodybadboy.popularmovies.utils.ViewUtils;

public class BottomArcImageView extends AppCompatImageView {
  private Path maskPath;
  private Path convexPath;
  private int height, width;
  private float arcHeight;
  private float elevation;
  private Bitmap tempBitmap;
  private Canvas tempCanvas;
  private Paint maskPaint;

  public BottomArcImageView(@NonNull Context context) {
    super(context);
    init(context, null);
  }

  public BottomArcImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    convexPath = new Path();
    maskPath = new Path();

    maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    maskPaint.setAntiAlias(true);
    maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.BottomArcImageView, 0, 0);
    arcHeight = a.getDimension(R.styleable.BottomArcImageView_arcHeight,
        ViewUtils.dpToPx(context, 50));
    elevation = a.getDimension(R.styleable.BottomArcImageView_arcElevation, 0);
    a.recycle();

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }


  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (tempBitmap != null) {
      tempBitmap.recycle();
    }
    tempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    tempCanvas = new Canvas(tempBitmap);

    if (height == 0 && width == 0 && w != 0 && h != 0) {
      height = h;
      width = w;

      doLayout();
    }
  }

  private void doLayout() {

    convexPath.moveTo(0, 0);
    convexPath.lineTo(0, height - arcHeight);
    convexPath.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
    convexPath.lineTo(width, 0);

    maskPath.moveTo(0, height);
    maskPath.lineTo(0, height - arcHeight);
    maskPath.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
    maskPath.lineTo(width, height);

    maskPath.close();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      ViewCompat.setElevation(this, elevation);
      setOutlineProvider(new ViewOutlineProvider() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void getOutline(View view, Outline outline) {
          outline.setConvexPath(convexPath);
        }
      });
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(tempCanvas);
    tempCanvas.drawPath(maskPath, maskPaint);
    canvas.drawBitmap(tempBitmap, 0f, 0f, null);
  }
}
