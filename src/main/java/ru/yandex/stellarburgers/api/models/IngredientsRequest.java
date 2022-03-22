package ru.yandex.stellarburgers.api.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class IngredientsRequest {

    List<String> ingredients;
}
