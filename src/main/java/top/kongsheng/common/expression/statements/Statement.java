package top.kongsheng.common.expression.statements;

import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * abs
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 10:15
 */
@Data
@ToString
public abstract class Statement implements Serializable {
    static final long serialVersionUID = 42L;

    /**
     * 优先级
     */
    private int order = 0;

    /**
     * 开始位置
     */
    private int startPosition;

    /**
     * 结束位置
     */
    private int endPosition;

    /**
     * 执行
     *
     * @param variableValueGetFun 变量值获取器
     * @return 结果
     * @throws ExpressionException 异常
     */
    public abstract Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException;

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
