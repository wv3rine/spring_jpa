package ru.wv3rine.abspringwebapp.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

// Просто хорошая идея, взятая отсюда:
// https://struchkov.dev/blog/ru/pagination-in-springboot/

// Пока я ее не использую, но перетащил себе на всякий случай))
/**
 * Enum, отвечающий за виды сортировок при запросах
 * ([столбец]_[ASC or DESC]
 */
@Getter
@RequiredArgsConstructor
public enum SortMode {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id"));
    private final Sort sortValue;
}