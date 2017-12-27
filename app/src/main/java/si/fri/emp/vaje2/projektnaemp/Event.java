package si.fri.emp.vaje2.projektnaemp;

/**
 * Created by Matic on 27. 12. 2017.
 */

public class Event {
    public int id;
    public String name;
    public String description;
    public float price;

    public Event(int id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
