package top.kongsheng.common.expression.expressions.values;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.enums.TokenTypeEnum;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.expressions.Expression;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * 布尔表达式
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 10:13
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ValueExpression extends Expression {

    /**
     * 节点类型
     */
    private TokenTypeEnum type;

    /**
     * 处理后有类型的值
     */
    private Object value;

    /**
     * 原始值
     */
    private String raw;

    public ValueExpression(TokenTypeEnum type, Object value, String raw) {
        this.type = type;
        this.value = value;
        this.raw = raw;
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        return getHandleValue(variableValueGetFun);
    }

    public boolean isEmpty() {
        return value == null || "".equals(value) || Objects.equals(value, 0.0D) || Objects.equals(value, 0.0F) || Objects.equals(value, 0);
    }

    public Object getHandleValue(Function<String, Object> variableValueGetFun) throws ExpressionException {
        if (type == TokenTypeEnum.VARIABLE) {
            if (variableValueGetFun == null) {
                throw new ExpressionException("执行失败，未设置变量值获取器。");
            }
            return variableValueGetFun.apply(String.valueOf(this.value));
        }
        return value;
    }
}
