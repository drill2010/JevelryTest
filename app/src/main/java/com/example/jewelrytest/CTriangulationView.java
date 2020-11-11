package com.example.jewelrytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CTriangulationView extends View {

    public CTriangulationView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CTriangulationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(4);

        float scale  = Triangulation.getInstance().scale;

        for (int i = 0; i<Triangulation.getInstance().listOfQuadrangles.size(); i++){

            Triangulation.TQadrangle qadrangle = Triangulation.getInstance().listOfQuadrangles.get(i);
            canvas.drawLine((float)qadrangle.p0.x *scale, (float)qadrangle.p0.y*scale, (float)qadrangle.p1.x*scale, (float)qadrangle.p1.y*scale, paint);
            canvas.drawLine((float)qadrangle.p2.x *scale, (float)qadrangle.p2.y*scale, (float)qadrangle.p1.x*scale, (float)qadrangle.p1.y*scale, paint);
            canvas.drawLine((float)qadrangle.p2.x *scale, (float)qadrangle.p2.y*scale, (float)qadrangle.p3.x*scale, (float)qadrangle.p3.y*scale, paint);
            canvas.drawLine((float)qadrangle.p0.x *scale, (float)qadrangle.p0.y*scale, (float)qadrangle.p3.x*scale, (float)qadrangle.p3.y*scale, paint);
            canvas.drawLine((float)qadrangle.p3.x *scale, (float)qadrangle.p3.y*scale, (float)qadrangle.p1.x*scale, (float)qadrangle.p1.y*scale, paint);
        }

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10f);
        //for (int i=0; i<Triangulation.getInstance().listOfPoints.size(); i++){
            for (int i=0; i<4; i++){
            Triangulation.TPoint point = Triangulation.getInstance().listOfPoints.get(i);
            canvas.drawPoint((float)point.x*scale, (float)point.y*scale, paint);
        }

        final float xMax = Triangulation.getInstance().maxX;
        final float yMax = Triangulation.getInstance().maxY;
        canvas.drawLine(0,0, xMax*scale, 0, paint);
        canvas.drawLine(xMax*scale,0,xMax*scale, yMax*scale, paint);
        canvas.drawLine(xMax*scale, yMax*scale, 0, yMax*scale, paint);
        canvas.drawLine(0,yMax*scale,0,0, paint);

        paint.setColor(Color.RED);
        for (int i=1; i<Triangulation.getInstance().listOfOuterPoints.size(); i++){
            Triangulation.TPoint startPoint = Triangulation.getInstance().listOfOuterPoints.get(i);
            Triangulation.TPoint endPoint = Triangulation.getInstance().listOfOuterPoints.get(i-1);
            canvas.drawLine((float)startPoint.x*scale, (float)startPoint.y*scale, (float)endPoint.x*scale, (float)endPoint.y*scale, paint);
        }

      //  int n = Triangulation.getInstance().listOfOuterPoints.size()-1;
      //  Triangulation.TPoint startPoint = Triangulation.getInstance().listOfOuterPoints.get(n);
      //  Triangulation.TPoint endPoint = Triangulation.getInstance().listOfOuterPoints.get(n-1);
     //   canvas.drawLine((float)startPoint.x, (float)startPoint.y, (float)endPoint.x, (float)endPoint.y, paint);

        Triangulation.TPoint startPoint = Triangulation.getInstance().listOfPoints.get(3);
        canvas.drawPoint((float)startPoint.x*scale, (float)startPoint.y*scale, paint);

    }
}
