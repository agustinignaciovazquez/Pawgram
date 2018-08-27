package ar.edu.itba.pawgram.webapp.form;

import javax.validation.Valid;

public class CommentsForm {
    @Valid
    private CommentForm parentForm;

    @Valid
    private CommentForm[] childForms;

    public CommentForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(CommentForm parentForm) {
        this.parentForm = parentForm;
    }

    public CommentForm[] getChildForms() {
        return childForms;
    }

    public void setChildForms(CommentForm[] childForms) {
        this.childForms = childForms;
    }

    public CommentForm getChildForm(int index) {
        return childForms[index];
    }
}
