package opendroid.nox.opendroid.model;

/**
 * Created by Brian on 26/04/2015.
 */
public class InstanceDetail {

    private String status;
    private String flavor = "";
    private String addressIP4;
    private String name;
    private String dateCreated;
    private String image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getAddressIP4() {
        return addressIP4;
    }

    public void setAddressIP4(String addressIP4) {
        this.addressIP4 = addressIP4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
