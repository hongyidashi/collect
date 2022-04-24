# nio之文件编程

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [一、FileChannel常用](#%E4%B8%80filechannel%E5%B8%B8%E7%94%A8)
    - [1. 获取FileChannel](#1-%E8%8E%B7%E5%8F%96filechannel)
    - [2. 读取和写入](#2-%E8%AF%BB%E5%8F%96%E5%92%8C%E5%86%99%E5%85%A5)
    - [3. FileChannel的位置](#3-filechannel%E7%9A%84%E4%BD%8D%E7%BD%AE)
    - [4. 获取文件大小](#4-%E8%8E%B7%E5%8F%96%E6%96%87%E4%BB%B6%E5%A4%A7%E5%B0%8F)
- [二、channel的相互传输](#%E4%BA%8Cchannel%E7%9A%84%E7%9B%B8%E4%BA%92%E4%BC%A0%E8%BE%93)
- [三、Path 和 Paths 类](#%E4%B8%89path-%E5%92%8C-paths-%E7%B1%BB)
- [四、Files类](#%E5%9B%9Bfiles%E7%B1%BB)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 一、FileChannel常用

### 1. 获取FileChannel

有一个文件text.txt，其内容如下：

```te
abcdef
```

不能直接打开 FileChannel，必须通过 FileInputStream、FileOutputStream 或者 RandomAccessFile 来获取 FileChannel，它们都有 getChannel 方法

**通过 FileInputStream 获取**

```java
    static void getByFileInputStream(){
        // 使用FileInputStream获取channel
        try(FileInputStream fileInputStream=new FileInputStream("/Users/panhongtong/mydir/text.txt")){
        FileChannel channel=fileInputStream.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(10);
        channel.read(buffer);
        // channel.write(buffer);
        buffer.flip();
        System.out.println((print(buffer)));
        }catch(IOException e){
        e.printStackTrace();
        }
        }

static String print(ByteBuffer b){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<b.limit();i++){
        stringBuilder.append((char)b.get(i));
        }
        return stringBuilder.toString();
        }
```

通过 FileInputStream 获取的 channel 只能读，如果使用写入write方法，会抛出异常：

```tex
Exception in thread "main" java.nio.channels.NonWritableChannelException
	at sun.nio.ch.FileChannelImpl.write(FileChannelImpl.java:201)
	at com.cloud.bssp.nio.FileChannel.GetFileChannel.main(GetFileChannel.java:21)
```

**通过 FileOutputStream 获取**

```java
    static void getByFileOutputStream(){
        // 使用FileOutputStream获取channel
        try(FileOutputStream fileOutputStream=new FileOutputStream("/Users/panhongtong/mydir/text.txt",true)){
        FileChannel channel2=fileOutputStream.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(10);
        buffer.put(StandardCharsets.UTF_8.encode("helloworld"));
        buffer.flip();
        channel2.write(buffer);
        }catch(IOException e){
        e.printStackTrace();
        }
        }

static String print(ByteBuffer b){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<b.limit();i++){
        stringBuilder.append((char)b.get(i));
        }
        return stringBuilder.toString();
        }
```

文件被写入，这里注意FileOutputStream 的属性append，如果是true，表示追加，否则覆盖。本文使用的追加。

```tex
abcdefhelloworld
```

通过 FileOutputStream 获取的 channel 只能写，如果使用read方法，会抛出异常：

```tex
Exception in thread "main" java.nio.channels.NonReadableChannelException
	at sun.nio.ch.FileChannelImpl.read(FileChannelImpl.java:149)
	at com.cloud.bssp.nio.FileChannel.GetFileChannel.main(GetFileChannel.java:28)
```

**通过 RandomAccessFile 获取**

```java
    static void getByRandomAccessFile(){
        // 使用RandomAccessFile获取channel
        try(RandomAccessFile file=new RandomAccessFile("/Users/panhongtong/mydir/text.txt","rw")){
        FileChannel channel=file.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(15);
        //读取文件内容到buffer
        channel.read(buffer);
        buffer.flip();
        System.out.println(print(buffer));

        // 切换为写模式，并且清空buffer
        buffer.clear();
        //写入helloworld到文件
        buffer.put(StandardCharsets.UTF_8.encode("helloworld"));
        buffer.flip();
        channel.write(buffer);
        }catch(IOException e){
        e.printStackTrace();
        }
        }

static String print(ByteBuffer b){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<b.limit();i++){
        stringBuilder.append((char)b.get(i));
        }
        return stringBuilder.toString();
        }
```

结果：这里读取的少了一个字节，因为我指定的buffer只有15，文档中是16，只读取了一次,。

```tex
abcdefhelloworl
```

文档内容被修改为如下，将channel读取到的内容以及新加入的内容拼接在了一起

```tex
abcdefhelloworlhelloworld
```

通过 RandomAccessFile 是否能读写根据构造 RandomAccessFile 时的读写模式决定，指定rw（读写模式）。

### 2. 读取和写入

**读取**

在前面的获取例子中已经给出了关于读取的方式，如下所示，会返回int类型，从 channel 读取数据填充ByteBuffer，返回值表示读到了多少字节，返回值为-1 表示到达了文件的末尾。

```java
int readBytes=channel.read(buffer);
```

仍然使用上面的第一个例子，如果文档是空的话，则会返回-1

```java
 int read=channel1.read(buffer);
        System.out.println(read);

// -1
```

**写入**

如上一章节的例子，已经演示了如何写入数据，利用write方法，将buffer的数据写入channel，但是正确的写入方式应该如下所示：

```java
while(buffer.hasRemaining()){
        channel.write(buffer);
        }
```

hasRemaining()是buffer的一个方法，判断position是否小于limit，是则返回true，表示buffer仍然有未读取的数据。

在 while 中调用 channel.write 是因为 write 方法并不能保证一次将 buffer 中的内容全部写入 channel。

**强制写入**

操作系统出于性能的考虑，会将数据缓存，不是立刻写入磁盘。可以调用 channel.force(true)  方法将文件内容和元数据（文件的权限等信息）立刻写入磁盘。

```java
public abstract void force(boolean metaData)throws IOException;
```

### 3. FileChannel的位置

获取当前位置

```java
long pos=channel.position();
```

设置当前位置

```java
long newPos=...;
        channel.position(newPos);
```

如下获取文件channel：

```
        // 文件内容为10个字节的helloworld
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\P50\\Desktop\\text.txt", "rw");
        FileChannel channel = file.getChannel();
```

打印不同设置时的位置：

```java
        // 打印位置，没有读取时是0
        System.out.println(channel.position());

                // 读取后是文件的长度
                ByteBuffer buffer=ByteBuffer.allocate(10);
                channel.read(buffer);
                System.out.println(channel.position());

                // 设置位置后的长度
                FileChannel position=channel.position(5);
                System.out.println(position.position());
```

结果：

```
0
10
5
```

### 4. 获取文件大小

```java
channel.size();
```

## 二、channel的相互传输

channel提供两个用来channel相互传输数据的方法：

```java
/**
 * 将一个channel的数据传输到target这个channel中，其中position，count，都是调用此方法的channel的
 * in.transferTo(0, in.size(), out);
 */
transferTo(long position,long count,WritableByteChannel target)
```

```java
/**
 * 一个channel从src这个channel获取数据，其中position，count，都是src这个channel的
 * out.transferFrom(in,0,in.size());
 */
transferFrom(ReadableByteChannel src,long position,long count)
```

使用例子如下：

```java
    public static void fileChannelCopy(){
        long start=System.currentTimeMillis();
        String sPath="/Users/panhongtong/mydir/text.txt";
        String tPath="/Users/panhongtong/mydir/text-copy.txt";

        try(FileInputStream fi=new FileInputStream(sPath);
        FileOutputStream fo=new FileOutputStream(tPath);
        //得到对应的文件通道
        FileChannel in=fi.getChannel();
        //得到对应的文件通道
        FileChannel out=fo.getChannel();
        ){
        //连接两个通道，并且从in通道读取，然后写入out通道
        in.transferTo(0,in.size(),out);
        }catch(Exception e){
        e.printStackTrace();
        }
        long end=System.currentTimeMillis();
        System.out.println("用时为："+(end-start)+"ms");
        }
```

**channel的最大传输值**

channel的传输是有大小限制的，最大为2个g，超过会导致数据丢失。所以需要使用循环去多次传输数据。

```java
    static void fileChannelCopyMax(){
        long start=System.currentTimeMillis();
        String sPath="/Users/panhongtong/mydir/text.txt";
        String tPath="/Users/panhongtong/mydir/text-copy.txt";

        try(FileInputStream fi=new FileInputStream(sPath);
        FileOutputStream fo=new FileOutputStream(tPath);
        // 得到对应的文件通道
        FileChannel in=fi.getChannel();
        // 得到对应的文件通道
        FileChannel out=fo.getChannel();
        ){
        // 总文件大小
        long size=in.size();
        // left 剩余文件的数量
        for(long left=size;left>0;){
        System.out.println("position = "+(size-left)+"，left = "+left);
        // transferTo返回传输的数量，剩余的减去传输的，就是当前剩余的数量
        left-=in.transferTo((size-left),left,out);
        }
        }catch(Exception e){
        e.printStackTrace();
        }

        long end=System.currentTimeMillis();
        System.out.println("用时为："+(end-start)+"ms");
        }
```

## 三、Path 和 Paths 类

jdk7 引入了 Path 和 Paths 类

- Path 用来表示文件路径
- Paths 是工具类，用来获取 Path 实例

```java
// 相对路径 使用 user.dir 环境变量来定位 1.txt
Path source=Paths.get("1.txt");

// 绝对路径 代表了  d:\1.txt
        Path source=Paths.get("d:\\1.txt");

// 绝对路径 同样代表了  d:\1.txt
        Path source=Paths.get("d:/1.txt");

        // 代表了  d:\data\projects
        Path projects=Paths.get("d:\\data","projects");
```

- `.` 代表了当前路径
- `..` 代表了上一级路径

例如目录结构如下

```
d:
	|- data
		|- projects
			|- a
			|- b
```

代码

```java
Path path=Paths.get("/Users/panhongtong/mydir/../text.txt");
        System.out.println(path);
// 正常化路径
        System.out.println(path.normalize());
```

输出结果

```
/Users/panhongtong/mydir/../text.txt
/Users/panhongtong/text.txt
```

## 四、Files类

检查文件是否存在

```java
Path path=Paths.get("helloword/data.txt");
        System.out.println(Files.exists(path));
```

创建一级目录

```java
Path path=Paths.get("helloword/d1");
        Files.createDirectory(path);
```

- 如果目录已存在，会抛异常 FileAlreadyExistsException
- 不能一次创建多级目录，否则会抛异常 NoSuchFileException

创建多级目录用

```java
Path path=Paths.get("helloword/d1/d2");
        Files.createDirectories(path);
```

拷贝文件

```java
Path source=Paths.get("helloword/data.txt");
        Path target=Paths.get("helloword/target.txt");

        Files.copy(source,target);
```

- 如果文件已存在，会抛异常 FileAlreadyExistsException

如果希望用 source 覆盖掉 target，需要用 StandardCopyOption 来控制

```java
Files.copy(source,target,StandardCopyOption.REPLACE_EXISTING);
```

移动文件

```java
Path source=Paths.get("helloword/data.txt");
        Path target=Paths.get("helloword/data.txt");

        Files.move(source,target,StandardCopyOption.ATOMIC_MOVE);
```

- StandardCopyOption.ATOMIC_MOVE 保证文件移动的原子性

删除文件

```java
Path target=Paths.get("helloword/target.txt");

        Files.delete(target);
```

- 如果文件不存在，会抛异常 NoSuchFileException

删除目录

```java
Path target=Paths.get("helloword/d1");

        Files.delete(target);
```

- 如果目录还有内容，会抛异常 DirectoryNotEmptyException

遍历目录文件

```java
static void traverseDir(){
        Path path=Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk");
        AtomicInteger dirCount=new AtomicInteger();
        AtomicInteger fileCount=new AtomicInteger();
        try{
        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
@Override
public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes attrs)
        throws IOException{
        System.out.println(dir);
        dirCount.incrementAndGet();
        return super.preVisitDirectory(dir,attrs);
        }

@Override
public FileVisitResult visitFile(Path file,BasicFileAttributes attrs)
        throws IOException{
        System.out.println(file);
        fileCount.incrementAndGet();
        return super.visitFile(file,attrs);
        }
        });
        }catch(IOException e){
        e.printStackTrace();
        }
        System.out.println(dirCount);
        System.out.println(fileCount);
        }
```

删除多级目录

```java
    static void deleteMultiLevelDir(){
        Path path=Paths.get("/Users/panhongtong/logs");
        try{
        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
@Override
public FileVisitResult visitFile(Path file,BasicFileAttributes attrs)
        throws IOException{
        Files.delete(file);
        return super.visitFile(file,attrs);
        }

@Override
public FileVisitResult postVisitDirectory(Path dir,IOException exc)
        throws IOException{
        Files.delete(dir);
        return super.postVisitDirectory(dir,exc);
        }
        });
        }catch(IOException e){
        e.printStackTrace();
        }

        }
```

> 删除是危险操作，确保要递归删除的文件夹没有重要内容

拷贝多级目录

```java
    static void copyMultiLevelDir(){
        long start=System.currentTimeMillis();
        String source="/Users/panhongtong/nacos";
        String target="/Users/panhongtong/nacos-test";

        try(Stream<Path> walk=Files.walk(Paths.get(source))){
        walk.forEach(path->{
        try{
        String targetName=path.toString().replace(source,target);
        // 是目录
        if(Files.isDirectory(path)){
        Files.createDirectory(Paths.get(targetName));
        }
        // 是普通文件
        else if(Files.isRegularFile(path)){
        Files.copy(path,Paths.get(targetName));
        }
        }catch(IOException e){
        e.printStackTrace();
        }
        });
        }catch(IOException e){
        e.printStackTrace();
        }
        long end=System.currentTimeMillis();
        System.out.println(end-start);
        }
```

关于NIO文件编程此处就写到这了