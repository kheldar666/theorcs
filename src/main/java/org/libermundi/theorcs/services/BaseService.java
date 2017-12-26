package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.libermundi.theorcs.repositories.BaseRepository;
import org.springframework.data.domain.Example;

import java.util.Optional;


/**
 * Interface of any Manager BaseService
 *
 */
public interface BaseService<T extends Identity> {

	/**
	 * Duplicate the Object
	 * @param original Object to duplicate
	 * @return duplicated Object
	 */
	T duplicate(T original);

	/**
	 * Duplicate the Object with the ability to reset the Primary Key
	 * @param original Object to duplicate
	 * @param resetPrimaryKey boolean to reset or not the Primary  Key in duplicating the object
	 * @return duplicated Object
	 */
	T duplicate(T original, boolean resetPrimaryKey);
	
	/**
	 * Delete the object defined by id
	 * @param id int
	 */
	void deleteById(Long id);
	
	/**
	 * Delete the object defined by id
	 * @param entity object
	 */
	void delete(T entity);
	
	/**
	 * Get all Object and return them into a List
	 * @return List of Object 
	 */
	Iterable<T> findAll();

	/**
	 * Get one Object similar to the one provided
	 * @return result
	 */
	Optional<T> findOne(Example<T> examplemple);
	
	/**
	 * Make a simple "select count(*)"
	 * @return the total numbers of managed items in the database
	 */
	long count();
	
	/**
	 * Getter of the DAO of the BaseService Manager
	 * @return the instance of the Dao used by the Manager
	 */
	BaseRepository<T,Long> getRepository();

	Class<T> getClassType();

	/**
	 * Setter of the DAO of the BaseService Manager
	 * @param repository
	 */
	void setRepository(BaseRepository<T, Long> repository, Class<T> classType);
	
	/**
	 * return a newly created T Object
	 */
	T createNew();
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	T save(T entity);
	
	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	Iterable<T> saveAll(Iterable<T> entities);
	
	/**
	 * Initialize Data when the Application starts for the first time.
	 */
	void initData();

	/**
	 * Get the last Entity saved in the DB
	 * @return T (or null is the table is empty)
	 */
	T getLast();

	/**
	 * Get the first Entity saved in the DB
	 * @return T (or null is the table is empty)
	 */
	T getFirst();

	T findById(Long id);
}
