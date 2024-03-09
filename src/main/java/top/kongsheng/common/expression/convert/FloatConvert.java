package top.kongsheng.common.expression.convert;

import cn.hutool.core.util.NumberUtil;

/**
 * float转换器
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/8 15:46
 */
public class FloatConvert {

    public static Float convert(Object obj) {
        if (obj instanceof Float) {
            return ((Float) obj);
        }
        if (obj instanceof Integer) {
            return Float.valueOf(((Integer) obj));
        }
        if (obj instanceof Long) {
            return Float.valueOf(((Long) obj));
        }
        if (obj instanceof Double) {
            return Float.valueOf(String.valueOf(obj));
        }
        if (obj instanceof CharSequence) {
            String objStr = String.valueOf(obj);
            if (NumberUtil.isNumber(objStr)) {
                return NumberUtil.parseFloat(objStr);
            }
        }
        return null;
    }
}
