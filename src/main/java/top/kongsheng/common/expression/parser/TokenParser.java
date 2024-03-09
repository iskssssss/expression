package top.kongsheng.common.expression.parser;

import top.kongsheng.common.expression.constants.ExpressionConstant;
import top.kongsheng.common.expression.enums.TokenTypeEnum;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.function.Function;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static top.kongsheng.common.expression.constants.ExpressionConstant.*;

/**
 * token解析器
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 15:52
 */
public class TokenParser extends AbsParser<TokenParser, List<Token>> {

    /**
     * 语法树构建器
     */
    private StatementTreeBuilder statementTreeBuilder = null;

    public TokenParser(String expression) {
        super(expression);
    }

    @Override
    protected List<Token> _parse() throws ExpressionException {
        List<Token> tokenNodeStack = new LinkedList<>();
        Token tokenNode;
        while (this.isNotIndexLast() && (tokenNode = nextToken()) != null) {
            tokenNodeStack.add(tokenNode);
        }
        return tokenNodeStack;
    }

    public Object exec() throws ExpressionException {
        return exec(null);
    }

    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        if (statementTreeBuilder == null) {
            synchronized (this) {
                if (statementTreeBuilder == null) {
                    statementTreeBuilder = new StatementTreeBuilder(super.expression, super.result);
                    statementTreeBuilder.build();
                }
            }
        }
        return statementTreeBuilder.exec(variableValueGetFun);
    }

    /**
     * 获取下一token
     *
     * @return token
     */
    private Token nextToken() throws ExpressionException {
        this.pass(SPACE_SET);
        Character firstChar = this.getItem(), curChar = null;
        int startPosition = this.index;
        Token token = null;
        // 变量：@(字母|_)(_|字母|数字)*
        if (firstChar == VARIABLE) {
            int startIndex = this.index++;
            StringBuilder source = new StringBuilder();
            for (; this.isNotIndexLast() && ((curChar = this.getItem()) == '_' || isLetter(curChar) || isNumber(curChar)); this.index++) {
                source.append(curChar);
            }
            if (startIndex == this.index) {
                throw new ExpressionException("变量格式无法解析");
            }
            String valueStr = source.toString();
            token = new Token(TokenTypeEnum.VARIABLE, VARIABLE + valueStr);
        }
        // 标识串/关键字
        else if (firstChar == '_' || isLetter(firstChar)) {
            StringBuilder source = new StringBuilder();
            for (; this.isNotIndexLast() && ((curChar = this.getItem()) == '_' || isLetter(curChar) || isNumber(curChar)); this.index++) {
                source.append(curChar);
            }
            String valueStr = source.toString();
            TokenTypeEnum typeEnum = TokenTypeEnum.IDENTIFIER;
            if (KEYWORD_SET.contains(valueStr)) {
                typeEnum = TokenTypeEnum.KEYWORD;
            }
            token = new Token(typeEnum, valueStr);
        }
        // 数字：(0-9)* || firstChar == '-'
        else if (isNumber(firstChar)) {
            curChar = this.nextItem();
            boolean point = false, _point;
            StringBuilder source = new StringBuilder();
            source.append(firstChar);
            while (this.isNotIndexLast() && ((_point = (curChar == POINT)) || isNumber(curChar) || curChar == DOUBLE_LAST_CHAR || curChar == FLOAT_LAST_CHAR)) {
                source.append(curChar);
                if (_point) {
                    point = true;
                }
                curChar = this.nextItem();
            }
            String valueStr = source.toString();
            token = new Token(TokenTypeEnum.NUMBER, valueStr);
            token.setPoint(point);
        }
        // 字符串 ('|")(w)*('|")
        else if (firstChar == SINGLE_QUOTE || firstChar == DOUBLE_QUOTE) {
            int startIndex = this.index++;
            StringBuilder source = new StringBuilder();
            for (; this.isNotIndexLast() && !Objects.equals((curChar = this.getItem()), firstChar); this.index++) {
                source.append(curChar);
            }
            if (startIndex == this.index) {
                throw new ExpressionException("字符串格式无法解析");
            }
            if (!Objects.equals(firstChar, curChar)) {
                throw new ExpressionException("字符串未闭合。");
            }
            this.index++;
            String valueStr = source.toString();
            token = new Token(TokenTypeEnum.STRING, firstChar + valueStr + firstChar);
        }
        // 括号 ((w)*)
        else if (firstChar == LPAREN || firstChar == RPAREN || firstChar == '?' || firstChar == POINT || firstChar == ',') {
            this.index++;
            String raw = firstChar + "";
            token = new Token(TokenTypeEnum.PUNCTUATOR, raw);
        }
        // + - * /
        else if (firstChar == ADD_CHAR || firstChar == SUBTRACT_CHAR || firstChar == MULTIPLY_CHAR || firstChar == DIVIDE_CHAR) {
            this.index++;
            token = new Token(TokenTypeEnum.PUNCTUATOR, firstChar + "");
        }
        // &&
        else if (firstChar == '&') {
            if (this.nextItem() == '&') {
                this.index++;
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.AND);
            }
        }
        // ||
        else if (firstChar == '|') {
            if (this.nextItem() == '|') {
                this.index++;
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.OR);
            }
        }
        // ==
        else if (firstChar == '=') {
            if (this.nextItem() == '=') {
                if (this.nextItem() == '=') {
                    this.index++;
                    token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.EQ1);
                } else {
                    token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.EQ);
                }
            }
        }
        // > >=
        else if (firstChar == '>') {
            if (this.nextItem() == '=') {
                this.index++;
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.GE);
            } else {
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.GT);
            }
        }
        // < <= <>
        else if (firstChar == '<') {
            char nextChar = this.nextItem();
            if (nextChar == '=') {
                this.index++;
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.LE);
            } else if (nextChar == '>') {
                this.index++;
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.NE);
            } else {
                token = new Token(TokenTypeEnum.PUNCTUATOR, ExpressionConstant.LT);
            }
        }
        if (token != null) {
            token.setStartPosition(startPosition);
            token.setEndPosition(this.index - 1);
        }
        return token;
    }
}
