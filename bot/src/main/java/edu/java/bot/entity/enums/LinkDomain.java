package edu.java.bot.entity.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LinkDomain {
    GitHub {
        @Override
        public boolean isDomainForUrl(String url) {
            String regex = "^(https?://)?github\\.com/[^/]+/[^/]+/?$";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);

            return matcher.matches();
        }
    },

    StackOverflow {
        @Override
        public boolean isDomainForUrl(String url) {
            String regex = "^(https?://)?stackoverflow\\.com/questions/(\\d+)+/[^/]+/?$";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);

            return matcher.matches();
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
