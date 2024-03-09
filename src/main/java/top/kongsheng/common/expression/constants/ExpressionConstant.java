package top.kongsheng.common.expression.constants;

import java.util.*;

/**
 * 表达式常量类
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/21 16:42
 */
public final class ExpressionConstant {

    public static final int BUILD_TOKEN_NUM = 3;
    public static final char POINT = '.';

    public static final String AND = "&&";
    public static final String OR = "||";

    public static final String EQ = "==";
    public static final String EQ1 = "===";
    public static final String NE = "<>";
    public static final String GT = ">";
    public static final String GE = ">=";
    public static final String LT = "<";
    public static final String LE = "<=";

    public static final String IS_EMPTY = "is empty";
    public static final String IS_NOT_EMPTY = "is not empty";
    public static final String IS_NULL = "is null";
    public static final String IS_NOT_NULL = "is not null";

    public static final String IF = "IF";
    public static final String THEN = "THEN";
    public static final String ELIF = "ELIF";
    public static final String ELSE = "ELSE";
    public static final String END = "END";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final char FLOAT_LAST_CHAR = 'f';
    public static final char DOUBLE_LAST_CHAR = 'd';

    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";

    public static final char ADD_CHAR = '+';
    public static final char SUBTRACT_CHAR = '-';
    public static final char MULTIPLY_CHAR = '*';
    public static final char DIVIDE_CHAR = '/';

    public static final char VARIABLE = '@';
    public static final String VARIABLE_STR = "@";
    public static final char LPAREN = '(';
    public static final String LPAREN_STR = "(";
    public static final char RPAREN = ')';
    public static final String RPAREN_STR = ")";
    public static final char SINGLE_QUOTE = '\'';
    public static final String SINGLE_QUOTE_STR = "'";
    public static final char DOUBLE_QUOTE = '"';
    public static final String DOUBLE_QUOTE_STR = "\"";


    /**
     * 是否是字母
     *
     * @param c 字符
     * @return 是否是字母
     */
    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 是否是数字
     *
     * @param c 字符
     * @return 是否是数字
     */
    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }


    public static final Set<String> KEYWORD_SET = new HashSet<String>() {{
        add(IF);
        add(THEN);
        add(ELIF);
        add(ELSE);
        add(END);
        add(TRUE);
        add(FALSE);
    }};

    public static final Set<Character> SPACE_SET = new LinkedHashSet<Character>() {{
        add(' ');
        add('\t');
        add('\r');
        add('\n');
    }};

    public static final Map<String, Integer> CONDITION_ORDER_MAP = new LinkedHashMap<String, Integer>() {{
        put(ExpressionConstant.AND, 1);
        put(ExpressionConstant.OR, 1);

        put(ExpressionConstant.EQ, 3);
        put(ExpressionConstant.EQ1, 3);
        put(ExpressionConstant.NE, 3);
        put(ExpressionConstant.GE, 3);
        put(ExpressionConstant.GT, 3);
        put(ExpressionConstant.LT, 3);
        put(ExpressionConstant.LE, 3);

        put(ExpressionConstant.IS_EMPTY, 3);
        put(ExpressionConstant.IS_NOT_EMPTY, 3);
        put(ExpressionConstant.IS_NULL, 3);
        put(ExpressionConstant.IS_NOT_NULL, 3);

        put(ExpressionConstant.ADD, 5);
        put(ExpressionConstant.SUBTRACT, 5);

        put(ExpressionConstant.MULTIPLY, 7);
        put(ExpressionConstant.DIVIDE, 7);
    }};

    public static final Set<String> CONDITION_IS_KIND_SET = new LinkedHashSet<String>() {{
        add(ExpressionConstant.IS_EMPTY);
        add(ExpressionConstant.IS_NOT_EMPTY);
        add(ExpressionConstant.IS_NULL);
        add(ExpressionConstant.IS_NOT_NULL);
    }};
}
