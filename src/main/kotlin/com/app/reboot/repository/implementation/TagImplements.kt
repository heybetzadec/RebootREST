package com.app.reboot.repository.implementation

import com.app.reboot.entity.Tag
import com.app.reboot.repository.TagRepository
import org.springframework.data.domain.*
import java.util.*

class TagImplements: TagRepository {

    override fun existsByLink(link: String): Boolean {
        val tag = Tag(link)
        val matcher = ExampleMatcher.matching().withMatcher("link", ExampleMatcher.GenericPropertyMatchers.startsWith())
        val example = Example.of<Tag>(tag, matcher)
        return findOne(example).isEmpty
    }

    override fun findByLink(link: String): Tag? {
        val tag = Tag(link)
        val matcher = ExampleMatcher.matching().withMatcher("link", ExampleMatcher.GenericPropertyMatchers.startsWith()).withIgnoreNullValues()
        val example = Example.of<Tag>(tag, matcher)
        return findOne(example).get()
    }

    override fun <S : Tag?> save(entity: S): S {
        return save(entity)
    }

    override fun deleteInBatch(entities: MutableIterable<Tag>) {
        deleteInBatch(entities)
    }

    override fun findAll(): MutableList<Tag> {
        return findAll()
    }

    override fun findAll(sort: Sort): MutableList<Tag> {
        return findAll(sort)
    }


    override fun <S : Tag?> findAll(example: Example<S>): MutableList<S> {
        return findAll(example)
    }

    override fun <S : Tag?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        return findAll(example, sort)
    }

    override fun <S : Tag?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        return findAll(example, pageable)
    }

    override fun findAll(pageable: Pageable): Page<Tag> {
        return findAll(pageable)
    }

    override fun deleteById(id: Long) {
        deleteById(id)
    }

    override fun deleteAllInBatch() {
        deleteAllInBatch()
    }

    override fun <S : Tag?> saveAndFlush(entity: S): S {
        return saveAndFlush(entity)
    }

    override fun flush() {
        flush()
    }

    override fun deleteAll(entities: MutableIterable<Tag>) {
        deleteAll(entities)
    }

    override fun deleteAll() {
        deleteAll()
    }

    override fun <S : Tag?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        return saveAll(entities)
    }

    override fun <S : Tag?> findOne(example: Example<S>): Optional<S> {
        return findOne(example)
    }

    override fun count(): Long {
        return count()
    }

    override fun <S : Tag?> count(example: Example<S>): Long {
        return count(example)
    }

    override fun getOne(id: Long): Tag {
        return getOne(id)
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Tag> {
        return findAllById(ids)
    }

    override fun existsById(id: Long): Boolean {
        return existsById(id)
    }

    override fun <S : Tag?> exists(example: Example<S>): Boolean {
        return exists(example)
    }

    override fun findById(id: Long): Optional<Tag> {
        return findById(id)
    }

    override fun delete(entity: Tag) {
        delete(entity)
    }

}