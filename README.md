# Decompiler
A Decompiler can read the class file, and decompile the bytecode to java code.

一个反编译器，可以读取指定的 class 文件，将 class 文件的字节码进行解析，输出 java 代码。



博客园文章:

#### class 文件反编译器的 java 实现
  最近由于公司项目需要，了解了很多关于类加载方面的知识，给项目带来了一些热部署方面的突破。 由于最近手头工作不太忙，同时驱于对更底层知识的好奇与渴求，因此决定学习了一下 class 文件结构，并通过一周的不懈努力，已经掌握了class 的文件结构，并用 java 实现了一个简单的反编译器：读取 class 文件，反编译成纯 java 代码。下面来看一下具体的实现思路和代码分析。

+ class 文件是一种平台无关性的二进制文件，通过 IO 流可以读取成byte[]，将字节数组转换为十六进制（字符串）之后，class 的数据结构便一目了然了，对 class 文件的解析即变成了对整个十六进制串的分割、解析。

+ 那么如何分割呢？事实上，class 的文件采用一种“伪结构体”的形式来存储数据，这种“伪结构体”只有两种数据类型：无符号数和表（表中的数据也都是无符号数）。 表的概念我们都知道，那什么是无符号数呢？我们都知道，在计算机中最基本数据单位是字节，1字节（byte）= 8位（bit），也就是8个长度的二进制，而4个长度的二进制可以代表1个长度的十六进制，因此，两个十六进制代表一个字节，用无符号数标识即 ：

 - u1代表一个字节，代表2长度的十六进制（如0x01）；
 - u2代表两个字节，代表4长度的十六进制（如0x0001）；
 - u4代表4个字节，代表8长度的十六进制（如0x00000001）

+ 整个 class 文件就是一张表，表中的字段有：魔数、虚拟机的次版本、主版本、常量池的大小、常量池、访问标识、当前类、父类、实现的接口数量、接口集合、字段表数量、字段表集合、方法表数量、方法表集合、属性表数量、属性表集合。   其中，魔数、主次版本、常量池大小、访问标识、当前类、父类、表集合数量等都是无符号数。     常量池、字段表集合、方法表集合、属性表集合等都是表结构，有的表结构中的字段又嵌套了其他的表结构。  具体的无符号数大小和表结构在此不进行展开赘述，用一句话来说：class 文件的数据结构是一种表结构的嵌套。

　　+ 上边对 class 文件的数据结构进行了简略的介绍，现在我们开始讨论如何解析并存储 class 文件。 我们可以按照class 文件中的各种表结构，建立相应的 Bean，例如 对于整个 class 文件，即class_info，我们可以建立如下的 bean：


```
public class Class_info {
    private String magic;  //魔数
    private String minor_version;  //虚拟机次版本
    private String major_version;  //虚拟机主版本
    private int cp_count;  //常量池大小
    private Map<Integer, Constant_X_info> constant_pool_Map;  //常量池
    private String access_flag;  //访问标识
    private int this_class_index;  //当前类索引
    private int super_class_index;  //父类索引
    private int interfaces_count;  //接口数量
    private List<Integer> interfacesList;  //接口集合
    private int fields_count;  //字段表数量
    private List<Fields_info> fields_info_List;  //字段表集合
    private int Methods_count;  //方法表数量
    private List<Methods_info> methods_info_List;  //方法表集合
    private int attributes_count;  //属性表数量
    private List<Attribute_info> attributes;  //属性表集合
    
    public String getMagic() {
        return magic;
    }
    public void setMagic(String magic) {
        this.magic = magic;
    }
　　
　　..... 省略其他 get set

}
```

     将所有的表结构都搭建好后，我们可以开始对 class 文件读取到的 十六进制字符串进行切割，将切割到的数据填充到我们的 bean 中。在此，提供一种切割字符串的思路：创建一个静态指针，指向切割字符串的 start 位置，每次切割length 长度后，对指针进行初始化，即 start = start + length。如果要进行切割数据，那么只需要调用 cutString（int len） 就可以了。 代码如下：



```
　　 private static int start_pointer = 0; 
    private static String hexString = ""; // 十六进制串

    private static String cutString(int len) {
        String cutStr = hexString.substring(start_pointer, start_pointer + len);
        // 初始化指针
        start_pointer = start_pointer + len;
        return cutStr;
    }
```
　　
+ 请注意，上述虽然说起来简单，然而切割数据不可以弄错任何一个字节的长度，如果弄错任何一个字节的长度，那后边的数据完全是错位的，必须推倒重来！ 经过一系列努力后，终于把所有的数据都进行切割并填充到了 bean 中，下面就是利用数据，拼装 java 源代码了。这一部分最重要的无非是方法体的拼装，在编译的过程中，编译器已经将 方法体中的java 语句编译成了字节码指令，完全是内存的堆栈操作，跟我们之前的 java 代码比完全变了形式和语法。那么，如何根据字节码指令，推导出java源代码呢？总结所有的 java 语法，无非是：

 - new 对象 
 - 方法调用（静态方法、构造方法、成员方法、接口方法）
 - 参数传递
 - 计算、判断、赋值
 - 其他的语句（if for while try等）
　　我们需要对阅读字节码指令相当熟练，需要达到1.看着 java 代码，推敲出编译后的字节码指令 2.看着字节码，反推敲出 java 代码。  在此基础上，进行大量的规律总结，这也是反编译最难、最核心的地方了。由于内容比较复杂，在此不进行赘述，可以查看笔者项目的源码。

 

+ 笔者实现的简易反编译器已经开源到 github： https://github.com/MalcolmFF/Decompiler ，其中最重要的两个类为：com.xuanjie.app.App.java(main 方法所在类，解析 class 文件将数据存储到 bean 中) 和 com.xuanjie.core.SrcCreator.java(用于 java 源代码的拼装)。

　　   欢迎读者进行赏阅，提出建议一起维护完善。

