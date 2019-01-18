package models.dao;

import models.User;

public interface UserRelatedEntityDao<K, T> extends Dao<K, T> {
    T findById(K id, User user);
}
