import alessandrosalerno.gendarme.GendarmeUser;

public class MyUser implements GendarmeUser {
    @Override
    public boolean hasPermission(String permission) {
        return "order.create.limit".equals(permission);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}