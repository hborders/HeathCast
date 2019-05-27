package com.github.hborders.heathcast.models;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.annotation.Nullable;

public final class PodcastIdentifiedList implements List<Identified<Podcast>> {
    private final List<Identified<Podcast>> podcastIdentifieds;

    public PodcastIdentifiedList(List<Identified<Podcast>> podcastIdentifieds) {
        this.podcastIdentifieds = podcastIdentifieds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodcastIdentifiedList that = (PodcastIdentifiedList) o;
        return podcastIdentifieds.equals(that.podcastIdentifieds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(podcastIdentifieds);
    }

    @Override
    public String toString() {
        return "PodcastIdentifiedList{" +
                "podcastIdentifieds=" + podcastIdentifieds +
                '}';
    }

    @Override
    public int size() {
        return podcastIdentifieds.size();
    }

    @Override
    public boolean isEmpty() {
        return podcastIdentifieds.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return podcastIdentifieds.contains(o);
    }

    @Override
    public Iterator<Identified<Podcast>> iterator() {
        return podcastIdentifieds.iterator();
    }

    @Nullable
    @Override
    public Object[] toArray() {
        return podcastIdentifieds.toArray();
    }

    @Override
    public <T> T[] toArray(@Nullable T[] a) {
        return podcastIdentifieds.toArray(a);
    }

    @Override
    public boolean add(Identified<Podcast> podcastIdentified) {
        return podcastIdentifieds.add(podcastIdentified);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return podcastIdentifieds.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return podcastIdentifieds.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Identified<Podcast>> c) {
        return podcastIdentifieds.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Identified<Podcast>> c) {
        return podcastIdentifieds.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return podcastIdentifieds.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return podcastIdentifieds.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<Identified<Podcast>> operator) {
        podcastIdentifieds.replaceAll(operator);
    }

    @Override
    public void sort(@Nullable Comparator<? super Identified<Podcast>> c) {
        podcastIdentifieds.sort(c);
    }

    @Override
    public void clear() {
        podcastIdentifieds.clear();
    }

    @Override
    public Identified<Podcast> get(int index) {
        return podcastIdentifieds.get(index);
    }

    @Override
    public Identified<Podcast> set(int index, Identified<Podcast> element) {
        return podcastIdentifieds.set(index, element);
    }

    @Override
    public void add(int index, Identified<Podcast> element) {
        podcastIdentifieds.add(index, element);
    }

    @Override
    public Identified<Podcast> remove(int index) {
        return podcastIdentifieds.remove(index);
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return podcastIdentifieds.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return podcastIdentifieds.lastIndexOf(o);
    }

    @Override
    public ListIterator<Identified<Podcast>> listIterator() {
        return podcastIdentifieds.listIterator();
    }

    @Override
    public ListIterator<Identified<Podcast>> listIterator(int index) {
        return podcastIdentifieds.listIterator(index);
    }

    @Override
    public List<Identified<Podcast>> subList(int fromIndex, int toIndex) {
        return podcastIdentifieds.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<Identified<Podcast>> spliterator() {
        return podcastIdentifieds.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super Identified<Podcast>> filter) {
        return podcastIdentifieds.removeIf(filter);
    }

    @Override
    public Stream<Identified<Podcast>> stream() {
        return podcastIdentifieds.stream();
    }

    @Override
    public Stream<Identified<Podcast>> parallelStream() {
        return podcastIdentifieds.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Identified<Podcast>> action) {
        podcastIdentifieds.forEach(action);
    }
}
