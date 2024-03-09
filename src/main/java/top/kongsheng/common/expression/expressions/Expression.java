package top.kongsheng.common.expression.expressions;

import top.kongsheng.common.expression.statements.Statement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 表达式
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 10:13
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class Expression extends Statement {

    public String getFullPath() {
        return null;
    }
}
