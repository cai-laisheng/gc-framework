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