package org.waabox.banten.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(name = "mock_entity_with_tuplizer")
public class MockEntityWithTuplizer {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "name")
  private String name;

  @Transient
  private transient String dependency;

  MockEntityWithTuplizer(final String aDependency) {
    dependency = aDependency;
  }

  public MockEntityWithTuplizer(final String aName, final boolean a) {
    name = aName;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  /** Retrieves the dependency.
   * @return the dependency
   */
  public String getDependency() {
    return dependency;
  }

}
