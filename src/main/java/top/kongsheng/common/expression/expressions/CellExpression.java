package top.kongsheng.common.expression.expressions;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import top.kongsheng.common.expression.ExpCore;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.statements.Statement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 调用表达式
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 10:39
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class CellExpression extends Expression {

    /**
     * 调用方法
     */
    private String method;

    /**
     * 调用方
     */
    private Statement callee;

    /**
     * 调用参数
     */
    private Statement[] args;

    /**
     * 是否允许调用方为空值
     */
    private boolean allowNull = false;

    public CellExpression() {
        this.setOrder(9);
    }

    @Override
    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        Object execResult = this.callee == null ? null : this.callee.exec(variableValueGetFun);
        boolean execResultNull = execResult == null;
        if (execResultNull) {
            if (this.allowNull) {
                if (BOOLEAN_METHOD_SET.contains(this.method)) {
                    return "isEmpty".equals(this.method);
                }
                return null;
            }
            throw new ExpressionException("方法" + this.method + "执行失败，" + "调用者为null。");
        }
        try {
            Object[] values = null;
            if (this.args != null) {
                int length = this.args.length;
                values = new Object[length];
                for (int i = 0; i < length; i++) {
                    Statement arg = args[i];
                    values[i] = arg.exec(variableValueGetFun);
                }
            }
            switch (this.method) {
                case "isEmpty":
                    checkArgs(values, 0);
                    return ObjectUtil.isEmpty(execResult);
                case "isNotEmpty":
                    checkArgs(values, 0);
                    return ObjectUtil.isNotEmpty(execResult);
                case "toString":
                    checkArgs(values, 0);
                    return toString(execResult);
                case "replace":
                    checkArgs(values, 2);
                    Object value1 = values[0], value2 = values[1];
                    return toString(execResult).replace(toString(value1), toString(value2));
                case "format":
                    checkArgs(values, 1);
                    String formatStr = toString(values[0]);
                    if (execResult instanceof LocalDateTime) {
                        return LocalDateTimeUtil.format(((LocalDateTime) execResult), formatStr);
                    } else if (execResult instanceof Date) {
                        return DateUtil.format(((Date) execResult), formatStr);
                    }
                    throw new ExpressionException("format方法执行失败，不支持的类型（" + execResult.getClass() + "）。");
                case "contains":
                    checkArgs(values, 1);
                    Object firstValue = values[0];
                    if (execResult instanceof Collection) {
                        return ((Collection<?>) execResult).contains(firstValue);
                    } else if (execResult instanceof Object[]) {
                        Object[] objects = (Object[]) execResult;
                        for (Object object : objects) {
                            if (ObjectUtil.equal(object, firstValue)) {
                                return true;
                            }
                        }
                        return false;
                    } else if (execResult instanceof Map) {
                        Map<?, ?> execResult1 = (Map<?, ?>) execResult;
                        return execResult1.containsKey(firstValue);
                    } else if (execResult instanceof CharSequence) {
                        CharSequence execResultStr = (CharSequence) execResult;
                        return StrUtil.contains(execResultStr, toString(values[0]));
                    }
                    throw new ExpressionException("contains方法执行失败，暂时只支持数组、字典、字符串。");
                default:
            }
            Class<?> leftValueClass = execResult.getClass();
            return ExpCore.CLASS_METHOD_CACHE.execute(leftValueClass, this.method, execResult, values);
        } catch (ExpressionException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            throw new ExpressionException("无可用的方法(" + this.method + ")。", e);
        } catch (Exception e) {
            throw new ExpressionException("方法执行失败。", e);
        }
    }

    private void checkArgs(Object[] values, int len) throws ExpressionException {
        if ((len != 0 && values == null) || values.length < len) {
            throw new ExpressionException("方法执行失败，方法参数缺少（需要" + len + "个参数）。");
        }
        if (values.length > len) {
            throw new ExpressionException("方法执行失败，方法参数过多（只需要" + len + "个参数）。");
        }
    }

    public void setArgs(Statement[] args) {
        this.args = args;
    }

    private static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    private static final Set<String> BOOLEAN_METHOD_SET = new HashSet<>();
    static {
        BOOLEAN_METHOD_SET.add("isEmpty");
        BOOLEAN_METHOD_SET.add("isNotEmpty");
        BOOLEAN_METHOD_SET.add("contains");
    }
}
