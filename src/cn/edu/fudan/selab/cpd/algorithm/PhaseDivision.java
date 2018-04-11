package cn.edu.fudan.selab.cpd.algorithm;

import cn.edu.fudan.selab.cpd.util.DatabaseOperation;
import cn.edu.fudan.selab.cpd.util.TaskAssignment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 下午7:37
 */
public class PhaseDivision {
    /**
     * Figure out how to assign micro-tasks to workers in a certain task path
     * @param singlePath
     * @return
     */
    ArrayList<Integer> participatedWorkers;
    ArrayList<TaskAssignment> taskAssignmentResult;
    public int finishFlag;

    public PhaseDivision(ArrayList<Integer> participatedWorkers) {
        this.participatedWorkers = participatedWorkers;
        taskAssignmentResult = new ArrayList<TaskAssignment>();
        finishFlag = 1;
    }

    public void findAssignmentDistribution(ArrayList<String> singlePath){
        TaskAssignment ta;
        int singleWorkerNum = checkSingleWorker(singlePath).size();
        //原子任务
        if(singlePath.size() <= 2){
            if(checkSingleWorker(singlePath).size() > 0){
                ta = new TaskAssignment(checkSingleWorker(singlePath).get(0),singlePath);
                taskAssignmentResult.add(ta);
                return;
            }else{
                finishFlag = -1;
                return;
            }
        }

        //If someone can finish this single path on his own
        if(singleWorkerNum > 0){
            //TODO: should choose the best candidate worker here
            ta = new TaskAssignment(checkSingleWorker(singlePath).get(0),singlePath);
            taskAssignmentResult.add(ta);
            return;
        }else{
            int[] startArray = DatabaseOperation.findFurthestFromStart(singlePath);
            int[] endArray = DatabaseOperation.findFurthestFromEnd(singlePath);
            int startWorker = startArray[0], startFurthest = startArray[1];
            int endWorker = endArray[0], endFurthest = endArray[1];

//            System.out.println("startWorker="+startWorker+"startFurthest="+startFurthest+"endWorker="+endWorker+
//                "endFurthest="+endFurthest);
            if(startWorker == -1 || endWorker == -1){
                finishFlag = -1;
                return;
            }

            //if subroute(src, n1) and subroute(n2, dest) have overlapping part
            if(startFurthest >= endFurthest){
                //TODO: choose node nj in the overlapping part which causes minimun extra moving distance for w2;
                int overlappingPoint = startFurthest;
                TaskAssignment ta1 = new TaskAssignment(startWorker,new ArrayList<String>(singlePath.subList(0, overlappingPoint+1)) );
                TaskAssignment ta2 = new TaskAssignment(endWorker, new ArrayList<String>(singlePath.subList(overlappingPoint, singlePath.size())));
                taskAssignmentResult.add(ta1);
                taskAssignmentResult.add(ta2);
                return;
            }else{
                TaskAssignment ta1 = new TaskAssignment(startWorker, new ArrayList<String>( singlePath.subList(0, startFurthest+1)));
                TaskAssignment ta2 = new TaskAssignment(endWorker, new ArrayList<String>( singlePath.subList(endFurthest, singlePath.size())));
                taskAssignmentResult.add(ta1);
                taskAssignmentResult.add(ta2);
                findAssignmentDistribution(new ArrayList<String>(singlePath.subList(startFurthest,endFurthest+1)));
            }
        }





    }

    /**
     * Check if any worker can finish the whole task path on his own
     * @param singlePath
     * @return
     */
    public ArrayList<Integer> checkSingleWorker(ArrayList<String> singlePath){
        ArrayList<Integer> result = new ArrayList<Integer>();
        HashMap<Integer,Integer> temp = new HashMap<Integer, Integer>();
        for(int i = 0; i < participatedWorkers.size();i++){
            temp.put(i,1);
        }

        for(int j = 0; j < participatedWorkers.size();j++){
            int idworker = participatedWorkers.get(j);
            String locationStr = DatabaseOperation.checkWorkerInfo(idworker, 2);
            String serviceStr = DatabaseOperation.checkWorkerInfo(idworker,3);
            String willingRegionStr = DatabaseOperation.checkWorkerInfo(idworker,4);
            for(String location : singlePath){
                //System.out.println(location);
                if(!locationStr.contains(location) && !serviceStr.contains(location)){
                    temp.put(j,-1);
                }
                if(!willingRegionStr.contains(location)){
                    temp.put(j,-1);
                }

            }
        }

        Iterator iter = temp.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if((Integer)val == 1){
                result.add(participatedWorkers.get((Integer) key));
            }
        }

        return result;
    }

    public static ArrayList<String> convertToTaskPath(String gplotFileName, String src, String dest){
        ArrayList<String> result = new ArrayList<String>();
        GplotReader gplotReader = new GplotReader();
        gplotReader.readFromGplotFile(gplotFileName);
        HashMap<Integer,String> hm = gplotReader.getNodename();
        //HashMapUtil.printMapValue(hm);
        int[][] matrix = new int[hm.size()][hm.size()];
        matrix = gplotReader.getMatrix();
        PathFinder pf = new PathFinder(matrix, hm, src, dest);
        pf.getPath();
        for(String s : pf.getResult()){
            result.add(s);
        }
        return result;
    }

    public static void main(String[] args){
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(1);
        al.add(2);
        al.add(3);
        al.add(4);
        al.add(5);
        PhaseDivision pd = new PhaseDivision(al);
        String s = "B->K->I->service3";
        String[] split = s.split("->");
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for(int i = 0; i < split.length;i++ ){
            stringArrayList.add(split[i]);
            //System.out.println(split[i]);
        }

        pd.findAssignmentDistribution(stringArrayList);
        if(pd.finishFlag == -1){
            System.out.println("No matching result.");
            System.exit(0);
        }
        ArrayList<TaskAssignment> taskAssignmentArrayList = pd.taskAssignmentResult;
        for(TaskAssignment ta : taskAssignmentArrayList){
            System.out.print("WorkerID: "+ta.getWorkerId());
            System.out.print("  Route: ");
            for(String sub : ta.getSubTaskPath()){
                System.out.print(sub+" ");
            }
            System.out.println("");
        }
    }
}
