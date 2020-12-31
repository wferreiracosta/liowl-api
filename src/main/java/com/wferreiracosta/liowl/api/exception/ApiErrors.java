package com.wferreiracosta.liowl.api.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wferreiracosta.liowl.exception.BusinessException;

import org.springframework.validation.BindingResult;

public class ApiErrors {

    private List<String> errors;

	public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(
            error -> this.errors.add(error.getDefaultMessage())
        );
	}

    public ApiErrors(BusinessException ex) {
        this.errors = Arrays.asList(ex.getMessage());
	}

	public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}