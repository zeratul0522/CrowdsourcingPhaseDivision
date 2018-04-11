package cn.edu.fudan.selab.cpd.util;

import java.util.*;

/**
 * @Desc
 * @Author Fan Zejun E-mail:fzj0522@outlook.com
 * @Version 2018/4/10 下午4:46
 */
public class HashMapUtil {
    //返回hashmap中指定value对应的key值
    public static List<Integer> findIndexInHashmap(String str, HashMap<Integer,String> hashMap) {
        List<Integer> keyList = new ArrayList<Integer>();
        for (Map.Entry<Integer, String> e : hashMap.entrySet()) {
            if (str.equals(e.getValue())) keyList.add(e.getKey());
        }

        return keyList;
    }

    public static void printMapKey(HashMap<Integer,String> hashMap) {
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key);
        }
    }
    public static void printMapValue(HashMap<Integer,String> hashMap) {
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(val);
        }
    }

    public static ArrayList<Map.Entry<Integer,Integer>> sortMap(Map map){
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> obj1 , Map.Entry<Integer, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        return (ArrayList<Map.Entry<Integer, Integer>>) entries;
    }
}
