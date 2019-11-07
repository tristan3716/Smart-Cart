package kr.co.royworld.smartcart.utils;

import kr.co.royworld.smartcart.model.Position;

public class BeaconUtils {
    public static final int PROXIMITY_IMMEDIATE = 1;
    public static final int PROXIMITY_NEAR = 2;
    public static final int PROXIMITY_FAR = 3;
    public static final int PROXIMITY_UNKNOWN = 0;

    // 비콘 거리 계산
    private final static Position POS_LT = new Position(0, 0, -1);
    private final static Position POS_RT = new Position(11, 0, -1);
    private final static Position POS_RB = new Position(11, 9, -1);
    private final static Position POS_LB = new Position(0, 9, -1);

    public static double distacne (double TXpower, double RSSI){
        double n = 2.0;    // 장애물이 없는경우
        return Math.pow(10.0, (TXpower - RSSI) / (10*n));
    }

    public static Position pos(double r1, double r2, double r3, double r4, Position prePos) {
        // 좌상/우하의 접점
        int pxmt1 = BeaconUtils.calculateProximity(r1);
        int pxmt2 = BeaconUtils.calculateProximity(r2);
        int pxmt3 = BeaconUtils.calculateProximity(r3);
        int pxmt4 = BeaconUtils.calculateProximity(r4);

        Position p1 = null;
        Position p2 = null;
        Position p3 = null;

        if(pxmt1 != PROXIMITY_UNKNOWN && pxmt1 != PROXIMITY_FAR){
            p1 = POS_LT;
            p1.setDistance(r1);

            if(pxmt2 != PROXIMITY_UNKNOWN && pxmt2 != PROXIMITY_FAR){
                p2 = POS_RB;
                p2.setDistance(r2);
            }

            if(pxmt3 != PROXIMITY_UNKNOWN && pxmt3 != PROXIMITY_FAR){
                if(p2 == null){
                    p2 = POS_LB;
                    p2.setDistance(r3);
                }else{
                    p3 = POS_LB;
                    p3.setDistance(r3);
                }
            }

            if(pxmt4 != PROXIMITY_UNKNOWN && pxmt4 != PROXIMITY_FAR){
                if(p2 == null){
                    p2 = POS_RT;
                    p2.setDistance(r4);
                } else {
                    p3 = POS_RT;
                    p3.setDistance(r4);
                }
            }
        }else if(pxmt2 != PROXIMITY_UNKNOWN && pxmt2 != PROXIMITY_FAR){
            p1 = POS_RB;
            p1.setDistance(r2);
            if(pxmt3 != PROXIMITY_UNKNOWN && pxmt3 != PROXIMITY_FAR){
                p2 = POS_LB;
                p2.setDistance(r3);
            }
            if(pxmt4 != PROXIMITY_UNKNOWN && pxmt4 != PROXIMITY_FAR){
                if(p2 == null){
                    p2 = POS_RT;
                    p2.setDistance(r4);
                } else {
                    p3 = POS_RT;
                    p3.setDistance(r4);
                }
            }
        }else if(pxmt3 != PROXIMITY_UNKNOWN && pxmt3 != PROXIMITY_FAR){
            p1 = POS_LB;
            p1.setDistance(r3);
            if(pxmt4 != PROXIMITY_UNKNOWN && pxmt4 != PROXIMITY_FAR){
                p2 = POS_RT;
                p2.setDistance(r4);
            }
        }


        Position nextPos = null;
        if(p1 != null){
            if(p2 != null){
                if(p3 == null){
                    int X = p2.getPosX() - p1.getPosX();
                    int Y = p2.getPosY() - p1.getPosY();
                    if (X == 0) return new Position(0, Y, -1);
                    double D = Math.sqrt(X * X + Y * Y);
                    double T1 = Math.acos((r1 * r1 - r2 * r2 + D * D) / (2 * r1 * D));
                    double T2 = Math.atan(Y / X);
                    nextPos = new Position((int) (p1.getPosX() * 1 + r1 * Math.cos(T2 + T1)), (int) (p1.getPosY() * 1 + r1 * Math.sin(T2 + T1)), -1);
                }else{
                    nextPos = BeaconUtils.getLocationByTrilateration(p1, p2, p3);
                }
            } else{
                nextPos = p1;
            }
        }

        if(nextPos != null){
            if(prePos == null || (Math.abs(prePos.getPosX() - nextPos.getPosX()) < 3 && Math.abs(prePos.getPosY() - nextPos.getPosY()) < 3)){
                return nextPos;
            }else{
                return prePos;
            }
        }else{
            return prePos;
        }
    }

    public static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0)    return -1.0; // if we cannot determine accuracy, return -1.

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0)    return Math.pow(ratio,10);
        else                return (0.89976)*Math.pow(ratio,7.7095) + 0.111;
    }

    public static int calculateProximity(double accuracy) {
        if (accuracy < 0) {
            return PROXIMITY_UNKNOWN;
        }
        if (accuracy < 0.5 ) {
            return PROXIMITY_IMMEDIATE;
        }
        // 일반적으로는 3.0까지이지만 현재 내 비콘 체감상 4.0
        if (accuracy <= 4.0) {
            return PROXIMITY_NEAR;
        }
        // 8.0이상은 너무 먼 것읏으로 간주
        return PROXIMITY_FAR;

    }

    private static Position getLocationByTrilateration(Position location1, Position location2, Position location3){
            //DECLARE VARIABLES
            double[] P1   = new double[2];
            double[] P2   = new double[2];
            double[] P3   = new double[2];
            double[] ex   = new double[2];
            double[] ey   = new double[2];
            double[] p3p1 = new double[2];
            double jval  = 0;
            double temp  = 0;
            double ival  = 0;
            double p3p1i = 0;
            double triptx;
            double tripty;
            double xval;
            double yval;
            double t1;
            double t2;
            double t3;
            double t;
            double exx;
            double d;
            double eyy;

            //TRANSALTE POINTS TO VECTORS
            //POINT 1
            P1[0] = location1.getPosX();
            P1[1] = location1.getPosY();
            //POINT 2
            P2[0] = location2.getPosX();
            P2[1] = location2.getPosY();
            //POINT 3
            P3[0] = location3.getPosX();
            P3[1] = location3.getPosY();

            //TRANSFORM THE METERS VALUE FOR THE MAP UNIT
            //DISTANCE BETWEEN POINT 1 AND MY LOCATION
            location1.setDistance(location1.getDistance() / 100000);
            //DISTANCE BETWEEN POINT 2 AND MY LOCATION
            location2.setDistance(location2.getDistance() / 100000);
            //DISTANCE BETWEEN POINT 3 AND MY LOCATION
            location3.setDistance(location3.getDistance() / 100000);

            for (int i = 0; i < P1.length; i++) {
                t1   = P2[i];
                t2   = P1[i];
                t    = t1 - t2;
                temp += (t*t);
            }
            d = Math.sqrt(temp);
            for (int i = 0; i < P1.length; i++) {
                t1    = P2[i];
                t2    = P1[i];
                exx   = (t1 - t2)/(Math.sqrt(temp));
                ex[i] = exx;
            }
            for (int i = 0; i < P3.length; i++) {
                t1      = P3[i];
                t2      = P1[i];
                t3      = t1 - t2;
                p3p1[i] = t3;
            }
            for (int i = 0; i < ex.length; i++) {
                t1 = ex[i];
                t2 = p3p1[i];
                ival += (t1*t2);
            }
            for (int  i = 0; i < P3.length; i++) {
                t1 = P3[i];
                t2 = P1[i];
                t3 = ex[i] * ival;
                t  = t1 - t2 -t3;
                p3p1i += (t*t);
            }
            for (int i = 0; i < P3.length; i++) {
                t1 = P3[i];
                t2 = P1[i];
                t3 = ex[i] * ival;
                eyy = (t1 - t2 - t3)/Math.sqrt(p3p1i);
                ey[i] = eyy;
            }
            for (int i = 0; i < ey.length; i++) {
                t1 = ey[i];
                t2 = p3p1[i];
                jval += (t1*t2);
            }
            xval = (Math.pow(location1.getDistance(), 2) - Math.pow(location2.getDistance(), 2) + Math.pow(d, 2))/(2*d);
            yval = ((Math.pow(location1.getDistance(), 2) - Math.pow(location3.getDistance(), 2) + Math.pow(ival, 2) + Math.pow(jval, 2))/(2*jval)) - ((ival/jval)*xval);

            t1 = location1.getPosX();
            t2 = ex[0] * xval;
            t3 = ey[0] * yval;
            triptx = t1 + t2 + t3;

            t1 = location1.getPosX();
            t2 = ex[1] * xval;
            t3 = ey[1] * yval;
            tripty = t1 + t2 + t3;

            return new Position((int)triptx, (int)tripty, -1);
    }
}
