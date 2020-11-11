package com.example.jewelrytest;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Triangulation{
private static final Triangulation ourInstance=new Triangulation();

public static Triangulation getInstance(){return ourInstance; }

    class TPoint implements Comparable<TPoint>{
        double x,y;
        TPoint(){};
        TPoint(double x, double y){
            this.x = x; this.y = y;
        }
        public int compareTo(@NonNull TPoint o){
            if (x==o.x) return 0;
            if (x<o.x) return -1;
            return 1;
        }

    }

   /* class TEdge{
        TPoint a, b;
        TEdge(){};
        TEdge(TPoint a, TPoint b){
            this.a = a; this.b = b;
        }
    }
    */


    class TVector{
        double x, y;
        TVector(){};
        TVector(double x, double y){
            this.x = x; this.y = y;
        }
    }

    ArrayList <TPoint> listOfPoints = new ArrayList<>();
    ArrayList <TQadrangle> listOfQuadrangles = new ArrayList<>();
    ArrayList <TPoint> listOfOuterPoints  = new ArrayList<>();

    int maxX =1500; int maxY = 215; int numOfPoints = 22;

    public int scale = 1;

    final double eps = 1e-9;

    class TQadrangle{
       TPoint p0; //inner
       TPoint p1; // left
       TPoint p2; // outer
       TPoint p3; // right    p1-p3 is a border btw 2 triangles

        TQadrangle(){};
        TQadrangle(TPoint p0, TPoint p1, TPoint p2, TPoint p3){
            this.p0 = p0; this.p1 = p1; this.p2 = p2; this.p3 = p3;
        }

       boolean CheckDelaunayCondition() {

           double sa = (p2.x - p3.x) * (p2.x - p1.x) + (p2.y - p3.y) * (p2.y - p1.y);
           double sb = (p0.x - p3.x) * (p0.x - p1.x) + (p0.y - p3.y) * (p0.y - p1.y);

           if (sa >= 0 && sb >= 0) return true;

           if (!(sa < 0 && sb < 0)) {
               double sc = Cross(new TVector(p2.x - p3.x, p2.y - p3.y), new TVector(p2.x - p1.x, p2.y - p1.y));
               double sd = Cross(new TVector(p0.x - p3.x, p0.y - p3.y), new TVector(p0.x - p1.x, p0.y - p1.y));
               if (sc < 0) sc = -sc;
               if (sd < 0) sd = -sd;
               if (sc * sb + sa * sd > -eps) {
                   return true;
               }
           }
           return false;
       }

    }

    void SwitchQadrangle(TQadrangle q){
       TPoint tempPoint = q.p0;
       q.p0 = q.p1;
       q.p1 = q.p2;
       q.p2 = q.p3;
       q.p3 = tempPoint;
    }


   TPoint getQPoint(TPoint p0, TPoint p1){
        for (TQadrangle q:listOfQuadrangles){
            if ((q.p1 == p0 && q.p2==p1)||(q.p1==p1 && q.p2==p0)) return q.p3;
            if ((q.p3 == p0 && q.p2==p1)||(q.p3==p1 && q.p2==p0)) return q.p1;

            if ((q.p0 == p0 && q.p1==p1)||(q.p0==p1 && q.p1==p0)) return q.p3;
            if ((q.p0 == p0 && q.p3==p1)||(q.p0==p1 && q.p3==p0)) return q.p1;
        }
        TPoint t0 = listOfOuterPoints.get(0); TPoint t1 = listOfOuterPoints.get(1); TPoint t2 = listOfOuterPoints.get(2);
        if ((t0==p0 && t1==p1)||(t0==p1 && t1==p0)) return t2;
        else
        if ((t0==p0 && t2==p1)||(t0==p1 && t2==p0)) return t1;
        else return t0;
   }

    double Cross(TVector lv, TVector rv){
        return lv.x * rv.y - lv.y * rv.x;
    }
    int currentStep = 0;
    public void AddPoint(int p){
        TPoint point = listOfPoints.get(p);
        TPoint lastPoint = listOfPoints.get(p-1);

        // going left
        int leftBorder = listOfOuterPoints.indexOf(lastPoint);
        int rightBorder = leftBorder;
        TVector lastVector = new TVector(lastPoint.x - point.x, lastPoint.y - point.y );
        TPoint nextPoint = listOfOuterPoints.get(leftBorder-1);
        TVector nextVector = new TVector(nextPoint.x - point.x , nextPoint.y - point.y);
        while ( Cross(lastVector, nextVector) > -eps ){

            leftBorder--;
            if (leftBorder==0) break;
            lastVector = nextVector;
            nextPoint = listOfOuterPoints.get(leftBorder-1);
            nextVector = new TVector(nextPoint.x - point.x , nextPoint.y - point.y);
        }

        // going right

        lastPoint = listOfPoints.get(p-1);
        point = listOfPoints.get(p);
        lastVector = new TVector(lastPoint.x - point.x, lastPoint.y - point.y );
        nextPoint = listOfOuterPoints.get(rightBorder+1);
        nextVector = new TVector(nextPoint.x - point.x , nextPoint.y - point.y);

        while ( Cross(lastVector, nextVector) < eps ){

            rightBorder++;
            if (rightBorder == listOfOuterPoints.size()-1) break;
            lastVector = nextVector;
            nextPoint = listOfOuterPoints.get(rightBorder+1);
            nextVector = new TVector(nextPoint.x - point.x , nextPoint.y - point.y);
        }

        for (int i=leftBorder; i<rightBorder; i++){
            TPoint inPoint = getQPoint(listOfOuterPoints.get(i), listOfOuterPoints.get(i+1));
            TQadrangle qadrangle = new TQadrangle(inPoint, listOfOuterPoints.get(i), point, listOfOuterPoints.get(i+1));
            listOfQuadrangles.add(qadrangle);
            if (!qadrangle.CheckDelaunayCondition()) SwitchQadrangle(qadrangle);

        }

    //    Log.d("Jevelry", "listofOuterpoints "+listOfOuterPoints.size());
   //     Log.d("Jevelry", "currentstep "+currentStep+ "leftborder "+leftBorder);
   //     Log.d("Jevelry", "currentstep "+currentStep+ "rightborder "+rightBorder);

        for (int i=leftBorder+1; i<rightBorder; i++){
            listOfOuterPoints.remove(leftBorder+1);
        }

        listOfOuterPoints.add(leftBorder+1, point);
    }

    void FillandSort(){

        listOfOuterPoints.clear();
        listOfQuadrangles.clear();
        listOfPoints.clear();
        currentStep = 2;

        for (int i=0; i<numOfPoints; i++){
            listOfPoints.add( new TPoint(Math.random()*maxX, Math.random()*maxY));
        }
        Collections.sort(listOfPoints);

       /* listOfPoints.set(0, new TPoint(50,197));
        listOfPoints.set(1, new TPoint(61,52));
        listOfPoints.set(2, new TPoint(191,21));
        listOfPoints.set(3, new TPoint(221,82));
        /*
        listOfPoints.set(4, new TPoint(406,120));
        listOfPoints.set(5, new TPoint(428,24));

       */
        //listOfPoints.set(6, new TPoint(290,110));
       // listOfPoints.set(7, new TPoint(330,160));
       // listOfPoints.set(8, new TPoint(370,120));




        listOfOuterPoints.add(listOfPoints.get(0));

        TPoint point0 = listOfPoints.get(0);
        TPoint point1 = listOfPoints.get(1);
        TPoint point2 = listOfPoints.get(2);
        if (point0.y > point1.y){
            listOfOuterPoints.add(point1);
            listOfOuterPoints.add(point2);
        } else{
            listOfOuterPoints.add(point2);
            listOfOuterPoints.add(point1);
        }
        listOfOuterPoints.add(listOfPoints.get(0));

        Log.d("Jevelry","------------------");
        for (int i=0; i<6; i++){
            TPoint point = listOfPoints.get(i);
            Log.d("Jevelry", i + " x:"+point.x+" y:"+point.y);
        }

       // for (int i=3; i<numOfPoints; i++) AddPoint(i);
    //    for (int i=3; i<6; i++) AddPoint(i);
     //   AddPoint(3);
      /*  AddPoint(4);
        AddPoint(5);
        AddPoint(6);
        AddPoint(7);
        AddPoint(8);

      */
    }
}
    