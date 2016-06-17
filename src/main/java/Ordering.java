import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Ordering {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int totalPrice;

    public Ordering() {}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        if (!client.getOrders().contains(this)) {
            client.getOrders().add(this);
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (!product.getOrders().contains(this)) {
            product.getOrders().add(this);
        }
        if (amount > 0) {
            totalPrice = this.product.getPrice() * amount;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (product != null) {
            totalPrice = product.getPrice() * this.amount;
        }
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
