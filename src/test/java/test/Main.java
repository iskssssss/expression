package test;

import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.parser.TokenParser;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/12/13 16:34
 */
public class Main {

    public static void main(String[] args) throws ExpressionException {
        Map<Integer, String> set = new HashMap<>();
        set.put(1, "");
        set.put(2, "");

        Map<String, Object> map = new HashMap<>();
        map.put("set", set);

        String expression = "IF @set?.containsKey(2) THEN " +
                "(IF @set?.contains(1) THEN 1d ELSE 'false' END) " +
                "ELSE " +
                "2 " +
                "END";
        TokenParser parser = new TokenParser(expression);
        Object execResult = parser.parse().exec(map::get);
        System.out.println(execResult);
        // 2024-03-09 18:05:33.192 INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）解析耗时：2ms
        // 2024-03-09 18:05:33.198 INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）构建耗时：1ms
        // 2024-03-09 18:05:33.200 INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）执行耗时：2ms
        // 2024-03-09 18:05:33.201 INFO:  - 执行结果：1.0
        // 1.0
    }

}
