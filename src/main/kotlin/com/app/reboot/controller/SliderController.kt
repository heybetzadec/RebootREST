package com.app.reboot.controller


import com.app.reboot.entity.Content
import com.app.reboot.entity.Slider
import com.app.reboot.exception.StorageException
import com.app.reboot.help.Body
import com.app.reboot.help.Final
import com.app.reboot.help.Problem
import com.app.reboot.help.Response
import com.app.reboot.repository.ContentRepository
import com.app.reboot.repository.SliderRepository
import com.app.reboot.service.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RestController
class SliderController(@Autowired private val sliderRepository : SliderRepository, @Autowired private val contentRepository : ContentRepository) {

    @PersistenceContext
    lateinit var em: EntityManager

    @Autowired
    private lateinit var storageService: StorageService


//    @RequestMapping(value = ["/slider/upload/img"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
//    fun upload(@RequestParam file: MultipartFile, @RequestParam oldImage: String, @RequestParam type: Int, @RequestParam id: Long): Response {

    @RequestMapping(value = ["/slider/upload/img"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
    fun upload(@RequestParam file: MultipartFile, @RequestParam oldImage: String, @RequestParam type: Int, @RequestParam id: Long): Response {
        println("isledi bura")
        val response = Response()
        try {
            val result =  contentRepository.findById(id)
            if (result.isEmpty){
                val error = Problem(404,"Bazada uyğun məzmun yoxdur!","Not found contents!")
                response.problem = error
                response.status = HttpStatus.NOT_ACCEPTABLE
            } else {
                response.status = HttpStatus.OK
                val content = result.get()
                if (!file.name.equals(content.imageName)){
                    storageService.uploadImageSetSize(file, file.originalFilename ?: "img.jpg", Final.sliderDimension(type).width,Final.sliderDimension(type).height)
                }
                try {
                    if (!oldImage.equals(content.imageName)){
                        storageService.removeFile(oldImage)
                    }
                } catch (e:Exception){
                    println("Failed file delete")
                }

            }

        } catch (e: StorageException){
            val error = Problem(404,"Yükləmə zamanı problem yarandı!", e.message.toString())
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return response
    }


    @RequestMapping(value = ["/slider/get/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getSlider(@PathVariable id :Long): Response {
        val response = Response()
        val result = sliderRepository.findById(id)
        if (result.isEmpty){
            val error = Problem(404,"Məlumat yoxdur!","Not found sliders!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        } else {
            val slider = result.get()
            val body = Body()
            body.slider = slider
            response.body = body
        }
        response.status = HttpStatus.OK
        return  response
    }


    @RequestMapping(value = ["/slider/get/model/type/{type}/id/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getModel(@PathVariable type :Int, @PathVariable id :Long): Response {
        val response = Response()
        val result =  contentRepository.findById(id)
        var sliderId:Long? = null
        var oldImageName = ""
        if (result.isEmpty){
            val error = Problem(404,"Məlumat yoxdur!","Not found contents!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        } else {
            // Showcase type is 2,3,4 then find edited type
            if (arrayOf(2, 3, 4).contains(type)){
                val cb = em.criteriaBuilder
                val cq = cb.createQuery(Slider::class.java)
                val root = cq.from(Slider::class.java)
                cq.select(root)
                cq.where(
                        cb.equal(root.get<Int>("typeId"), type)
                )

                val query = this.em.createQuery<Slider>(cq)
                val result = query.resultList
                if (result.size > 0){
                    sliderId = result[0].id
                    oldImageName = result[0].imageName
                }
            }

            val content = result.get()
            var endIndex = if(content.html.length > 255)  255 else content.html.length
            val des = content.html.substring(0, endIndex).replace("\\<(.*?)\\>".toRegex(), "") + "..."
            val slider = Slider(sliderId, type, content.title, des, Final.urlApp +"movzu/$id/ad/${content.link}", content.imageName,0,0, content.createDate, Date(), Date())
            slider.content = content
            val body = Body()
            body.slider = slider
            response.status = HttpStatus.OK
            response.body = body
        }
        return response
    }

    @RequestMapping(value = ["/slide/get/offset/{offset}/limit/{limit}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun getSliders(@PathVariable offset :Int, @PathVariable limit: Int): Response {
        val response = Response()
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(Slider::class.java)
        val root = cq.from(Slider::class.java)
        cq.select(root)
        val orderList = listOf(cb.desc(root.get<Long>("id")))
        cq.orderBy(orderList)
        val query = em.createQuery<Slider>(cq)
        var sliders = mutableListOf<Slider>()
        if (limit == 0) {
            sliders = query.resultList
        } else {
            sliders = query.setFirstResult(offset).setMaxResults(limit).resultList
        }
        if (sliders.size > 0) {
            val body = Body()
            body.sliders = sliders
            response.body = body
            response.status = HttpStatus.OK
        } else {
            val error = Problem(404,"Məlumat yoxdur!","Not found sliders!")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
        }
        return  response
    }

    @RequestMapping(value = ["/slider/save"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun saveSlider(@RequestBody slider : Slider): Response {
        val response = Response()
        try {
            if (slider.oldImageName != slider.imageName && slider.oldImageName != ""){
                val cb = em.criteriaBuilder
                val cq = cb.createQuery(Content::class.java)
                val root = cq.from(Content::class.java)
                cq.select(root)
                cq.where(
                        cb.equal(root.get<String>("imageName"), slider.oldImageName)
                )
                val query = em.createQuery<Content>(cq)
                val result = query.resultList
                if (result.size == 0){
                    try {

                    } catch (e:Exception){
                        println("Remove file error: ${e.message}")
                    }
                    storageService.removeFile(slider.oldImageName)
                }
            }
            sliderRepository.save(slider)
            val body = Body()
            body.slider = slider
            response.body = body
            response.status = HttpStatus.OK
        }catch (e:Exception){
            val error = Problem(500,"Məzmun saxlanlayarkən problem yarandı!","${e.message}")
            response.problem = error
            response.status = HttpStatus.NOT_ACCEPTABLE
            try {
                storageService.removeFile(slider.imageName)
            } catch (e: StorageException){
                println("problem remove file: ${e.message}")
            }
        }
        return response
    }

}