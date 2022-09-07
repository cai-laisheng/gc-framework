## 什么是binlog、redo log
   binlog属于逻辑日志，是逻辑操作。innodb redo属于物理日志，是物理变更。逻辑日志有个缺点是难以并行，而物理日志可以比较好的并行操作。
   
   * 1.binlog是MySQL Server层记录的日志， redo log是InnoDB存储引擎层的日志。 两者都是记录了某些操作的日志(不是所有)自然有些重复（但两者记录的格式不同）。
   * 2.选择binlog日志作为replication
   
   
   **bin log**
   
   即二进制日志,它记录了数据库上的所有改变，并以二进制的形式保存在磁盘中；它可以用来查看数据库的变更历史、数据库增量备份和恢复、Mysql的复制（主从数据库的复制）。语句以“事件”的形式保存，它描述数据更改。
   
   因为有了数据更新的binlog，所以可以用于实时备份，与master/slave复制。高可用与数据恢复。
   
   * 1.恢复使能够最大可能地更新数据库，因为二进制日志包含备份后进行的所有更新。
   * 2.在主复制服务器上记录所有将发送给从服务器的语句。
   
    
   
   **Redo log**
   
   记录的是新数据的备份。在事务提交前，只要将Redo Log持久化即可，不需要将数据持久化。当系统崩溃时，虽然数据没有持久化，但是RedoLog已经持久化。系统可以根据RedoLog的内容，将所有数据恢复到最新的状态。
   
   InnoDB有buffer pool（简称bp）。bp是数据库页面的缓存，对InnoDB的任何修改操作都会首先在bp的page上进行，然后这样的页面将被标记为dirty并被放到专门的flush list上，后续将由master thread或专门的刷脏线程阶段性的将这些页面写入磁盘（disk or ssd）。这样的好处是避免每次写操作都操作磁盘导致大量的随机IO，阶段性的刷脏可以将多次对页面的修改merge成一次IO操作，同时异步写入也降低了访问的时延。然而，如果在dirty page还未刷入磁盘时，server非正常关闭，这些修改操作将会丢失，如果写入操作正在进行，甚至会由于损坏数据文件导致数据库不可用。为了避免上述问题的发生，Innodb将所有对页面的修改操作写入一个专门的文件，并在数据库启动时从此文件进行恢复操作，这个文件就是redo log file。这样的技术推迟了bp页面的刷新，从而提升了数据库的吞吐，有效的降低了访问时延。带来的问题是额外的写redo log操作的开销（顺序IO，当然很快），以及数据库启动时恢复操作所需的时间。
   
   **Undo Log**
   
   Undo Log是为了实现事务的原子性，在MySQL数据库InnoDB存储引擎中，还用UndoLog来实现多版本并发控制(简称：MVCC)。
   
   -事务的原子性(Atomicity)
   
   事务中的所有操作，要么全部完成，要么不做任何操作，不能只做部分操作。如果在执行的过程中发了错误，要回滚(Rollback)到事务开始前的状态，就像这个事务从来没有执行过。
   
   **binlog格式**
   
   binlog有三种格式：Statement、Row以及Mixed。从安全性来看，ROW（最安全）、MIXED（不推荐）、STATEMENT（不推荐）。
   
   –基于SQL语句的复制(statement-based replication,SBR)，
    
   –基于行的复制(row-based replication,RBR)， 
   
   –混合模式复制(mixed-based replication,MBR)。
   
## MySQL的逻辑架构图
![](https://static001.geekbang.org/resource/image/0d/d9/0d2070e8f84c4801adbfa03bda1f98d9.png)

MySQL整体来看，其实就有两块：**一块是Server层**，它主要做的是MySQL功能层面的事情；**还有一块是引擎层**，负责存储相关的具体事宜。上面我们聊到的粉板**redo log是InnoDB引擎特有的日志**，而**Server层也有自己的日志，称为binlog（归档日志）**。

**crash-safe:** 有了redo log，InnoDB就可以保证即使数据库发生异常重启，之前提交的记录都不会丢失.

## redo log 与 bin log 区别
* 1、redo log是InnoDB引擎特有的；binlog是MySQL的Server层实现的，所有引擎都可以使用。

* 2、redo log是物理日志，记录的是“在某个数据页上做了什么修改”；binlog是逻辑日志，记录的是这个语句的原始逻辑，比如“给ID=2这一行的c字段加1 ”。

* 3、redo log是循环写的，空间固定会用完；binlog是可以追加写入的。“追加写”是指binlog文件写到一定大小后会切换到下一个，并不会覆盖以前的日志。

![](./images/redo-log与bin-log区别.png)

## 两阶段执行过程
### 提交步骤：
commit步骤是属于begin…commit语句中的一个步骤，且是最后一个步骤，两个commit是包含的关系。

1、prepare阶段，写redo log；

2、commit阶段，写binlog并且将redo log的状态改成commit状态；

![](https://img-blog.csdnimg.cn/20200320093349450.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhaWppZ3Vv,size_16,color_FFFFFF,t_70#pic_center)

**prepare：** redolog写入log buffer，并fsync持久化到磁盘，在redolog事务中记录2PC的XID，在redolog事务打上prepare标识.

**commit：** binlog写入log buffer，并fsync持久化到磁盘，在binlog事务中记录2PC的XID，同时在redolog事务打上commit标识.

其中，prepare和commit阶段所提到的“事务”，都是指**内部XA事务，即2PC**

### 示例过程：

![](https://static001.geekbang.org/resource/image/2e/be/2e5bff4910ec189fe1ee6e2ecc7b4bbe.png)

**过程：**

* 1、执行器先找引擎取ID=2这一行。ID是主键，引擎直接用树搜索找到这一行。如果ID=2这一行所在的数据页本来就在内存中，就直接返回给执行器；否则，需要先从磁盘读入内存，然后再返回。
* 2、执行器拿到引擎给的行数据，把这个值加上1，比如原来是N，现在就是N+1，得到新的一行数据，再调用引擎接口写入这行新数据。
* 3、引擎将这行新数据更新到内存中，同时将这个更新操作记录到redo log里面，此时redo log处于prepare状态。然后告知执行器执行完成了，随时可以提交事务。
* 4、执行器生成这个操作的binlog，并把binlog写入磁盘。
* 5、执行器调用引擎的提交事务接口，引擎把刚刚写入的redo log改成提交（commit）状态，更新完成。

## 由于redo log和binlog是两个独立的逻辑，如果不用两阶段提交，要么就是先写完redo log再写binlog，或者采用反过来的顺序。我们看看这两种方式会有什么问题。
* **先写redo log后写binlog。**
 
假设在redo log写完，binlog还没有写完的时候，MySQL进程异常重启。由于我们前面说过的，redo log写完之后，系统即使崩溃，仍然能够把数据恢复回来，所以恢复后这一行c的值是1。

但是由于binlog没写完就crash了，这时候binlog里面就没有记录这个语句。因此，之后备份日志的时候，存起来的binlog里面就没有这条语句。

然后你会发现，如果需要用这个binlog来恢复临时库的话，由于这个语句的binlog丢失，这个临时库就会少了这一次更新，恢复出来的这一行c的值就是0，与原库的值不同。

* **先写binlog后写redo log。**

如果在binlog写完之后crash，由于redo log还没写，崩溃恢复以后这个事务无效，所以这一行c的值是0。但是binlog里面已经记录了“把c从0改成1”这个日志。所以，在之后用binlog来恢复的时候就多了一个事务出来，恢复出来的这一行c的值就是1，与原库的值不同。

## 恢复步骤
redolog中的事务如果经历了二阶段提交中的prepare阶段，则会打上prepare标识，如果经历commit阶段，则会打上commit标识（此时redolog和binlog均已落盘）。

Step1. 按顺序扫描redolog，如果redolog中的事务既有prepare标识，又有commit标识，就直接提交（复制redolog disk中的数据页到磁盘数据页）

Step2 .如果redolog事务只有prepare标识，没有commit标识，则说明当前事务在commit阶段crash了，binlog中当前事务是否完整未可知，此时拿着redolog中当前事务的XID（redolog和binlog中事务落盘的标识），去查看binlog中是否存在此XID
 * a. 如果binlog中有当前事务的XID，则提交事务（复制redolog disk中的数据页到磁盘数据页）

 * b. 如果binlog中没有当前事务的XID，则回滚事务（使用undolog来删除redolog中的对应事务）

可以将mysql redolog和binlog二阶段提交和广义上的二阶段提交进行对比，广义上的二阶段提交，若某个参与者超时未收到协调者的ack通知，则会进行回滚，回滚逻辑需要开发者在各个参与者中进行记录。mysql二阶段提交是通过xid进行恢复。
