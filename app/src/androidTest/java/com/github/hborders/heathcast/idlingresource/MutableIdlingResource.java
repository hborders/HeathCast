package com.github.hborders.heathcast.idlingresource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import com.github.hborders.heathcast.core.EmptyEither;
import com.github.hborders.heathcast.core.Function;
import com.github.hborders.heathcast.core.VoidFunction;

public interface MutableIdlingResource extends IdlingResource {
    void registerIdleTransitionCallback(@Nullable ResourceCallback callback);

    void setBusy();

    void setIdle();

    interface State {
        State IDLE = new Idle();
        State BUSY = new Busy();

        final class Idle extends EmptyEither.Left<
                Idle,
                Busy
                > implements State {
        }
        final class Busy extends EmptyEither.Right<
                Idle,
                Busy
                > implements State {
        }

        <T> T reduce(
                Function<
                        ? super Idle,
                        ? extends T
                        > leftReducer,
                Function<
                        ? super Busy,
                        ? extends T
                        > rightReducer
        );

        void act(
                VoidFunction<? super Idle> leftAction,
                VoidFunction<? super Busy> rightAction
        );
    }

    default void setState(State state) {
        state.act(
                idle -> setIdle(),
                busy -> setBusy()
        );
    }
}
