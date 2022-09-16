package com.seafoodshop.role;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.seafoodshop.common.entity.Role;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepo;

	@Override
	public List<Role> findAll() {
		return roleRepo.findAll();
	}

	@Override
	public Optional<Role> findById(Integer id) {
		return roleRepo.findById(id);
	}

	@Override
	public <S extends Role> List<S> saveAll(Iterable<S> entities) {
		return roleRepo.saveAll(entities);
	}

	@Override
	public boolean existsById(Integer id) {
		return roleRepo.existsById(id);
	}

	@Override
	public long count() {
		return roleRepo.count();
	}

	@Override
	public <S extends Role> boolean exists(Example<S> example) {
		return roleRepo.exists(example);
	}

	@Override
	public void deleteById(Integer id) {
		roleRepo.deleteById(id);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		roleRepo.deleteAllByIdInBatch(ids);
	}

	@Override
	public void delete(Role entity) {
		roleRepo.delete(entity);
	}

	@Override
	public <S extends Role, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return roleRepo.findBy(example, queryFunction);
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		roleRepo.deleteAllById(ids);
	}

	@Override
	public Role getOne(Integer id) {
		return roleRepo.getOne(id);
	}

	@Override
	public void deleteAll() {
		roleRepo.deleteAll();
	}

	@Override
	public Role getById(Integer id) {
		return roleRepo.getById(id);
	}
	

}
