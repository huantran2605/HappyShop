package com.happyshop.role;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.happyshop.common.entity.Role;

public interface RoleService {

	Role getById(Integer id);

	void deleteAll();

	Role getOne(Integer id);

	void deleteAllById(Iterable<? extends Integer> ids);

	<S extends Role, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	void delete(Role entity);

	void deleteAllByIdInBatch(Iterable<Integer> ids);

	void deleteById(Integer id);

	<S extends Role> boolean exists(Example<S> example);

	long count();

	boolean existsById(Integer id);

	<S extends Role> List<S> saveAll(Iterable<S> entities);

	Optional<Role> findById(Integer id);

	List<Role> findAll();

}
