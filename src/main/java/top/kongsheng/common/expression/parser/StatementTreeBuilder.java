package top.kongsheng.common.expression.parser;

import top.kongsheng.common.expression.enums.TokenTypeEnum;
import top.kongsheng.common.expression.exceptions.ExpressionException;
import top.kongsheng.common.expression.expressions.ArithmeticExpression;
import top.kongsheng.common.expression.expressions.CellExpression;
import top.kongsheng.common.expression.expressions.Expression;
import top.kongsheng.common.expression.expressions.logicals.AndExpression;
import top.kongsheng.common.expression.expressions.logicals.BoolExpression;
import top.kongsheng.common.expression.expressions.logicals.LogicalExpression;
import top.kongsheng.common.expression.expressions.logicals.OrExpression;
import top.kongsheng.common.expression.expressions.values.ValueExpression;
import top.kongsheng.common.expression.function.Function;
import top.kongsheng.common.expression.logs.LogUtil;
import top.kongsheng.common.expression.statements.IfStatement;
import top.kongsheng.common.expression.statements.Statement;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static top.kongsheng.common.expression.constants.ExpressionConstant.*;

/**
 * 语法树构建
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/25 16:55
 */
public class StatementTreeBuilder extends AbsIterable<Token> {
    /**
     * 表达式字符串
     */
    protected final String expression;
    /**
     * 构建结果
     */
    protected Statement buildResult;
    /**
     * 执行结果
     */
    protected Object executeResult;

    public StatementTreeBuilder(String expression, List<Token> tokenList) {
        super(tokenList.toArray(new Token[]{}));
        this.expression = expression;
    }

    public Statement build() throws ExpressionException {
        long startTime = System.currentTimeMillis();
        this.buildResult = logicalExpressionBuild();
        long endTime = System.currentTimeMillis();
        String finalExpression = super.length > 128 ? this.expression.substring(0, 128) + "..." : this.expression;
        LogUtil.info("表达式（{}）构建耗时：{}ms。", finalExpression, endTime - startTime);
        return this.buildResult;
    }

    public Object exec() throws ExpressionException {
        return exec(null);
    }

    public Object exec(Function<String, Object> variableValueGetFun) throws ExpressionException {
        long startTime = System.currentTimeMillis();
        this.executeResult = this.buildResult.exec(variableValueGetFun);
        long endTime = System.currentTimeMillis();
        String finalExpression = super.length > 128 ? this.expression.substring(0, 128) + "..." : this.expression;
        LogUtil.info("表达式（{}）执行耗时：{}ms。", finalExpression, endTime - startTime);
        LogUtil.info(" - 执行结果：{}。", executeResult);
        return this.executeResult;
    }

    public String getExpression() {
        return expression;
    }

    public Statement getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(Statement buildResult) {
        this.buildResult = buildResult;
    }

    public Object getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Object executeResult) {
        this.executeResult = executeResult;
    }

    /**
     * 逻辑表达式构建
     *
     * @return 结果
     */
    private Statement logicalExpressionBuild() throws ExpressionException {
        Stack<Statement> statementStack = new Stack<>();
        Statement left = this.nextStatement();
        statementStack.push(left);
        Statement statement;
        while ((statement = this.nextStatement()) instanceof LogicalExpression) {
            this.buildTokenNodeTree(statementStack, statement);
            statementStack.push(statement);
            Statement right = this.nextStatement();
            statementStack.push(right);
        }
        this.buildTokenNodeTree(statementStack, null);
        Statement last = statementStack.pop();
        statementStack = null;
        return last;
    }

    private Statement nextStatement() throws ExpressionException {
        Token firstToken = getItem();
        if (firstToken == null) {
            return null;
        }
        TokenTypeEnum firstTokenType = firstToken.getType();
        String firstTokenRaw = firstToken.getRaw();
        int firstTokenRawLength = firstTokenRaw.length();
        Statement item = null;
        switch (firstTokenType) {
            case KEYWORD:
                switch (firstTokenRaw) {
                    case IF:
                        this.index++;
                        item = this.ifStatementBuild();
                        break;
                    case THEN: case ELIF: case ELSE: case END:
                        break;
                    case TRUE: case FALSE:
                        this.index++;
                        item = new ValueExpression(firstTokenType, TRUE.equals(firstTokenRaw), firstTokenRaw);
                        break;
                    default:
                        throw new ExpressionException("无效关键字（" + firstTokenRaw + "）。");
                }
                break;
            case PUNCTUATOR:
                if (RPAREN_STR.equals(firstTokenRaw)) {
                    return null;
                }
                this.index++;
                switch (firstTokenRaw) {
                    case LPAREN_STR:
                        item = this.logicalExpressionBuild();
                        Token lastToken = getItem();
                        if (lastToken == null || !RPAREN_STR.equals(lastToken.getRaw())) {
                            throw new ExpressionException("括号未闭合。");
                        }
                        this.index++;
                        break;
                    case AND:
                        item = new AndExpression();
                        break;
                    case OR:
                        item = new OrExpression();
                        break;
                    case EQ: case EQ1: case NE: case GT:
                    case GE: case LT: case LE:
                        item = new BoolExpression(firstTokenRaw);
                        break;
                    case ADD: case SUBTRACT:
                    case MULTIPLY: case DIVIDE:
                        item = new ArithmeticExpression(firstTokenRaw);
                        break;
                    default:
                        throw new ExpressionException("无效标点符号（" + firstTokenRaw + "）。");
                }
                break;
            case IDENTIFIER:
                throw new ExpressionException("无效标识符（" + firstTokenRaw + "）。");
            case VARIABLE:
                this.index++;
                item = new ValueExpression(firstTokenType, firstTokenRaw.substring(1), firstTokenRaw);
                break;
            case NUMBER:
                this.index++;
                char lastChar = firstTokenRaw.charAt(firstTokenRawLength - 1);
                boolean point = firstToken.isPoint();
                Object handleValue;
                if (lastChar == FLOAT_LAST_CHAR) {
                    handleValue = Float.valueOf(firstTokenRaw);
                } else if (lastChar == DOUBLE_LAST_CHAR || point) {
                    handleValue = Double.valueOf(firstTokenRaw);
                } else {
                    try {
                        handleValue = Integer.valueOf(firstTokenRaw);
                    } catch (NumberFormatException e) {
                        handleValue = Long.valueOf(firstTokenRaw);
                    }
                }
                item = new ValueExpression(firstTokenType, handleValue, firstTokenRaw);
                break;
            case STRING:
                this.index++;
                String value = firstTokenRaw.substring(1, firstTokenRawLength - 1);
                item = new ValueExpression(firstTokenType, value, firstTokenRaw);
                break;
            default:
        }
        if (firstTokenType == TokenTypeEnum.VARIABLE || firstTokenType == TokenTypeEnum.STRING) {
            // 方法调用
            String raw = getItem().getRaw();
            if (".".equals(raw) || "?".equals(raw)) {
                item = this.cellExpressionBuild(item);
            }
        }
        if (item != null) {
            item.setStartPosition(firstToken.getStartPosition());
            item.setEndPosition(firstToken.getEndPosition());
        }
        return item;
    }

    /**
     * 构建if-else
     *
     * @return 结果
     * @throws ExpressionException 异常
     */
    private Statement ifStatementBuild() throws ExpressionException {
        Statement ifStatement = null;
        do {
            Statement logicalExpression = this.logicalExpressionBuild();
            String nextTokenRaw = getItem().getRaw();
            if (THEN.equals(nextTokenRaw)) {
                this.index++;
                ifStatement = new IfStatement();
                ((IfStatement) ifStatement).setTest(logicalExpression);
                continue;
            }
            if (ELIF.equals(nextTokenRaw) || ELSE.equals(nextTokenRaw)) {
                if (ifStatement == null) {
                    throw new ExpressionException("缺少布尔表达式。");
                }
                ((IfStatement) ifStatement).setConsequent(logicalExpression);
                this.index++;
                ((IfStatement) ifStatement).setAlternate(ifStatementBuild());
                break;
            }
            if (END.equals(nextTokenRaw)) {
                this.index++;
                ifStatement = logicalExpression;
                break;
            }
        } while (isNotIndexLast());
        return ifStatement;
    }

    /**
     * 构建调用表达式
     *
     * @param callee 调用方
     * @return 表达式
     * @throws ExpressionException 异常
     */
    private Expression cellExpressionBuild(Statement callee) throws ExpressionException {
        String lastTokenRaw = getItem().getRaw();
        CellExpression cellExpression = new CellExpression();
        if ("?".equals(lastTokenRaw)) {
            this.index++;
            cellExpression.setAllowNull(true);
        }
        cellExpression.setCallee(callee);
        this.index++;
        String method = getItem().getRaw();
        cellExpression.setMethod(method);
        List<Expression> args = new LinkedList<>();
        Stack<String> parenStack = new Stack<>();
        this.index++;
        String argsStartTokenRaw = getItem().getRaw();
        if (!LPAREN_STR.equals(argsStartTokenRaw)) {
            throw new ExpressionException("缺少左括号。");
        }
        parenStack.push(argsStartTokenRaw);
        this.index++;
        while (!parenStack.empty()) {
            String argTokenRaw = getItem().getRaw();
            if (",".equals(argTokenRaw)) {
                this.index++;
                continue;
            }
            if (LPAREN_STR.equals(argTokenRaw)) {
                parenStack.push(argTokenRaw);
                this.index++;
                continue;
            }
            if (RPAREN_STR.equals(argTokenRaw)) {
                parenStack.pop();
                this.index++;
                continue;
            }
            Statement statement = this.nextStatement();
            if (statement instanceof Expression) {
                args.add(((Expression) statement));
                continue;
            }
            throw new ExpressionException("非法的方法参数。");
        }
        cellExpression.setArgs(args.toArray(new Statement[]{}));
        if (this.isIndexLast()) {
            return cellExpression;
        }
        Token token = getItem();
        Expression result = cellExpression;
        String raw = token.getRaw();
        if (".".equals(raw) || "?".equals(raw)) {
            result = this.cellExpressionBuild(cellExpression);
        }
        return result;
    }

    /**
     * 构建解析树
     *
     * @param statementStack 节点树
     */
    private void buildTokenNodeTree(Stack<Statement> statementStack, Statement statement) {
        int size = statementStack.size();
        if ((statement != null && !(statement instanceof LogicalExpression)) || size < BUILD_TOKEN_NUM) {
            return;
        }
        LogicalExpression condition = (LogicalExpression) statement;
        while (size >= BUILD_TOKEN_NUM && (statement == null || (statementStack.get(size - (BUILD_TOKEN_NUM - 1)).getOrder() >= condition.getOrder()))) {
            Statement lastRight = statementStack.pop();
            Statement expression = statementStack.pop();
            Statement lastLeft = statementStack.pop();
            LogicalExpression logicalExpression = ((LogicalExpression) expression);
            logicalExpression.setRight(lastRight);
            logicalExpression.setLeft(lastLeft);
            statementStack.push(logicalExpression);
            size = statementStack.size();
        }
    }
}
