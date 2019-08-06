package configuration.response;

import java.util.Set;

public class ResponseWrapper<T extends ResponseItem> {

    private String next;
    private String previous;
    private Set<T> resources;

    public ResponseWrapper() {
    }

    public Set<T> getResources() {
        return resources;
    }

    public void setResources(Set<T> resources) {
        this.resources = resources;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
