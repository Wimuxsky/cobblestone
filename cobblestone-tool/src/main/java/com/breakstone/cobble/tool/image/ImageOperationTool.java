package com.breakstone.cobble.tool.image;

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
 * @wiki: http://www.imagemagick.org/Usage/compose/
 * @wiki: http://www.imagemagick.org/Usage/layers/
 * @wiki: http://www.graphicsmagick.org/composite.html
 * @wiki: https://o2team.github.io/notes/2018/06/06/ImageMagick_intro/
 * @wiki: https://www.liangzl.com/get-article-detail-21501.html
 **/
@Slf4j
public class ImageOperationTool {


    public static void main(String[] args) {
//        String imagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0.png";
//        String saveImagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0_rotate.png";
//        rotate(imagePath, saveImagePath, -50d);

//        appendVertically(Lists.newArrayList(image_path + "1.png", image_path + "2.png"), image_path + "vertically.png");
//        composite();

        sepiaTone(imagePath("1.png"), imagePath("sepiaTone.png"));
    }

    private static String imagePath(String name) {
        return image_path + name;
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

        IMOperation op = new IMOperation();
        op.addImage(sourceImagePath);
        op.rotate(imageRotate);
        op.addImage(targetImagePath);
        return exeConvertCmd(op, true);
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
        IMOperation op = new IMOperation();
        op.addImage(sourceImagePath);
        op.crop(targetWidth, targetHeight, startX, startY);
        op.addImage(targetImagePath);
        return exeConvertCmd(op, true);
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
        IMOperation op = new IMOperation();
        op.addImage(sourceImagePath);
        op.resize(targetWidth, targetHeight);
        op.addImage(targetImagePath);
        return exeConvertCmd(op, true);
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

    public static void addImageWatermark(String srcImagePath, String destImagePath, String watermarkImagePath) {
        IMOperation op = new IMOperation();
        // 水印图片位置
        op.geometry(5, 5);
        // 水印透明度
        op.dissolve(50);
        // 水印
        op.addImage(watermarkImagePath);
        // 原图
        op.addImage(srcImagePath);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }


    /**
     * 黑白图片效果
     *
     * @param srcImagePath
     * @param destImagePath
     */
    public static void gray(String srcImagePath, String destImagePath) {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.colorspace("gray");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 电影效果，老照片效果
     *
     * @param srcImagePath
     * @param destImagePath
     */
    public static void sepiaTone(String srcImagePath, String destImagePath) {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.sepiaTone(55000.0);
        op.addImage(destImagePath);
        exeConvertCmd(op, false);
    }


    /**
     * 图片 等离子，磨砂 效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void plasma(String srcImagePath, String destImagePath) {
        IMOperation op = new IMOperation();
        op.seed().addRawArgs("6215");
        op.size(400, 324).addRawArgs("plasma:fractal");
        op.fx("lightness");
        op.alpha("activate").channel("a").fx("r").channel("rgba");
        op.addImage(srcImagePath);
        op.addImage(srcImagePath);
        op.compose("src_in").composite();
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }


    /**
     * 图片 median，模糊效果，油画效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void median(String srcImagePath, String destImagePath) {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.median(30.0);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 照片突出效果
     *
     * @param srcImagePath  原文件路径
     * @param destImagePath 目标文件路径
     * @param size          边框突兀程度
     * @throws Exception
     */
    public static void button(String srcImagePath, String destImagePath, int size) {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.raise(size);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 照片突出效果
     *
     * @param srcImagePath  原文件路径
     * @param destImagePath 目标文件路径
     * @throws Exception
     */
    public static void button2(String srcImagePath, String destImagePath) {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.flip();
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片加边框效果，立体
     *
     * @param srcImagePath
     * @param width           竖向边框厚度
     * @param height          横向边框厚度
     * @param outerBevelWidth 外边框斜面宽带
     * @param innerBevelWidth 内边框斜面宽度
     * @param destImagePath
     * @throws Exception
     */
    public static void frame(String srcImagePath, Integer width, Integer height, Integer outerBevelWidth, Integer innerBevelWidth, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.frame(width, height, outerBevelWidth, innerBevelWidth);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }


    /**
     * 照片外星人效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width
     * @param height
     * @throws Exception
     */
    public static void liquidRescale(String srcImagePath, String destImagePath, Integer width, Integer height) throws Exception {
        // 原始图片信息
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.liquidRescale(width, height);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }


    private static final String image_path = "/Users/Wimux/T_image/";


    public static boolean appendHorizontally(List<String> appendImagePathList, String savePath) {
        IMOperation op = new IMOperation();
        appendImagePathList.forEach(imagePath -> op.addImage(imagePath));
        op.appendHorizontally();
        op.addImage(savePath);
        return exeConvertCmd(op, true);
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
        return exeConvertCmd(op, true);
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
        return exeCompositeCmd(op, true);
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
