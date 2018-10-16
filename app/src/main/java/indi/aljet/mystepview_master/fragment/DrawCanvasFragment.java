package indi.aljet.mystepview_master.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import indi.aljet.mystepview_master.R;

public class DrawCanvasFragment extends Fragment {

    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return new RectView(container.getContext());
    }


    public class RectView extends View{

        public RectView(Context context) {
            super(context);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            setBackgroundResource(R.drawable.default_bg);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(40);
            canvas.drawRect(200,200,800,220,paint);

            canvas.drawCircle(350,350,
                    100,paint);

            Paint pathPaint = new Paint();
            pathPaint.setAntiAlias(true);
            pathPaint.setColor(Color.WHITE);
            pathPaint.setStyle(Paint.Style.STROKE);
            pathPaint.setStrokeWidth(2);
            DashPathEffect mEffects = new DashPathEffect
                    (new float[] {8,8,8,8},1);
            Path path = new Path();
            path.moveTo(200,600);
            path.lineTo(800,600);
            pathPaint.setPathEffect(mEffects);
            canvas.drawPath(path,pathPaint);
        }
    }
}
