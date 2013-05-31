package services;

import daos.OrderDao;
import models.Order;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artyom
 * Date: 31.05.13
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class OrderManagementService {
    public List<Order> getActiveOrders()
    {
        return new OrderDao().getActive();
    }

    public List<Order> getNewOrders(Date from, Date to)
    {
        return new OrderDao().getActiveByTime(from, to);
    }
}
