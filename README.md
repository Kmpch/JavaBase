# JavaBase
Basic Java Learning!
## Java：集合
###Set接口
 存入Set的每个元素都必须是唯一的，Set接口不保证维护元素的次序；

- HashSet类：为快速查找设计的Set,存入HashSet的对象必须定义hashCode(),它不保证集合的迭代顺序
- LinkedHashSet类：具有HashSet的查询速度，且内部使用链表维护元素的顺序（仅限插入的顺序）

###List接口
List按对象进入的顺序保存对象，不做排序等操作

- ArrayList类： 由数组实现的List,允许对元素进行快速随机访问，但是向List中间插入与移除元素的速度很慢
- LinkedList类：对顺序访问进行了优化，向List中间插入与删除的开销并不大，随机访问则相对较慢。



