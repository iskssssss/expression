package top.kongsheng.common.expression.exceptions;

/**
 * 表达式异常
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/27 11:47
 */
public class ExpressionException extends Exception {

    public ExpressionException(String message) {
        super(message);
    }

    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}
