package com.github.hborders.heathcast.core;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public abstract class ObjectTransaction<
        ObjectTransactionType extends ObjectTransaction<
                ObjectTransactionType,
                ObjectType
                >,
        ObjectType
        > {
    public interface ObjectAction0<
            ObjectType
            > {
        void act(ObjectType object);
    }

    public interface ObjectAction1<
            ObjectType,
            ArgType1
            > {
        void act(
                ObjectType object,
                ArgType1 arg1
        );
    }

    public interface ObjectAction2<
            ObjectType,
            ArgType1,
            ArgType2
            > {
        void act(
                ObjectType object,
                ArgType1 arg1,
                ArgType2 arg2
        );
    }

    public interface ObjectAction3<
            ObjectType,
            ArgType1,
            ArgType2,
            ArgType3
            > {
        void act(
                ObjectType object,
                ArgType1 arg1,
                ArgType2 arg2,
                ArgType3 arg3
        );
    }

    private final ArrayList<
            ObjectAction0<ObjectType>
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
            ObjectAction0<ObjectType> emptyAction
    ) {
        if (transacted) {
            throw new IllegalStateException("Can't act after transact");
        }

        emptyActions.add(emptyAction);
        return getSelf();
    }

    public final <
            ObjectActionType extends ObjectAction1<
                                ObjectType,
                                ArgType1
                                >,
            ArgType1
            > ObjectTransactionType act(
            ObjectActionType action,
            ArgType1 arg1
    ) {
        return act(
                object ->
                        action.act(
                                object,
                                arg1
                        )
        );
    }

    public final <
            ObjectAction2Type extends ObjectAction2<
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
                object ->
                        action.act(
                                object,
                                arg1,
                                arg2
                        )
        );
    }

    public final <
            ObjectAction3Type extends ObjectAction3<
                    ObjectType,
                    ArgType1,
                    ArgType2,
                    ArgType3
                    >,
            ArgType1,
            ArgType2,
            ArgType3
            > ObjectTransactionType act(
            ObjectAction3Type action,
            ArgType1 arg1,
            ArgType2 arg2,
            ArgType3 arg3
    ) {
        return act(
                object ->
                        action.act(
                                object,
                                arg1,
                                arg2,
                                arg3
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
        for (ObjectAction0<ObjectType> emptyAction : emptyActions) {
            emptyAction.act(object);
        }

        return completableSubject;
    }

    public abstract Completable complete();
}
