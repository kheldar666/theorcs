package org.libermundi.theorcs.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.libermundi.theorcs.services.BaseService;
import org.libermundi.theorcs.utils.ObjectUtils;
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

	private UndeletableRepository<T,Long> repository;

	private Class<T> classType;
	
	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#getRepository()
	 */
	@Override
	public UndeletableRepository<T,Long> getRepository() {
		return this.repository;
	}

	/*
	 * (non-Javadoc)
	 * @see org.libermundi.theorcs.services.base.BaseService#setDao(org.libermundi.theorcs.repositories.base.UndeletableRepository)
	 */
	@Override
	public void setRepository(UndeletableRepository<T,Long> repository, Class<T> classType) {
		if(log.isDebugEnabled()) {
			log.debug("Set Repository : " + repository);
		}
		this.repository = repository;
		this.classType = classType;
	}

	public Class<T> getClassType() {
		return this.classType;
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
			throw new EntityNotFoundException("The object of type "+ getClassType() + " was not found");
		}
		return optional.get();
	}

	@Override
	public T getLast() {
		return getRepository().findFirstByOrderByIdDesc();
	}

	@Override
	public T getFirst() {
		return getRepository().findFirstByOrderByIdAsc();
	}

	@Override
	public T findById(Long id) {
		return getResultfromOptional(getRepository().findById(id));
	}
}
