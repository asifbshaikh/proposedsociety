package models.dao;

public interface Dao<K, T> {
	T findById(K id);
	void delete(T obj);
	void save(T obj);
}
