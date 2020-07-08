package com.breakstone.cobble.tool.file;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.UUID;

/**
 * @author: siqigang
 * @date: 2020/5/15
 * @description:
 **/
public class FileUtil {
    private static final String DEFALT_SAVEPATH = "/Users/Wimux/tmp/file";


    public static byte[] download(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        DataInputStream dis = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            URL downloadUrl = new URL(url);
            dis = new DataInputStream(downloadUrl.openStream());

            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = dis.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
                if (null != dis) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static File saveFile(byte[] fileBytes, String fileName, String savePath, String suffix) {
        if (null == fileBytes || fileBytes.length <= 0) {
            return null;
        }
        String fileSavePath = StringUtils.isEmpty(savePath) ? DEFALT_SAVEPATH : savePath;
        String fileSaveName = StringUtils.isEmpty(fileName) ? UUID.randomUUID().toString().replace("-", "") : fileName;
        String fileSaveSuffix = suffix;
        if (fileSaveName.contains(".")) {
            if (StringUtils.isEmpty(fileSaveSuffix)) {
                fileSaveSuffix = fileSaveName.substring(fileSaveName.lastIndexOf(".") - 1);
            }
            fileSaveName = fileSaveName.substring(0, fileSaveName.lastIndexOf("."));
        }
        if (StringUtils.isNotEmpty(fileSaveSuffix) && !fileSaveSuffix.startsWith(".")) {
            fileSaveSuffix = "." + fileSaveSuffix;
        }
        File file = null;
        FileOutputStream fileOutputStream = null;

        try {
            file = new File(fileSavePath + "/" + fileSaveName + fileSaveSuffix);
            fileOutputStream = new FileOutputStream(file);
            int fileLength = fileBytes.length;
            for (int i = 0; i < fileLength; i += 1024) {
                int length = fileLength - i < 1024 ? fileLength - i : 1024;
                fileOutputStream.write(fileBytes, i, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}
