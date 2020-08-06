package com.breakstone.cobble.tool.image;

import com.jhlabs.composite.MiscComposite;
import com.jhlabs.image.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.*;
import org.im4java.process.ArrayListOutputConsumer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    /**
     * 水印图片
     */
    private static Image watermarkImage = null;

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
     * 图片棋盘效果，暫時有問題，http://kyle-in-jp.blogspot.com/2009/05/im4java_27.html
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void checkerboard(String srcImagePath, String destImagePath) throws Exception {
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

        op = new IMOperation();
        op.size(400, 324).addRawArgs("pattern:checkerboard");
        op.addImage(destImagePath);
        op.composite();
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
     * 图片缩放，按照比例
     *
     * @param srcImagePath
     * @param rate         0-1比例
     * @throws Exception
     */
    public static void resize(String srcImagePath, String destImagePath, Double rate, Double quality) throws Exception {
        // 原始图片信息
        IMOperation op = new IMOperation();
        BufferedImage buffimg = ImageIO.read(new File(srcImagePath));
        int w = buffimg.getWidth();
        int h = buffimg.getHeight();
        op.addImage(srcImagePath);
        BigDecimal rateBD = new BigDecimal(Double.toString(rate));
        // 宽度
        BigDecimal w1 = new BigDecimal(Double.toString(w));
        BigDecimal wResult = w1.multiply(rateBD).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal h1 = new BigDecimal(Double.toString(h));
        BigDecimal hResult = h1.multiply(rateBD).setScale(2, BigDecimal.ROUND_HALF_UP);
        op.resize(wResult.intValue(), hResult.intValue());
        op.quality(quality);
        // 也可以这种方式，设置图片质量参数
        if (quality > 0) {
            op.addRawArgs("-quality", String.valueOf(quality));
        }
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片缩放，按照宽高
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width
     * @param height
     * @throws Exception
     */
    public static void geometry(String srcImagePath, String destImagePath, Integer width, Integer height) throws Exception {
        // 原始图片信息
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.geometry(width, height);
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

    /**
     * 图片截取
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width         宽度
     * @param height        高度
     * @param x             左上角x
     * @param y             左上角y
     * @throws Exception
     */
    public static void crop(String srcImagePath, String destImagePath, Integer width, Integer height, Integer x, Integer y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.crop(width, height, x, y);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片变形，倾斜
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void shear(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.shear(x, y);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片变形，拉伸放射效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void activate(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.alpha("activate");
        op.virtualPixel("transparent");
        op.distort("perspective", "0,0, 50,50, 200,0, 150,50, 0,200, 0,200, 200,200, 200,200");
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片多个小图组合
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void distort(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.virtualPixel("tile");
        op.distort("SRT", "0.25 10");
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 拉伸效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void distort1(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.addRawArgs("-matte");
        op.virtualPixel("transparent");
        op.distort("Barrel", "0.0 0.0 0.0 1.0   0.0 0.0 0.5 0.5");
        op.transparent("white");// 背景透明
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }


    public static void tile(String tmpImagePath, String srcImagePath, String destImagePath) throws Exception {
        tile1(tmpImagePath, srcImagePath, destImagePath);
    }

    public static void tile1(String tmpImagePath, String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(6, 6);
        op.addRawArgs("xc:gray50");
        op.frame(2, 2, 2, 0);
        op.addImage(tmpImagePath);
        exeConvertCmd(op, true);
    }

    public static void tile2(String tmpImagePath, String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.resize(20, 20);
        op.filter("point");
        op.resize(200, 200);
        op.size(200, 200);
        op.tile(tmpImagePath);
        op.compose("overlay");
        op.composite();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 左右翻转，左右变形效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void flop(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.flop();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 上下翻转，倒影效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param x
     * @param y
     * @throws Exception
     */
    public static void flip(String srcImagePath, String destImagePath, Double x, Double y) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.flip();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片旋转
     *
     * @param srcImagePath
     * @param destImagePath
     * @param degrees       参数为旋转角度，正数为顺时针旋转，负数为逆时针旋转
     * @throws Exception
     */
    public static void rotate2(String srcImagePath, String destImagePath, Double degrees) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.rotate(degrees);
        // op.transparent("white");// 背景透明
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片水印
     *
     * @param srcImagePath  源图片路径
     * @param destImagePath 目标图片路径
     * @param dissolve      透明度（0-100）
     * @throws Exception
     */
    public static void addImgWatermark(String watermarkImagePath, String srcImagePath, String destImagePath, Integer dissolve) throws Exception {
        BufferedImage buffimg = ImageIO.read(new File(srcImagePath));
        int w = buffimg.getWidth();
        int h = buffimg.getHeight();

        IMOperation op = new IMOperation();
        // 水印图片位置
        op.geometry(watermarkImage.getWidth(null), watermarkImage.getHeight(null), w - watermarkImage.getWidth(null) - 10, h - watermarkImage.getHeight(null) - 10);
        // 水印透明度
        op.dissolve(dissolve);
        // 水印
        op.addImage(watermarkImagePath);
        // 原图
        op.addImage(srcImagePath);
        // 目标
        op.addImage(destImagePath);
        exeCompositeCmd(op, true);
    }

    /**
     * 文字水印
     *
     * @param srcImagePath  源图片路径
     * @param destImagePath 目标图片路径
     * @param content       文字内容（不支持汉字）
     * @throws Exception
     */
    public static void addTextWatermark(String srcImagePath, String destImagePath, String content) throws Exception {
        IMOperation op = new IMOperation();
        op.font("E:\\deploy\\zuomeitu\\font\\CANDY.TTF");//此处文件名不能中文
        // 文字方位-东南
        op.gravity("southeast");
        // 文字大小
        op.pointsize(28);
        //文字颜色
        op.fill("#ACBFC8");
        op.draw("text 10,10 " + content);
        // 原图
        op.addImage(srcImagePath);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 旋涡，打旋
     *
     * @param srcImagePath
     * @param destImagePath
     * @param degrees       参数为旋涡强度
     * @throws Exception
     */
    public static void swirl(String srcImagePath, String destImagePath, Double degrees) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.swirl(degrees);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 色彩抖动 http://kyle-in-jp.blogspot.com/2008/04/imagemagick_1233.html
     *
     * @param srcImagePath
     * @param destImagePath
     * @param method        如h4x4a h6x6a h8x8a
     * @param level         4
     * @throws Exception
     */
    public static void dither(String srcImagePath, String destImagePath, String method, String level) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        if (StringUtils.isNotBlank(level)) {
            op.orderedDither(method, level);
        } else {
            op.orderedDither(method);
        }
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 加边框
     *
     * @param srcImagePath
     * @param destImagePath
     * @param color         边框颜色
     * @param width         竖边框尺寸，即左右边框
     * @param height        横边框尺寸，即上下边框
     * @throws Exception
     */
    public static void border(String srcImagePath, String destImagePath, Integer red, Integer blue, Integer green, String color, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.bordercolor(color);
        op.border(width, height);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 改变图片颜色
     *
     * @param srcImagePath
     * @param destImagePath
     * @param red
     * @param blue
     * @param green
     * @throws Exception
     */
    public static void colorize(String srcImagePath, String destImagePath, Integer red, Integer blue, Integer green) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.colorize(red, blue, green);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片上写文字
     *
     * @param srcImagePath
     * @param destImagePath
     * @param font
     * @param gravity
     * @param pointsize
     * @param content
     * @throws Exception
     */
    public static void draw(String srcImagePath, String destImagePath, String font, String gravity, Integer pointsize, String content) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.font("E:\\deploy\\zuomeitu\\font\\CANDY.TTF");// 此处不能中文
        op.gravity("center");
        op.pointsize(48);
        op.fill("#FFFFFF");
        op.draw("text 10,10 " + "KG,aimblood");
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片缩放
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width
     * @param height
     * @throws Exception
     */
    public static void scale(String srcImagePath, String destImagePath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.scale(width, height);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片缩放
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width
     * @param height
     * @throws Exception
     */
    public static void sample(String srcImagePath, String destImagePath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.sample(width, height);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片缩放
     *
     * @param srcImagePath
     * @param destImagePath
     * @param width
     * @param height
     * @throws Exception
     */
    public static void thumbnail(String srcImagePath, String destImagePath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.thumbnail(width, height);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 放大一倍
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void magnify(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.magnify();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片中心集中，使内爆
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void implode(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.implode(new Double(0.8));
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 写真效果，加边框，倾斜，人造偏光板
     *
     * @param srcImagePath
     * @param destImagePath
     * @param degree
     * @throws Exception
     */
    public static void polaroid(String srcImagePath, String destImagePath, Double degree) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.polaroid(degree);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片波浪效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param amplitude     振幅 上下
     * @param wavelength    波长 左右
     * @throws Exception
     */
    public static void wave(String srcImagePath, String destImagePath, Double amplitude, Double wavelength) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.wave(amplitude, wavelength);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 倒立，旋转180度
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void flip(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.flip();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片模糊处理
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius        半径
     * @param sigma         模糊度
     * @throws Exception
     */
    public static void blur(String srcImagePath, String destImagePath, Double radius, Double sigma) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.blur(radius, sigma);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 局部旋转
     *
     * @param srcImagePath
     * @param destImagePath
     * @param angle         角度，強度
     * @throws Exception
     */
    public static void radialBlur(String srcImagePath, String destImagePath, Double angle) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.radialBlur(angle);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 滤镜-锐化 快速聚焦模糊边缘，提高图像中某一部位的清晰度或者焦距程度，使图像特定区域的色彩更加鲜明
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius        半径
     * @throws Exception
     */
    public static void sharpen(String srcImagePath, String destImagePath, Double radius) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.sharpen(radius);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片模糊化，噪点
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius
     * @throws Exception
     */
    public static void noise(String srcImagePath, String destImagePath, Double radius) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.noise(radius);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 去除杂点，使其更加平滑
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void despeckle(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.despeckle();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片变成照片底片效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void negate(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.negate();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 改变图片亮度
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void contrast(String srcImagePath, String destImagePath, Double value) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.brightnessContrast(value);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片色彩均化
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void equalize(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.equalize();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片过度曝光
     *
     * @param srcImagePath
     * @param destImagePath
     * @param value
     * @throws Exception
     */
    public static void solarize(String srcImagePath, String destImagePath, Double value) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.solarize(value);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片油画特效
     *
     * @param srcImagePath
     * @param destImagePath
     * @param value
     * @throws Exception
     */
    public static void paint(String srcImagePath, String destImagePath, Double value) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.paint(value);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片素描；略图；梗概
     *
     * @param srcImagePath
     * @param destImagePath
     * @param value
     * @throws Exception
     */
    public static void sketch(String srcImagePath, String destImagePath, Double value) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.sketch(value);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片素描效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void charcoal(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.charcoal(factor);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 色彩渲染效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void tint(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.fill("#Fbddff");
        op.tint(120.0);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片水彩效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void modulate(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.modulate(100.0, 250.0);
        op.sketch(0.0, 10.0, 135.0);
        op.spread(1); // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片磨砂效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void colorspace(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.colorspace("gray").colors(4);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片水彩效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void emboss(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.spread(1).median(2.0).emboss(1.0);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片黑白效果，素描效果，非常漂亮
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void threshold(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.threshold().addRawArgs("55%");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 添加边框效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void border(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.bordercolor("#fbfaf7");
        op.border(10, 10);
        op.bordercolor("#918164");
        op.border(5, 5);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 生成文字图片效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param factor
     * @throws Exception
     */
    public static void font(String srcImagePath, String destImagePath, Integer factor) throws Exception {
        IMOperation op = new IMOperation();
        op.size(200, 200);
        op.addImage("xc:white");
        op.font("Tahoma");
        op.pointsize(50);
        op.gravity("center");
        op.fill("#7799ff");
        op.draw("text 0,0 'im4java'");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 艺术文字 颗粒组成
     *
     * @param destImagePath
     * @throws Exception
     */
    public static void font1(String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(120, 40);
        op.addImage("xc:white");
        op.stroke("#fad759");
        op.fill("#f6b739");
        op.font("Tahoma");
        op.draw("font-size 30 text 2,30 'Sample'");
        op.spread(1);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 文字特效，突兀的文字
     *
     * @param destImagePath
     * @throws Exception
     */
    public static void font2(String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(400, 150);
        op.pointsize(50);
        op.gravity("center");
        op.fill("#bbddff");
        op.font("E:\\deploy\\zuomeitu\\font\\shaonv.ttf");// A.ttf不能用中文名文件，改成英文名文件即可
        op.addRawArgs("label:中国人民站起来了");
        op.virtualPixel("background");
        op.background("white");
        op.fx("p{i,(j-cos(pi*i/w*2)*0.7*h/2)/(1-cos(pi*i/w*2)*0.7)}");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 文字 字间距可以调整
     *
     * @param destImagePath
     * @throws Exception
     */
    public static void fontKerning(String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(600, 100);
        op.fill("#F577ee");
        op.font("E:\\deploy\\zuomeitu\\font\\lishu.ttf");// A.ttf不能用中文名文件，改成英文名文件即可
        op.pointsize(50);
        op.background("none");
        op.kerning(10);// 字间距
        op.addRawArgs("label:" + "中国人民站起来了");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片特效，锐化
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius
     * @throws Exception
     */
    public static void edge(String srcImagePath, String destImagePath, Double radius) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.edge(radius);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 装饰图案，圆形效果，如果输入GIF，背景透明
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius
     * @throws Exception
     */
    public static void cicle(String srcImagePath, String destImagePath, Double radius, Double sigma) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.alpha("set");
        op.background("none");
        op.vignette(radius, sigma);
        op.transparent("white");// 背景透明
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 浮雕效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @param radius
     * @throws Exception
     */
    public static void emboss(String srcImagePath, String destImagePath, Double radius) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.emboss(radius);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片阴影，变暗
     *
     * @param srcImagePath
     * @param destImagePath
     * @param azimuth
     * @throws Exception
     */
    public static void shade(String srcImagePath, String destImagePath, Double azimuth) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.shade(azimuth);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片拼接
     *
     * @param srcImagePaths 多图
     * @param destImagePath
     * @param direction     方向，1水平，2垂直
     * @throws Exception
     */
    public static void append(List<String> srcImagePaths, String destImagePath, Integer direction) throws Exception {
        IMOperation op = new IMOperation();
        for (String string : srcImagePaths) {
            op.addImage(string);
        }
        if (direction == 2) {
            op.appendVertically();// 垂直
        } else {
            op.appendHorizontally();
        }
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 水中倒影效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void mirror(String srcImagePath, String destImagePath) throws Exception {
        /* 鏡面画像 */
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight() * 2, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D gr = img2.createGraphics();
        gr.drawImage(img, 0, 0, null);
        gr.dispose();

        MirrorFilter mf = new MirrorFilter();
        /* 角度 */
        // not implemented?
        // mf.setAngle(0f/180f*ImageMath.PI);
        /* 距離 */
        // not implemented?
        // mf.setDistance(0);
        /* 回転 */
        // not implemented?
        // mf.setRotation(0f/180f*ImageMath.PI);
        /* gap */
        mf.setGap(0.01f);
        /* 透明度 */
        mf.setOpacity(0.6f);
        /* 中央Y */
        mf.setCentreY(0.5f);

        BufferedImage img3 = mf.filter(img2, null);
        BufferedImage img4 = new BufferedImage(img.getWidth(), img.getHeight() * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr2 = img4.createGraphics();
        gr2.drawImage(img3, 0, 0, null);
        gr2.dispose();

        ImageIO.write(img4, "jpg", new File(destImagePath));
    }

    /**
     * 图片 叠加功能
     *
     * @param srcImagePath1
     * @param srcImagePath2
     * @param destImagePath
     * @throws Exception
     */
    public static void composite(String srcImagePath1, String srcImagePath2, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath1);
        op.addImage(srcImagePath2);
        op.geometry(110, 90, 230, 220);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    public static void blackThreshold(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.blackThreshold(40.0, true);
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片扇形效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void sector(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage imga = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        CircleFilter cf = new CircleFilter();
        cf.setHeight(120);
        cf.setAngle(2 * ImageMath.PI * -10 / 360);
        System.out.println(2 * ImageMath.PI * -10 / 360);
        cf.setSpreadAngle(2 * ImageMath.PI * 160 / 360);
        System.out.println(2 * ImageMath.PI * 160 / 360);
        img = cf.filter(img, imga);
        ImageIO.write(img, "png", new File(destImagePath));
    }

    /**
     * 图片 扭曲变形，拉伸变形
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void distort(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.alpha("activate");
        op.virtualPixel("transparent");
        // perspective 参数解释
        // 0,0->50,50的坐标变化，200,0->150,50的坐标变化，0,200->0,200的坐标变化,200,200->200,200的坐标变化
        op.distort("perspective", "0,0, 0,0, 324,0, 324,50, 324,324, 324,394, 0,324, 0,324");
        // 左上,右上,右下,左下
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 水波效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void wave(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        SwimFilter sf = new SwimFilter();
        sf.setAmount(10f);
        sf.setScale(32f);
        sf.setStretch(10f);
        sf.setAngle(10f / 180f * ImageMath.PI);
        sf.setTurbulence(1f);
        sf.setTime(0.1f);
        BufferedImage img2 = sf.filter(img, null);
        ImageIO.write(img2, "jpg", new File(destImagePath));
    }

    /**
     * 图片效果，有问题http://kyle-in-jp.blogspot.com/2009/04/imagemagick.html
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void composite(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.addRawArgs("-matte");
        op.channel("rgba");
        op.size(200, 200);
        op.addRawArgs("gradient:#ffffffff-#00000000");
        op.distort("SRT", "45");
        op.compose("dst_in");
        op.composite();
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 截取圆形
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void cicle(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.verbose();
        op.addImage(srcImagePath);
        IdentifyCmd identify = new IdentifyCmd();
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identify.setOutputConsumer(output);
        identify.run(op);
        ArrayList<String> lines = output.getOutput();
        int width = 0;
        int height = 0;
        for (String line : lines) {
            if (line.startsWith("  Geometry: ")) {
                if (line.indexOf("+") != -1) {
                    String sz[] = line.substring("  Geometry: ".length(), line.indexOf("+")).split("x");
                    width = new Integer(sz[0]);
                    height = new Integer(sz[1]);
                }
            }
        }

        IMOperation op2 = new IMOperation();
        op2.size(new Integer(width), new Integer(height));
        op2.addImage("xc:none");
        op2.alpha("activate").channel("rgba");
        op2.fill("white");
        op2.draw("ellipse " + (width / 2) + "," + (height / 2) + "," + (width / 2 - 1) + "," + (height / 2 - 1) + ",0,360");
        op2.addImage(srcImagePath);
        op2.compose("src_in").composite();

        op2.addImage(destImagePath);
        exeConvertCmd(op2, true);
    }

    /**
     * 图片 四角 圆角效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void angles(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.verbose();
        op.addImage(srcImagePath);

        IdentifyCmd identify = new IdentifyCmd();
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identify.setOutputConsumer(output);
        identify.run(op);
        ArrayList<String> lines = output.getOutput();

        int width = 0;
        int height = 0;
        for (String line : lines) {
            if (line.startsWith("  Geometry: ")) {
                if (line.indexOf("+") != -1) {
                    String sz[] = line.substring("  Geometry: ".length(), line.indexOf("+")).split("x");
                    width = new Integer(sz[0]);
                    height = new Integer(sz[1]);
                }
            }
        }

        // 画像サイズと同じ大きさの角丸四角で切り取り
        IMOperation op2 = new IMOperation();
        op2.size(new Integer(width), new Integer(height));
        op2.addImage("xc:none");
        op2.alpha("activate").channel("rgba");
        op2.fill("white");
        op2.draw("roundrectangle 0,0," + (width - 1) + "," + (height - 1) + ",30,30");
        op2.addImage(srcImagePath);
        op2.compose("src_in").composite();

        op2.addImage(destImagePath);
        exeConvertCmd(op2,true);
    }

    /**
     * 图片 网格效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void net(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.fx("i%2*j%2?p:p*0.7");
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片复古效果 有问题
     * http://kyle-in-jp.blogspot.com/2009/12/pywin32imagemagick_27.html
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void fugu(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(200, 200);
        op.addRawArgs("xc:none");
        op.fill("white");
        op.draw("rectangle 10,10 189,189");
        op.spread(5);
        op.median(new Double(2));
        op.addImage(srcImagePath);
        op.sepiaTone(new Double(0.8));
        op.compose("src_in");
        op.composite();
        op.addRawArgs("xc:white");
        op.p_swap();
        op.compose("src-over");
        op.composite();
        op.sketch(new Double(0), new Double(10), new Double(120));
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 带文字标题，倾斜带阴影的卡片效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void card(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.font("E:\\deploy\\zuomeitu\\font\\shaonv.ttf");
        op.pointsize(20);// 文字大小
        op.fill("#303030");
        op.caption("中华人民共和国欢迎您\n山东人民欢迎您");
        op.addImage(srcImagePath);
        op.background("#909090");
        op.polaroid(new Double(20));// 倾斜度
        op.background("#ffffff");
        op.gravity("center");
        op.flatten();
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 艺术毛边效果 嘶哑的图像的边缘处理
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void HairEdge(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.size(350, 225);
        op.addRawArgs("xc:none");
        op.alpha("activate");
        op.channel("rgba");
        op.fill("#303030");
        op.draw("rectangle 20,20,279,204");
        op.blur(1D);
        op.spread(1);
        op.addImage(srcImagePath);
        op.compose("src_in");
        op.composite();
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 拉伸效果，变形
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void stretching(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.shear(10.0, 0.0);
        // op.shear(0.0,10.0);
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    /**
     * 图片 编织效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void weave(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage imga = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        WeaveFilter wf = new WeaveFilter();
        img = wf.filter(img, imga);
        ImageIO.write(img, "png", new File(destImagePath));
    }

    /**
     * 图片艺术效果 古道感觉
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void ancient(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage imga = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        DiffuseFilter gf = new DiffuseFilter();
        gf.setScale(2);
        img = gf.filter(img, imga);

        GaussianFilter gaf = new GaussianFilter(3f);
        img = gaf.filter(img, null);

        BufferedImage img2 = ImageIO.read(new File(srcImagePath));
        ContrastFilter ctf = new ContrastFilter();
        ctf.setBrightness(0.7f);
        ctf.setContrast(0.7f);
        img2 = ctf.filter(img2, imga);

        CompositeFilter cf = new CompositeFilter(MiscComposite.getInstance(MiscComposite.DODGE, 0.5f));
        BufferedImage img3 = cf.filter(img2, img);

        ImageIO.write(img3, "png", new File(destImagePath));
    }

    /**
     * 图片 手写风效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void ancient1(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage imga = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // 拡散
        DiffuseFilter gf = new DiffuseFilter();
        gf.setScale(3);
        img = gf.filter(img, imga);

        // モーションブラー
        MotionBlurFilter mbf = new MotionBlurFilter(12f, 100f, 0.0f, 0.0f);
        img = mbf.filter(img, null);

        BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ScratchFilter sf = new ScratchFilter();
        sf.setAngle(10);
        sf.setAngleVariation(0.08f);
        sf.setColor(0xff808080);
        sf.setDensity(3.9f);
        sf.setLength(0.06f);
        sf.setWidth(0.7f);
        sf.setSeed(2010);
        img2 = sf.filter(img2, null);
        CompositeFilter cf = new CompositeFilter(MiscComposite.getInstance(MiscComposite.ADD, 0.25f));
        BufferedImage img5 = cf.filter(img2, img);
        ImageIO.write(img5, "png", new File(destImagePath));
    }

    /**
     * 图片 颜色渐变效果
     *
     * @param srcImagePath
     * @param destImagePath
     * @throws Exception
     */
    public static void gradient(String srcImagePath, String destImagePath) throws Exception {
        BufferedImage img = ImageIO.read(new File(srcImagePath));
        BufferedImage imga = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        GrayscaleFilter gf = new GrayscaleFilter();
        BufferedImage img2 = gf.filter(img, imga);

        BufferedImage img3 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        GradientFilter grf = new GradientFilter(new Point(0, 0), new Point(0, img.getHeight()), 0xff3070a0, 0xffffff10, false, GradientFilter.LINEAR, GradientFilter.INT_SMOOTH);

        BufferedImage img4 = grf.filter(img3, null);

        CompositeFilter cf = new CompositeFilter(MiscComposite.getInstance(MiscComposite.BLEND, 0.5f));

        BufferedImage img5 = cf.filter(img2, img4);
        ImageIO.write(img5, "png", new File(destImagePath));
    }

    public static void pintu2(String srcImagePath, String srcAimImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.addImage(srcAimImagePath);
        op.alpha("set");
        op.virtualPixel("transparent");
        op.compose("displace");
        op.set("option:compose:args", "-5x-5");
        op.composite();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    public static void pintu(String srcImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.crop(350, 225, 175, 625);
        op.colorspace("gray");
        op.blur(10d, 65000d);
        op.autoLevel();
        // 目标
        op.addImage(destImagePath);
        exeConvertCmd(op, true);
    }

    public static void pintu3(String srcImagePath, String srcAimImagePath, String destImagePath) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImagePath);
        op.addImage(srcAimImagePath);
        op.geometry(350, 225, 175, 625);
        op.compose("over");
        op.composite();
        // 目标
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
