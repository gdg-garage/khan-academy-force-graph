package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class MainJsGen {

	private static Set<String> printedNodes = new HashSet<String>();
	private static Set<String> printedEdges = new HashSet<String>();
    public static final int LIMIT = 100;

    public static void main(String[] args) {
	    Gson g = new GsonBuilder().create();
	    Node root = g.fromJson(new InputStreamReader(MainJsGen.class.getResourceAsStream("/topictree.csv")), Node.class);

	    print(root, false);
	    print(root, true);
    }

	private static void print(Node root, boolean edges) {
		if (!"Topic".equals(root.getKind())) return;
		if (!edges) {
            if (printedNodes.size()>LIMIT) {
                return;
            }
			if (!printedNodes.contains(root.getId())) {
				System.out.println("var " + root.getId() + " = graph.newNode({label: '" + escape(root.getTitle()) + "'});");
				printedNodes.add(root.getId());
			}
			if (root.getChildren() != null) {
				for (Node node : root.getChildren()) {
					print(node, false);
				}
			}
		} else {
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
	}

	private static void printEdge(Node root, Node node) {
		String edgeKey = root.getId()+"-"+node.getId();
		if (!printedEdges.contains(edgeKey)) {
			if (printedNodes.contains(root.getId()) && printedNodes.contains(node.getId())) {
				System.out.println("graph.newEdge(" + root.getId() + ", " + node.getId() + ");");
				printedEdges.add(edgeKey);
			}
		}
		print(node, true);
	}

	private static String escape(String title) {
		return title.replaceAll("'", "\"").replaceAll("\\s", " ");
	}

}
