package com.github.hborders.heathcast.core;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;

public abstract class ObjectTransaction<
        ObjectTransactionType extends ObjectTransaction<
                ObjectTransactionType,
                ObjectType
                >,
        ObjectType
        > {
    public interface ObjectEmptyAction<
            ObjectTransactionType extends ObjectTransaction<
                    ObjectTransactionType,
                    ObjectType
                    >,
            ObjectType
            > {
        void act(ObjectType viewFacade);
    }

    public interface ObjectAction<
            ObjectTransactionType extends ObjectTransaction<
                    ObjectTransactionType,
                    ObjectType
                    >,
            ObjectType,
            ArgType
            > {
        void act(
                ObjectType viewFacade,
                ArgType arg
        );
    }

    public interface ObjectAction2<
            ObjectTransactionType extends ObjectTransaction<
                    ObjectTransactionType,
                    ObjectType
                    >,
            ObjectType,
            ArgType1,
            ArgType2
            > {
        void act(
                ObjectType viewFacade,
                ArgType1 arg1,
                ArgType2 arg2
        );
    }

    private final ArrayList<
            ObjectEmptyAction<
                    ObjectTransactionType,
                    ObjectType
                    >
            > emptyActions = new ArrayList<>();
    private final CompletableSubject completableSubject = CompletableSubject.create();
    private final Class<ObjectTransactionType> objectTransactionClass;
    private final ObjectType object;
    private boolean transacted;

    protected ObjectTransaction(Class<ObjectTransactionType> objectTransactionClass, ObjectType object) {
        this.objectTransactionClass = objectTransactionClass;
        this.object = object;
    }

    public final ObjectTransactionType act(
            ObjectEmptyAction<
                    ObjectTransactionType,
                    ObjectType
                    > emptyAction
    ) {
        if (transacted) {
            throw new IllegalStateException("Can't act after transact");
        }

        emptyActions.add(emptyAction);
        return getSelf();
    }

    public final <
            ObjectActionType extends ObjectAction<
                    ObjectTransactionType,
                    ObjectType,
                    ArgType
                    >,
            ArgType
            > ObjectTransactionType act(
            ObjectActionType action,
            ArgType arg
    ) {
        return act(
                viewFacade ->
                        action.act(
                                viewFacade,
                                arg
                        )
        );
    }

    public final <
            ObjectAction2Type extends ObjectAction2<
                    ObjectTransactionType,
                    ObjectType,
                    ArgType1,
                    ArgType2
                    >,
            ArgType1,
            ArgType2
            > ObjectTransactionType act(
            ObjectAction2Type action,
            ArgType1 arg1,
            ArgType2 arg2
    ) {
        return act(
                viewFacade ->
                        action.act(
                                viewFacade,
                                arg1,
                                arg2
                        )
        );
    }

    protected final ObjectTransactionType getSelf() {
        return Objects.requireNonNull(objectTransactionClass.cast(this));
    }

    protected final CompletableSubject transact() {
        if (transacted) {
            throw new IllegalStateException("Can only transact once");
        }

        transacted = true;
        for (ObjectEmptyAction<
                ObjectTransactionType,
                ObjectType
                > emptyAction : emptyActions) {
            emptyAction.act(object);
        }

        return completableSubject;
    }

    public abstract Completable complete();
}
