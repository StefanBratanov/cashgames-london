package common;

import twitter4j.Status;

public interface StatusProcessor {

    void process(Status status);
}
