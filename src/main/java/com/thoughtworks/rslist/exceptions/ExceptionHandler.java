package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({IndexOutOfBoundsException.class,
                                                                NullPointerException.class,
                                                                MethodArgumentNotValidException.class,
                                                                MyException.class})
    public ResponseEntity<CommentException> handleCommentException(Exception ex) {
        CommentException commentException=new CommentException();
        if (ex instanceof MethodArgumentNotValidException){
            commentException.setError("invalid param");
            return ResponseEntity.badRequest().body(commentException);
        }
        commentException.setError("invalid index");
        return ResponseEntity.badRequest().body(commentException);
    }
}
