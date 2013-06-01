package controllers;

import daos.AdminDao;
import daos.DishSortDao;
import flexjson.JSONSerializer;
import models.DishSort;
import models.Order;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.OrderManagementService;
import services.ShowMenuService;
import views.html.indexadmin;
import views.html.menu;
import views.html.orders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Alina
 * Date: 29.05.13
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public class AdministrationController extends Controller {

    @Transactional
    public static Result indexadmin(int id) {
        return ok(indexadmin.render(id, new ShowMenuService().getDishSorts(),
                new ShowMenuService().getDishSortById(id).getDishes()));
    }

    @Transactional
    public static Result login(String login, String password) {
        AdminDao adminDao = new AdminDao();
        if (adminDao.Verify(login, /*MD5.getHash(password)*/password))
            return ok("Checked");
        else
            return badRequest("Login or password is wrong");
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SS");

    @Transactional
    public static Result getOrders() {
        session("lastCheck", dateFormat.format(new Date()));
        return ok(orders.render(new OrderManagementService().getActiveOrders()));
    }

    @Transactional
    public static Result getNewOrders() {
        String date = session("lastCheck");
        Date lastUpdate = new Date(0, 0, 0, 0, 0, 0);
        if (date != null) {
            try {
                lastUpdate = dateFormat.parse(date);
            } catch (ParseException e) {
                lastUpdate = new Date(0, 0, 0, 0, 0, 0);
            }
        }
        Date currentUpdate = new Date();
        session().put("lastUpdate", dateFormat.format(currentUpdate));


        List<Order> orders = new OrderManagementService().getNewOrders(new Date(0, 0, 0, 0, 0, 0), currentUpdate);
        JSONSerializer serializer = new JSONSerializer().include("items").exclude("class", "items.class", "items.orderId", "items.dish.class", "items.dish.sort", "items.dish.picturePath", "items.dish.description");
        return ok(serializer.serialize(orders));
    }

    @Transactional
    public static Result addDish(int id) {
        return ok(indexadmin.render(id, new ShowMenuService().getDishSorts(),
                new ShowMenuService().getDishSortById(id).getDishes()));
    }

}
