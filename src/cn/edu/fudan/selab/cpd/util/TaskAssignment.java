package cn.edu.fudan.selab.cpd.util;

import java.util.ArrayList;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 下午8:35
 */
public class TaskAssignment {
    /**
     * startIndex and endIndex are the index in the task path list
     */
   private int workerId;
   private ArrayList<String> subTaskPath;

    public TaskAssignment(int workerId, ArrayList<String> subTaskPath) {
        this.workerId = workerId;
        this.subTaskPath = subTaskPath;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public ArrayList<String> getSubTaskPath() {
        return subTaskPath;
    }

    public void setSubTaskPath(ArrayList<String> subTaskPath) {
        this.subTaskPath = subTaskPath;
    }
}
