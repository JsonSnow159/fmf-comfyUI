package com.fmf.fmf.comfyui.utils;

import java.util.Random;

/**
 * @Author:吴金才
 * @Date:2024/2/4 11:08
 */
public class SeedUtil {
    public static int generateRandom4DigitNumber() {
        // 创建 Random 对象
        Random random = new Random();

        // 生成 4 位数字，范围在 1000 到 9999 之间
        int randomNumber = 1000 + random.nextInt(9000);

        return randomNumber;
    }
}
