package com.github.blombler008.twitchbot.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Node {

    private static Map<String, Node> nodes = new HashMap<>();


    private String name;
    private String id;
    private JPanel panel;
    private DefaultMutableTreeNode treeNode;

    public Node(String name, String id) {
        this.id = id;
        this.name = name;
        treeNode = new DefaultMutableTreeNode(this);

        if(nodes.containsKey(id)) {
            throw new IllegalArgumentException("node already exist");
        }
    }


    public static Map<String, Node> getNodes() {
        return nodes;
    }
    public static Node getNode(String id) {
        Node node = nodes.get(id);
        if(Objects.isNull(node)) {
            throw new IllegalArgumentException("node does not exist");
        }
        return node;
    }

    public static boolean hasNode(String id) {
        return nodes.containsKey(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public JPanel getPanel() {
        return panel;
    }

    public DefaultMutableTreeNode asTreeNode() {
        return treeNode;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
}
