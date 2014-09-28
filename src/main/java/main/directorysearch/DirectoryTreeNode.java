package main.directorysearch;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

public class DirectoryTreeNode implements TreeNode {

    private final TreeNode parentNode;
    private final File file;
    private final Vector<TreeNode> children;

    public DirectoryTreeNode(File file) {
        this(null, file);
    }

    private DirectoryTreeNode(TreeNode parentNode, File file) {
        this.parentNode = parentNode;
        this.file = file;

        File[] childrenFiles = file.listFiles();
        if (childrenFiles == null) {
            throw new IllegalArgumentException("Cannot list children of file " + file);
        }
        Arrays.sort(childrenFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && !o2.isDirectory()) {
                    return -1;
                }
                if (o2.isDirectory() && !o1.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });

        this.children = new Vector<>(childrenFiles.length);
        for (File childFile : childrenFiles) {
            if (childFile.isDirectory()) {
                children.add(new DirectoryTreeNode(this, childFile));
            } else {
                children.add(new CardTreeNode(this, childFile));
            }
        }
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parentNode;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration children() {
        return children.elements();
    }

    @Override
    public String toString() {
        return file.getName() + " (" + children.size() + ")";
    }

}
