package tree;

import java.util.Collection;

/**
 * This class represents a non-empty search tree. An instance of this class
 * should contain:
 * <ul>
 * <li>A key
 * <li>A value (that the key maps to)
 * <li>A reference to a left Tree that contains key:value pairs such that the
 * keys in the left Tree are less than the key stored in this tree node.
 * <li>A reference to a right Tree that contains key:value pairs such that the
 * keys in the right Tree are greater than the key stored in this tree node.
 * </ul>
 * 
 */
public class NonEmptyTree<K extends Comparable<K>, V> implements Tree<K, V> {

	/* Provide whatever instance variables you need */
	private Tree<K, V> left, right;
	private K key;
	private V value;

	/**
	 * Only constructor we need.
	 * 
	 * @param key
	 * @param value
	 * @param left
	 * @param right
	 */
	public NonEmptyTree(K key, V value, Tree<K, V> left, Tree<K, V> right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}

	public V search(K key) {
		if (key.compareTo(this.key) == 0) {
			return value;
		} else if (this.key.compareTo(key) < 0) {
			return right.search(key);
		} else {
			return left.search(key);
		}
	}

	public NonEmptyTree<K, V> insert(K key, V value) {
		if (key.compareTo(this.key) == 0) {
			this.value = value;
		} else if (this.key.compareTo(key) > 0) {
			left = left.insert(key, value);
		} else {
			right = right.insert(key, value);
		}
		return this;
	}

	public Tree<K, V> delete(K key) {
		if (this.key.compareTo(key) == 0) {
			try {
				K maximum = left.max();
				this.key = maximum;
				value = left.search(maximum);
				left = left.delete(maximum);

			} catch (TreeIsEmptyException e) {
				try {
					K minimum = right.min();
					this.key = minimum;
					value = right.search(minimum);
					right = right.delete(minimum);
				} catch (TreeIsEmptyException e2) {
					return EmptyTree.getInstance();
				}
			}
		}

		if (this.key.compareTo(key) < 0) {
			this.right = right.delete(key);
		} else if (this.key.compareTo(key) > 0) {
			this.left = left.delete(key);
		}

		return this;

	}

	public K max() {
		try {
			return right.max();
		} catch (TreeIsEmptyException e) {
			return key;
		}
	}

	public K min() {
		try {
			return left.min();
		} catch (TreeIsEmptyException e) {
			return key;
		}
	}

	public int size() {
		return 1 + left.size() + right.size();
	}

	public void addKeysToCollection(Collection<K> c) {
		left.addKeysToCollection(c);
		c.add(key);
		right.addKeysToCollection(c);
	}

	public Tree<K, V> subTree(K fromKey, K toKey) {
		Tree<K, V> result = null;

		if (this.key.compareTo(fromKey) < 0) {
			result = this.right.subTree(fromKey, toKey);
			return result;
		}

		else if (toKey.compareTo(this.key) < 0) {
			result = this.left.subTree(fromKey, toKey);
			return result;
		} else {
			return new NonEmptyTree<K, V>(this.key, this.value, left.subTree(fromKey, toKey),
					right.subTree(fromKey, toKey));
		}
	}

	public int height() {
		return 1 + (Math.max(left.height(), right.height()));
	}

	public void inorderTraversal(TraversalTask<K, V> p) {
		if (p != null) {

			left.inorderTraversal(p);
			p.performTask(key, value);
			right.inorderTraversal(p);
			
		}
	}

	public void rightRootLeftTraversal(TraversalTask<K, V> p) {
		if (p != null) {
			right.rightRootLeftTraversal(p);
			p.performTask(key, value);
			left.rightRootLeftTraversal(p);
		}
	}

}
