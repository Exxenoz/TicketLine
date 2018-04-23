package at.ac.tuwien.inso.sepm.ticketline.rest.info;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Info {

    private Git git;
    private Duration uptime;

    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }

    public Duration getUptime() {
        return uptime;
    }

    public void setUptime(Duration uptime) {
        this.uptime = uptime;
    }

    public static class Git {

        private String branch;
        private String tags;
        private Build build;
        private Commit commit;

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public Build getBuild() {
            return build;
        }

        public void setBuild(Build build) {
            this.build = build;
        }

        public Commit getCommit() {
            return commit;
        }

        public void setCommit(Commit commit) {
            this.commit = commit;
        }

        public static class Build {

            private String version;

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private ZonedDateTime time;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public ZonedDateTime getTime() {
                return time;
            }

            public void setTime(ZonedDateTime time) {
                this.time = time;
            }
        }

        public static class Commit {

            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private ZonedDateTime time;

            private Id id;

            public ZonedDateTime getTime() {
                return time;
            }

            public void setTime(ZonedDateTime time) {
                this.time = time;
            }

            public Id getId() {
                return id;
            }

            public void setId(Id id) {
                this.id = id;
            }

            public static class Id {

                private String abbrev;

                public String getAbbrev() {
                    return abbrev;
                }

                public void setAbbrev(String abbrev) {
                    this.abbrev = abbrev;
                }
            }
        }
    }
}