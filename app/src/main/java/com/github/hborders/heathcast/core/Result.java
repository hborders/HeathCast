package com.github.hborders.heathcast.core;

import java.util.function.Function;

public abstract class Result {
    private Result() {
    }

    public static final class Success extends Result {
        public static final Success INSTANCE = new Success();

        private Success() {
        }

        @Override
        public <T> T map(
                Function<Success, T> successMapper,
                Function<Failure, T> failureMapper
        ) {
            return successMapper.apply(this);
        }
    }

    public static final class Failure extends Result {
        public static final Failure INSTANCE = new Failure();

        private Failure() {
        }

        @Override
        public <T> T map(
                Function<Success, T> successMapper,
                Function<Failure, T> failureMapper
        ) {
            return failureMapper.apply(this);
        }
    }

    public abstract <T> T map(
            Function<Success, T> successMapper,
            Function<Failure, T> failureMapper
    );
}
