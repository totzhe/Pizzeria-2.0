package controllers;

import daos.AdminDao;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ShowMenuService;
import views.html.indexadmin;
import views.html.menu;
import views.html.orders;

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

    @Transactional
    public static Result addDish(int id) {
        return ok(indexadmin.render(id, new ShowMenuService().getDishSorts(),
                new ShowMenuService().getDishSortById(id).getDishes()));
    }

    public static Result orders() {
        return ok(orders.render());
    }

}
