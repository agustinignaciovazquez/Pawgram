package ar.edu.itba.pawgram.webapp.form;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Size;

public class CommentForm {

    @Size(min = 1, max = 1024)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = StringUtils.strip(content);
    }
}