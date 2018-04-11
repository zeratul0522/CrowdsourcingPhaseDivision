package cn.edu.fudan.selab.cpd.algorithm;

import cn.edu.fudan.selab.cpd.util.DatabaseOperation;
import cn.edu.fudan.selab.cpd.util.HashMapUtil;

import java.io.*;
import java.util.HashMap;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 下午1:44
 */
public class GplotReader {
    private HashMap<Integer,String> nodename;
    private int[][] matrix;
    private int separator;

    /**
     * 从txt格式的拓扑图中读取需要的数据
     * @param filename
     */
    public void readFromGplotFile(String filename){
        try{
            String filepath = "gplot/"+ filename + ".txt";
            File file = new File(filepath);
            if(file.isFile() && file.exists()){
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(inputStreamReader);
                String line = "";
                int flag1 = 0, flag2 = 0;
                int count = 0;
                nodename = new HashMap<Integer, String>();
                //locations and services
                while((line = br.readLine()) != null && flag2 == 0){
                    if(line.equals("=====")){
                        if(flag1 == 0){
                            flag1 = 1;
                            separator = count;
                        }else{
                            flag2 = 1;
                        }
                        continue;
                    }

                    if(!(DatabaseOperation.databaseQueryOperation("SELECT idlocation FROM test_crowdsourcingdb.location " +
                                    "where namelocation='"+line+"';")||
                            DatabaseOperation.databaseQueryOperation("SELECT idservice FROM test_crowdsourcingdb.offline_service " +
                                    "where nameservice='"+line+"';")) ){
                        System.out.println("Location "+line+" was not included in gplot: "+ filename +".txt");
                        System.exit(0);
                    }
                    nodename.put(count++, line);
                }
                matrix = new int[nodename.size()][nodename.size()];
                for(int i = 0; i < nodename.size(); i++){
                    for(int j = 0; j < nodename.size();j++){
                        matrix[i][j] = -1;
                    }
                }
                //links
                while((line = br.readLine()) != null){
                    String[] split = line.split("-");
                    String src = split[0];
                    String dest = split[1];
                    matrix[HashMapUtil.findIndexInHashmap(src,nodename).
                            get(0)][HashMapUtil.findIndexInHashmap(dest,nodename).get(0)] = 1;

                }
                br.close();
            }else{
                System.out.println("文件不存在！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public HashMap<Integer, String> getNodename() {
        return nodename;
    }

    public void setNodename(HashMap<Integer, String> nodename) {
        this.nodename = nodename;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getSeparator() {
        return separator;
    }

    public void setSeparator(int separator) {
        this.separator = separator;
    }
    public static void main(String[] args){
        GplotReader gplotReader = new GplotReader();
        gplotReader.readFromGplotFile("1");
        HashMap<Integer,String> hm = gplotReader.getNodename();
        //HashMapUtil.printMapValue(hm);
        int[][] matrix = new int[hm.size()][hm.size()];
        matrix = gplotReader.getMatrix();
        for(int i = 0; i < hm.size(); i++){
            for(int j = 0; j < hm.size();j++){
                System.out.println(matrix[i][j]);
            }
        }
     }
}
