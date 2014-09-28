package main.directorysearch;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CardsTreeView extends JTree {

    public CardsTreeView() {
        super(new DirectoryTreeNode(Settings.getCardsImagesDirectory()));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setRootVisible(false);
        this.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.print("NEW SELECTION: ");
                if (CardsTreeView.this.getSelectionCount() > 0)
                for (TreePath treePath : CardsTreeView.this.getSelectionPaths()) {
                    System.out.println(((TreeNode) treePath.getLastPathComponent()).toString());
                }
            }
        });
    }

}
