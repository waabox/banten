package org.waabox.banten.hibernate;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "mock_entity_with_collection_with_tuplizer")
public class MockEntityWithCollectionWithTuplizers {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "name")
  private String name;

  @OneToMany
  @Cascade(CascadeType.ALL)
  private List<MockEntityWithTuplizer> withTuplizers = new LinkedList<>();

  MockEntityWithCollectionWithTuplizers() {}

  MockEntityWithCollectionWithTuplizers(final String aName,
      final List<MockEntityWithTuplizer> blabla) {
    name = aName;
    withTuplizers = blabla;
  }

  /** Retrieves the id.
   * @return the id
   */
  public long getId() {
    return id;
  }

  /** Retrieves the name.
   * @return the name
   */
  public String getName() {
    return name;
  }

  /** Retrieves the withTuplizers.
   * @return the withTuplizers
   */
  public List<MockEntityWithTuplizer> getWithTuplizers() {
    return withTuplizers;
  }

}
