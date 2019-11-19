###一、简介

Spring3中引入了Spring表达式语言—SpringEL,SpEL是一种强大,简洁的装配Bean的方式,他可以通过运行期间执行的表达式将值装配到我们的属性或构造函数当中,更可以调用JDK中提供的静态常量,获取外部Properties文件中的的配置
###二、用法
1、文本表达式

文本表达式支持: 字符串(需要用单引号声明)、日期、数字、布尔类型及null,对数字支持负数、指数及小数, 默认情况下实数使用Double.parseDouble()进行表达式类型转换

parser.parseExpression("'hello'").getValue(String.class); // hello , 注意单引号
parser.parseExpression("1.024E+3").getValue(Long.class);  // 1024  , 指数形式
parser.parseExpression("0xFFFF").getValue(Integer.class); // 65535 , 十六进制
parser.parseExpression("true").getValue(Boolean.class);   // true
parser.parseExpression("null").getValue();    

2、变量
复制代码

// 定义变量
String name = "Tom";
EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
context.setVariable("myName", name);                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
ExpressionParser parser = new SpelExpressionParser();
// 访问变量
parser.parseExpression("#myName").getValue(context, String.class);   // Tom , 使用变量
// 直接使用构造方法创建对象
parser.parseExpression("new String('aaa')").getValue(String.class);   // aaa

复制代码
3、属性和方法调用

    属性可直接使用属性名,属性名首字母大小写均可(只有首字母可不区分大小写);
    数组、列表可直接通过下表形式(list[index])访问;
    map可以直接把key当成索引来访问(map[key]);
    方法可以直接访问;

复制代码

// 准备工作
Person person = new Person("Tom", 18); // 一个普通的POJO
List<String> list = Lists.newArrayList("a", "b");
Map<String, String> map = Maps.newHashMap();
map.put("A", "1");
map.put("B", "2");
EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
context.setVariable("person", person);                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
context.setVariable("map", map);
context.setVariable("list", list);
ExpressionParser parser = new SpelExpressionParser();
// 属性
parser.parseExpression("#person.name").getValue(context, String.class);       // Tom , 属性访问
parser.parseExpression("#person.Name").getValue(context, String.class);       // Tom , 属性访问, 但是首字母大写了
// 列表
parser.parseExpression("#list[0]").getValue(context, String.class)           // a , 下标
// map
parser.parseExpression("#map[A]").getValue(context, String.class);           // 1 , key
// 方法
parser.parseExpression("#person.getAge()").getValue(context, Integer.class); // 18 , 方法访问

复制代码
4、类型

T操作符可以获取类型, 可以调用对象的静态方法

// 获取类型
parser.parseExpression("T(java.util.Date)").getValue(Class.class); // class java.util.Date
// 访问静态成员(方法或属性)
parser.parseExpression("T(Math).abs(-1)").getValue(Integer.class); // 1
// 判断类型
parser.parseExpression("'asdf' instanceof T(String)").getValue(Boolean.class); // true;

5、操作符

Spring EL 支持大多数的数学操作符、逻辑操作符、关系操作符.

    关系操作符, 包括: eq(==), ne(!=), lt()<, le(<=), gt(>), ge(>=)
    逻辑运算符, 包括: and(&&), or(||), not(!)
    数学操作符, 包括: 加(+), 减(-), 乘(*), 除(/), 取模(%), 幂指数(^)
    其他操作符, 如: 三元操作符, instanceof, 赋值(=), 正则匹配

另外三元操作符有个特殊的用法, 一般用于赋默认值, 比如: parseExpression("#name?:'defaultName'"), 如果变量name为空时设置默认值.
复制代码

parser.parseExpression("1 > -1").getValue(Boolean.class);         // true
parser.parseExpression("1 gt -1").getValue(Boolean.class);        // true
parser.parseExpression("true or true").getValue(Boolean.class);   // true
parser.parseExpression("true || true").getValue(Boolean.class);   // true
parser.parseExpression("2 ^ 3").getValue(Integer.class);          // 8
parser.parseExpression("true ? true : false").getValue(Boolean.class); // true
parser.parseExpression("#name ?: 'default'").getValue(context, String.class); // default
parser.parseExpression("1 instanceof T(Integer)").getValue(Boolean.class); // true
parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class); // true
parser.parseExpression("#person.name").getValue(context, String.class);  // Tom , 原来的值
parser.parseExpression("#person.name = 'Jim'").getValue(context, String.class); // Jim , 赋值之后
parser.parseExpression("#person.name").getValue(context, String.class);  // Jim, 赋值起了作用

复制代码
6、避免空指针

当访问一个对象的属性或方法时, 若该对象为null, 就会出现空指针异常. 安全导航会判断对象是否为null,如果是的话, 就返回null而不是抛出空指针异常. 使用方式就是在对象后面加个?. 如下:

// 使用这种表达式可以避免抛出空指针异常
parser.parseExpression("#name?.toUpperCase()").getValue(context, String.class); // null

7、#this变量

有个特殊的变量#this来表示当前的对象. 常用于集合的过滤

// this 使用示例
parser.parseExpression("{1, 3, 5, 7}.?[#this > 3]").getValue(); // [5, 7]

8、集合选择

可以使用选择表达式对集合进行过滤或一些操作，从而生成一个新的符合选择条件的集合, 有如下一些形式:

    ?[expression]: 选择符合条件的元素
    ^[expression]: 选择符合条件的第一个元素
    $[expression]: 选择符合条件的最后一个元素
    ![expression]: 可对集合中的元素挨个进行处理

对于集合可以配合#this变量进行过滤, 对于map, 可分别对keySet及valueSet分别使用key和value关键字;
复制代码

// 集合
parser.parseExpression("{1, 3, 5, 7}.?[#this > 3]").getValue(); // [5, 7] , 选择元素
parser.parseExpression("{1, 3, 5, 7}.^[#this > 3]").getValue(); // 5 , 第一个
parser.parseExpression("{1, 3, 5, 7}.$[#this > 3]").getValue(); // 7 , 最后一个
parser.parseExpression("{1, 3, 5, 7}.![#this + 1]").getValue(); // [2, 4, 6, 8] ,每个元素都加1
// map
Map<Integer, String> map = Maps.newHashMap();
map.put(1, "A");
map.put(2, "B");
map.put(3, "C");
map.put(4, "D");
EvaluationContext context = new StandardEvaluationContext();
context.setVariable("map", map);
parser.parseExpression("#map.?[key > 3]").getValue(context);             // {4=D}
parser.parseExpression("#map.?[value == 'A']").getValue(context);        // {1=A}
parser.parseExpression("#map.?[key > 2 and key < 4]").getValue(context); // {3=C}

复制代码
9、模板表达式

模板表达式允许文字和表达式混合使用, 一般选择使用#{}作为一个定界符:

parser.parseExpression("他的名字为#{#person.name}", new TemplateParserContext()).getValue(context); // 他的名字为Tom

10、综合使用

//模板表达式，三元表达式结合使用
parser.parseExpression("#{#person.name!=null?'他的名字叫'+#person.name+'先生':'不知道名字'}", new TemplateParserContext()).getValue(context);


参考：https://www.cnblogs.com/wangzhongqiu/archive/2018/08/24/9530454.html