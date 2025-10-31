package com.restaurant.wefood.dto;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {

}
