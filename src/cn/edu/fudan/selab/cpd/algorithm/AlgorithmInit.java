package cn.edu.fudan.selab.cpd.algorithm;

import cn.edu.fudan.selab.cpd.util.TaskAssignment;
import sun.jvm.hotspot.opto.Phase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 上午10:57
 */
public class AlgorithmInit {
    public static void main(String[] args){
        ArrayList<String> taskpaths = PhaseDivision.convertToTaskPath("1","B","service3");
        Collections.sort(taskpaths);

        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(1);
        al.add(2);
        al.add(3);
        al.add(4);
        al.add(5);

        //倒序排列
        for(int i = taskpaths.size()-1; i >= 0; i-- ){
            System.out.println(taskpaths.get(i));
            String s = taskpaths.get(i);
            String[] split = s.split("->");
            ArrayList<String> stringArrayList = new ArrayList<String>();
            for(int j = 0; j < split.length;j++ ){
                stringArrayList.add(split[j]);
                //System.out.println(split[i]);
            }

            PhaseDivision pd = new PhaseDivision(al);
            pd.findAssignmentDistribution(stringArrayList);
            if(pd.finishFlag == -1){
                System.out.println("No matching result.");
                break;
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
            return;
        }


    }
}
