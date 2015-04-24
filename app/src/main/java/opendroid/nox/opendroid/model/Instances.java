package opendroid.nox.opendroid.model;

/**
 * Created by Brian on 21/04/2015.
 */
public class Instances {

    /**
     * alues to match Instance request
     */

    private String name;
    private String id;
    private String[] links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getLinks() {
        return links;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }
}
