import alessandrosalerno.gendarme.GendarmeCommandGroup;
import alessandrosalerno.gendarme.GendarmeResponse;
import alessandrosalerno.gendarme.annotations.GendarmeCommand;
import alessandrosalerno.gendarme.annotations.GendarmeDescription;
import alessandrosalerno.gendarme.annotations.GendarmeRequireAuthentication;
import alessandrosalerno.gendarme.annotations.GendarmeRequirePermission;

@GendarmeDescription("Order-related commands")
public class OrderCommands extends GendarmeCommandGroup<MyUser> {
    @GendarmeDescription("Order creation commands")
    public static class CreateCommands extends GendarmeCommandGroup<MyUser> {
        @GendarmeCommand
        @GendarmeRequireAuthentication
        @GendarmeRequirePermission("order.create.limit")
        @GendarmeDescription("Create a limit order")
        public GendarmeResponse limit(MyUser commandIssuer, String symbol, Integer quantity, Double price, String side) {
            return new GendarmeResponse("Order created successfully");
        }

        @GendarmeCommand
        @GendarmeRequireAuthentication
        @GendarmeRequirePermission("order.create.market")
        @GendarmeDescription("Create a market order")
        public GendarmeResponse market(MyUser commandIssuer, String symbol, Integer quantity, String side) {
            return new GendarmeResponse("Order created successfully");
        }
    }

    public CreateCommands create = new CreateCommands();

    @GendarmeCommand
    @GendarmeRequireAuthentication
    public GendarmeResponse list(MyUser commandIssuer) {
        GendarmeResponse res = new GendarmeResponse("All done", "Order ID",
                "Symbol",
                "Quantity",
                "Price",
                "Side");
        res.addTableRow(1, "UNIT", 1000, 3700, "BUY");
        res.addTableRow(2, "UNIT", 1000, 3750, "BUY");
        return res;
    }
}