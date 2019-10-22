package fast.cloud.nacos.mybatis.bean.request;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2018-12-10 14:12
 */
public enum MatchType {

    equal,        // filed = value
    //下面四个用于Number类型的比较
    gt,   // filed > value
    ge,   // field >= value
    lt,              // field < value
    le,      // field <= value
    in,      // field <= value
    notIn,
    notEqual,            // field != value
    like,   // field like value
    notLike,    // field not like value
    // 下面四个用于可比较类型(Comparable)的比较
    greaterThan,        // field > value
    greaterThanOrEqualTo,   // field >= value
    lessThan,               // field < value
    lessThanOrEqualTo   // field <= value
}
