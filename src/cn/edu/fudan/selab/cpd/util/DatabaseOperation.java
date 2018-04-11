package cn.edu.fudan.selab.cpd.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/9 下午4:37
 */
public class DatabaseOperation {
    public static int databaseOperation(String sql){
        int result = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句
           result = state.executeUpdate(sql);         //将sql语句上传至数据库执行

            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean databaseQueryOperation(String sql){
        boolean result = false;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句
            rs = state.executeQuery(sql);         //将sql语句上传至数据库执行
            if(rs.next()) result = true;
            else result = false;
            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * mode: 1 for name, 2 for location permission, 3 for service permission, 4 for willing region
     * @param idworker
     * @param mode
     * @return
     */
    public static String checkWorkerInfo(int idworker, int mode){
        String result = "";
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句
            switch (mode){
                case 1:
                    rs = state.executeQuery("SELECT nameworker FROM test_crowdsourcingdb.worker " +
                            "where idworker = "+idworker+";");
                    break;
                case 2:
                    rs = state.executeQuery("SELECT permission_location FROM test_crowdsourcingdb.worker " +
                            "where idworker = "+idworker+";");
                    break;
                case 3:
                    rs = state.executeQuery("SELECT permission_resource FROM test_crowdsourcingdb.worker " +
                            "where idworker = "+idworker+";");
                    break;
                case 4:
                    rs = state.executeQuery("SELECT spatial_willing_region FROM test_crowdsourcingdb.worker " +
                            "where idworker = "+idworker+";");
                    break;
            }
                 //将sql语句上传至数据库执行
            //int count = 0;
            if(rs.next()){
                if(rs.getString(1)==null){
                    result = "";
                }else {
                    result = (rs.getString(1));
                }
            }
            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Find if worker idworker can reach node nodename
     * @param idworker
     * @param nodename
     * @return
     */
    public static boolean findIfWorkerCanReachNode(int idworker, String nodename){
        boolean result = false;
        ResultSet rs1 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句
            rs1 = state.executeQuery("SELECT idworker FROM test_crowdsourcingdb.worker where" +
                    " (FIND_IN_SET('" + nodename + "',permission_location) or  FIND_IN_SET('" + nodename
                    + "',permission_resource)) " + "and FIND_IN_SET('" + nodename + "',spatial_willing_region) and idworker = " + idworker + ";");         //将sql语句上传至数据库执行

            if(rs1.next()) result = true;
            else result = false;

            //System.out.println(startReachableSet.get(0));

            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static int[] findFurthestFromStart(ArrayList<String> singlePath){
        int[] result = new int[2]; //int[0] for furthest worker id, int[1] for furthest node index
        ResultSet rs1 = null;
        //boolean findResult1 = false, findResult2 = false, findResult3 = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句
            rs1 = state.executeQuery("SELECT idworker FROM test_crowdsourcingdb.worker where" +
                    " (FIND_IN_SET('" + singlePath.get(0) + "',permission_location) or  FIND_IN_SET('" + singlePath.get(0)
                    + "',permission_resource)) " + "and FIND_IN_SET('" + singlePath.get(0) + "',spatial_willing_region);");         //将sql语句上传至数据库执行

            ArrayList<Integer> startReachableSet = new ArrayList<Integer>();
            while (rs1.next()) {
                startReachableSet.add(rs1.getInt(1));
            }
            if(startReachableSet.size() < 1) {
                result[0] = -1;
                result[1] = -1;
            }else{



                HashMap<Integer,Integer> furthestRecord = new HashMap<Integer,Integer>();
                for(int e : startReachableSet){
                    furthestRecord.put(e,0);
                }

                for(int worker : startReachableSet){
                    int pathcnt = 1;
                    while(pathcnt < singlePath.size()){
                        if(!findIfWorkerCanReachNode(worker,singlePath.get(pathcnt))){
                            break;

                        }
                        furthestRecord.put(worker,pathcnt);
                        //System.out.println("update furthest record:" + worker+ " "+ singlePath.get(pathcnt));
                        pathcnt++;
                    }

                }

                ArrayList<Map.Entry<Integer,Integer>> entries= HashMapUtil.sortMap(furthestRecord);
                result[0] = entries.get(0).getKey();
                result[1] = entries.get(0).getValue();
            }

            //System.out.println(startReachableSet.get(0));

            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    public static int[] findFurthestFromEnd(ArrayList<String> singlePath){
        int[] result = new int[2]; //int[0] for furthest worker id, int[1] for further node index
        ResultSet rs1 = null;
        //boolean findResult1 = false, findResult2 = false, findResult3 = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动

            String jdbc = "jdbc:mysql://127.0.0.1:3306/test_crowdsourcingdb?characterEncoding=UTF-8&useSSL=false";
            Connection conn = DriverManager.getConnection(jdbc, "root", "1227");//链接到数据库

            Statement state = conn.createStatement();   //容器
            //String sql = "insert into requester(namerequester) values(\"ashiley\");";   //SQL语句

            rs1 = state.executeQuery("SELECT idworker FROM test_crowdsourcingdb.worker where" +
                    " (FIND_IN_SET('" + singlePath.get(singlePath.size()-1) + "',permission_location) or  FIND_IN_SET('" + singlePath.get(singlePath.size()-1)
                    + "',permission_resource)) " + "and FIND_IN_SET('" + singlePath.get(singlePath.size()-1) + "',spatial_willing_region);");         //将sql语句上传至数据库执行

            ArrayList<Integer> endReachableSet = new ArrayList<Integer>();
            while (rs1.next()) {
                endReachableSet.add(rs1.getInt(1));
            }
            if(endReachableSet.size() < 1) {
                result[0] = -1;
                result[1] = -1;
            }else{

                HashMap<Integer,Integer> furthestRecord = new HashMap<Integer,Integer>();
                for(int e : endReachableSet){
                    furthestRecord.put(e,singlePath.size()-1);
                }

                for(int worker : endReachableSet){
                    int pathcnt = singlePath.size() - 2;
                    while(pathcnt >= 0){
                        if(!findIfWorkerCanReachNode(worker,singlePath.get(pathcnt))){
                            break;

                        }
                        furthestRecord.put(worker,pathcnt);
                        //System.out.println("update furthest record:" + worker+ " "+ singlePath.get(pathcnt));
                        pathcnt--;
                    }

                }

                ArrayList<Map.Entry<Integer,Integer>> entries= HashMapUtil.sortMap(furthestRecord);
                result[0] = entries.get(entries.size()-1).getKey();
                result[1] = entries.get(entries.size()-1).getValue();
            }

            //System.out.println(startReachableSet.get(0));

            conn.close();//关闭通道


        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws SQLException{
        String s = "A->B->C->E->H->I->K->L->service2";
        String[] split = s.split("->");
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for(int i = 0; i < split.length;i++ ){
            stringArrayList.add(split[i]);
            //System.out.println(split[i]);
        }
        int[] fff = findFurthestFromEnd(stringArrayList);
        System.out.println(fff[0]);
        System.out.println(fff[1]);

    }
}
