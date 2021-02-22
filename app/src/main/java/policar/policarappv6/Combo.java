package policar.policarappv6;

/**
 * Created by jonathan on 28/05/2016.
 */
public class Combo {


    private String id;
    private String name;


    public Combo(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //to display object as a string in spinner
    public String toString() {
        return name;
    }
}
