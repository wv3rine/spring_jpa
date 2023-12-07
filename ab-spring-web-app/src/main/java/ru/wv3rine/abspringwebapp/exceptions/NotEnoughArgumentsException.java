package ru.wv3rine.abspringwebapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// https://stackoverflow.com/questions/62896233/how-to-throw-custom-exception-in-proper-way-when-using-javax-validation-valid
// чтобы с аннотацией @NotNull сходилось. Если будет больше validation ошибок, то поставлю в точности как на стэковерфлоу
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughArgumentsException extends RuntimeException {

    public NotEnoughArgumentsException(String message) {
        super(message);
    }
}
