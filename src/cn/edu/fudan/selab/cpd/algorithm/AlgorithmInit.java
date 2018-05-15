package cn.edu.fudan.selab.cpd.algorithm;

import cn.edu.fudan.selab.cpd.util.DatabaseOperation;
import cn.edu.fudan.selab.cpd.util.TaskAssignment;
import cn.edu.fudan.selab.cpd.util.TaskPhase;
import sun.jvm.hotspot.opto.Phase;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 上午10:57
 */
public class AlgorithmInit {

    public static void main(String[] args){

        //The task phases are specified by requester.
        ArrayList<TaskPhase> taskPhaseArrayList = new ArrayList<TaskPhase>();
        taskPhaseArrayList.add(new TaskPhase("Mail Room","Fetch Product"));
        taskPhaseArrayList.add(new TaskPhase("","IT Equipment Assemble"));
        taskPhaseArrayList.add(new TaskPhase("Library","Material Registration"));
        init("motivation",taskPhaseArrayList);
    }

    public static void init(String filename,ArrayList<TaskPhase> taskPhaseArrayList){

        ArrayList<String> taskpaths = new ArrayList<String>();
        int phaseNum = taskPhaseArrayList.size();
        HashMap<Integer,ArrayList<String >> phaseMap = new HashMap<Integer,ArrayList<String >>();
        String lastDest = "INITIAL";
        int cnt = 0;
        for(TaskPhase tp : taskPhaseArrayList){
            if(lastDest.equals("INITIAL")) {

                phaseMap.put(cnt,PhaseDivision.convertToTaskPath(filename,tp.getSrc(),tp.getDest()));
                cnt++;
                lastDest = tp.getDest();
            }else{
                if(tp.getSrc().equals("")){
                    ArrayList<String> temppaths = PhaseDivision.convertToTaskPath(filename,lastDest,tp.getDest());
                    phaseMap.put(cnt,temppaths);
                    cnt++;


                }else{
                    ArrayList<String> temppaths1 = PhaseDivision.convertToTaskPath(filename,lastDest,tp.getSrc());
                    phaseMap.put(cnt,temppaths1);
                    cnt++;

                    ArrayList<String> temppaths2 = PhaseDivision.convertToTaskPath(filename,tp.getSrc(),tp.getDest());
                    phaseMap.put(cnt,temppaths2);
                    cnt++;

                    phaseNum++;

                }
                lastDest = tp.getDest();

            }

        }


       taskpaths = conbination(phaseMap);
        for(int i = 0; i < taskpaths.size(); i++){
          taskpaths.set(i,taskpaths.get(i).substring(0,taskpaths.get(i).length()-2));
        }

        Collections.sort(taskpaths);
        //Print all possible task paths
        for(String s : taskpaths){
            System.out.println(s);
        }
        System.out.println(" ");
        ArrayList<Integer> al = new ArrayList<Integer>();
        for(int i = 6; i <=12; i++){
            al.add(i);
        }


        //倒序排列
        for(int i = taskpaths.size()-1; i >= 0; i-- ){
            System.out.println("Work on route: "+taskpaths.get(i));
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
                continue;
            }
            ArrayList<TaskAssignment> taskAssignmentArrayList = pd.taskAssignmentResult;
            System.out.println("");

            for(TaskAssignment ta : taskAssignmentArrayList){
                String temp = "";
                System.out.print("WorkerID: "+ta.getWorkerId());
                System.out.print(", Worker Name: "+ DatabaseOperation.checkWorkerInfo(ta.getWorkerId(),1));
                System.out.print(" | Route: ");
                for(String sub : ta.getSubTaskPath()){
                    temp += sub+"->";
                    //System.out.print(sub+",");
                }
                temp = temp.substring(0,temp.length()-2);
                System.out.print(temp);
                System.out.println("");

            }
            return;
        }


    }

    /**
     * Cut the start of the path
     * For example, change Hallway B->Administration Building->Material Registration
     * into Administration Building->Material Registration
     * @param input
     * @return
     */
    public static String cutPath(String input){
        String[] strings = input.split("->");
        String result = "";
        for(int i = 1; i < strings.length-1;i++){
            result += strings[i] + "->";
        }
        result += strings[strings.length-1];
        return result;
    }

    public static ArrayList<String> conbination(HashMap<Integer,ArrayList<String>> inputMap){
        ArrayList<String> result = new ArrayList<String>();
        String current = "";
        conbination(inputMap, 0, current, result);
        return result;
    }

    public static void conbination(HashMap<Integer,ArrayList<String>> inputMap, int index, String current, ArrayList<String> result){
        if(index == inputMap.size()){
            result.add(current);
            return;
        }else{
            ArrayList<String> list = inputMap.get(index);
            for(String s : list){
                String currentCopy = current;
                if(index > 0){
                    currentCopy+=(cutPath(s)+"->");
                }else{
                    currentCopy+=(s+"->");
                }

                conbination(inputMap, index+1, currentCopy, result);
            }
        }

    }
 }
