import alessandrosalerno.gendarme.*;
import alessandrosalerno.gendarme.exceptions.*;

public class Main {
    public static void main(String[] args) {
        GendarmeCommandRoot<MyUser> commandRoot = new GendarmeCommandRoot<>("NSE Commands");
        OrderCommands orderCommands = new OrderCommands();
        commandRoot.addCommand("order", orderCommands);

        MyUser user = new MyUser();

        try {
            GendarmeParser parser = new GendarmeParser("order create market UNIT 1000 BUY");
            Object[] commandArgs = parser.parse();
            GendarmeResponse res = commandRoot.invoke(user, commandArgs);
            System.out.println(res.getTable());
            System.out.println(res.getMessage());
        } catch (GendarmeInvalidSyntaxException e) {
            System.out.println(e);
        } catch (GendarmeAccessRestrictedException
                 | GendarmePermissionDeniedException
                 | GendarmeNoSuchCommandException e) {
            System.out.println(e);
        } catch (GendarmeException e) {
            System.out.println(e);
        }
    }
}