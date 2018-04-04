package spollina.com.Asteroid_Field_Cleanup_spollina;

import android.animation.TimeAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * A simple toy view for demo purposes.
 * Displays a circle and a trail that can be controlled via {@link #setVelocity(float, float)}.
 */
public class BugView extends View implements TimeAnimator.TimeListener {

    private static final float BUG_RADIUS_DP = 4f;
    private static final float BUG_TRAIL_DP = 200f;

    private Paint paint;
    private TimeAnimator animator;

    private float density;
    private int width, height;
    public PointF position;
    private PointF velocity;
    private Path path;
    private PathMeasure pathMeasure;

    public BugView(Context context) {
        super(context);
        init(context);
    }

    public BugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BugView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        animator = new TimeAnimator();
        animator.setTimeListener(this);

        paint = new Paint();
        paint.setColor(Color.WHITE);

        density = getResources().getDisplayMetrics().density;

        path = new Path();
        pathMeasure = new PathMeasure();
        position = new PointF();
        velocity = new PointF();
    }

    /**
     * Start applying velocity as soon as view is on-screen.
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    /**
     * Stop animations when the view hierarchy is torn down.
     */
    @Override
    public void onDetachedFromWindow() {
        animator.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        position.set(0, 0);
        path.rewind();
        path.moveTo(position.x, position.y);
    }

    /**
     * Set bug velocity in dips.
     */
    public void setVelocity(float vxDps, float vyDps) {
        velocity.set(vxDps * density, vyDps * density);
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        final float dt = deltaTime / 1000f; // seconds

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        bound();

        path.lineTo(position.x, position.y);

        invalidate();
    }

    /**
     * Bound position.
     */

    DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
    final int w = metrics.widthPixels;
    final int h = metrics.heightPixels;

    private void bound() {
        if (position.x < 0)
        {
            position.x = 0;
        }
        if (position.x > w - MainActivity.playerCharImage.getWidth())
        {
            position.x = w - MainActivity.playerCharImage.getWidth();
        }
        if (position.y < 0)
        {
            position.y = 0;
        }
        if (position.y > h - (MainActivity.playerCharImage.getHeight()))
        {
            position.y = h - (MainActivity.playerCharImage.getHeight());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        pathMeasure.setPath(path, false);
        float length = pathMeasure.getLength();

        if (length > BUG_TRAIL_DP * density) {
            // Note - this is likely a poor way to accomplish the result. Just for demo purposes.
            @SuppressLint("DrawAllocation")
            PathEffect effect = new DashPathEffect(new float[]{length, length}, -length + BUG_TRAIL_DP * density);
            paint.setPathEffect(effect);
        }

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(position.x, position.y, BUG_RADIUS_DP * density, paint);
    }
}
