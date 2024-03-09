package top.kongsheng.common.expression.statements;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * if
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/23 13:08
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class IfStatement extends Statement {

    /**
     * 布尔表达式
     */
    private Statement test;

    /**
     * 结果
     */
    private Statement consequent;

    /**
     * 其它
     */
    private Statement alternate;

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Object exec = test.exec(variableValueGetFun);
        if (((boolean) exec)) {
            return consequent.exec(variableValueGetFun);
        }
        return alternate.exec(variableValueGetFun);
    }
}
