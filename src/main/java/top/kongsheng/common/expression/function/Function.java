package top.kongsheng.common.expression.function;

import top.kongsheng.common.expression.exceptions.ExpressionException;

/**
 * 获取方法
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/18 16:52
 */
@FunctionalInterface
public interface Function<T, R> {

    /**
     * 获取方法
     *
     * @param t 传入值
     * @return 获取值
     * @throws ExpressionException 表达式异常
     */
    R apply(T t) throws ExpressionException;
}
