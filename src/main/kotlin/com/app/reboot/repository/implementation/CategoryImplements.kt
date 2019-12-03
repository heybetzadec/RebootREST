package com.app.reboot.repository.implementation

import com.app.reboot.entity.Category
import com.app.reboot.repository.CategoryRepository
import org.springframework.data.domain.*
import java.util.*

class CategoryImplements :CategoryRepository{


    override fun findByLink(link: String): Optional<Category> {
        val category = Category(link)
        val matcher = ExampleMatcher.matching().withMatcher("link", ExampleMatcher.GenericPropertyMatchers.startsWith())
        val example = Example.of<Category>(category, matcher)
        return findOne(example)
    }

    override fun <S : Category?> save(entity: S): S {
        return save(entity)
    }

    override fun deleteInBatch(entities: MutableIterable<Category>) {
        deleteInBatch(entities)
    }

    override fun findAll(): MutableList<Category> {
        return findAll()
    }

    override fun findAll(sort: Sort): MutableList<Category> {
        return findAll(sort)
    }


    override fun <S : Category?> findAll(example: Example<S>): MutableList<S> {
        return findAll(example)
    }

    override fun <S : Category?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return findAll(example, sort)
    }

    override fun <S : Category?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return findAll(example, pageable)
    }

    override fun findAll(pageable: Pageable): Page<Category> {
        return findAll(pageable)
    }

    override fun deleteById(id: Long) {
        deleteById(id)
    }

    override fun deleteAllInBatch() {
        deleteAllInBatch()
    }

    override fun <S : Category?> saveAndFlush(entity: S): S {
        return saveAndFlush(entity)
    }

    override fun flush() {
        flush()
    }

    override fun deleteAll(entities: MutableIterable<Category>) {
        deleteAll(entities)
    }

    override fun deleteAll() {
        deleteAll()
    }

    override fun <S : Category?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return saveAll(entities)
    }

    override fun <S : Category?> findOne(example: Example<S>): Optional<S> {
        return findOne(example)
    }

    override fun count(): Long {
        return count()
    }

    override fun <S : Category?> count(example: Example<S>): Long {
        return count(example)
    }

    override fun getOne(id: Long): Category {
        return getOne(id)
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Category> {
        return findAllById(ids)
    }

    override fun existsById(id: Long): Boolean {
        return existsById(id)
    }

    override fun <S : Category?> exists(example: Example<S>): Boolean {
        return exists(example)
    }

    override fun findById(id: Long): Optional<Category> {
        return findById(id)
    }

    override fun delete(entity: Category) {
        delete(entity)
    }


}