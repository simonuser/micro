package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.response.MetaData;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;

/**
 * Exception Filter
 *
 * @author lry
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public MetaData defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);

        // Custom exception
        if (e instanceof AbstractMicroException) {
            if (!(e instanceof MicroErrorException)) {
                AbstractMicroException ex = (AbstractMicroException) e;
                return MetaData.build(traceId, ex.getCode(), ex.getMessage(), null);
            }
        }

        // Third party exception
        if (e instanceof NoHandlerFoundException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        }
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return MetaData.build(traceId, HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), null);
        }
        if (e instanceof MaxUploadSizeExceededException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Upload file size should not exceed 1M", null);
        }
        if (e instanceof ConstraintViolationException) {
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) e).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getMessageTemplate(), null);
            }

            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        }
        if (e instanceof MethodArgumentNotValidException) {
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getDefaultMessage(), null);
            }

            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        }

        if (e instanceof HttpMessageNotReadableException) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof JsonParseException
                        || e.getCause() instanceof com.google.gson.JsonParseException
                        || e.getCause() instanceof org.springframework.boot.json.JsonParseException) {
                    return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Body by JSON Parse Fail", e.getMessage());
                }
            }
        }

        if (e instanceof MethodArgumentTypeMismatchException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Argument Type", e.getMessage());
        }

        log.error("Internal Server Error", e);
        return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
    }

}