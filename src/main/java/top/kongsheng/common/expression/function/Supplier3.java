package top.kongsheng.common.expression.function;

import top.kongsheng.common.expression.exceptions.ExpressionException;

/**
 * 3参数Supplier
 */
@FunctionalInterface
public interface Supplier3<T, P1, P2, P3> {

	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 * @return 目标对象
	 * @throws ExpressionException 表达式异常
	 */
	T get(P1 p1, P2 p2, P3 p3) throws ExpressionException;
}
