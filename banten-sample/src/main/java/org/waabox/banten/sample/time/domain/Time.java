package org.waabox.banten.sample.time.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** Represents a calendar.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@Entity
@Table(name = "time")
public class Time {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  /** The GMT. */
  @Column(name = "gmt", nullable = false)
  private String gmt;

  /** Creates a new instance of the Time for Hibernate.*/
  Time() {
  }

  /** Creates a new instance of the Time.
   * @param aGMT the GMT.
   */
  public Time(final String aGMT) {
    gmt = aGMT;
  }

  /** Retrieves the id.
   * @return the id
   */
  public long getId() {
    return id;
  }

  /** Retrieves the gmt.
   * @return the gmt
   */
  public String getGmt() {
    return gmt;
  }

  /** Retrieves the time for this GMT.
   * @return the time.
   */
  public String getTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat();
    df.setTimeZone(TimeZone.getTimeZone(gmt));
    return df.format(calendar.getTime());
  }

}
