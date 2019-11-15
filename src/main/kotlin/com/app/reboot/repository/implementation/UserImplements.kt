package com.app.reboot.repository.implementation

import com.app.reboot.entity.User
import com.app.reboot.repository.UserRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class UserImplements :UserRepository{

    @PersistenceContext
    lateinit var em: EntityManager

    override fun findByEmail(mail: String): MutableList<User>? {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(User::class.java)
        val root = cq.from(User::class.java)
        cq.select(root)
        cq.where(
                cb.equal(root.get<String>("mail"), mail)
        )
        val query = em.createQuery<User>(cq)
        return query.resultList
    }

    override fun <S : User?> save(entity: S): S {
        return save(entity)
    }

    override fun deleteInBatch(entities: MutableIterable<User>) {
        deleteInBatch(entities)
    }

    override fun findAll(): MutableList<User> {
        return findAll()
    }

    override fun findAll(sort: Sort): MutableList<User> {
        return findAll(sort)
    }


    override fun <S : User?> findAll(example: Example<S>): MutableList<S> {
        return findAll(example)
    }

    override fun <S : User?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return findAll(example, sort)
    }

    override fun <S : User?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return findAll(example, pageable)
    }

    override fun findAll(pageable: Pageable): Page<User> {
        return findAll(pageable)
    }

    override fun deleteById(id: Long) {
        deleteById(id)
    }

    override fun deleteAllInBatch() {
        deleteAllInBatch()
    }

    override fun <S : User?> saveAndFlush(entity: S): S {
        return saveAndFlush(entity)
    }

    override fun flush() {
        flush()
    }

    override fun deleteAll(entities: MutableIterable<User>) {
        deleteAll(entities)
    }

    override fun deleteAll() {
        deleteAll()
    }

    override fun <S : User?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return saveAll(entities)
    }

    override fun <S : User?> findOne(example: Example<S>): Optional<S> {
        return findOne(example)
    }

    override fun count(): Long {
        return count()
    }

    override fun <S : User?> count(example: Example<S>): Long {
        return count(example)
    }

    override fun getOne(id: Long): User {
        return getOne(id)
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<User> {
        return findAllById(ids)
    }

    override fun existsById(id: Long): Boolean {
        return existsById(id)
    }

    override fun <S : User?> exists(example: Example<S>): Boolean {
        return exists(example)
    }

    override fun findById(id: Long): Optional<User> {
        return findById(id)
    }

    override fun delete(entity: User) {
        delete(entity)
    }

}