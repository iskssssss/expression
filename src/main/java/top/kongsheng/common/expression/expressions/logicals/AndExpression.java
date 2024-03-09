package top.kongsheng.common.expression.expressions.logicals;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.constants.ExpressionConstant;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.statements.Statement;

/**
 * 并且
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 10:29
 */
public class AndExpression extends LogicalExpression {

    public AndExpression() {
        super(ExpressionConstant.AND);
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Statement left = super.getLeft();
        Statement right = super.getRight();
        Object leftValue = left.exec(variableValueGetFun);
        boolean leftValueTrue = (leftValue instanceof Boolean && ((Boolean) leftValue));
        if (!leftValueTrue && !(right instanceof OrExpression)) {
            // 1.当左树的结果为false，右子树的操作符不为||时无需遍历右子树直接返回。
            return leftValue;
        }
        Object rightValue = right.exec(variableValueGetFun);
        return leftValueTrue && ((boolean) rightValue);
    }
}
