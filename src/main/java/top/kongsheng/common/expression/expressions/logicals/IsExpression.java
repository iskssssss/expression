package top.kongsheng.common.expression.expressions.logicals;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.constants.ExpressionConstant;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.statements.Statement;

import java.util.Collection;
import java.util.Map;

/**
 * 是
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 10:27
 */
public class IsExpression extends LogicalExpression {

    public IsExpression(String operator) {
        super(operator);
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Statement left = super.getLeft();
        Object leftValue = left.exec(variableValueGetFun);
        String operator = super.getOperator();
        switch (operator) {
            case ExpressionConstant.IS_EMPTY:
                return leftValue == null || "".equals(leftValue) ||
                        (leftValue instanceof Collection && ((Collection<?>) leftValue).isEmpty()) ||
                        (leftValue instanceof Map && ((Map<?, ?>) leftValue).isEmpty());
            case ExpressionConstant.IS_NOT_EMPTY:
                return leftValue != null && !"".equals(leftValue) &&
                        !(leftValue instanceof Collection && ((Collection<?>) leftValue).isEmpty()) &&
                        !(leftValue instanceof Map && ((Map<?, ?>) leftValue).isEmpty());
            case ExpressionConstant.IS_NULL:
                return leftValue == null;
            case ExpressionConstant.IS_NOT_NULL:
                return leftValue != null;
            default:
        }
        return null;
    }
}
