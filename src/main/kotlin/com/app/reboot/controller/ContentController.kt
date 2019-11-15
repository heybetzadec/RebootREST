package com.app.reboot.controller

import com.app.reboot.entity.Content
import com.app.reboot.exception.StorageException
import com.app.reboot.repository.ContentRepository
import com.app.reboot.repository.TagRepository
import com.app.reboot.request.Body
import com.app.reboot.request.Problem
import com.app.reboot.request.Response
import com.app.reboot.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@RestController
class ContentController(@Autowired private val contentRepository : ContentRepository, @Autowired private val tagRepository : TagRepository) {

    @PersistenceContext
    lateinit var em: EntityManager
    @Autowired
    private lateinit var storageService: StorageService

    @RequestMapping(value = ["/content/upload/img"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
    fun upload(@RequestParam file: MultipartFile, @RequestParam oldImage: String) {
        try {
            storageService.uploadImageWithThumbnail(file)
            if (oldImage.isNotEmpty()){
                storageService.removeFile(oldImage)
                storageService.removeFile("th_$oldImage")
            }
        } catch (e: StorageException){
            println("problem upload file: ${e.message}}")
        }

    }

    @RequestMapping(value = ["/content/remove/image/name/{name}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun removeFile(@PathVariable name:String): Response {
        val response = Response()
        response.status = HttpStatus.OK
        try {
            storageService.removeFile(name)
            storageService.removeFile("th_$name")
        } catch (e: StorageException){
            println("problem remove file: ${e.message}")
        }
        return response
    }


    @ExceptionHandler(StorageException::class)
    fun handleStorageFileNotFound(e: StorageException) {
        println("handleStorageFileNotFound: ${e.message}")
    }

    @RequestMapping(value = ["/loadcontent"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun loadContent(): Response {
        val response = Response()

        val content = Content(
                1, "Ilk meqale", "ilk_meqale", "embeed",
                "description", "keyword", "content yazi burdaa",
                "", 1, 2, true, 0, Date(), Date()
        )
        val body = Body()
        body.content = content
        response.body = body
        response.status = HttpStatus.OK

        return response
    }

    @RequestMapping(value = ["/content/get/model"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getModel(): Content {
        return Content(
                null, "", "", "",
                "", "", "",
                "", 1, 2, true, 0, Date(), Date()
        )
    }

    @RequestMapping(value = ["/content/get/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getContent(@PathVariable id :Long): Response {
        val response = Response()
        val result = contentRepository.findById(id)
        if (result.isEmpty){
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        } else {
            val content = result.get()
            val body = Body()
            body.content = content
            response.body = body
        }
        response.status = HttpStatus.OK
        return  response
    }

    @RequestMapping(value = ["/contents/get/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getContents(@PathVariable offset :Int, @PathVariable limit: Int): Response {
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Content::class.java)
        val root = cq.from(Content::class.java)
//        cq.select(root)
        cq.multiselect(
                root.get<Long?>("id"),
                root.get<String>("title"),
                root.get<String>("imageName"),
                root.get<Boolean>("visible"),
                root.get<Int>("viewCount"),
                root.get<Date>("createDate")
//                cb.substring(root.get<String>("html"), 1, 200)
        )
//        cq.select(
//                cb.construct(
//                        Content::class.java,
//                        root.get<String>("title")
//                )
//        )
//        cq.where(
//                cb.equal(root.get<Long>("mail"), "heybetzadec@gmail.com")
//        )
        val orderList = listOf(cb.desc(root.get<Long>("id")))
        cq.orderBy(orderList)
        val query = em.createQuery<Content>(cq)

        val contents: MutableList<Content>
        if (limit == 0) {
            contents = query.resultList
        } else {
            contents = query.setFirstResult(offset).setMaxResults(limit).resultList
        }
        if (contents.size > 0) {
            val body = Body()
            body.contents = contents
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["/content/save"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun addContent(@RequestBody content : Content): Response {
        val response = Response()

        var linkUnique = false
        var isNew = false
        if (content.id == null){
            isNew = true
            val cb = em.criteriaBuilder
            val cq = cb.createQuery(Content::class.java)
            val root = cq.from(Content::class.java)
            cq.select(root)
            cq.where(
                    cb.equal(root.get<String>("link"), content.link)
            )
            val query = em.createQuery<Content>(cq)
            if (query.resultList.size == 0)
                linkUnique = true
        }

        try {
            content.tags?.forEach {
                try {
                    tagRepository.save(it)
                }catch (e:DataIntegrityViolationException){
                    println("${it.name} alredy exist! Probllem: ${e.message}")
                }
            }

            if(linkUnique){
                contentRepository.save(content)
                val body = Body()
                body.content = content
                response.body = body
                response.status = HttpStatus.OK
            } else {
                if (isNew){
                    val now = LocalDateTime.now().toString()
                    val len = now.length
                    val forLink = now.substring(IntRange(len-5,len-1))
                    content.link = "${content.link}_$forLink"
                }
                contentRepository.save(content)
                val body = Body()
                body.content = content
                response.body = body
                response.status = HttpStatus.OK
            }
        }catch (e:Exception){
            e.printStackTrace()
            val error = Problem(500,"Məzmun saxlanlayarkən problem yarandı!","${e.message}")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
            if (content.id == null) {
                try {
                    storageService.removeFile(content.imageName)
                    storageService.removeFile("th_${content.imageName}")
                } catch (e: StorageException) {
                    println("problem remove file: ${e.message}")
                }
            }
        }
        return response
    }

    @RequestMapping(value = ["/content/remove"], method = [RequestMethod.DELETE])
    fun removeContent(@RequestBody content : Content): Response {
        val response = Response()
        if (contentRepository.existsById(content.id ?: 0)){
            contentRepository.delete(content)
            response.status = HttpStatus.OK
            response.body = Body()
            response.body?.content = content
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "${content.title} məzmunu yoxdur.","Not found content")
        }
        return response
    }

//    @RequestMapping(value = ["/content/remove/id/{id}"], method = [RequestMethod.GET])
//    fun removeContent(@PathVariable id:Long): Response {
    @RequestMapping(value = ["/content/remove/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun removeContent(@PathVariable id :Long): Response {
        val response = Response()
        if (contentRepository.existsById(id)){
            response.status = HttpStatus.OK
            response.body = Body()
            response.body?.content = contentRepository.findById(id).get()
            try {
                storageService.removeFile(response.body?.content?.imageName ?: "")
            } catch (e: StorageException) {
                println("problem remove file: ${e.message}")
            }
            contentRepository.deleteById(id)
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "Silinməsi istənilən məzmun yoxdur","Not found content")
        }
        return response
    }



}