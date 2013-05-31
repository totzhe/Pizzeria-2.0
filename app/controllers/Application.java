package controllers;

import daos.DishSortDao;
import daos.GenericDao;
import flexjson.JSONDeserializer;
import models.*;
import org.codehaus.jackson.JsonNode;
import play.*;
import play.api.templates.Html;
import play.data.Form;
import play.db.jpa.*;
import services.ShowMenuService;
import views.html.*;
import views.html.helper.form;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import flexjson.JSONSerializer;

public class Application extends Controller {
    @Transactional(readOnly = true)
    public static Result index() {
        Order order = CacheController.loadOrder();
        return ok(index.render(new ShowMenuService().getDishSorts(), order.getSum()));
    }

    public static Result login() {
        return ok(login.render());
    }

    static java.util.Random random = new java.util.Random();

    @Transactional
    public static Result menu(int id) {
        Order order = CacheController.loadOrder();
        List<Integer> selectedItems = new LinkedList<Integer>();

        for (Iterator<OrderItem> i = order.getItems().iterator(); i.hasNext(); ) {
            OrderItem item = i.next();
            selectedItems.add(item.getDish().getId());
        }
        return ok(menu.render(id, new ShowMenuService().getDishSorts(),
                new ShowMenuService().getDishSortById(id).getDishes(),
                selectedItems, order.getSum()));
    }

    public static Result user(Long id) {
        return ok("You selected id = " + id);
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    @Transactional
    public static Result JsonExample() {
        response().setContentType("application/json");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").getTextValue();
            if (name == null) {
                return badRequest("Missing parameter [name]");
            } else {
                List<DishSort> sorts = new DishSortDao().getAll();
                JSONSerializer serializer = new JSONSerializer().exclude("dishes", "class");
                return ok("Server received: " + json.toString() + System.getProperty("line.separator") + "Server sent: " + serializer.serialize(sorts));
            }
        }
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result JsonExamplePOST() {
        response().setContentType("application/json");
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").getTextValue();
            if (name == null) {
                return badRequest("Missing parameter [name]");
            } else {
                return ok("Server received: " + json.toString());
            }
        }
    }

    @Transactional
    public static Result JsonExampleGET() {
        List<DishSort> sorts = new DishSortDao().getAll();
        JSONSerializer serializer = new JSONSerializer().exclude("dishes", "class");
        return ok(serializer.serialize(sorts));
    }

    @Transactional
    public static Result cart() {
        Order order = CacheController.loadOrder();
        return ok(cart.render(order, OrderController.userForm));
    }

    @Transactional
    public static Result delivery() {
        Order order = CacheController.loadOrder();
        return ok(delivery.render(order));
    }

    @Transactional
    public static Result contacts() {
        Order order = CacheController.loadOrder();
        return ok(contacts.render(order));
    }

    @Transactional
    public static Result about() {
        Order order = CacheController.loadOrder();
        return ok(about.render(order));
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        // Routes
                        //controllers.routes.javascript.Application.JsonExample(),
                        controllers.routes.javascript.Application.JsonExampleGET(),
                        //controllers.routes.javascript.Application.JsonExamplePOST(),
                        controllers.routes.javascript.Application.user(),
                        controllers.routes.javascript.OrderController.addItem(),
                        controllers.routes.javascript.OrderController.editItem(),
                        controllers.routes.javascript.OrderController.removeItem(),
                        controllers.routes.javascript.AdministrationController.login(),
                        controllers.routes.javascript.AdministrationController.getNewOrders()
                )
        );
    }
}