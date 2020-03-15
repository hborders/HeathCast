package com.github.hborders.heathcast.models;

import com.github.hborders.heathcast.core.Opt;

import java.net.URL;

public class URLOpt extends Opt<URL> {
    public static final URLOpt EMPTY = new URLOpt();
    public static final Factory FACTORY = new Factory();

    public static final class Factory implements Opt.Factory<URLOpt, URL> {
        private Factory() {
        }

        @Override
        public URLOpt empty() {
            return EMPTY;
        }

        @Override
        public URLOpt of(URL value) {
            return new URLOpt(value);
        }
    }

    private URLOpt() {
    }

    public URLOpt(URL value) {
        super(value);
    }
}
