package com.collect.javase.func;

/**
 * 描述: Function工具类
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:38
 **/
public class FuncUtils {

    /**
     * 抛出异常
     * <p> 如果 b == true 则抛出异常
     * <p> 下面注释掉的方法跟这个等价，目的是方便不太熟悉lambda的同学对照
     *
     * @param b 是否要抛出异常
     * @return 函数式接口
     */
    public static ThrowExceptionFunction isThrow(boolean b) {
        return (code, message) -> {
            if (b) {
                throw new RuntimeException(message);
            }
        };
    }

    /**
     * 分支操作
     * <p> condition 用于确定执行哪个方法
     * <p> 下面注释掉的方法跟这个等价，目的是方便不太熟悉lambda的同学对照
     *
     * @param condition 条件
     * @return 函数式接口
     */
    public static BranchHandle condition(boolean condition) {
        return (trueHandle, falseHandle) -> {
            if (condition) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    /**
     * obj 不为空则消费，为空则执行其他操作
     *
     * @param obj 消费对象
     * @return 函数式接口
     **/
    public static PresentOrElseHandler<?> isNotNull(Object obj) {
        return (consumer, runnable) -> {
            if (obj == null) {
                runnable.run();
            } else {
                consumer.accept(obj);
            }
        };
    }

    public static <T, R> BranchValueHandle<T, R> condition(boolean condition, T t) {
        return (trueHandle, falseHandle) -> condition ? trueHandle.apply(t) : falseHandle.apply(t);
    }

//    public static ThrowExceptionFunction isThrow(boolean b) {
//        return new ThrowExceptionFunction() {
//            @Override
//            public void throwMessage(int code, String message) {
//                if (b) {
//                    throw new RuntimeException(message);
//                }
//            }
//        };
//    }

//    public static BranchHandle condition(boolean b) {
//        return new BranchHandle() {
//            @Override
//            public void branch(Runnable trueHandle, Runnable falseHandle) {
//                if (b) {
//                    trueHandle.run();
//                } else {
//                    falseHandle.run();
//                }
//            }
//        };
//    }

    private FuncUtils() {
        // private
    }

}
