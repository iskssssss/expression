package top.kongsheng.common.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表达式核心
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/22 10:50
 */
public class ExpCore {

    public static final ClassMethodMap CLASS_METHOD_CACHE = new ClassMethodMap();

    public static class ClassMethodMap extends ConcurrentHashMap<Class<?>, Map<String, Map<Integer, List<Method>>>> {

        final java.util.function.Function<? super Class<?>, ? extends Map<String, Map<Integer, List<Method>>>> mappingFunction = aClass -> {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            Map<String, Map<Integer, List<Method>>> map = new HashMap<>(declaredMethods.length + 1);
            for (Method declaredMethod : declaredMethods) {
                int parameterCount = declaredMethod.getParameterCount();
                Map<Integer, List<Method>> listMap = map.computeIfAbsent(declaredMethod.getName(), key -> new HashMap<>());
                listMap.computeIfAbsent(parameterCount, key -> new LinkedList<>()).add(declaredMethod);
            }
            return map;
        };
        public List<Method> list(Class<?> valueClass, String methodName, int paramsLength) throws NoSuchMethodException {
            Map<String, Map<Integer, List<Method>>> map = this.computeIfAbsent(valueClass, mappingFunction);
            if (map.isEmpty()) {
                throw new NoSuchMethodException();
            }
            Map<Integer, List<Method>> listMap = map.get(methodName);
            if (listMap == null || listMap.isEmpty()) {
                throw new NoSuchMethodException();
            }
            List<Method> methodList = listMap.get(paramsLength);
            if (methodList == null || methodList.isEmpty()) {
                throw new NoSuchMethodException();
            }
            return methodList;
        }

        public Method find(Class<?> valueClass, String methodName) throws NoSuchMethodException {
            List<Method> methodList = this.list(valueClass, methodName, 0);
            return methodList.iterator().next();
        }

        public Object execute(Class<?> valueClass, String methodName, Object source, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            int paramsLength = args != null ? args.length : 0;
            if (paramsLength == 0) {
                Method method = this.find(valueClass, methodName);
                Object result = execute(method, source, args);
                return result;
            }
            List<Method> methodList = this.list(valueClass, methodName, paramsLength);
            for (Method method : methodList) {
                try {
                    Object result = execute(method, source, args);
                    return result;
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
                }
            }
            throw new NoSuchMethodException();
        }

        public Object execute(Method method, Object source, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
            method.setAccessible(true);
            Object[] finalArgs = null;
            if (args != null && args.length > 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                this.checkArgs(args, parameterTypes);
                finalArgs = args;
                //finalArgs = this.toArgs(parameterTypes, args);
            }
            Object invoke = method.invoke(source, finalArgs);
            return invoke;
        }

        private void checkArgs(Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException {
            int argsLength = args.length;
            if (argsLength != parameterTypes.length) {
                throw new NoSuchMethodException();
            }
            for (int i = 0; i < argsLength; i++) {
                Object arg = args[i];
                Class<?> parameterType = parameterTypes[i];
                if ("java.lang.Object".equals(parameterType.getName())) {
                    continue;
                }
                if (arg.getClass() != parameterType) {
                    throw new NoSuchMethodException();
                }
            }
        }

        @Override
        public Map<String, Map<Integer, List<Method>>> computeIfAbsent(Class<?> key, java.util.function.Function<? super Class<?>, ? extends Map<String, Map<Integer, List<Method>>>> mappingFunction) {
            if (mappingFunction == null) {
                mappingFunction = this.mappingFunction;
            }
            return super.computeIfAbsent(key, mappingFunction);
        }

        private Object[] toArgs(Class<?>[] parameterTypes, Object[] args) {
            int parameterTypesLength = args.length;
            Object[] finalArgs = new Object[parameterTypesLength];
            for (int i = 0; i < parameterTypesLength; i++) {
                Class<?> parameterType = parameterTypes[i];
                Object v = args[i];
                if (parameterType.isPrimitive()) {
                    // 基础类型
                    parameterType = METHOD_VALUE_OF_CACHE.get(parameterType);
                }
                try {
                    Method valueOfMethod = parameterType.getDeclaredMethod("valueOf", String.class);
                    v = valueOfMethod.invoke(null, String.valueOf(v));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                }
                finalArgs[i] = v;
            }
            return finalArgs;
        }
    }

    protected static final Map<Class<?>, Class<?>> METHOD_VALUE_OF_CACHE = new ConcurrentHashMap<Class<?>, Class<?>>() {{
        put(byte.class, Byte.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(char.class, Character.class);
        put(boolean.class, Boolean.class);
    }};
}
