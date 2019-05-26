package smartspace.data;

public interface SmartspaceEntity<K> {
	public K getKey();

	public void setKey(K key);
}
