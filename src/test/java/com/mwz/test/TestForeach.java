package com.mwz.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzm
 * @date 2019年08月22日 17:46
 */
public class TestForeach {

    public static void main(String[] args) {
        Map<String,List<String>> cameraMap = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        list1.add("01");
        list1.add("11");
        List<String> list2 = new ArrayList<>();
        list2.add("05");
        list2.add("07");
        cameraMap.put("A",list1);
        cameraMap.put("B",list2);
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        cameraMap.forEach((team, cameraIds) -> cameraIds.forEach(cameraId -> {
            Long startTime = System.currentTimeMillis();
            System.out.println(team + "---->" + cameraId);
            for (String str : list){
                System.out.println(str);
            }
            Long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
        }));
    }


}
