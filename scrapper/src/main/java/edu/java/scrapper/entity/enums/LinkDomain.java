package edu.java.scrapper.entity.enums;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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
        public Map<String, String> getCredentials(URI url) {
            var hashMap = new HashMap<String, String>();

            if (!isDomainForUrl(url.toString())) {
                return hashMap;
            }

            var path = url.getPath();
            var pathSplit = path.split("/");
            hashMap.put("ownerName", pathSplit[1]);
            hashMap.put("repoName", pathSplit[2]);
            return hashMap;
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
        public Map<String, String> getCredentials(URI url) {
            var hashMap = new HashMap<String, String>();

            if (!isDomainForUrl(url.toString())) {
                return hashMap;
            }

            var path = url.getPath();
            var pathSplit = path.split("/");
            hashMap.put("questionId", pathSplit[pathSplit.length - 2]);
            return hashMap;
        }
    },

    NOT_SUPPORTED {
        @Override
        public boolean isDomainForUrl(String url) {
            return true;
        }

        @Override
        public Map<String, String> getCredentials(URI url) {
            return new HashMap<>();
        }
    };

    public abstract boolean isDomainForUrl(String url);

    public abstract Map<String, String> getCredentials(URI url);
}
