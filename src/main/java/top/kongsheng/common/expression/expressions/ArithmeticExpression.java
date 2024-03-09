package top.kongsheng.common.expression.expressions;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.constants.ExpressionConstant;
import top.kongsheng.common.expression.convert.DoubleConvert;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.expressions.logicals.LogicalExpression;
import top.kongsheng.common.expression.statements.Statement;

/**
 * 算术
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 10:29
 */
public class ArithmeticExpression extends LogicalExpression {

    public ArithmeticExpression(String operator) {
        super(operator);
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Statement left = super.getLeft();
        Statement right = super.getRight();
        Object leftValue = left.exec(variableValueGetFun);
        Object rightValue = right.exec(variableValueGetFun);
        String operator = super.getOperator();
        switch (operator) {
            case ExpressionConstant.ADD:
                if (leftValue instanceof CharSequence || rightValue instanceof CharSequence) {
                    return String.valueOf(leftValue) + rightValue;
                }
                Object result = calc(leftValue, rightValue, operator);
                if (result == null) {
                    throw new ExpressionException(leftValue.getClass() + "无法和" + rightValue.getClass() + "做加法运算。");
                }
                return result;
            case ExpressionConstant.SUBTRACT:
                result = calc(leftValue, rightValue, operator);
                if (result == null) {
                    throw new ExpressionException(leftValue.getClass() + "无法和" + rightValue.getClass() + "做减法运算。");
                }
                return result;
            case ExpressionConstant.MULTIPLY:
                result = calc(leftValue, rightValue, operator);
                if (result == null) {
                    throw new ExpressionException(leftValue.getClass() + "无法和" + rightValue.getClass() + "做乘法运算。");
                }
                return result;
            case ExpressionConstant.DIVIDE:
                result = calc(leftValue, rightValue, operator);
                if (result == null) {
                    throw new ExpressionException(leftValue.getClass() + "无法和" + rightValue.getClass() + "做除法运算。");
                }
                return result;
            default:
        }
        return null;
    }

    private Object calc(Object leftValue, Object rightValue, String op) throws ExpressionException {
        Double leftValueFloat = DoubleConvert.convert(leftValue);
        Double rightValueFloat = DoubleConvert.convert(rightValue);
        if (leftValueFloat == null || rightValueFloat == null) {
            return null;
        }
        Double result;
        switch (op) {
            case ExpressionConstant.ADD:
                result = leftValueFloat + rightValueFloat;
                break;
            case ExpressionConstant.SUBTRACT:
                result = leftValueFloat - rightValueFloat;
                break;
            case ExpressionConstant.MULTIPLY:
                result = leftValueFloat * rightValueFloat;
                break;
            case ExpressionConstant.DIVIDE:
                if (rightValueFloat.equals(0.0)) {
                    throw new ExpressionException("不能除于0。");
                }
                result = leftValueFloat / rightValueFloat;
                break;
            default:
                return null;
        }
        if (leftValue instanceof Long || rightValue instanceof Long) {
            return result.longValue();
        }
        if (leftValue instanceof Float || rightValue instanceof Float) {
            return result.floatValue();
        }
        if (leftValue instanceof Integer || rightValue instanceof Integer) {
            return result.intValue();
        }
        return result;
    }
}
