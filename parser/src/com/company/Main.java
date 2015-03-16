package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
	    Gson g = new GsonBuilder().create();
	    Node root = g.fromJson(new InputStreamReader(Main.class.getResourceAsStream("/topictree.csv")), Node.class);

	    print(root, 0);
    }

	private static void print(Node root, int i) {
		if (!"Topic".equals(root.getKind())) return;
		indent(i);
		System.out.println(root.getId() + "(" + root.getKind() + ", "+root.getTitle()+")");
		if (root.getChild_data() != null) {
			indent(i);
			System.out.println("- child_data = ");
			for (Node node : root.getChild_data()) {
				print(node, i + 1);
				if (!root.getChildren().contains(node)) {
					// throw new IllegalStateException("111");
				}
			}
		}
		if (root.getChildren() != null) {
			indent(i);
			System.out.println("- children = ");
			for (Node node : root.getChildren()) {
				print(node, i + 1);
				if (!root.getChildren().contains(node)) {
					throw new IllegalStateException("111");
				}
			}
		}

	}

	private static void indent(int i) {
		for (int a=0;a<i; a++) {
			System.out.print("  ");
		}
	}
}
