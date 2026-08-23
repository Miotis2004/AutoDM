package com.autodm.server.service;

public record CheckResult(
    int baseRoll,
    int modifier,
    int total,
    int difficultyClass,
    boolean isSuccess
) {}
