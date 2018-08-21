package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.webapp.form.UploadForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

@RequestMapping("/upload")
@Controller
public class UploadController {

    // save uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://temp//";

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
                continue; //next pls
            }

            try {

                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);

                sj.add(file.getOriginalFilename());

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
