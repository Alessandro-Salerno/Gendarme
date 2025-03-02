import alessandrosalerno.gendarme.*;

public class Main {
    public static void main(String[] args) {
        GendarmeCommandRoot<MyUser> commandRoot = new GendarmeCommandRoot<>("NSE Commands");
        commandRoot.addCommand("order", new OrderCommands());

        MyUser user = new MyUser();
//        GendarmeResponse res = commandRoot.invoke(user, "order", "create", "limit", "UNIT", 50, 3700.00, "BUY");
//        GendarmeResponse res = commandRoot.invoke(user, "order", "list");
        GendarmeResponse res = commandRoot.invoke(user, "order", "create", "fdsfsd");
        System.out.println(res.getTable());
        System.out.println(res.getMessage());
    }
}