package com.company;

import java.util.List;

/**
 * Created by tomucha on 16.03.15.
 */
public class Node {

	private String id;
	private String kind;
	private String title;

	private List<Node> child_data;
	private List<Node> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public List<Node> getChild_data() {
		return child_data;
	}

	public void setChild_data(List<Node> child_data) {
		this.child_data = child_data;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Node)) return false;

		Node node = (Node) o;

		if (id != null ? !id.equals(node.id) : node.id != null) return false;
		if (kind != null ? !kind.equals(node.kind) : node.kind != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (kind != null ? kind.hashCode() : 0);
		return result;
	}
}
