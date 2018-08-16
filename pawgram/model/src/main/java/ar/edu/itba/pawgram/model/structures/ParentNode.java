package ar.edu.itba.pawgram.model.structures;

import java.util.ArrayList;
import java.util.List;

public class ParentNode<T> {
	
	private final T parent;
	private final List<T> children;
	
	public ParentNode(T parent) {
		this.parent = parent;
		this.children = new ArrayList<T>();
	}
	
	public T getParent() {
		return parent;
	}

	public List<T> getChildren() {
		return children;
	}

	public void addChild(T child) {
		this.children.add(child);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + children.hashCode();
		result = prime * result + parent.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ParentNode))
			return false;
		
		@SuppressWarnings("unchecked")
		ParentNode<T> other = (ParentNode<T>) obj;
		return parent.equals(other.getParent()) && children.equals(other.getChildren());
	}

}