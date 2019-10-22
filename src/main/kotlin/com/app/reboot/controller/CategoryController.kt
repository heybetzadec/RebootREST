package com.app.reboot.controller

import com.app.reboot.entity.Category
import com.app.reboot.help.Body
import com.app.reboot.help.Problem
import com.app.reboot.help.Response
import com.app.reboot.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RestController
class CategoryController (@Autowired private val categoryRepository : CategoryRepository) {
    @PersistenceContext
    lateinit var em: EntityManager

    @RequestMapping(method = arrayOf(RequestMethod.GET), path = arrayOf("/loadcategories"))
    public fun loadDefault(): Response {
        val categories = mutableListOf<Category>()
        categories.add(Category("Elm","Ən son Elm və Texnologiya Xəbərləri","elm",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Cihazlar","Kompüter və mobil cihaz xəbərləri","cihazlar",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))

        categories.add(Category("Geyilən","Geyilən Texnologiya Xəbərləri və mobil cihaz xəbərləri","geyilen",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("İnternet","İnternet Xəbərləri","internet",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Kompaniya","Kampaniya Xəbərləri","kompaniya",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Kripto valyuta","Kripto valyuta","kripto",true,"&lt;p&gt;Uzunca bir s&amp;uuml;redir var olsa da Bitcoin&amp;#39;in pop&amp;uuml",""))
        categories.add(Category("Mobil","Mobil Telefon və Tablet Xəbərləri","tablet",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Mobil Tətbiq","Mobil Tətbiq","mobil_tetbiq",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.add(Category("Avtomobil","Avtomobil","avtomobil",true,"Elm və texnologiya dünyasının ən son xəbərləri ilə innovativ texnoloji elm xəbərlərindən agah olun!",""))
        categories.forEach {
            categoryRepository.save(it)
        }
        val body = Body()
        body.categories = categories
        return Response(HttpStatus.OK, Problem(0,"sehv var","NullPointerException"), body)
    }

    @RequestMapping(value = ["categories/get/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getCategories(@PathVariable offset :Int, @PathVariable limit: Int): Response{
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Category::class.java)
        val root = cq.from(Category::class.java)
        cq.select(root)
//        cq.where(
//                cb.equal(root.get<Long>("mail"), "heybetzadec@gmail.com")
//        )
        val query = em.createQuery<Category>(cq)
        var categories = mutableListOf<Category>()
        if (limit == 0) {
            categories = query.resultList
        } else {
            categories = query.setFirstResult(offset).setMaxResults(limit).resultList
        }
        if (categories.size > 0) {
            val body = Body()
            body.categories = categories
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found categories!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }


    @RequestMapping(value = ["/addcategory"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun addUser(@RequestBody category :Category): Response {
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Category::class.java)
        val root = cq.from(Category::class.java)
        cq.select(root)
        cq.where(
                cb.equal(root.get<Long>("link"), category.link)
        )
        val query = em.createQuery<Category>(cq)

        if(query.resultList.size == 0){
            categoryRepository.save(category)
            val body = Body()
            body.category = category
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(503,"Link bənzərsiz olmalıdır!","${category.link} alredy exist!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return response
    }

    @RequestMapping(value = ["/editcategory"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun editUser(@RequestBody category :Category): Response {
        val response = Response()

        if(categoryRepository.existsById(category.id ?: 0)){
            categoryRepository.save(category)
            val body = Body()
            body.category = category
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(503,"Redaktəsi istənilən kateqoria yoxdur.","${category.name} not found!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return response
    }

    @RequestMapping(value = ["/removeuser"], method = [RequestMethod.DELETE])
    fun removeUser(@RequestBody category :Category): Response {
        val response = Response()
        if (categoryRepository.existsById(category.id ?: 0)){
            categoryRepository.delete(category)
            response.status = HttpStatus.OK
            response.body = Body()
            response.body?.category = category
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "${category.name} kateqoriyası yoxdur.","Not found user")
        }
        return response
    }

    @RequestMapping(value = ["/removecategory/id/{id}"], method = [RequestMethod.DELETE])
    fun removeUser(@PathVariable id:Long): Response {
        val response = Response()
        if (categoryRepository.existsById(id)){
            response.status = HttpStatus.OK
            response.body = Body()
            response.body?.category = categoryRepository.findById(id).get()
            categoryRepository.deleteById(id)
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "Silinməsi istənilən katiqoriyya yoxdur","Not found category")
        }
        return response
    }

}