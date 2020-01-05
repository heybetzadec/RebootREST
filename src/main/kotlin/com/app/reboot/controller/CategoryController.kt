package com.app.reboot.controller

import com.app.reboot.entity.Category
import com.app.reboot.repository.CategoryRepository
import com.app.reboot.response.Body
import com.app.reboot.response.CategoryNode
import com.app.reboot.response.Problem
import com.app.reboot.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@RestController
class CategoryController (@Autowired private val categoryRepository : CategoryRepository) {
    @PersistenceContext
    lateinit var em: EntityManager

    @RequestMapping(value = ["/category/get/model"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getModel(): Category {
        return Category(null, "", "", "", "", "", true, Date(),  Date())
    }

    @RequestMapping(value = ["/category/get/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getCategory(@PathVariable id :Long): Response {
        val response = Response()
        val result = categoryRepository.findById(id)
        if (result.isEmpty){
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        } else {
            val category = result.get()
            val body = Body()
            body.category = category
            response.body = body
        }
        response.status = HttpStatus.OK
        return  response
    }

    @RequestMapping(value = ["/categories"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getCategories(): Response{
        val response = Response()
        val categoryNodes = em.createQuery(
                "select NEW com.app.reboot.response.CategoryNode(id, name, title, link, visible, parentCategory.id) " +
                        "from Category c order by id desc", CategoryNode::class.java)
                .resultList
        if (categoryNodes.size > 0) {
            val body = Body()
            categoryNodes.forEachIndexed { index, categoryNode ->
                if (categoryNode.parentCategoryId != null){
                    val parentCategory = categoryNodes.findLast { s -> s.data?.id == categoryNode.parentCategoryId }
                    if (parentCategory?.children == null){
                        parentCategory?.children = mutableListOf()
                    }
                    parentCategory?.children?.add(categoryNodes[index])
                }
            }
            categoryNodes.removeAll { it.parentCategoryId !== null }
            body.categoryNodes = categoryNodes
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["/categories/get/select/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getCategoriesForSelect(@PathVariable id :Long): Response{
        val response = Response()
        val categories: MutableList<Category> = em.createQuery(
                "select c " +
                        "from Category c WHERE c.id != :id  order by id desc", Category::class.java).setParameter("id", id).resultList
        if (categories.size > 0) {
            val body = Body()
            response.body = body
            response.body?.categories = categories
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return response
    }


    @RequestMapping(value = ["/secure/category/save"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun saveCategory(@RequestBody category :Category): Response {
        val response = Response()
        var linkUnique = false
        var isNew = false
        if (category.id == null){
            isNew = true
            val cb = em.criteriaBuilder
            val cq = cb.createQuery(Category::class.java)
            val root = cq.from(Category::class.java)
            cq.select(root)
            if (category.id == null){
                cq.where(
                        cb.equal(root.get<Long>("link"), category.link)
                )
            }
            val query = em.createQuery<Category>(cq)
            if (query.resultList.size == 0)
                linkUnique = true
        }

        try {
            if(linkUnique){
                categoryRepository.save(category)
                val body = Body()
                body.category = category
                response.body = body
                response.status = HttpStatus.OK
            } else {
                if (isNew){
                    val now = LocalDateTime.now().toString()
                    val len = now.length
                    val forLink = now.substring(IntRange(len-5,len-1))
                    category.link = "${category.link}_$forLink"
                }
                categoryRepository.save(category)
                val body = Body()
                body.category = category
                response.body = body
                response.status = HttpStatus.OK
            }
        } catch (e:Exception){
            val error = Problem(503,"Link bənzərsiz olmalıdır!","${category.link} alredy exist!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }

        return response
    }

    @RequestMapping(value = ["/secure/category/remove/id/{id}"], method = [RequestMethod.GET])
    fun removeCategory(@PathVariable id:Long): Response {
        val response = Response()
        if (categoryRepository.existsById(id)){
            response.status = HttpStatus.OK
            categoryRepository.deleteById(id)
            val categoryNodes = em.createQuery(
                    "select NEW com.app.reboot.response.CategoryNode(id, name, title, link, visible, parentCategory.id) " +
                            "from Category c order by id desc", CategoryNode::class.java)
                    .resultList
            if (categoryNodes.size > 0) {
                val body = Body()
                categoryNodes.forEachIndexed { index, categoryNode ->
                    if (categoryNode.parentCategoryId != null) {
                        val parentCategory = categoryNodes.findLast { s -> s.data?.id == categoryNode.parentCategoryId }
                        if (parentCategory?.children == null) {
                            parentCategory?.children = mutableListOf()
                        }
                        parentCategory?.children?.add(categoryNodes[index])
                    }
                }
                categoryNodes.removeAll { it.parentCategoryId !== null }
                body.categoryNodes = categoryNodes
                response.body = body
                response.status = HttpStatus.OK
            }
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "Silinməsi istənilən katiqoriyya yoxdur","Not found category")
            return response
        }
        return response
    }

}