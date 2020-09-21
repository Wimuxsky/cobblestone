package com.breakstone.cobble.tool.test.image;

import org.junit.jupiter.api.Test;

/**
 * @Author: siqigang
 * @Date: 2020/8/18 下午8:35
 **/
public class JavaTest {

    @Test
    public void ta() {
        System.out.println(a());
    }


    public int a() {
        int i=0;
        try{
            return i++;
        }catch (Exception e) {
            return 10;
        }finally {
            return i+10;
        }
    }
}
