package com.personal.wordmaster.constant;

public class AppConstant {

    // DeepSeek API（Key 由用户在自己手机上填写，见 ApiKeyUtils）
    public static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    public static final String DEEPSEEK_MODEL = "deepseek-chat";
    public static final String DEEPSEEK_SYSTEM_PROMPT =
        "你是一个专业的单词表解析工具。我会给你一段OCR识别出的文本，来自一张中英对照单词表的照片。" +
        "文本中每行或每组都包含一个英文单词和对应的中文释义。" +
        "请提取所有英文单词和对应中文释义，仅返回标准JSON数组，格式为[{\"word\":\"英文单词\",\"meaning\":\"中文释义\"},...]。" +
        "忽略页码、标题等无关内容。无多余文字、无解释、无备注。只返回JSON。";

    // 图片压缩
    public static final int IMAGE_MAX_WIDTH = 1024;
    public static final int IMAGE_MAX_HEIGHT = 1024;
    public static final int IMAGE_QUALITY = 80;

    // 掌握状态
    public static final int STATUS_UNFAMILIAR = 0;
    public static final int STATUS_MASTERED = 1;

    // 掌握率阈值
    public static final float MASTERY_THRESHOLD = 0.8f;

    // 复习间隔（天）：毫秒
    public static final long INTERVAL_UNKNOWN_FIRST = 1L * 24 * 60 * 60 * 1000;
    public static final long INTERVAL_KNOWN_FIRST = 3L * 24 * 60 * 60 * 1000;
    public static final long INTERVAL_MAX = 15L * 24 * 60 * 60 * 1000;

    // 数据库名称
    public static final String DB_NAME = "wordmaster.db";

    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
}
