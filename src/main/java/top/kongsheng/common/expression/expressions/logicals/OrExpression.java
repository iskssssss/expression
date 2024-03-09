package top.kongsheng.common.expression.expressions.logicals;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.constants.ExpressionConstant;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.statements.Statement;

/**
 * 或者
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 10:29
 */
public class OrExpression extends LogicalExpression {

    public OrExpression() {
        super(ExpressionConstant.OR);
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Statement left = super.getLeft();
        Object leftValue = left.exec(variableValueGetFun);
        if ((boolean) leftValue) {
            // 1.当条件符为||时先校验左树是否满足，满足则直接返回无需遍历右子树。
            return leftValue;
        }
        Statement right = super.getRight();
        Object rightValue = right.exec(variableValueGetFun);
        return ((boolean) leftValue) || ((boolean) rightValue);
    }
}
