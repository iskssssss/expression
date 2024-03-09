package top.kongsheng.common.expression.parser;

import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.logs.LogUtil;

/**
 * 抽象解析器
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/20 9:43
 */
public abstract class AbsParser<T, R> extends AbsIterable<Character> {
    /**
     * 表达式字符串
     */
    protected final String expression;

    /**
     * 解析结果
     */
    protected R result;

    protected AbsParser(String expression) {
        super(toCharArray(expression));
        this.expression = expression;
    }

    /**
     * 解析表达式
     *
     * @return this
     */
    public T parse() throws ExpressionException {
        long startTime = System.currentTimeMillis();
        this.result = _parse();
        long endTime = System.currentTimeMillis();
        String finalExpression = super.length > 128 ? this.expression.substring(0, 128) + "..." : this.expression;
        LogUtil.info("表达式（{}）解析耗时：{}ms。", finalExpression, endTime - startTime);
        return ((T) this);
    }

    protected abstract R _parse() throws ExpressionException;

    public String getExpression() {
        return expression;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }
}
