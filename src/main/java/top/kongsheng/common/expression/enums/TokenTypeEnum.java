package top.kongsheng.common.expression.enums;

/**
 * token类型
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/21 16:44
 */
public enum TokenTypeEnum {
    /**
     * 关键字
     */
    KEYWORD,
    /**
     * 变量
     */
    VARIABLE,
    /**
     * 标识符
     */
    IDENTIFIER,
    /**
     * 数字
     */
    NUMBER,
    /**
     * 字符串
     */
    STRING,
    /**
     * 标点符号
     */
    PUNCTUATOR,
    /**
     * (过时)符号
     */
    CONDITION,
    /**
     * (过时)列表
     */
    ARRAY
}
