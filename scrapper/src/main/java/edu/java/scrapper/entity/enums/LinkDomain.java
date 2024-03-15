package edu.java.scrapper.entity.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LinkDomain {
    GITHUB {
        private final String regex = "^(https?://)?github\\.com/[^/]+/[^/]+/?$";
        private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        @Override
        public boolean isDomainForUrl(String url) {
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }

        @Override
        public String toString() {
            return "GitHub";
        }
    },

    STACKOVERFLOW {
        private final String regex = "^(https?://)?stackoverflow\\.com/questions/(\\d+)+/[^/]+/?$";
        private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        @Override
        public boolean isDomainForUrl(String url) {
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }

        @Override
        public String toString() {
            return "StackOverflow";
        }
    },

    NOT_SUPPORTED {
        @Override
        public boolean isDomainForUrl(String url) {
            return true;
        }
    };

    public abstract boolean isDomainForUrl(String url);
}
