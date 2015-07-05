
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class March2013_BreedAssignment {
    @SuppressWarnings("unchecked")
    public static void main(String[]args) throws IOException {
        String problemName = "assign";
        BufferedReader br = new BufferedReader(new FileReader(new File(problemName + ".in")));
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[]parts = br.readLine().split("\\s+");

        int n = Integer.parseInt(parts[0]);
        int k = Integer.parseInt(parts[1]);

        Set<Integer>[] groups = new Set[n];
        for(int i = 0; i < groups.length; i++){
            groups[i] = new HashSet<Integer>();
            groups[i].add(i+1);
        }

        String[] relations = new String[k];

        for(int i = 0; i < k; i++){
            relations[i] = br.readLine();
            parts = relations[i].split("\\s+");
            if("S".equals(parts[0])){
                int id1 = Integer.parseInt(parts[1]);
                int id2 = Integer.parseInt(parts[2]);
                int gr1 = -1;
                int gr2 = -1;
                for(int j = 0; j < groups.length; j++){
                    if(groups[j] != null && groups[j].contains(id1)){
                        gr1 = j;
                    }
                    if(groups[j] != null && groups[j].contains(id2)){
                        gr2 = j;
                    }
                }
                if(gr1 == -1 && gr2 == -1){
                    Set<Integer> newSet = new HashSet<Integer>();
                    newSet.add(id1);
                    newSet.add(id2);
                    for(int j = 0; j < groups.length; j++){
                        if(groups[j] == null){
                            groups[j] = newSet;
                            break;
                        }
                    }
                } else if(gr1 != -1 && gr2 != -1){
                    groups[gr1].addAll(groups[gr2]);
                    groups[gr2] = null;
                } else if(gr1 == -1){
                    groups[gr2].add(id1);
                } else if(gr2 == -1){
                    groups[gr1].add(id2);
                }
            }
        }

        for(int i = 0; i < n; i++){
            if(groups[i] == null){
                for(int j = i; j < n-1; j++){
                    groups[j] = groups[j+1];
                }
                groups[n-1] = null;
            }
        }

        FileWriter fw = new FileWriter(new File(problemName + ".out"));

        Set<Integer>[] exclusiveGroups = new Set[n];
        try {
            for(int i = 0; i < k; i++){
                parts = relations[i].split("\\s+");
                if("D".equals(parts[0])){
                    int id1 = Integer.parseInt(parts[1]);
                    int id2 = Integer.parseInt(parts[2]);
                    int gr1 = -1;
                    int gr2 = -1;

                    for(int j = 0; j < n; j++){
                        if(groups[j] != null && groups[j].contains(id1)){
                            gr1 = j;
                        }
                        if(groups[j] != null && groups[j].contains(id2)){
                            gr2 = j;
                        }
                    }

                    if(gr1 == gr2){
                        throw  new IllegalArgumentException();
                    }

                    int exGr1 = -1;
                    int exGr2 = -1;
                    for(int j = 0; j < n; j++){
                        if(exclusiveGroups[j] != null && exclusiveGroups[j].contains(gr1)){
                            exGr1 = j;
                        }
                        if(exclusiveGroups[j] != null && exclusiveGroups[j].contains(gr2)){
                            exGr2 = j;
                        }
                    }

                    int finalExGr = -1;
                    if(exGr1 == -1 && exGr2 == -1){
                        Set<Integer> newSet = new HashSet<Integer>();
                        newSet.add(gr1);
                        newSet.add(gr2);
                        for(int j = 0; j < n; j++){
                            if(exclusiveGroups[j] == null){
                                exclusiveGroups[j] = newSet;
                                finalExGr = j;
                                break;
                            }
                        }
                    } else if(exGr1 != -1 && exGr2 != -1){
                        exclusiveGroups[exGr1].addAll(exclusiveGroups[gr2]);
                        exclusiveGroups[gr2] = null;
                        finalExGr = exGr1;
                    } else if(exGr1 != -1){
                        exclusiveGroups[exGr1].add(gr2);
                        finalExGr = exGr1;
                    } else if(exGr2 != -1){
                        exclusiveGroups[exGr2].add(gr1);
                        finalExGr = exGr2;
                    }
                    if(exclusiveGroups[finalExGr].size() > 3){
                        throw new IllegalArgumentException();
                    }
                }
            }
        } catch(IllegalArgumentException ex){
            fw.write("0\n");
            fw.close();
            return;
        }

        int res = 1;

        for(int j = 0; j < n; j++){
            if(exclusiveGroups[j] != null){
                if(exclusiveGroups[j].size() == 2){
                    res *= 6;
                }
                for(Integer gr : exclusiveGroups[j]){
                    groups[gr] = null;
                }
            }
        }
        for(int j = 0; j < n; j++){
            if(groups[j] != null){
                res *= 3;
            }
        }
        fw.write("" + res + "\n");
        fw.close();
    }
}
