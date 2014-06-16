package org.daniels.examples.dao.hibernate;

import java.util.List;

import org.daniels.examples.dao.RoleDao;
import org.daniels.examples.model.Role;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


/**
 * This class interacts with hibernate session to save/delete and
 * retrieve Role objects.
*/

public class RoleDaoHibernate extends GenericDaoHibernate<Role, Long> implements RoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    
    public RoleDaoHibernate() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
        List roles = getSession().createCriteria(Role.class).add(Restrictions.eq("name", rolename)).list();
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        //Session session = getSessionFactory().getCurrentSession();
        Session session = getSession();
        session.delete(role);
    }
}
