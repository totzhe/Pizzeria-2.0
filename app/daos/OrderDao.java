package daos;

import models.DishSort;
import models.Order;
import net.sf.ehcache.util.counter.sampled.TimeStampedCounterValue;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artyom
 * Date: 14.04.13
 * Time: 2:46
 * To change this template use File | Settings | File Templates.
 */
public class OrderDao extends GenericDao<Order, Integer> {
    public OrderDao() {
        super(Order.class);
    }

    public List<Order> getActive()
    {
        Query query = em.createQuery("select ord from Order ord where (ord.sendingTime is null) order by ord.recievingTime");
        return query.getResultList();
    }

    public List<Order> getActiveByTime(Date from, Date to)
    {
        Query query = em.createQuery("select ord from Order ord where (ord.sendingTime is null) and (ord.recievingTime > :from) and (ord.recievingTime <= :to) order by ord.recievingTime");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }
}
