package ru.yandex.stellarburgers.api.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class IngredientsRequest {

    List<String> ingredients;
}
