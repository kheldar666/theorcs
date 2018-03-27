package org.libermundi.theorcs.repositories.base.impl;

import org.libermundi.theorcs.domain.jpa.base.Undeletable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;

@NoRepositoryBean
public class UndeletableRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    public static final String COUNT_QUERY_STRING_UNDELETABLE = "select count(%s) from %s where deleted=false";

    private final EntityManager entityManager;

    private final JpaEntityInformation<T, ?> entityInformation;

    private final PersistenceProvider provider;

    public UndeletableRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;

        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    @Override
    public void delete(T entity) {
        if(isUndeletable()){
            ((Undeletable)entity).setDeleted(Boolean.TRUE);
            save(entity);
        } else {
            super.delete(entity);
        }
    }

    @Override
    public List<T> findAll() {
        if(isUndeletable()){
            return getQuery(isNotDeleted(), Sort.unsorted()).getResultList();
        } else {
            return super.findAll();
        }


    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        if(isUndeletable()){
            Assert.notNull(ids, "The given Iterable of Id's must not be null!");

            if (!ids.iterator().hasNext()) {
                return Collections.emptyList();
            }

            if (entityInformation.hasCompositeId()) {

                List<T> results = new ArrayList<T>();

                for (ID id : ids) {
                    findById(id).ifPresent(results::add);
                }

                return results;
            }

            ByIdsSpecification<T> specification = new ByIdsSpecification<T>(entityInformation);
            specification.and(isNotDeleted());
            TypedQuery<T> query = getQuery(specification, Sort.unsorted());

            return query.setParameter(specification.parameter, ids).getResultList();
        } else {
            return super.findAllById(ids);
        }


    }

    @Override
    public List<T> findAll(Sort sort) {
        if(isUndeletable()){
            return super.findAll(isNotDeleted(),sort);
        } else {
            return super.findAll(sort);
        }
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if(isUndeletable()){
            return super.findAll(isNotDeleted(),pageable);
        } else {
            return super.findAll(pageable);
        }
    }

    @Override
    public long count() {
        if(isUndeletable()){
            return entityManager.createQuery(getCountQueryStringForUndeletable(), Long.class).getSingleResult();
        } else {
            return super.count();
        }
    }

    private boolean isUndeletable() {
        return Undeletable.class.isAssignableFrom(getDomainClass());
    }

    private Specification<T> isDeleted(){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("deleted"), Boolean.TRUE);
            }
        };
    }

    private Specification<T> isNotDeleted(){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("deleted"), Boolean.FALSE);
            }
        };
    }

    private static final class ByIdsSpecification<T> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;

        @Nullable
        ParameterExpression<Iterable> parameter;

        ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
         */
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }

    private String getCountQueryStringForUndeletable() {

        String countQuery = String.format(COUNT_QUERY_STRING_UNDELETABLE, provider.getCountQueryPlaceholder(), "%s");
        return getQueryString(countQuery, entityInformation.getEntityName());
    }

}
