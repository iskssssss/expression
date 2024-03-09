package top.kongsheng.common.expression;

import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.parser.TokenParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析器工具类
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/27 11:18
 */
public class ParserCore {

    /**
     * 表达式解析器缓存
     */
    private static final Map<String, TokenParser> PARSER_CACHE = new ConcurrentHashMap<>();

    /**
     * 解析表达式
     *
     * @param expression 表达式
     * @param cache      是否使用缓存
     * @return 结果
     * @throws ExpressionException 异常
     */
    public static TokenParser parse(String expression, boolean cache) throws ExpressionException {
        TokenParser parser;
        if (!cache || (parser = PARSER_CACHE.get(expression)) == null) {
            parser = new TokenParser(expression);
            parser.parse();
            PARSER_CACHE.put(expression, parser);
        }
        return parser;
    }

    /**
     * 执行表达式
     *
     * @param expression          表达式
     * @param cache               是否使用缓存
     * @param variableValueGetFun 变量值获取方法
     * @return 结果
     * @throws ExpressionException 异常
     */
    public static Object exec(String expression, boolean cache, Function<String, Object> variableValueGetFun) throws ExpressionException {
        TokenParser tokenParser = parse(expression, cache);
        Object execResult = tokenParser.exec(variableValueGetFun);
        return execResult;
    }
}
