package com.breakstone.cobble.tool.test.image;

import lombok.extern.slf4j.Slf4j;
import org.im4java.core.*;
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

    /**
     * 背景+容器+按钮+主图+2文
     */

    @Test
    public void testCompoiste() {
        String title = "让你今天更美丽";
        String subTitle = "Yestar艺星医疗美容医院";
        String mainPath = imagePath("picture.jpg");
        String backgroundPath = imagePath("background.png");


        // 合并主图到空白背景
        IMOperation op = new IMOperation();
        op.geometry(459, 140, 0, 0);
        op.addImage(mainPath);
        // 添加背景
        op.size(690, 140);
        op.addRawArgs("xc:none");
        op.addImage(imagePath("t1.png"));
        // T1
        exeCompositeCmd(op, true);

        // 合并背景到T1
        IMOperation op2 = new IMOperation();
        op2.geometry(681, 341, 232, -80);
        op2.resize(600, 300);
        op2.addImage(backgroundPath);
        // 添加背景
        op2.addImage(imagePath("t1.png"));
        op2.addImage(imagePath("t2.png"));
        // T2
        exeCompositeCmd(op2, true);
        // 合并title到T2
        IMOperation op3 = new IMOperation();
//        op3.geometry(681, 341, 386, 53);
        // 文字方位-东南
//        op.gravity("southeast");
        op3.font("/Users/Wimux/GitHub/fonts/Founder/FZLanTingHei/FZLTCHJW.TTF");
        // 文字大小
        op3.pointsize(28);
        //文字颜色
        op3.fill("#ACBFC8");
        op3.draw("text 386,65 '" + title+"'");

        // 文字大小
        op3.pointsize(20);
        op3.draw("text 386,100 '" + subTitle+"'");
        // 添加背景
        op3.addImage(imagePath("t2.png"));
        op3.addImage(imagePath("t3.png"));

        // T3
        exeConvertCmd(op3, true);

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

    @Test
    public void testText() {

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
