package com.breakstone.cobble.tool.image;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import java.util.List;


/**
 * @author: siqigang
 * @date: 2020/5/15
 * @description:
 **/
@Slf4j
public class ImageOperationTool {


    public static void main(String[] args) {
//        String imagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0.png";
//        String saveImagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0_rotate.png";
//        rotate(imagePath, saveImagePath, -50d);

//        appendVertically(Lists.newArrayList(image_path + "1.png", image_path + "2.png"), image_path + "vertically.png");
        composite();
    }

    /**
     * 1.图片旋转
     * 2.图片裁剪
     * 3.图片缩放
     * 4.图片组合-图+文
     * 5.图片组合-图+图
     * 6.图片动画-GIF
     * 7.图片透明度
     * 8.图片加水印
     */

    /**
     * 图片旋转
     *
     * @param sourceImagePath 输入的图片文件
     * @param targetImagePath 输出的图片文件
     * @param rotate          旋转角度(-360~360)
     */
    public static boolean rotate(String sourceImagePath, String targetImagePath, Double rotate) {
        // 参数判断
        // TODO throw Exception
        if (StringUtils.isEmpty(sourceImagePath) || StringUtils.isEmpty(targetImagePath)) {
            return false;
        }
        double imageRotate = null == rotate ? 0 : rotate;
        if (imageRotate < -360 || imageRotate > 360) {
            return false;
        }
        if (imageRotate < 0) {
            imageRotate = 360 + rotate;
        }

        IMOperation operation = new IMOperation();
        operation.addImage(sourceImagePath);
        operation.rotate(imageRotate);
        operation.addImage(targetImagePath);
        ConvertCmd cmd = new ConvertCmd(true);
        try {
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 图片裁剪
     *
     * @param sourceImagePath 输入的图片文件
     * @param targetImagePath 输出的图片文件
     * @param startX          裁剪起始X坐标
     * @param startY          裁剪起始Y坐标
     * @param targetWidth     裁剪目标尺寸width
     * @param targetHeight    裁剪目标尺寸height
     */
    public static boolean crop(String sourceImagePath, String targetImagePath, int startX, int startY, int targetWidth, int targetHeight) {
        IMOperation operation = new IMOperation();
        operation.addImage(sourceImagePath);
        operation.crop(targetWidth, targetHeight, startX, startY);
        operation.addImage(targetImagePath);
        ConvertCmd cmd = new ConvertCmd(true);
        try {
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 图片缩放
     *
     * @param sourceImagePath
     * @param targetImagePath
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static boolean resize(String sourceImagePath, String targetImagePath, int targetWidth, int targetHeight) {
        IMOperation operation = new IMOperation();
        operation.addImage(sourceImagePath);
        operation.resize(targetWidth, targetHeight);
        operation.addImage(targetImagePath);
        ConvertCmd cmd = new ConvertCmd(true);
        try {
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 给图片加水印
     *
     * @param srcPath 源图片路径
     */
    public static void addImgText(String srcPath) throws Exception {
        IMOperation op = new IMOperation();
        op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8")
                .draw("text 5,5 juziku.com");
        op.addImage();// 占位符？？
        op.addImage();
        ConvertCmd convert = new ConvertCmd();
        convert.run(op, srcPath, srcPath);
    }

    private static final String image_path = "/Users/Wimux/T_image/";


    public static boolean appendHorizontally(List<String> appendImagePathList, String savePath) {
        IMOperation op = new IMOperation();
        appendImagePathList.forEach(imagePath -> op.addImage(imagePath));
        op.appendHorizontally();
        op.addImage(savePath);
        ConvertCmd cmd = new ConvertCmd(true);
        try {
            cmd.run(op);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }

    public static boolean appendVertically(List<String> appendImagePathList, String savePath) {
        IMOperation op = new IMOperation();
        // 需要append的图片
        appendImagePathList.forEach(imagePath -> op.addImage(imagePath));
        // 垂直append(对齐方式：）
        op.appendVertically();
        // 输出imagePath
        op.addImage(savePath);
        System.out.println(op.toString());
        return exeConvertCmd(op);
    }


    public static boolean composite() {
        IMOperation op = new IMOperation();
        // 添加空白图片
        op.size(500, 500);
        op.addRawArgs("xc:none");
//        op.addRawArgs("xc:white");
//        op.addRawArgs("xc:#FFF");
        // 垂直append(对齐方式：）
        op.addImage(image_path + "1.png");
        op.addImage(image_path + "cc.png");
        return exeCompositeCmd(op);
    }

    private static boolean exeConvertCmd(IMOperation operation) {
        ConvertCmd cmd = new ConvertCmd(true);
        try {
            System.out.println(operation);
            cmd.run(operation);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return false;
    }

    private static boolean exeCompositeCmd(IMOperation operation) {
        CompositeCmd cmd = new CompositeCmd(true);
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
