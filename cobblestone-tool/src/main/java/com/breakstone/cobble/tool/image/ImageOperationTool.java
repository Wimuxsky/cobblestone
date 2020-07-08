package com.breakstone.cobble.tool.image;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;


/**
 * @author: siqigang
 * @date: 2020/5/15
 * @description:
 **/
@Slf4j
public class ImageOperationTool {

    public static void main(String[] args) {
        String imagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0.png";
        String saveImagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0_rotate.png";
        rotate(imagePath, saveImagePath, -50d);
    }

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


}
