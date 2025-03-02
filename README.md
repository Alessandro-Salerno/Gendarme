<p align="center">
    <h1 align="center">Gendarme</h1>
    <p align="center"> Structured user command management library for Java network applications </p>
</p>

<div align="center">

[contributors-shield]: https://img.shields.io/github/contributors/Alessandro-Salerno/Gendarme.svg?style=flat-square
[contributors-url]: https://github.com/Alessandro-Salerno/Gendarme/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Alessandro-Salerno/Gendarme.svg?style=flat-square
[forks-url]: https://github.com/Alessandro-Salerno/Gendarme/network/members
[stars-shield]: https://img.shields.io/github/stars/Alessandro-Salerno/Gendarme.svg?style=flat-square
[stars-url]: https://github.com/Alessandro-Salerno/Gendarme/stargazers
[issues-shield]: https://img.shields.io/github/issues/Alessandro-Salerno/Gendarme.svg?style=flat-square
[issues-url]: https://github.com/Alessandro-Salerno/Gendarme/issues
[license-shield]: https://img.shields.io/github/license/Alessandro-Salerno/Gendarme.svg?style=flat-square
[license-url]: https://github.com/Alessandro-Salerno/Gendarme/blob/master/LICENSE.txt

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
![](https://tokei.rs/b1/github/Alessandro-Salerno/Gendarme)

</div>

## Features
- Flexible and easy-to-refactor DOM-like command structure
- Permission and authentication control with depndency injection
- Integrated command parser
- Command polymorphism
- Fast command invokation
- Designed for CRUD operations, suitable for others too
- Can be used in both FOSS and proprietary software

## Structure
Gendarme has five main concepts:
- Command root
- Command group
- Command
- User
- Response

### Command root
The command root (represented by the `GendarmeCommandRoot` class) is the root of the command tree. Every command in a Gendarme application is part of a command tree that allows for nesting and polymorphism. The command root is implemented using a command group, meaning that all concepts that apply to command groups also apply to the command root.

### Command group
A command group (represented by the `GendarmeCommandGroup` class) is a group of methods and other command groups used to organize commands in the command tree. Command groups take a generic type argument which is then used to represent the user inside command handlers.

### Command
A command is simply a public method with the `@GendarmeCommand` annotation  inside a `GendarmeCommandGroup` or `GendarmeCommandRoot` class. A command method takes an instance of its group's `UserType` as its first argument. Command methods must return an instance of `GendrmeResponse`.

### User
users are represented with objects of classes that implement the `GendarmeUser` interface. These objects can be used inside command methods but are also used by Gendarme to apply access policies based on authentication and permissions.

### Respnose
Responses are command results returned by command methods. A response is comprised of a table and a string which are not mutually exclusive.

## Usage guidelines
- Only `Long`, `Double`, and `String` types are allowed as command arguments
- Do not use anonymous classes in command group fields: these may be treated as package protected and may result in nasty `IllegalAccessException`s
- Compile your application with parameter names in the binary, otherwise help menus won't show them

## Example
### User class
The `GendarmeUser` interface leaves full implementation freedom, but requires the presence of the `hasPermission` and `isAuthenticated` methods which are used to manage access to priviledged commands.
```java
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
```

### Command group
Command groups can be structured however desired, but all subgroups and commands must be public.
```java
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
        public GendarmeResponse limit(MyUser commandIssuer, String symbol, Long quantity, Double price, String side) {
            return new GendarmeResponse("Order created successfully");
        }

        @GendarmeCommand
        @GendarmeRequireAuthentication
        @GendarmeRequirePermission("order.create.market")
        @GendarmeDescription("Create a market order")
        public GendarmeResponse market(MyUser commandIssuer, String symbol, Long quantity, String side) {
            return new GendarmeResponse("Order created successfully");
        }
    }

    // The name used for this field will become the name of the subgroup
    public CreateCommands create = new CreateCommands();

    @GendarmeCommand
    @GendarmeRequireAuthentication
    public GendarmeResponse list(MyUser commandIssuer) {
        GendarmeResponse res = new GendarmeResponse("All done",
                                                    "Order ID",
                                                    "Symbol",
                                                    "Quantity",
                                                    "Price",
                                                    "Side");
        res.addTableRow(1, "UNIT", 1000, 3700, "BUY");
        res.addTableRow(2, "UNIT", 1000, 3750, "BUY");
        return res;
    }
}
```

### Main class
```java
import alessandrosalerno.gendarme.*;
import alessandrosalerno.gendarme.exceptions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        // Create the command root and populate it with command groups
        // NOTE: the command root can also be created just like a command group using a separate class
        //       with fields and methods, this is included to showcase the addCommand method
        GendarmeCommandRoot<MyUser> commandRoot = new GendarmeCommandRoot<>("Market commands");
        OrderCommands orderCommands = new OrderCommands();
        commandRoot.addCommand("order", orderCommands);

        // Simulate a new user connecting
        MyUser user = new MyUser();

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            GendarmeParser parser = new GendarmeParser(r.readLine());
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### Outcomes
- Input (Correct): `order create limit UNIT 1000 3750.00 BUY`
```
order create limit UNIT 1000 3750.00 BUY

Order created successfully
```
- Input (Correct): `order list`
```
order list
+----------+--------+----------+-------+------+
| Order ID | Symbol | Quantity | Price | Side |
+----------+--------+----------+-------+------+
|        1 | UNIT   |     1000 |  3700 | BUY  |
|        2 | UNIT   |     1000 |  3750 | BUY  |
+----------+--------+----------+-------+------+

All done
```
- Input (Permission denied): `order create market UNIT 1000 BUY`
```
order create market UNIT 1000 BUY
alessandrosalerno.gendarme.exceptions.GendarmePermissionDeniedException: Command "market" requires permission "order.create.market"
```
- Input (Typo): `order create lol`
```
order create lol
+---------+---------------------------------------------------------+-----------------------+
| Command |                        Signature                        |      Description      |
+---------+---------------------------------------------------------+-----------------------+
| market  | String symbol, Long quantity, String side               | Create a market order |
| limit   | String symbol, Long quantity, Double price, String side | Create a limit order  |
+---------+---------------------------------------------------------+-----------------------+
```

## Installing Gendarme with Maven
You can install Gendarme using the official [GitHub Package](https://github.com/Alessandro-Salerno/Gendarme/packages/).

## License
Gendarme is distributed under the Apache License 2.0.
