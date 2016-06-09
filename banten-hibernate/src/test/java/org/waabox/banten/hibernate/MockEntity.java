package org.waabox.banten.hibernate;

import javax.persistence.*;

/** Just a Mock Entity.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(name = "mock_entity")
public class MockEntity {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "name")
  private String name;

  MockEntity() {}

  public MockEntity(final String aName) {
    name = aName;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
