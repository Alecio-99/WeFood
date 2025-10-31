package com.restaurant.wefood.controller.handlers;

import com.restaurant.wefood.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex,
                                                HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Recurso não encontrado");
        pd.setType(URI.create("https://api.wefood.com/errors/resource-not-found"));
        pd.setInstance(URI.create(req.getRequestURI()));
        // extras opcionais
        pd.setProperty("errorCode", "RES_404");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex,
                                          HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Dados inválidos");
        pd.setType(URI.create("https://api.wefood.com/errors/validation"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setDetail("Revise os campos enviados.");

        List<LinkedHashMap<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fe -> {
                    LinkedHashMap<String, Object> m = new LinkedHashMap<>();
                    m.put("field", fe.getField());
                    m.put("message", fe.getDefaultMessage());
                    m.put("rejectedValue", fe.getRejectedValue());
                    return m;
                }).toList();

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex,
                                               HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Parâmetro inválido");
        pd.setType(URI.create("https://api.wefood.com/errors/illegal-argument"));
        pd.setInstance(URI.create(req.getRequestURI()));
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Erro interno");
        pd.setDetail("Ocorreu um erro inesperado.");
        pd.setType(URI.create("https://api.wefood.com/errors/internal"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("debugMessage", ex.getMessage());
        return pd;
    }
}
