package top.kongsheng.common.expression.expressions.logicals;

import top.kongsheng.common.expression.expressions.Expression;
import top.kongsheng.common.expression.statements.Statement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static top.kongsheng.common.expression.constants.ExpressionConstant.CONDITION_ORDER_MAP;

/**
 * 逻辑表达式
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 10:34
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class LogicalExpression extends Expression {

    /**
     * 操作符
     */
    protected String operator;

    /**
     * 左节点
     */
    protected Statement left;

    /**
     * 右节点
     */
    protected Statement right;

    public LogicalExpression(String operator) {
        this.operator = operator;
        this.setOrder(CONDITION_ORDER_MAP.getOrDefault(operator, 9));
    }

    /*private Object exec(Expression expression) throws FullException {
        if (expression == null) {
            return null;
        }
        if (expression instanceof ValueExpression) {
            ValueExpression valueExpression = (ValueExpression) expression;
            Function<String, Object> variableValueGetFun = this.getVariableValueGetFun();
            return valueExpression.getHandleValue(variableValueGetFun);
        }
        LogicalExpression logicalExpression = (LogicalExpression) expression;
        Expression left = logicalExpression.getLeft();
        String leftFullName = left.getFullName();
        Object leftValue = this.exec(left);
        Expression right = logicalExpression.getRight();
        if (logicalExpression.isIsKind() || right == null) {
            return logicalExpression.exec(leftFullName, leftValue, null, null);
        }
        String conditionTokenNodeSourceValue = logicalExpression.getOperator();
        boolean leftValueTrue = (leftValue instanceof Boolean && ((Boolean) leftValue));
        String rightSourceValue = right.getRaw();
        boolean isOr = ExpressionConstant.OR.equals(conditionTokenNodeSourceValue);
        boolean isAnd = ExpressionConstant.AND.equals(conditionTokenNodeSourceValue);
        if ((isOr && leftValueTrue) || (isAnd && !leftValueTrue && !ExpressionConstant.OR.equals(rightSourceValue))) {
            // 1.当条件符为||时先校验左树是否满足，满足则直接返回无需遍历右子树。
            // 2.当左树的结果为false，右子树的操作符不为||时无需遍历右子树直接返回。
            return leftValue;
        }
        Object rightValue = this.exec(right);
        Object execResult = logicalExpression.exec(leftFullName, leftValue, right.getFullName(), rightValue);
        return execResult;
    }*/
}
