package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.base.Identity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * Interface of any Manager Service
 *
 */
public interface Service<T extends Identity> {

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
	 * Getter of the DAO of the Service Manager
	 * @return the instance of the Dao used by the Manager
	 */
	CrudRepository<T,Long> getRepository();

	/**
	 * Setter of the DAO of the Service Manager
	 * @param repository
	 */
	void setRepository(JpaRepository<T, Long> repository);
	
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
	<S extends T> S save(S entity);
	
	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T> Iterable<S> saveAll(Iterable<S> entities);
	
	/**
	 * Initialize Data when the Application starts for the first time.
	 */
	void initData();
}
