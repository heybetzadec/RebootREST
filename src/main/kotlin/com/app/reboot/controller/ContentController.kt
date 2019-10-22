package com.app.reboot.controller

import com.app.reboot.entity.Content
import com.app.reboot.help.Body
import com.app.reboot.help.Problem
import com.app.reboot.help.Response
import com.app.reboot.repository.ContentRepository
import com.app.reboot.repository.UserRepository
import com.app.reboot.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.File
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64.getDecoder
import com.app.reboot.exception.StorageException
import com.app.reboot.repository.CategoryRepository
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime





@RestController
class ContentController(@Autowired private val contentRepository : ContentRepository) {

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
        cq.select(root)
//        cq.where(
//                cb.equal(root.get<Long>("mail"), "heybetzadec@gmail.com")
//        )

        val orderList = listOf(cb.desc(root.get<Long>("id")))
        cq.orderBy(orderList)
        val query = em.createQuery<Content>(cq)

        var contents = mutableListOf<Content>()
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

    @RequestMapping(value = ["/content/add"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun addContent(@RequestBody content : Content): Response {
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Content::class.java)
        val root = cq.from(Content::class.java)
        cq.select(root)
        cq.where(
                cb.equal(root.get<String>("link"), content.link)
        )
        val query = em.createQuery<Content>(cq)

        try {
            if(query.resultList.size == 0){
                contentRepository.save(content)
                val body = Body()
                body.content = content
                response.body = body
                response.status = HttpStatus.OK
            } else {
                val now = LocalDateTime.now().toString()
                val len = now.length
                val forLink = now.substring(IntRange(len-5,len-1))
                content.link = "${content.link}_$forLink"
                contentRepository.save(content)
                val body = Body()
                body.content = content
                response.body = body
                response.status = HttpStatus.OK
            }
        }catch (e:Exception){
            val error = Problem(500,"Məzmun saxlanlayarkən problem yarandı!","${e.message}")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
            try {
                storageService.removeFile(content.imageName)
            } catch (e: StorageException){
                println("problem remove file: ${e.message}")
            }
        }

        return response
    }

    @RequestMapping(value = ["/content/edit"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun editContent(@RequestBody content : Content): Response {
        val response = Response()

        if(contentRepository.existsById(content.id ?: 0)){
            contentRepository.save(content)
            val body = Body()
            body.content = content
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(503,"Redaktəsi istənilən istifadəçisi yoxdur.","${content.title} not found!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
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