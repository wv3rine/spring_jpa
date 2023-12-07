package ru.wv3rine.abspringwebapp.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.wv3rine.abspringwebapp.exceptions.NotEnoughArgumentsException;
import ru.wv3rine.abspringwebapp.models.User;
import ru.wv3rine.abspringwebapp.models.UserIdAndLogin;
import ru.wv3rine.abspringwebapp.services.UsersService;

import java.util.List;

// В исходной статье, которую кинули для тестов, формат ответа был
// в виде ResponseEntity. Но кажется это оверкилл, поэтому я попробовал
// без него (прав ли я? И вообще, когда стоит использовать ResponseEntity? Вроде
// это полезно, но пишут, что не стоит злоупотреблять. Почему?)

/**
 * Класс контроллера для взаимодействия с пользователями
 * (класс {@link User}
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public User createNewUser(@NotNull @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotEnoughArgumentsException("User must have login and password");
        }
        return usersService.addUser(user);
    }

    @GetMapping
    public List<UserIdAndLogin> getUsersNames() {
        return usersService.getIdAndLogins();
    }

    // Я изменил пагинацию на человеческую, считая,
    // что я посмотрел, и считая, зачем она нужна))
    // Стоит ли здесь делать кастомные экспешены? Я не нашел как, но
    // кажется и не нужно

    // Я пока совершенно не понимаю, почему message не отображаются,
    // а вместо них Validation Error. Его даже в jakarta constraints нет
    @GetMapping(params = {"page", "pageSize"})
    public Slice<UserIdAndLogin> getUsersNames(@Min(value = 1, message = "Page size must be greater than one!")
                                               @Max(value = 100, message = "Page size must be less than 100!")
                                               @RequestParam
                                               Integer pageSize,
                                               @Min(value = 0, message = "Page must be greater than zero!")
                                               @RequestParam
                                               Integer page) {
        return usersService.findIdAndLogins(pageSize, page);
    }

    // здесь лучше использовать Integer, чем int?
    @GetMapping(path = "/{id}")
    public User getUser(@NotNull @PathVariable("id") Integer id) {
        return usersService.getUserById(id);
    }

    // Здесь как бы два похожих метода, но они возвращают разные типы,
    // поэтому растащил на два метода
    // user здесь - это просто контейнер для полей, поэтому он не помечен
    // как @Valid
    @PutMapping(path = "/{id}")
    public User updatePerson(@PathVariable("id") Integer id,
                             @NotNull @RequestBody User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotEnoughArgumentsException("User must have login and password");
        }
        return usersService.updateUserById(id, user);
    }
}
