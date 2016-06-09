package org.waabox.banten.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/** Test repository.
 * @author waabox (waabox[at]gmail[dot]com)
 */
public class ATestRepository {

  private SessionFactory sf;

  @Autowired
  public ATestRepository(final SessionFactory sessionFactory) {
    sf = sessionFactory;
  }

  public void save(final MockEntity entity) {
    sf.getCurrentSession().saveOrUpdate(entity);
  }

  public void save(final MockEntityWithTuplizer entity) {
    sf.getCurrentSession().saveOrUpdate(entity);
  }

  public void save(final MockEntityWithCollectionWithTuplizers entity) {
    sf.getCurrentSession().saveOrUpdate(entity);
  }


  public MockEntity byName(final String name) {
    Criteria criteria = sf.getCurrentSession().createCriteria(MockEntity.class);
    criteria.add(Restrictions.eq("name", name));
    return (MockEntity) criteria.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<MockEntityWithTuplizer> all() {
    Criteria criteria = sf.getCurrentSession()
        .createCriteria(MockEntityWithTuplizer.class);
    return criteria.list();
  }

  @SuppressWarnings("unchecked")
  public List<MockEntityWithCollectionWithTuplizers> allWithTuplizers() {
    Criteria criteria = sf.getCurrentSession()
        .createCriteria(MockEntityWithCollectionWithTuplizers.class);
    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    return criteria.list();
  }

}
