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
           MyPoint point = new MyPoint();
           Record record = records.get(i);
           point.setY(record.getDepth());
           point.setX(Math.abs(Math.abs((point.getY()-points.get(points.size()-1).getY()))*(1/Math.tan(Math.toRadians(record.getPitch()))))+points.get(points.size()-1).getX());
           point.setZ(0);
           point.setPitch(record.getPitch());
           points.add(point);
       }

       return points;
   }
}
