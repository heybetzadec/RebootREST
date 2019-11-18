package com.app.reboot.repository.implementation

import com.app.reboot.entity.Role
import com.app.reboot.repository.RoleRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


class RoleImplements : RoleRepository{

//    @PersistenceContext
//    lateinit var em: EntityManager

    override fun findByName(name: String): Optional<Role> {
//        val cb = em.criteriaBuilder
//        val cq = cb.createQuery(Role::class.java)
//        val root = cq.from(Role::class.java)
//        cq.select(root)
//        cq.where(
//                cb.equal(root.get<String>("name"), name)
//        )
//        val query = em.createQuery<Role>(cq)
//        return query.resultList
        val privilege = Role(name)
        val matcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
        val example = Example.of<Role>(privilege, matcher)
        return findOne(example)
    }

    override fun <S : Role?> save(entity: S): S {
        return save(entity)
    }

    override fun deleteInBatch(entities: MutableIterable<Role>) {
        deleteInBatch(entities)
    }

    override fun findAll(): MutableList<Role> {
        return findAll()
    }

    override fun findAll(sort: Sort): MutableList<Role> {
        return findAll(sort)
    }


    override fun <S : Role?> findAll(example: Example<S>): MutableList<S> {
        return findAll(example)
    }

    override fun <S : Role?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return findAll(example, sort)
    }

    override fun <S : Role?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return findAll(example, pageable)
    }

    override fun findAll(pageable: Pageable): Page<Role> {
        return findAll(pageable)
    }

    override fun deleteById(id: Long) {
        deleteById(id)
    }

    override fun deleteAllInBatch() {
        deleteAllInBatch()
    }

    override fun <S : Role?> saveAndFlush(entity: S): S {
        return saveAndFlush(entity)
    }

    override fun flush() {
        flush()
    }

    override fun deleteAll(entities: MutableIterable<Role>) {
        deleteAll(entities)
    }

    override fun deleteAll() {
        deleteAll()
    }

    override fun <S : Role?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return saveAll(entities)
    }

    override fun <S : Role?> findOne(example: Example<S>): Optional<S> {
        return findOne(example)
    }

    override fun count(): Long {
        return count()
    }

    override fun <S : Role?> count(example: Example<S>): Long {
        return count(example)
    }

    override fun getOne(id: Long): Role {
        return getOne(id)
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Role> {
        return findAllById(ids)
    }

    override fun existsById(id: Long): Boolean {
        return existsById(id)
    }

    override fun <S : Role?> exists(example: Example<S>): Boolean {
        return exists(example)
    }

    override fun findById(id: Long): Optional<Role> {
        return findById(id)
    }

    override fun delete(entity: Role) {
        delete(entity)
    }
}