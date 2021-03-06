package com.app.reboot.controller

import com.app.reboot.config.Final
import com.app.reboot.entity.Content
import com.app.reboot.entity.Tag
import com.app.reboot.exception.StorageException
import com.app.reboot.repository.ContentRepository
import com.app.reboot.repository.RoleRepository
import com.app.reboot.repository.TagRepository
import com.app.reboot.response.*
import com.app.reboot.security.JwtValidator
import com.app.reboot.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@RestController
class ContentController(@Autowired private val contentRepository : ContentRepository) {

    @PersistenceContext
    lateinit var em: EntityManager
    @Autowired
    private lateinit var storageService: StorageService
    @Autowired
    private lateinit var tagRepository : TagRepository
    @Autowired
    private lateinit var roleRepository : RoleRepository


    @RequestMapping(value = ["secure/content/upload/img"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
    fun upload(@RequestParam file: MultipartFile, @RequestParam oldImage: String):Response {
        val response = Response()
        try {
            storageService.uploadImageWithThumbnail(file)
            if (oldImage.isNotEmpty()){
                storageService.removeFile(Final.contentImageMediaPath, oldImage)
                storageService.removeFile(Final.contentThubnailImageMediaPath,"th_$oldImage")
            }

            response.status = HttpStatus.OK
        } catch (e: StorageException){
            response.status = HttpStatus.NOT_MODIFIED
            response.problem = e.message?.let { Problem(400, "Yükləmə zamanı problem yarandı!", it) }
        } catch (e: Throwable){
            response.status = HttpStatus.NOT_ACCEPTABLE
            response.problem = e.message?.let { Problem(400, "Ölçü uyğun deyil", it) }
        }

        return response
    }

    @RequestMapping(value = ["secure/content/remove/image/name/{name}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun removeFile(@PathVariable name:String): Response {
        val response = Response()
        response.status = HttpStatus.OK
        try {
            storageService.removeFile(Final.contentImageMediaPath, name)
            storageService.removeFile(Final.contentThubnailImageMediaPath,"th_$name")
        } catch (e: StorageException){
            println("problem remove file: ${e.message}")
        }
        return response
    }

    @ExceptionHandler(StorageException::class)
    fun handleStorageFileNotFound(e: StorageException) {
        println("handleStorageFileNotFound: ${e.message}")
    }

    @RequestMapping(value = ["content/get/model"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getModel(): Response {
        val response = Response()
        val body = Body()
        body.contentThubnailImageMediaPath = Final.mediaPath+Final.contentThubnailImageMediaPath
        body.content = Content(null, "", "", "","", "", "","", null, null, true, 0, Date(), Date())
        response.body = body
        return response
    }

    @RequestMapping(value = ["content/get/id/{id}"], method = [RequestMethod.GET])
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
            body.contentImageMediaPath = Final.mediaPath + Final.contentImageMediaPath
            response.body = body
            response.status = HttpStatus.OK
        }
        return  response
    }

    @RequestMapping(value = ["contents/get/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
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
            body.contentThubnailImageMediaPath = Final.mediaPath + Final.contentThubnailImageMediaPath
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["secure/content/save"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun addContent(@RequestHeader("Authorisation") authorizationHeader :String, @RequestBody content : Content): Response {
        val response = Response()
        val jwtValidator = JwtValidator();
        val token = authorizationHeader.removePrefix("Token ")
        val jwtUser = jwtValidator.validate(token) ?: JwtUser("", 0, "")
        val role = roleRepository.findByName(jwtUser.role).get()
        val privlage = role.privileges?.find { it.entity == "content" }
        if (privlage != null){
            if (privlage.addable ?: false){
                if (content.id == null) {
                    content.addUserId = jwtUser.id
                } else {
                    content.editUserId = jwtUser.id
                }

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
                    val matcher = ExampleMatcher.matching().withMatcher("link", ExampleMatcher.GenericPropertyMatchers.startsWith())
                    if (content.tags != null){
                        val forSave = mutableListOf<Tag>()
                        forSave.addAll(content.tags!!)
                        forSave.removeAll {
                            tagRepository.existsByLink(it.link!!)
                        }
                        tagRepository.saveAll(forSave)
                        content.tags!!.forEach {
                            val t = tagRepository.findByLink(it.link ?: "-")
                            it.id = t?.id
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
                            storageService.removeFile(Final.contentImageMediaPath,content.imageName)
                            storageService.removeFile(Final.contentThubnailImageMediaPath,"th_${content.imageName}")
                        } catch (e: StorageException) {
                            println("problem remove file: ${e.message}")
                        }
                    }
                }
            } else {
                val error = Problem(500,"İcazəniz yoxdur!","No permission!")
                response.problem = error
                response.status = HttpStatus.METHOD_NOT_ALLOWED
            }
        }
        return response
    }

    @RequestMapping(value = ["secure/content/remove"], method = [RequestMethod.DELETE])
    fun removeContent(@RequestBody content : Content): Response {
        val response = Response()
        if (contentRepository.existsById(content.id ?: 0)){

            contentRepository.delete(content)
            response.status = HttpStatus.OK
            response.body = Body()
            response.body?.content = content
            try {
                storageService.removeFile(Final.contentImageMediaPath, content.imageName)
                storageService.removeFile(Final.contentThubnailImageMediaPath,"th_${content.imageName}")
            } catch (e: StorageException) {
                println("problem remove file: ${e.message}")
            }
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "${content.title} məzmunu yoxdur.","Not found content")
        }
        return response
    }

//    @RequestMapping(value = ["/content/remove/id/{id}"], method = [RequestMethod.GET])
//    fun removeContent(@PathVariable id:Long): Response {
    @RequestMapping(value = ["secure/content/remove/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun removeContent(@PathVariable id :Long): Response {
        val response = Response()
        if (contentRepository.existsById(id)){
            response.status = HttpStatus.OK
            response.body = Body()
            contentRepository.deleteById(id)
            try {
                val imgName = response.body?.content?.imageName ?: ""
                storageService.removeFile(Final.contentImageMediaPath, imgName)
                storageService.removeFile(Final.contentThubnailImageMediaPath,"th_${imgName}")
            } catch (e: StorageException) {
                println("problem remove file: ${e.message}")
            }
        } else {
            response.status = HttpStatus.NOT_FOUND
            response.problem = Problem(404, "Silinməsi istənilən məzmun yoxdur","Not found content")
        }
        return response
    }

    @RequestMapping(value = ["secure/test"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun test(@RequestHeader("Authorisation") authorizationHeader :String):Response{
        val response = Response()
        val jwtValidator = JwtValidator();
        val token = authorizationHeader.removePrefix("Token ")
        println("authorizationHeader = ${jwtValidator.validate(token).toString()}")
        val jwtUser = jwtValidator.validate(token) ?: JwtUser("", 0, "")
        val role = roleRepository.findByName(jwtUser.role).get()
        val privlage = role.privileges!!.find { it.entity == "content" }
        println("privlage content = ${privlage!!.addable}")
        response.body = Body()
        response.body!!.role = roleRepository.findByName(jwtUser.role).get()
        return response
    }

}