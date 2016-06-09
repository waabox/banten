package org.waabox.banten.hibernate;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.waabox.banten.hibernate.Transaction;
import org.waabox.banten.testsupport.BantenTest;

/** Test the Hibernate integration.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BantenTest(applicationClass = DatabaseTestModuleApplication.class)
public class HibernateTest {

  @Autowired ATestRepository repository;

  @Autowired Transaction tx;

  @Test
  @Transactional
  public void test() {
    repository.save(new MockEntity("foo"));
    MockEntity entity = repository.byName("foo");
    assertThat(entity.getName(), is("foo"));
  }

  @Test
  public void testTuplizer() {
    tx.start();
    repository.save(new MockEntityWithTuplizer("pepe", true));
    tx.commit();
    tx.start();
    List<MockEntityWithTuplizer> mocks = repository.all();
    assertThat(mocks.get(0).getDependency(), is("foo"));
    tx.commit();
  }

  @Test public void testCollectionTuplizer() {
    tx.start();
    MockEntityWithCollectionWithTuplizers a;
    a = new MockEntityWithCollectionWithTuplizers(
        "foo",
        Arrays.asList(
            new MockEntityWithTuplizer("pepe", true),
            new MockEntityWithTuplizer("pepe 2", true)
        )
    );
    repository.save(a);
    tx.commit();
    tx.start();
    MockEntityWithTuplizer b = repository.allWithTuplizers().get(0)
        .getWithTuplizers().get(0);
    assertThat(b.getDependency(), is("foo"));
    tx.commit();
  }

}
