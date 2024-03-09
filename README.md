## expression

为支持“easy-word-datainput”能够实现复杂的导出所设计并实现的表达式解析器，支持解析复杂的自定义表达式。

### 语法说明

####   变量

变量以@为开头。

例：

变量表：{ 

age : 2

} 

@age 的值就是 2

####   运算符

#####     关系运算符

| 符号 | 说明                                                     | 例子                                                    |
| ---- | -------------------------------------------------------- | ------------------------------------------------------- |
| ==   | 当左边的值等于右边的值时为true。                         | 1 == 1  true                                            |
| ===  | 当左边的值等于右边的值时为true（会尝试转换成相同类型）。 | 1 === 1  true<br />1 === "1"  true<br />1 === 1.0  true |
| <>   | 当左边的值不等于右边的值时为true。                       | 1 <> 1 false                                            |
| >    | 当左边的值大于右边的值时为true。                         | 1 > 1 false                                             |
| >=   | 当左边的值大于等于右边的值时为true。                     | 1 >= 1 true                                             |
| <    | 当左边的值小于右边的值时为true。                         | 1 < 1 false                                             |
| <=   | 当左边的值小于等于右边的值时为true。                     | 1 <= 1 true                                             |

#####     算术运算符 

| 符号 | 说明     | 例子  |
| ---- | -------- | ----- |
| +    | 加法运算 | 1+1 2 |
| -    | 减法运算 | 1-1 0 |
| *    | 乘法运算 | 1*1 1 |
| /    | 除法运算 | 1/1 1 |

####   支持方法

#####     replace(target, replacement) 

用于替换字符串中的字符。

例1：

"1,2,3,4".replace(',', '、')

例2：

变量表：{ 

str : '1,2,3,4'

} 

@str.replace(',', '、')

####   关键字

#####     布尔判断

is empty、is not empty、is null、is not null

|    关键字    |             说明              |
| :----------: | :---------------------------: |
|   is empty   |  用于判断变量/值是否为空值。  |
| is not empty | 用于判断变量/值是否不为空值。 |
|   is null    |  用于判断变量/值是否为NULL。  |
| is not null  | 用于判断变量/值是否不为NULL。 |

####   条件语句

#####     if…end

```
if 布尔表达式 then 
	布尔表达式为true时执行的语句 
end
```

#####     if…else…end

```
if 布尔表达式 then
	布尔表达式为true时执行的语句
else
	布尔表达式为false时执行的语句
end
```

#####     if…elif…else…end

```
if 布尔表达式1 then
	布尔表达式1为true时执行的语句
elif 布尔表达式2 then
	布尔表达式2为true时执行的语句
else
	当上面的布尔表达式都为false时执行的语句
end
```

##### 示例

```java
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

// 结果
// INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）解析耗时：2ms
// INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）构建耗时：1ms
// INFO: 表达式（IF @set?.containsKey(2) THEN (IF @set?.contains(1) THEN 1d ELSE 'false' END) ELSE 2 END）执行耗时：2ms
// INFO:  - 执行结果：1.0
// 1.0
```

