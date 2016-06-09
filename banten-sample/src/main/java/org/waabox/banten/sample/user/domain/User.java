package org.waabox.banten.sample.user.domain;

import java.util.List;

import javax.persistence.*;

import javax.persistence.Entity;

import org.waabox.banten.sample.time.domain.*;

/** The user entity.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(name = "users")
public class User {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  /** The user name. */
  @Column(name = "name")
  private String name;

  /** The time for the user.*/
  @ManyToOne(fetch = FetchType.EAGER)
  private Time time;

  /** The time repository. */
  @Transient
  private transient TimeRepository timeRepository;

  /** Creates a new instance of the user, for hibernate.
   * @param theTimeRepository the time repository.
   */
  User(final TimeRepository theTimeRepository) {
    timeRepository = theTimeRepository;
  }

  /** Creates a new instance of the user.
   * @param theTimeRepository the time repository.
   * @param userName the username.
   */
  public User(final TimeRepository theTimeRepository, final String userName,
      final String gmt) {
    this(theTimeRepository);
    name = userName;
    List<Time> timeList = timeRepository.find(gmt);
    if (timeList.isEmpty()) {
      Time aTime = new Time(gmt);
      timeRepository.save(aTime);
      time = aTime;
    } else {
      time = timeList.get(0);
    }
  }

  /** Retrieves the name.
   * @return the name
   */
  public String getName() {
    return name;
  }

  /** Retrieves the time.
   * @return the time
   */
  public Time getTime() {
    return time;
  }
}
