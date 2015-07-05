import java.io.*;
import java.util.*;

public class March2013_BreedProximity {
    public static void main(String[]args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("proximity.in")));

        String[]parts = br.readLine().split("\\s+");

        int n = Integer.parseInt(parts[0]);
        int k = Integer.parseInt(parts[1]);

        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

        for(int i = 0; i < n; i++){
            int id = Integer.parseInt(br.readLine());
            List<Integer> list = map.get(id);
            if(list == null){
                list = new ArrayList<Integer>();
            }
            list.add(i);
            map.put(id, list);
        }
        Set<Integer> result = new HashSet<Integer>();
        for(Integer id : map.keySet()){
            List<Integer> list = map.get(id);
            int prevPos = -1;
            for(Integer pos : list){
                if(prevPos == -1){
                    prevPos = pos;
                } else {
                    if(pos - prevPos <= k){
                        result.add(id);
                        break;
                    }
                }
            }
        }
        FileWriter fw = new FileWriter(new File("proximity.out"));

        if(result.size() == -1){
            fw.write("-1");
        } else {
            Integer[]arr = result.toArray(new Integer[0]);
            Arrays.sort(arr);
            fw.write("" + arr[arr.length-1]);
        }
        fw.write("\n");
        fw.close();

    }
}
