package com.app.reboot.repository.implementation

import com.app.reboot.entity.Privilege
import com.app.reboot.repository.PrivilegeRepository
import org.springframework.data.domain.*
import java.util.*
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith



class PrivilageImplements : PrivilegeRepository {

//    @PersistenceContext
//    lateinit var em: EntityManager

    override fun findByName(name: String): Optional<Privilege> {
        val privilege = Privilege(name)
        val matcher = ExampleMatcher.matching().withMatcher("name", startsWith())
        val example = Example.of<Privilege>(privilege, matcher)
        return findOne(example)
    }

    override fun <S : Privilege?> save(entity: S): S {
        return save(entity)
    }

    override fun deleteInBatch(entities: MutableIterable<Privilege>) {
        deleteInBatch(entities)
    }

    override fun findAll(): MutableList<Privilege> {
        return findAll()
    }

    override fun findAll(sort: Sort): MutableList<Privilege> {
        return findAll(sort)
    }


    override fun <S : Privilege?> findAll(example: Example<S>): MutableList<S> {
        return findAll(example)
    }

    override fun <S : Privilege?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return findAll(example, sort)
    }

    override fun <S : Privilege?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return findAll(example, pageable)
    }

    override fun findAll(pageable: Pageable): Page<Privilege> {
        return findAll(pageable)
    }

    override fun deleteById(id: Long) {
        deleteById(id)
    }

    override fun deleteAllInBatch() {
        deleteAllInBatch()
    }

    override fun <S : Privilege?> saveAndFlush(entity: S): S {
        return saveAndFlush(entity)
    }

    override fun flush() {
        flush()
    }

    override fun deleteAll(entities: MutableIterable<Privilege>) {
        deleteAll(entities)
    }

    override fun deleteAll() {
        deleteAll()
    }

    override fun <S : Privilege?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return saveAll(entities)
    }

    override fun <S : Privilege?> findOne(example: Example<S>): Optional<S> {
        return findOne(example)
    }

    override fun count(): Long {
        return count()
    }

    override fun <S : Privilege?> count(example: Example<S>): Long {
        return count(example)
    }

    override fun getOne(id: Long): Privilege {
        return getOne(id)
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Privilege> {
        return findAllById(ids)
    }

    override fun existsById(id: Long): Boolean {
        return existsById(id)
    }

    override fun <S : Privilege?> exists(example: Example<S>): Boolean {
        return exists(example)
    }

    override fun findById(id: Long): Optional<Privilege> {
        return findById(id)
    }

    override fun delete(entity: Privilege) {
        delete(entity)
    }
}