package cn.edu.fudan.selab.cpd.util;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/12 下午3:23
 */
public class TaskPhase {
    private String src;
    private String dest;

    public TaskPhase(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
