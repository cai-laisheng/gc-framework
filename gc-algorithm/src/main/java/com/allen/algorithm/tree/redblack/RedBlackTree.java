package com.allen.algorithm.tree.redblack;

import lombok.Data;

/**
 * @author xuguocai
 * @date 2022/9/14 15:44   红黑树
 *
 * https://blog.csdn.net/u014454538/article/details/120120216
 */
@Data
public class RedBlackTree {
    // 当前红黑树的根节点，默认为null
    private RedBlackTreeNode root;

    private static boolean RED = Boolean.TRUE;
    private static boolean BLACK = Boolean.FALSE;

    /**
     * 红黑树的左旋
     * @param p
     */
    public void leftRotate(RedBlackTreeNode p) {
        // 在当前节点不为null时，才进行左旋操作
        if (p != null) {
            // 先记录p的右儿子
            RedBlackTreeNode rightChild = p.right;

            // 1. 空出右儿子的左子树
            p.right = rightChild.left;
            // 左子树不为空，需要更新父节点
            if (rightChild.left != null) {
                rightChild.left.parent = p;
            }

            // 2. 空出节点p的父节点
            rightChild.parent = p.parent;
            // 父节点指向右儿子
            if (p.parent == null) { // 右儿子成为新的根节点
                this.root = rightChild;
            } else if (p == p.parent.left) { // 右儿子成为父节点的左儿子
                p.parent.left = rightChild;
            } else { // 右儿子成为父节点的右儿子
                p.parent.right = rightChild;
            }

            // 3. 右儿子和节点p成功会师，节点p成为左子树
            rightChild.left = p;
            p.parent = rightChild;
        }
    }

    /**
     * 红黑树的右旋
     * @param p
     */
    public void rightRotate(RedBlackTreeNode p) {
        if (p != null) {
            // 记录p的左儿子
            RedBlackTreeNode leftChild = p.left;

            // 1. 空出左儿子的右子树
            p.left = leftChild.right;
            // 右子树不为空，需要更新父节点
            if (leftChild.right != null) {
                leftChild.right.parent = p;
            }

            // 2. 空出节点p的父节点
            leftChild.parent = p.parent;
            // 父节点指向左儿子
            if (p.parent == null) { // 左儿子成为整棵树根节点
                this.root = leftChild;
            } else if (p.parent.left == p) { // 左儿子成为父节点左儿子
                p.parent.left = leftChild;
            } else { // 左儿子成为父节点的右儿子
                p.parent.right = leftChild;
            }

            // 3. 顺利会师
            leftChild.right = p;
            p.parent = leftChild;
        }
    }

    /**
     * 红黑树新增节点
     * @param x
     */
    public void fixAfterInsert(RedBlackTreeNode x) {
        // 新插入的节点，默认为红色
        x.color = RED;

        // p不为null、不是整棵树的根节点、父亲为红色，需要调整
        while (x != null && this.root != x && x.parent.color == RED) {
            // 父亲是祖父的左儿子
            if (parentOf(x) == parentOf(parentOf(x)).left) {
                // 父亲和叔叔都是红色
                RedBlackTreeNode uncle = parentOf(parentOf(x)).right;
                if (uncle.color == RED) {
                    // 父亲和叔叔都变成黑色
                    parentOf(x).color = BLACK;
                    uncle.color = BLACK;
                    // 祖父变成红色，继续从祖父开始进行调整
                    parentOf(parentOf(x)).color = RED;
                    x = parentOf(parentOf(x));
                } else { // 叔叔为黑色
                    // 自己是父亲的右儿子，需要对父亲左旋
                    if (x == parentOf(x).right) {
                        x = parentOf(x);
                        leftRotate(x);
                    }
                    // 自己是父亲的左儿子，变色后右旋，保持黑色高度
                    parentOf(x).color = BLACK;
                    parentOf(parentOf(x)).color = RED;
                    rightRotate(parentOf(parentOf(x)));
                }
            } else { //父亲是祖父的右儿子
                RedBlackTreeNode uncle = parentOf(parentOf(x)).left;
                // 父亲和叔叔都是红色
                if (uncle.color == RED) {
                    // 叔叔和父亲变成黑色
                    parentOf(x).color = BLACK;
                    uncle.color = BLACK;
                    // 祖父变为红色，从祖父开始继续调整
                    parentOf(parentOf(x)).color = RED;
                    x = parentOf(parentOf(x));
                } else {
                    // 自己是父亲的左儿子，以父亲为中心右旋
                    if (parentOf(x).left == x) {
                        x = parentOf(x);
                        rightRotate(x);
                    }
                    // 自己是父亲的右儿子，变色后左旋，保持黑色高度
                    parentOf(x).color = BLACK;
                    parentOf(parentOf(x)).color = RED;
                    leftRotate(parentOf(parentOf(x)));
                }
            }
        }

        // 最后将根节点置为黑色，以满足红黑规则1，又不会破坏规则5
        this.root.color = BLACK;
    }

    private static RedBlackTreeNode parentOf(RedBlackTreeNode p) {
        return (p == null ? null : p.parent);
    }

    /**
     * 删除节点
     * @param x
     */
    public void fixAfterDeletion(RedBlackTreeNode x) {
        // x不是根节点且颜色为黑色，开始循环调整
        while (x != root && x.color == BLACK) {
            // x是父亲的左儿子
            if (x == parentOf(x).left) {
                RedBlackTreeNode brother = parentOf(x).right;
                // 兄弟为红色
                if (brother.color == RED) {
                    // 兄弟变成黑色，父节点变成红色
                    brother.color = BLACK;
                    parentOf(x).color = RED;
                    // 父节点左旋，恢复左子树的黑色高度
                    leftRotate(parentOf(x));
                    // 更新兄弟
                    brother = parentOf(x).right;
                }

                // 兄弟为黑色，左右侄子为黑色
                if (brother.left.color == BLACK && brother.right.color == BLACK) {
                    // 兄弟变成红色
                    brother.color = RED;
                    // 从父节点开始继续调整
                    x = parentOf(x);
                } else {
                    // 右侄子为黑色（左侄子为红色）
                    if (brother.right.color == BLACK) {
                        // 左侄子变为黑色，兄弟变成红色
                        brother.left.color = BLACK;
                        brother.color = RED;
                        // 兄弟右旋，恢复右子树黑色高度
                        rightRotate(brother);
                        // 左侄子成为新的兄弟
                        brother = parentOf(x).right;
                    }
                    // 右侄子为红色，兄弟变成父节点颜色
                    brother.color = parentOf(x).color;
                    // 父节点和右侄子变成黑色
                    parentOf(x).color = BLACK;
                    brother.right.color = BLACK;
                    // 父节点左旋
                    leftRotate(parentOf(x));
                    // x指向根节点
                    x = root;
                }
            } else {
                RedBlackTreeNode brother = parentOf(x).left;
                // 兄弟为红色
                if (brother.color == RED) {
                    // 兄弟变黑色，父亲变红色
                    brother.color = BLACK;
                    parentOf(x).color = RED;
                    // 父亲右旋，恢复红黑色高度
                    rightRotate(parentOf(x));
                    // 更新兄弟为右侄子
                    brother = parentOf(x).left;
                }

                // 兄弟的左右儿子为黑色
                if (brother.left.color == BLACK && brother.right.color == BLACK) {
                    // 兄弟变为红色
                    brother.color = RED;
                    // x指向父节点，继续进行调整
                    x = parentOf(x);
                } else {
                    // 左侄子为黑色(右侄子为红色)
                    if (brother.left.color == BLACK) {
                        // 右侄子变黑色，兄弟变红色
                        brother.right.color = BLACK;
                        brother.color = RED;
                        // 对兄弟左旋
                        leftRotate(brother);
                        // 右侄子成为新的兄弟
                        brother = parentOf(x).left;
                    }

                    // 左侄子为红色，兄弟改为父节点颜色
                    brother.color = parentOf(x).color;
                    // 父节点和左侄子变成黑色
                    brother.left.color = BLACK;
                    parentOf(x).color = BLACK;
                    // 兄弟节点上提(右旋父节点)
                    rightRotate(parentOf(x));
                    // x指向根节点
                    x = root;
                }

            }
        }
        // 更新x为黑色
        x.color = BLACK;
    }

}
