package api.models;

import java.util.List;


public class OrdersRequest {

    private final List<String> ingredients;

    public OrdersRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
