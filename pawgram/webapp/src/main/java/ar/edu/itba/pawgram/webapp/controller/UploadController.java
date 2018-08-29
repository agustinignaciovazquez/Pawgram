package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.FileService;
import ar.edu.itba.pawgram.webapp.form.UploadForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.StringJoiner;

@RequestMapping("/upload")
@Controller
public class UploadController {

    // save uploaded file to this folder
    private static String UPLOAD_FOLDER = "D://temp//";
    @Autowired
    private FileService fileService;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("upload/upload");
    }

    @RequestMapping(value = "/uploadMulti", method = { RequestMethod.POST })
    public ModelAndView multiFileUpload(@ModelAttribute UploadForm form,
                                        RedirectAttributes redirectAttributes) {

        StringJoiner sj = new StringJoiner(" , ");

        for (MultipartFile file : form.getFiles()) {

            if (file.isEmpty()) {
                continue;
            }

            try {
                fileService.createFile(UPLOAD_FOLDER,file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String uploadedFileName = sj.toString();
        if (StringUtils.isEmpty(uploadedFileName)) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        } else {
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + uploadedFileName + "'");
        }

        return new ModelAndView("redirect:/upload/uploadStatus");

    }

    @RequestMapping(value = "/uploadStatus", method = { RequestMethod.GET })
    public ModelAndView uploadStatus() {
        return new ModelAndView("upload/uploadStatus");
    }

}
