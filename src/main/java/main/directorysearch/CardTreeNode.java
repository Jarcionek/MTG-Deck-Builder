package main.directorysearch;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.Enumeration;

public class CardTreeNode implements TreeNode {

    private final TreeNode parentNode;
    private final File file;

    public CardTreeNode(TreeNode parentNode, File file) {
        this.parentNode = parentNode;
        this.file = file;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    @Override
    public TreeNode getParent() {
        return parentNode;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public int getIndex(TreeNode node) {
        return -1;
    }

    @Override
    public Enumeration children() {
        return null;
    }

}
