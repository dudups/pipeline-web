package com.ezone.devops.report.utils;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * 解压缩文件：tar.gz
 */
public class UnGZipUtil {

    /**
     * @param tarZipSource 源文件
     * @param targetDir    目标目录
     */
    public static void decompress(String tarZipSource, String targetDir) {
        FileInputStream fInput = null;
        BufferedInputStream bufInput = null;
        ArchiveInputStream archiveInput = null;
        try {
            fInput = new FileInputStream(tarZipSource);
            bufInput = new BufferedInputStream(fInput);
            GZIPInputStream gzipInput = new GZIPInputStream(bufInput);
            ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
            archiveInput = archiveStreamFactory.createArchiveInputStream("tar", gzipInput);
            TarArchiveEntry entry = (TarArchiveEntry) archiveInput.getNextEntry();
            while (entry != null) {
                String entryName = entry.getName();
                if (entry.isDirectory()) {
                    createDir(targetDir, entryName, 1);
                } else if (entry.isFile()) {
                    String fullFileName = createDir(targetDir, entryName, 2);
                    warpOutput(fullFileName, archiveInput);
                }
                // 下一个条目
                entry = (TarArchiveEntry) archiveInput.getNextEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(archiveInput);
            close(bufInput);
            close(fInput);
        }
    }

    private static void warpOutput(String file, InputStream input) throws IOException {
        FileOutputStream fOuput = new FileOutputStream(file);
        BufferedOutputStream bufOutput = new BufferedOutputStream(fOuput);
        int b;
        while ((b = input.read()) != -1) {
            bufOutput.write(b);
        }
        bufOutput.flush();
        bufOutput.close();
    }

    /**
     * @param input
     */
    public static void close(InputStream input) {
        // 静默关闭处理
        try {
            if (null != input) {
                input.close();
            }
            input = null;
        } catch (IOException e) {
        }
    }

    /**
     * @param baseDir 根目录
     * @param entry   压缩包条目
     * @param type    类型：1、目录；2、文件
     * @return
     */
    public static String createDir(String baseDir, String entry, int type) {
        String[] items = entry.split(File.separator);
        String fullFilePath = baseDir;
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            fullFilePath = fullFilePath + File.separator + item;
            if (type == 2) {
                if (i != items.length - 1) {
                    File tmpFile = new File(fullFilePath);
                    if (!tmpFile.exists()) {
                        tmpFile.mkdir();
                    }
                }
            } else {
                File tmpFile = new File(fullFilePath);
                if (!tmpFile.exists()) {
                    tmpFile.mkdir();
                }
            }
        }
        // 返回目录全路径
        File fullFile = new File(fullFilePath);
        return fullFile.getAbsolutePath();
    }

}

