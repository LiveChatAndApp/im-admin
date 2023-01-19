package cn.wildfirechat.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    public static <T extends TreeNode> List<T> buildTree(List<T> treeNodes) {
        List<T> trees = new ArrayList<>();
        for (T treeNode : treeNodes) {
            if (treeNode.getParentId() == -1) {    // root
                trees.add(findChildren(treeNode, treeNodes)); // findAll children of child-node and add child-nodes to root
            }
        }
        return trees;
    }

    // set child nodes of root
    private static <T extends TreeNode> T findChildren(T root, List<T> treeNodes) {
        for (T node : treeNodes) {
            if (root.getChildren() == null) {
                root.setChildren(new ArrayList<>());
            }
            if (root.getId().equals(node.getParentId())) {    // child node
                root.getChildren().add(findChildren(node, treeNodes));    // findAll children of child-node and add child-nodes to root
            }
        }
        return root;
    }

    private Long id;

    private Long parentId;

    private List<TreeNode> children;


}
