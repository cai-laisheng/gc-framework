package com.allen.algorithm.tree.redblack;

import lombok.Data;

/**
 * @author xuguocai
 * @date 2022/9/14 15:34  红黑树节点
 */
@Data
public class RedBlackTreeNode {
    public int val;
    public RedBlackTreeNode left;
    public RedBlackTreeNode right;
    // 记录节点颜色的color属性，暂定true表示红色
    public boolean color;
    // 为了方便迭代插入，所需的parent属性
    public RedBlackTreeNode parent;

    // 一些构造函数，根据实际需求构建
    public RedBlackTreeNode() {}
}
