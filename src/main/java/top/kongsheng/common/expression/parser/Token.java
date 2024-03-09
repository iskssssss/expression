package top.kongsheng.common.expression.parser;

import top.kongsheng.common.expression.enums.TokenTypeEnum;
import lombok.Data;
import lombok.ToString;

/**
 * 节点
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/27 13:53
 */
@Data
@ToString
public class Token {

    /**
     * 节点类型
     */
    private TokenTypeEnum type;

    /**
     * 原始值
     */
    private String raw;

    /**
     * 是否存在小数点
     */
    private boolean point;

    /**
     * 开始位置
     */
    private int startPosition;

    /**
     * 结束位置
     */
    private int endPosition;

    public Token(TokenTypeEnum type, String raw) {
        this.type = type;
        this.raw = raw;
    }
}
