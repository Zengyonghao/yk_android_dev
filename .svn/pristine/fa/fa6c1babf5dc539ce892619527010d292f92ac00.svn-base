package com.yk.core.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Author:廖贵龙
 * Date:2015-07-06
 * FIXME
 * Todo
 */
public class Tools {
    /**
     * 字符串工具类
     */
    public static class StringTools {
        public static Boolean isNull(String str) {
            return TextUtils.isEmpty(str);
        }

        public static Boolean isNotNull(String str) {
            return !isNull(str);
        }

        /**
         * 去除空并转成小写
         *
         * @param str
         * @return
         * @throws Exception
         */
        public static String toString(String str) throws Exception {
            if (isNull(str)) {
                throw new Exception("Tools.StringTools类下的静态方法toString,传入的参数为空！");
            }
            return str.trim().toLowerCase();
        }

        /**
         * 按指定字符切割
         *
         * @param text
         * @param splitChar
         * @return 数组
         */
        public static String[] StringSplit(String text, String splitChar) {
            ArrayList<String> arrayString = ArrayListSplit(text, splitChar);
            String[] texts = new String[arrayString.size()];
            arrayString.toArray(texts);
            return texts;
        }

        /**
         * 按指定字符切割
         *
         * @param text
         * @param splitChar
         * @return 集合
         */
        public static ArrayList<String> ArrayListSplit(String text, String splitChar) {
            ArrayList<String> arrayString = new ArrayList<String>();
            while (text.indexOf(splitChar) != -1 || text.length() > 0) {
                if (text.indexOf(splitChar) == -1) {
                    arrayString.add(text);
                    text = "";
                } else {
                    arrayString.add(text.substring(0, text.indexOf(splitChar)));
                    if (text.indexOf(splitChar) == text.length() - 1) {
                        arrayString.add("");
                    }
                    text = text.substring(text.indexOf(splitChar) + splitChar.length());
                }
            }
            return arrayString;
        }

        /**
         * 返回字符串长度
         *
         * @return
         */
        public static int GetTextWidth(String text) {
            int a = 0; //小写字母个数
            int A = 0; //大写字母个数
            int f = 0; //中文字符字母个数
            int z = 0; //中文字个数
            String as = "qwertyuiopasdfghjklzxcvbnm,./;'[]1234567890-=~`!@#$%^&*()_+{}|:\"<>?\\";
            String As = "QWERTYUIOPASDFGHJKLZXCVBNM";
            String fs = "~·！@#￥%……&*（）——+-=|、】【{}：；“‘《》，。？、";
            while (text.length() > 0) {
                String c = text.substring(0, 1);
                if (as.indexOf(c) != -1) { //小写字母
                    a++;
                } else if (As.indexOf(c) != -1) { //大写字母
                    A++;
                } else if (fs.indexOf(c) != -1) { //中文字符
                    f++;
                } else { //文字
                    z++;
                }
                text = text.substring(1);
            }

            return a + A + f + z;
        }
    }

    /**
     * 对象工具类
     */
    public static class ObjectTools {
        public static Boolean isNull(Object obj) {
            if (obj == null) {
                return true;
            }
            return false;
        }

        public static Boolean isNotNull(Object obj) {
            return !isNull(obj);
        }

    }

    /**
     * 线程工具类
     */
    public static class ThreadTools {

        /**
         * sleep
         *
         * @param ss 秒
         */
        public static void sleep(int ss) {
            try {
                Thread.sleep(ss * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

