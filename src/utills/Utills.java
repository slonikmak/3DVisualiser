package utills;


import model.MyPoint;
import model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oceanos on 09.11.2016.
 */
public class Utills {

   public static  List<MyPoint> createPoints(List<Record> records){
       List<MyPoint> points = new ArrayList<>();
       points.add(new MyPoint(0,0,0));
       for (int i = 10; i < records.size(); i = i+10) {
           MyPoint last = points.get(points.size()-1);
           MyPoint point = new MyPoint();
           Record record = records.get(i);
           point.setY(record.getDepth());
           double dx = ((Math.abs(record.getDepth()-points.get(points.size()-1).getY()))*(1/Math.tan(Math.toRadians(record.getPitch()))));
           //double dx = ((Math.abs(point.getY()-points.get(points.size()-1).getY()))*(Math.cos(Math.toRadians(record.getPitch()))));
           //System.out.println(dx);
           //double x = points.get(points.size()-1).getX() + (Math.tan(Math.toRadians(normalizeCourse(record.getCourse()))))*Math.abs(dx);
           //point.setX(x);
           point.setX(Math.abs(dx)+points.get(points.size()-1).getX());
           double dy = Math.abs(record.getDepth()-last.getY());
           double dx0 = Math.tan(Math.toRadians(record.getPitch()))*dy;
           ////double dx = Math.cos(Math.toRadians(normalizeCourse(record.getCourse())))*dx0;
           point.setZ(Math.sin(Math.toRadians(normalizeCourse(record.getCourse())))*dx0+points.get(points.size()-1).getZ());
           //point.setX(dx+last.getX());
           point.setPitch(record.getPitch());
           points.add(point);
       }

       return points;
   }

    public static double normalizeCourse(double course){
        return (450-course)%360;
    }

}
