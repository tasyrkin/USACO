import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class March2013_CowRace {

    static class RacePoint{
        int distance;
        Double t1 = null;
        Double t2 = null;

        public RacePoint(int distance){
            this.distance = distance;
        }

        public String toString(){
            return "dist=[" + distance + "], t1=[" + t1 + "], t2=[" + t2  + "]";
        }
    }

    public static void main(String[]args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("cowrace.in")));
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[]parts = br.readLine().split("\\s+");

        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);

        int[][]xs = new int[n][2];
        fillSpeedAndTime(br, n, xs);
        int[][]ys = new int[m][2];
        fillSpeedAndTime(br, m, ys);

        List<RacePoint> raceChain = new ArrayList<RacePoint>();

        int totalDistance = 0;
        int totalTime = 0;
        for(int i = 0; i < n; i++){
            totalDistance += xs[i][0] * xs[i][1];
            totalTime += xs[i][1];

            RacePoint racePoint = new RacePoint(totalDistance);
            racePoint.t1 = (double)totalTime;

            raceChain.add(racePoint);
        }

        totalDistance = 0;
        totalTime = 0;
        mainLoop:
        for(int i = 0; i < m; i++){
            totalDistance += ys[i][0] * ys[i][1];
            totalTime += ys[i][1];

            RacePoint racePoint = new RacePoint(totalDistance);
            racePoint.t2 = (double)totalTime;

            Iterator<RacePoint> iterator = raceChain.iterator();
            int positionToInsert = 0;
            while(iterator.hasNext()){
                RacePoint currRacePoint = iterator.next();
                if(currRacePoint.t2 != null){
                } else if(currRacePoint.distance == racePoint.distance){
                    currRacePoint.t2 = racePoint.t2;
                    continue mainLoop;
                } else if(currRacePoint.distance > racePoint.distance){
                    break;
                }
                ++positionToInsert;
            }
            raceChain.add(positionToInsert, racePoint);
        }

        raceChain.add(new RacePoint(1000000000));

        /*
        for(RacePoint racePoint : raceChain){
            System.out.println(racePoint.toString());
        }*/

        fillOpponentsTime(n, xs, raceChain, true);
        fillOpponentsTime(m, ys, raceChain, false);

        /*for(RacePoint racePoint : raceChain){
            System.out.println(racePoint.toString());
        }*/

        int leader = -1;
        int prevLeader = -1;
        int cnt = 0;
        for(RacePoint racePoint : raceChain){
            if(leader != -1)prevLeader = leader;
            if(leader == -1){
                leader = getLeader(racePoint);
                if(leader != -1 && prevLeader != -1){
                    if(leader != prevLeader)++cnt;
                }
            } else {
                int newLeader = getLeader(racePoint);
                if(newLeader == -1){
                    leader = -1;
                }
                else if(newLeader != leader){
                    ++cnt;
                    leader = newLeader;
                }
            }
        }
        //System.out.println(cnt);
        FileWriter fw = new FileWriter(new File("cowrace.out"));
        fw.write("" + cnt + "\n");
        fw.close();
    }

    private static int getLeader(RacePoint racePoint) {
        int leader = -1;
        if(isEqual(racePoint.t1, racePoint.t2)) return -1;
        if(racePoint.t1 < racePoint.t2)leader = 1;
        if(racePoint.t1 > racePoint.t2)leader = 2;
        return leader;
    }

    static boolean isEqual(double d1, double d2){
        if(Math.abs(d1-d2) < 1e-5){
            return true;
        }
        return false;
    }

    private static void fillOpponentsTime(int length, int[][] speedAndTime, List<RacePoint> raceChain, boolean isFirst) {
        int totalDistance;
        int totalTime;
        for(RacePoint racePoint : raceChain){
            if(isFirst && racePoint.t1 != null)continue;
            if(!isFirst && racePoint.t2 != null)continue;

            totalDistance = 0;
            totalTime = 0;
            boolean wasSet = false;
            for(int i = 0; i < length; i++){
                int newDistance = totalDistance + speedAndTime[i][0]*speedAndTime[i][1];
                int newTime = totalTime + speedAndTime[i][1];
                if(racePoint.distance == newDistance){
                    if(isFirst)
                        racePoint.t1 = (double)newTime;
                    else
                        racePoint.t2 = (double)newTime;
                    wasSet = true;
                    break;
                }
                if(racePoint.distance < newDistance){
                    double distance = racePoint.distance - totalDistance;
                    double time = totalTime + distance/speedAndTime[i][0];
                    if(isFirst)
                        racePoint.t1 = time;
                    else
                        racePoint.t2 = time;
                    wasSet = true;
                    break;
                }
                totalDistance = newDistance;
                totalTime = newTime;
            }
            if(!wasSet){
                double distance = racePoint.distance - totalDistance;
                double time = totalTime + distance/speedAndTime[speedAndTime.length-1][0];
                if(isFirst)
                    racePoint.t1 = time;
                else
                    racePoint.t2 = time;
            }
        }
    }

    private static void fillSpeedAndTime(BufferedReader br, int length, int[][] speedAndTime) throws IOException {
        String[] parts;
        for(int i = 0; i < length; i++){
            parts = br.readLine().split("\\s+");
            speedAndTime[i][0] = Integer.parseInt(parts[0]);
            speedAndTime[i][1] = Integer.parseInt(parts[1]);
        }
    }
}
