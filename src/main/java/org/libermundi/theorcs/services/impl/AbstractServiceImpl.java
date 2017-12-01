package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.services.BaseService;
import org.libermundi.theorcs.utils.ObjectUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Abstract BaseService
 * Implements an set of utility methods used by all manager
 * @param <T>
 *
 */
@Slf4j
@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public abstract class AbstractServiceImpl<T extends Identity> implements BaseService<T> {

	protected JpaRepository<T,Long> repository;
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#getRepository()
	 */
	@Override
	public CrudRepository<T,Long> getRepository() {
		return this.repository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#setDao(org.libermundi.theorcs.repositories.base.BaseRepository)
	 */
	@Override
	public void setRepository(JpaRepository<T,Long> repository) {
		if(log.isDebugEnabled()) {
			log.debug("Set Repository : " + repository);
		}
		this.repository = repository;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#delete(java.io.Serializable)
	 */
	@Override
	public void deleteById(Long id) {
		if(log.isDebugEnabled()) {
			log.debug("Delete Object with ID : " + id);
		}
		this.repository.deleteById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#delete(org.libermundi.theorcs.core.model.base.Identifiable)
	 */
	@Override
	public void delete(T entity) {
		this.repository.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#getAll()
	 */
	@Override
	public Iterable<T> findAll() {
		return this.repository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#duplicate(org.libermundi.theorcs.core.model.base.Identifiable)
	 */
	@Override
	public T duplicate(T original) {
		return duplicate(original, Boolean.TRUE);
	}
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#duplicate(org.libermundi.theorcs.core.model.base.Identifiable, boolean)
	 */
	@Override
	public T duplicate(T original, boolean resetPrimaryKey) {
		T copy = ObjectUtils.safeDeepCopy(original);
		if(resetPrimaryKey){
			copy.setId(null);
		}
		return copy;
	}
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#count()
	 */
	@Override
	public long count() {
		if(log.isDebugEnabled()) {
			log.debug(" Execute : getAllCount() ");
		}
		return this.repository.count();
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#findOne(java.io.Serializable)
	 */
	@Override
	public Optional<T> findOne(Example<T> example) {
		return this.repository.findOne(example);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#save(org.libermundi.theorcs.domain.base.Identifiable)
	 */
	@Override
	public T save(T entity) {
		if(log.isDebugEnabled()) {
			log.debug("Saving : " + entity);
		}
		return this.repository.save(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#saveAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<T> saveAll(Iterable<T> entities) {
		return this.repository.saveAll(entities);
	}
	
	protected T getResultfromOptional(Optional<T> optional) {
		if(!optional.isPresent()){
			throw new EntityNotFoundException();
		}
		return optional.get();
	}
}
