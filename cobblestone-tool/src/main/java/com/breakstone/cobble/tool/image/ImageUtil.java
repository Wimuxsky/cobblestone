package com.breakstone.cobble.tool.image;

import com.breakstone.cobble.tool.file.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author: siqigang
 * @date: 2020/5/15
 * @description:
 **/

public class ImageUtil {


    private static final String savePath = "/Users/Wimux/tmp/image";


    public static File downloadImage(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return null;
        }
        String suffix = imageUrl.substring(imageUrl.lastIndexOf(".") - 1);
        return FileUtil.saveFile(FileUtil.download(imageUrl), null, savePath, suffix);
    }


}
