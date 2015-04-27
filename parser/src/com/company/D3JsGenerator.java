/*
 * Copyright (c) 2015, Jumpshot
 */
package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class D3JsGenerator {

    private static Map<String,Integer> processedNodes = new HashMap<String, Integer>();
    private static Set<String> processedEdges = new HashSet<String>();

    private static JsonArray nodes = new JsonArray();
    private static JsonArray links = new JsonArray();

    public static final int LIMIT = 10000;

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        Node root = gson.fromJson(new InputStreamReader(MainJsGen.class.getResourceAsStream("/topictree.json")), Node.class);

        printNodeWithoutEdges(root);
        printNodeWithEdges(root);

        JsonObject data = new JsonObject();
        data.add("nodes", nodes);
        data.add("links", links);

        System.out.println(gson.toJson(data));
    }

    private static JsonObject createNode(String name) {
        JsonObject node = new JsonObject();
        node.addProperty("name", name);
        return node;
    }

    private static JsonObject createLink(Integer source, Integer target) {
        JsonObject node = new JsonObject();
        node.addProperty("source", source);
        node.addProperty("target", target);
        return node;
    }

    private static void printNodeWithEdges(Node root) {
        if (!"Topic".equals(root.getKind())) return;
        if (root.getChild_data() != null) {
            for (Node node : root.getChild_data()) {
                printEdge(root, node);
            }
        }
        if (root.getChildren() != null) {
            for (Node node : root.getChildren()) {
                printEdge(root, node);
            }
        }
    }

    private static void printNodeWithoutEdges(Node root) {
        if (!"Topic".equals(root.getKind())) return;
        if (processedNodes.size()>LIMIT) {
            return;
        }
        if (!processedNodes.containsKey(root.getId())) {
            nodes.add(createNode(root.getTitle()));
            processedNodes.put(root.getId(), processedNodes.size());
        }
        if (root.getChildren() != null) {
            for (Node node : root.getChildren()) {
                printNodeWithoutEdges(node);
            }
        }
    }

    private static void printEdge(Node root, Node node) {
        String edgeKey = root.getId()+"-"+node.getId();
        if (!processedEdges.contains(edgeKey)) {
            if (processedNodes.containsKey(root.getId()) && processedNodes.containsKey(node.getId())) {
                links.add(createLink(processedNodes.get(root.getId()), processedNodes.get(node.getId())));
                processedEdges.add(edgeKey);
            }
        }
        printNodeWithEdges(node);
    }
}
