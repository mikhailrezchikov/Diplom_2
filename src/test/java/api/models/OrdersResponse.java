package api.models;

import lombok.Data;

import java.util.List;

@Data
public class OrdersResponse {

    private boolean success;
    private List<Order> orders;

    @Data
    public static class Order {
        private List<String> ingredients;
    }
}
