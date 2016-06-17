import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    private int age;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ordering> orders = new ArrayList<Ordering>();

    public Client() {}

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void addOrder(Ordering order) {
        orders.add(order);
        if (order.getClient() != this) {
            order.setClient(this);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Ordering> getOrders() {
        return orders;
    }

    public void setOrders(List<Ordering> orders) {
        this.orders = orders;
    }
}
