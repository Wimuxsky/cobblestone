package com.breakstone.cobble.tool.test.image;

import lombok.extern.slf4j.Slf4j;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IMOperation;
import org.im4java.utils.BatchConverter;
import org.junit.jupiter.api.Test;

/**
 * @Author: siqigang
 * @Date: 2020/8/6 下午4:27
 **/
@Slf4j
public class CompositeUtil {
    private static String imagePath(String name) {
        return "/Users/Wimux/T_image/" + name;
    }


    @Test
    public void composite1() {
        IMOperation op = new IMOperation();
        // change
        op.addImage(imagePath("2.png"));


        // base-image
        op.addImage(imagePath("bg.png"));


        // 结果图片
        op.addImage(imagePath("r1.png"));

        exeCompositeCmd(op, true);
    }

    @Test
    public void composite2() {
        IMOperation op = new IMOperation();
        op.compose("difference");
        op.addImage(imagePath("2.png"));
        op.addImage(imagePath("bg.png"));
        op.addImage(imagePath("r2.png"));

        exeCompositeCmd(op, true);
    }

    @Test
    public void composite3() {
        IMOperation op = new IMOperation();
        // with,height
//        op.geometry(200,500);
        // width, height,x,y
        op.geometry(500, 500, -200, 300);
        op.addImage(imagePath("2.png"));

        op.addImage(imagePath("bg.png"));
        op.addImage(imagePath("r3.png"));

        exeCompositeCmd(op, true);
    }

    @Test
    public void composite4() {
        IMOperation op = new IMOperation();
        // with,height
//        op.geometry(200,500);
        // width, height,x,y
        op.geometry(500, 500, -200, 300);
        op.addImage(imagePath("2.png"));

        // 添加背景
        op.size(800, 1200);
        op.addRawArgs("xc:gray50");
        op.addImage(imagePath("r4.png"));


        exeCompositeCmd(op, true);
    }

    enum GravityEnum {
        NorthWest,
        North,
        NorthEast,
        West,
        Center,
        East,
        SouthWest,
        South,
        SouthEast
    }

    @Test
    public void composite5() {
        IMOperation op = new IMOperation();
        // with,height
//        op.geometry(200,500);
        // width, height,x,y
        // 方位
        op.gravity(GravityEnum.East.name());
        // 大小，起始位置
        op.geometry(500, 500, -200, 300);
        op.addImage(imagePath("2.png"));

        // 添加背景
        op.size(800, 1200);
        op.addRawArgs("xc:gray50");
        op.addImage(imagePath("r5.png"));


        exeCompositeCmd(op, true);
    }

    @Test
    public void composite6() {
        IMOperation op = new IMOperation();
        // 方位
        op.gravity(GravityEnum.East.name());
        // 大小，起始位置
        op.geometry(500, 500, -200, 300);

        op.addImage(imagePath("2.png"));


        // 添加背景
        op.size(800, 1200);
        op.addRawArgs("xc:gray50");
        op.addImage(imagePath("r6.png"));

        exeCompositeCmd(op, true);

    }


    private static boolean exeConvertCmd(IMOperation operation, boolean useGraphicsMagick) {
        ConvertCmd cmd = new ConvertCmd(useGraphicsMagick);
        try {
            System.out.println(operation);
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }


    private static boolean exeCompositeCmd(IMOperation operation, boolean useGraphicsMagick) {
        CompositeCmd cmd = new CompositeCmd(useGraphicsMagick);
        try {
            System.out.println(operation);
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }
}