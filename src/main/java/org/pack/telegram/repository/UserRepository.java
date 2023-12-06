package org.pack.telegram.repository;

import org.pack.telegram.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
