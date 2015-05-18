/*
 * Copyright (c) 2015, Jumpshot
 */
package com.company;

import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.*;

public class D3JsGenerator {

    private static Map<String, Integer> processedNodes = new HashMap<String, Integer>();
    private static Set<String> processedEdges = new HashSet<String>();
    private static Set<Integer> sources = new HashSet<Integer>();
    private static List<Link> links = new ArrayList<Link>();

    private static JsonArray nodes = new JsonArray();

    public static final int LIMIT = 10000;

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        Node root = gson.fromJson(new InputStreamReader(MainJsGen.class.getResourceAsStream("/topictree.json")), Node.class);

        printNodeWithoutEdges(root);
        printNodeWithEdges(root);

        JsonObject data = new JsonObject();
        data.add("nodes", nodes);

        // convert links to json and remove leaf nodes in the process
        JsonArray linksJson = new JsonArray();
        for (Link link : links) {
            if (sources.contains(link.target)) {
                JsonObject node = new JsonObject();
                node.addProperty("source", link.source);
                node.addProperty("target", link.target);
                linksJson.add(node);
            }
        }
        data.add("links", linksJson);

        System.out.println(gson.toJson(data));
    }

    private static JsonObject createNode(String name) {
        JsonObject node = new JsonObject();
        node.addProperty("name", name);
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
        if (processedNodes.size() > LIMIT) {
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
        String edgeKey = root.getId() + "-" + node.getId();
        if (!processedEdges.contains(edgeKey)) {
            if (processedNodes.containsKey(root.getId()) && processedNodes.containsKey(node.getId())) {
                int source = processedNodes.get(root.getId());
                links.add(new Link(source, processedNodes.get(node.getId())));
                sources.add(source);
                processedEdges.add(edgeKey);
            }
        }
        printNodeWithEdges(node);
    }

    public static class Link {
        public int source;
        public int target;

        public Link(int source, int target) {
            this.source = source;
            this.target = target;
        }
    }
}
