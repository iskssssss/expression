package top.kongsheng.common.expression.parser;

import java.io.Closeable;
import java.util.Collection;

/**
 * AbsIterable
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2024/1/12 15:24
 */
public abstract class AbsIterable<TYPE> implements Closeable {
    /**
     * 当前下标
     */
    protected int index;
    /**
     * 数组
     */
    protected TYPE[] arrays;
    /**
     * 长度
     */
    protected int length;

    public AbsIterable(TYPE[] arrays) {
        this.index = 0;
        this.arrays = arrays;
        this.length = arrays.length;
    }

    @Override
    public void close() {
        this.index = 0;
        this.arrays = null;
        this.length = 0;
    }

    /**
     * 获取当前项
     *
     * @return 当前项
     */
    protected TYPE getItem() {
        return getItem(this.index);
    }

    /**
     * 获取当前项
     *
     * @param index 下标
     * @return 当前项
     */
    protected TYPE getItem(int index) {
        if (index >= this.length) {
            return null;
        }
        return this.arrays[index];
    }

    /**
     * 获取下一项
     *
     * @return 下一项
     */
    protected TYPE nextItem() {
        this.index++;
        return getItem();
    }

    /**
     * 跳过指定数据
     */
    protected void pass(Collection<TYPE> list) {
        while (list.contains(this.getItem())) {
            this.index++;
        }
    }

    /**
     * 是否是最后下标
     *
     * @return 是否是最后下标
     */
    public boolean isIndexLast() {
        return this.index == this.length;
    }

    /**
     * 是否不是最后下标
     *
     * @return 是否不是最后下标
     */
    public boolean isNotIndexLast() {
        return !isIndexLast();
    }

    protected static Character[] toCharArray(String expression) {
        if (expression == null || "".equals(expression)) {
            throw new IllegalArgumentException("表达式不可为空。");
        }
        int length = expression.length();
        Character[] characters = new Character[length];
        char[] chars = expression.toCharArray();
        for (int i = 0; i < length; i++) {
            characters[i] = chars[i];
        }
        return characters;
    }
}
