import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ordering> orders = new ArrayList<>();

    public Product() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Ordering> getOrders() {
        return orders;
    }

    public void addOrder(Ordering order) {
        orders.add(order);
        if (order.getProduct() != this) {
            order.setProduct(this);
        }
    }
}
