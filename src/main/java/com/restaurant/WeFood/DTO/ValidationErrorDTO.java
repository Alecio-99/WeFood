package com.restaurant.WeFood.DTO;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {

}
