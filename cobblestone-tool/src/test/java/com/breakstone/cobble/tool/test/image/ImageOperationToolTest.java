package com.breakstone.cobble.tool.test.image;

import com.breakstone.cobble.tool.image.ImageOperationTool;

/**
 * @author: siqigang
 * @date: 2020/5/15
 * @description:
 **/
public class ImageOperationToolTest {


    public void testRotate() {
        String imagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0.png";
        String saveImagePath = "/Users/Wimux/tmp/image/aaa0aa67616546ec8f3513b054dee7ad0_rotate.png";
        ImageOperationTool.rotate(imagePath,saveImagePath,45d);
    }
}
